package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.Period

abstract class PeriodRepository : BaseRepository() {

    abstract suspend fun retrievePeriods(): LiveData<MutableList<Period>?>
}