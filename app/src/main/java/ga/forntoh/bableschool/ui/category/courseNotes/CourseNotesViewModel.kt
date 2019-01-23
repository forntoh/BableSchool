package ga.forntoh.bableschool.ui.category.courseNotes

import androidx.lifecycle.ViewModel
import ga.forntoh.bableschool.data.repository.CourseNoteRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class CourseNotesViewModel(private val courseNoteRepository: CourseNoteRepository) : ViewModel() {

    var code = ""

    val allCourseNotes by lazyDeferred {
        courseNoteRepository.retrieveCourses()
    }

    val singleCourseNote by lazyDeferred {
        courseNoteRepository.retrieveSingleCourse(code)
    }

}