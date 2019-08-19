package ga.forntoh.bableschool.ui.category.courseNotes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ga.forntoh.bableschool.data.repository.CourseNoteRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class CourseNotesViewModel(private val courseNoteRepository: CourseNoteRepository) : ViewModel() {

    init {
        courseNoteRepository.scope = viewModelScope
    }

    var code = ""

    val allCourseNotes by lazyDeferred { courseNoteRepository.retrieveCourses() }

    val singleCourseNote by lazyDeferred { courseNoteRepository.retrieveSingleCourse(code) }

    val videos by lazyDeferred { courseNoteRepository.videosOfCourse(code) }

    val documents by lazyDeferred { courseNoteRepository.documentsOfCourse(code) }

    val numberOfVideos by lazyDeferred { courseNoteRepository.numberOfVideos(code) }

    val numberOfDocuments by lazyDeferred { courseNoteRepository.numberOfDocuments(code) }

}