package ga.forntoh.bableschool.ui.category.profile.timeTable

import androidx.lifecycle.ViewModel
import ga.forntoh.bableschool.data.repository.PeriodRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class TimeTableViewModel(periodRepository: PeriodRepository) : ViewModel() {

    val periods by lazyDeferred {
        periodRepository.retrievePeriods()
    }
}