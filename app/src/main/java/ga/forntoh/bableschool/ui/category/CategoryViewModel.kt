package ga.forntoh.bableschool.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ga.forntoh.bableschool.data.repository.CategoryRepository
import ga.forntoh.bableschool.data.repository.CourseNoteRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class CategoryViewModel(private val categoryRepository: CategoryRepository, private val courseNoteRepository: CourseNoteRepository) : ViewModel() {

    init {
        categoryRepository.scope = viewModelScope
        courseNoteRepository.scope = viewModelScope
    }

    val categories by lazyDeferred {
        categoryRepository.retrieveCategories()
    }

    val isPasswordChanged = categoryRepository.passwordChanged()

    suspend fun initCourseNotes() {
        courseNoteRepository.initCourseNotesData()
    }

}