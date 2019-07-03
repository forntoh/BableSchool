package ga.forntoh.bableschool.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ga.forntoh.bableschool.data.repository.CategoryRepository

class CategoryViewModelFactory(private val categoryRepository: CategoryRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryViewModel(categoryRepository) as T
    }
}