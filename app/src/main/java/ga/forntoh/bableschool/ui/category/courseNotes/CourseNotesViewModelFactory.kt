package ga.forntoh.bableschool.ui.category.courseNotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ga.forntoh.bableschool.data.repository.CourseNoteRepository

class CourseNotesViewModelFactory(private val courseNoteRepository: CourseNoteRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CourseNotesViewModel(courseNoteRepository) as T
    }
}