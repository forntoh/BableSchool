package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.RankingDao
import ga.forntoh.bableschool.data.model.other.TopSchool
import ga.forntoh.bableschool.data.model.other.TopStudent
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class RankingRepositoryImpl(
        private val rankingDao: RankingDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : RankingRepository() {

    init {
        bableSchoolDataSource.downloadedTopSchools.observeForever {
            scope.launch { if (it != null) saveTopSchools(*it.toTypedArray()) }
        }
        bableSchoolDataSource.downloadedTopStudents.observeForever {
            scope.launch { if (it != null) saveTopStudents(*it.toTypedArray()) }
        }
    }

    override fun resetState() {
        appStorage.clearLastSaved(DataKey.TOP_SCHOOLS)
        appStorage.clearLastSaved(DataKey.TOP_STUDENTS)
    }

    // Main
    override suspend fun retrieveTopSchools(): LiveData<MutableList<TopSchool>> =
            withContext(Dispatchers.IO) {
                initTopSchoolsData()
                return@withContext rankingDao.retrieveTopSchools()
            }

    // Main
    override suspend fun retrieveTopStudents(): LiveData<MutableList<TopStudent>> =
            withContext(Dispatchers.IO) {
                initTopStudentsData()
                return@withContext rankingDao.retrieveTopStudents()
            }

    private suspend fun initTopSchoolsData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.TOP_SCHOOLS)) || rankingDao.numberOfItemsTopSchools() < +0) {
            bableSchoolDataSource.topSchools()
            appStorage.setLastSaved(DataKey.TOP_SCHOOLS, ZonedDateTime.now())
            delay(500)
        }
    }

    private suspend fun initTopStudentsData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.TOP_STUDENTS)) || rankingDao.numberOfItemsTopStudents() < +0) {
            bableSchoolDataSource.getTopStudents(appStorage.loadUser()?.profileData?.matriculation
                    ?: return)
            appStorage.setLastSaved(DataKey.TOP_STUDENTS, ZonedDateTime.now())
            delay(500)
        }
    }

    private suspend fun saveTopSchools(vararg topSchools: TopSchool) =
            rankingDao.saveTopSchools(*topSchools)

    private suspend fun saveTopStudents(vararg topStudents: TopStudent) =
            rankingDao.saveTopStudents(*topStudents)
}