package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.RankingDao
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class RankingRepositoryImpl(
        private val rankingDao: RankingDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : RankingRepository {

    init {
        bableSchoolDataSource.downloadedTopSchools.observeForever {
            saveTopSchools(*it.toTypedArray())
        }
        bableSchoolDataSource.downloadedTopStudents.observeForever {
            saveTopStudents(*it.toTypedArray())
        }
    }

    // Main
    override suspend fun retrieveTopSchools(): MutableList<TopSchool> =
            withContext(Dispatchers.IO) {
                initTopSchoolsData()
                val data = rankingDao.retrieveTopSchools()
                if (data.isNullOrEmpty()) {
                    appStorage.clearLastSaved(DataKey.TOP_SCHOOLS)
                    initTopSchoolsData()
                }
                return@withContext data
            }

    // Main
    override suspend fun retrieveTopStudents(): MutableList<TopStudent> =
            withContext(Dispatchers.IO) {
                initTopStudentsData()
                val data = rankingDao.retrieveTopStudents()
                if (data.isNullOrEmpty()) {
                    appStorage.clearLastSaved(DataKey.TOP_STUDENTS)
                    initTopStudentsData()
                }
                return@withContext data
            }

    private suspend fun initTopSchoolsData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.TOP_SCHOOLS))) {
            bableSchoolDataSource.topSchools()
            appStorage.setLastSaved(DataKey.TOP_SCHOOLS, ZonedDateTime.now())
            delay(500)
        }
    }

    private suspend fun initTopStudentsData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.TOP_STUDENTS))) {
            bableSchoolDataSource.getTopStudents(appStorage.loadUser()?.profileData?.matriculation
                    ?: return)
            appStorage.setLastSaved(DataKey.TOP_STUDENTS, ZonedDateTime.now())
            delay(500)
        }
    }

    private fun saveTopSchools(vararg topSchools: TopSchool) =
            rankingDao.saveTopSchools(*topSchools)

    private fun saveTopStudents(vararg topStudents: TopStudent) =
            rankingDao.saveTopStudents(*topStudents)
}