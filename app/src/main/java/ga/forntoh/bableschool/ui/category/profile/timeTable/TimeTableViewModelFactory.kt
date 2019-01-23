package ga.forntoh.bableschool.ui.category.profile.timeTable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ga.forntoh.bableschool.data.repository.PeriodRepository

class TimeTableViewModelFactory(private val periodRepository: PeriodRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TimeTableViewModel(periodRepository) as T
    }
}