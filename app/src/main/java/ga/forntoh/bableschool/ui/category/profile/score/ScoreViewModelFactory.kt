package ga.forntoh.bableschool.ui.category.profile.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ga.forntoh.bableschool.data.repository.ScoreRepository

class ScoreViewModelFactory(private val scoreRepository: ScoreRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ScoreViewModel(scoreRepository) as T
    }
}