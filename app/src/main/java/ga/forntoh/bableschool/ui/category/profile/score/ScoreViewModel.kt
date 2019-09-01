package ga.forntoh.bableschool.ui.category.profile.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ga.forntoh.bableschool.data.repository.ScoreRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class ScoreViewModel(private val scoreRepository: ScoreRepository) : ViewModel() {

    init {
        scoreRepository.scope = viewModelScope
    }

    val annualRank by lazyDeferred {
        scoreRepository.retrieveYearScore()
    }
    val firstTermScores by lazyDeferred {
        scoreRepository.retrieveTermScores(1)
    }
    val secondTermScores by lazyDeferred {
        scoreRepository.retrieveTermScores(2)
    }
    val thirdTermScores by lazyDeferred {
        scoreRepository.retrieveTermScores(3)
    }
    val firstTermScoresAsync by lazyDeferred {
        scoreRepository.retrieveTermScoresAsync(1)
    }
    val secondTermScoresAsync by lazyDeferred {
        scoreRepository.retrieveTermScoresAsync(2)
    }
    val thirdTermScoresAsync by lazyDeferred {
        scoreRepository.retrieveTermScoresAsync(3)
    }

    fun resetState(term: Int) = scoreRepository.resetState(term)
}