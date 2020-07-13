package ga.forntoh.bableschool.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ga.forntoh.bableschool.data.repository.CategoryRepository
import ga.forntoh.bableschool.data.repository.CourseNoteRepository

class CategoryViewModelFactory(private val categoryRepository: CategoryRepository, private val courseNoteRepository: CourseNoteRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryViewModel(categoryRepository, courseNoteRepository) as T
    }
}