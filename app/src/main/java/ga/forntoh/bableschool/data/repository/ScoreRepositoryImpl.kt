package ga.forntoh.bableschool.data.repository

import android.database.sqlite.SQLiteConstraintException
import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.ScoreDao
import ga.forntoh.bableschool.data.model.main.Score
import ga.forntoh.bableschool.data.model.other.AnnualRank
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.Utils
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ScoreRepositoryImpl(
        private val scoreDao: ScoreDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : ScoreRepository() {

    override fun resetState(term: Int) {
        appStorage.clearLastSaved(getKey(term))
        scope.launch {
            withContext(Dispatchers.IO) {
                scoreDao.deleteAllScores()
            }
        }
    }

    init {
        bableSchoolDataSource.downloadedTermScores.observeForever {
            scope.launch { saveScores(it) }
        }
        bableSchoolDataSource.downloadedAnnualRank.observeForever {
            scope.launch { saveAnnualRank(it) }
        }
    }

    override suspend fun retrieveTermScores(term: Int) = withContext(Dispatchers.IO) {
        initTermScores(term)
        return@withContext scoreDao.retrieveTermScores(term)
    }

    override suspend fun retrieveTermScoresAsync(term: Int) = withContext(Dispatchers.IO) {
        return@withContext scoreDao.retrieveTermScoresAsync(term)
    }

    override suspend fun retrieveYearScore(): AnnualRank? = withContext(Dispatchers.IO) {
        initYearScore()
        val data = scoreDao.retrieveYearScore()
        if (data == null) {
            appStorage.clearLastSaved(DataKey.TOP_SCHOOLS)
            initYearScore()
        }
        return@withContext data
    }

    private suspend fun initTermScores(term: Int) {
        if (isFetchNeeded(appStorage.getLastSaved(getKey(term))) || scoreDao.numberOfItemsTermScores() <= 0) {
            bableSchoolDataSource.getTermScores(appStorage.loadUser()?.profileData?.matriculation
                    ?: return, term, Utils.termYear)
            appStorage.setLastSaved(getKey(term), ZonedDateTime.now())
            delay(200)
        }
    }

    private suspend fun initYearScore() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.ANNUAL_SCORE)) || scoreDao.numberOfItemsYearScore() <= 0) {
            bableSchoolDataSource.annualRank(appStorage.loadUser()?.profileData?.matriculation
                    ?: return, Utils.termYear)
            appStorage.setLastSaved(DataKey.ANNUAL_SCORE, ZonedDateTime.now())
        }
    }

    private fun getKey(term: Int) = when (term) {
        1 -> DataKey.TERM_1
        2 -> DataKey.TERM_2
        else -> DataKey.TERM_3
    }

    private suspend fun saveScores(scores: List<Score>) = try {
        scoreDao.saveTermScores(scores.map {
            Score(it.firstSequenceMark, it.secondSequenceMark, it.rank, it.termRank, it.termAvg).apply {
                course_code = it.course!!.code
                term = it.term
            }
        })
    } catch (e: SQLiteConstraintException) {
    }

    private suspend fun saveAnnualRank(rank: AnnualRank) =
            scoreDao.saveYearScore(rank)
}