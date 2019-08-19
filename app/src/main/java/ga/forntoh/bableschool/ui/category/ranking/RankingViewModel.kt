package ga.forntoh.bableschool.ui.category.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ga.forntoh.bableschool.data.repository.RankingRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class RankingViewModel(private val rankingRepository: RankingRepository) : ViewModel() {

    init {
        rankingRepository.scope = viewModelScope
    }

    val topStudent by lazyDeferred {
        rankingRepository.retrieveTopStudents()
    }

    val topSchool by lazyDeferred {
        rankingRepository.retrieveTopSchools()
    }
}