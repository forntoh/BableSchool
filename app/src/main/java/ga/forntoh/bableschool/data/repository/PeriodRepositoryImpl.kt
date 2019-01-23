package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.PeriodDao
import ga.forntoh.bableschool.data.model.main.Period
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class PeriodRepositoryImpl(
        private val periodDao: PeriodDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : PeriodRepository {

    init {
        bableSchoolDataSource.downloadedTimetable.observeForever {
            savePeriods(it)
        }
    }

    override suspend fun retrievePeriods(): MutableList<Period> = withContext(Dispatchers.IO) {
        initPeriodsData()
        val data = periodDao.retrievePeriods()
        if (data.isNullOrEmpty()) {
            appStorage.clearLastSaved(DataKey.PERIODS)
            initPeriodsData()
        }
        return@withContext data
    }

    private suspend fun initPeriodsData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.PERIODS), 60)) {
            bableSchoolDataSource.getTimetable(appStorage.loadUser()?.classe ?: return)
            appStorage.setLastSaved(DataKey.PERIODS, ZonedDateTime.now())
            delay(200)
        }
    }

    private fun savePeriods(periods: List<Period>) =
            periodDao.savePeriods(periods)
}