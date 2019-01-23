package ga.forntoh.bableschool.data.repository

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
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ScoreRepositoryImpl(
        private val scoreDao: ScoreDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : ScoreRepository {

    init {
        bableSchoolDataSource.downloadedTermScores.observeForever {
            saveScores(it)
        }
        bableSchoolDataSource.downloadedAnnualRank.observeForever {
            saveAnnualRank(it)
        }
    }

    override suspend fun retrieveTermScores(term: Int): MutableList<Score> =
            withContext(Dispatchers.IO) {
                initTermScores(term)
                val data = scoreDao.retrieveTermScores(term)
                if (data.isNullOrEmpty()) {
                    appStorage.clearLastSaved(getKey(term))
                    initTermScores(term)
                }
                return@withContext data
            }

    override suspend fun retrieveYearScore(): AnnualRank? = withContext(Dispatchers.IO) {
        initYearScore()
        return@withContext scoreDao.retrieveYearScore()
    }

    private suspend fun initTermScores(term: Int) {
        if (isFetchNeeded(appStorage.getLastSaved(getKey(term)))) {
            bableSchoolDataSource.getTermScores(appStorage.loadUser()?.profileData?.matriculation
                    ?: return, term, Utils.termYear)
            appStorage.setLastSaved(getKey(term), ZonedDateTime.now())
            delay(200)
        }
    }

    private suspend fun initYearScore() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.ANNUAL_SCORE))) {
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

    private fun saveScores(scores: List<Score>) =
            scoreDao.saveTermScores(scores)

    private fun saveAnnualRank(rank: AnnualRank) =
            scoreDao.saveYearScore(rank)
}