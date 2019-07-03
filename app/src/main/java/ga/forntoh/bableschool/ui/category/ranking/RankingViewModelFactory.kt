package ga.forntoh.bableschool.ui.category.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ga.forntoh.bableschool.data.repository.RankingRepository

class RankingViewModelFactory(private val rankingRepository: RankingRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RankingViewModel(rankingRepository) as T
    }
}