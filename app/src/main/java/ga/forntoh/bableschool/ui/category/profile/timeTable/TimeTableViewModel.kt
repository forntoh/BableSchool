package ga.forntoh.bableschool.ui.category.profile.timeTable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ga.forntoh.bableschool.data.repository.PeriodRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class TimeTableViewModel(periodRepository: PeriodRepository) : ViewModel() {

    init {
        periodRepository.scope = viewModelScope
    }

    val periods by lazyDeferred {
        periodRepository.retrievePeriods()
    }
}