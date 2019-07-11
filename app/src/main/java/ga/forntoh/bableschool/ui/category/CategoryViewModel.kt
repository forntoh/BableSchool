package ga.forntoh.bableschool.ui.category

import androidx.lifecycle.ViewModel
import ga.forntoh.bableschool.data.repository.CategoryRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {

    val categories by lazyDeferred {
        categoryRepository.retrieveCategories()
    }

    val isPasswordChanged = categoryRepository.passwordChanged()

    fun setPasswordChanged() = categoryRepository.setPasswordChanged()

}