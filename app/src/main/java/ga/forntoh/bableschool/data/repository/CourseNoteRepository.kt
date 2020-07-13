package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.Course
import ga.forntoh.bableschool.data.model.main.Document
import ga.forntoh.bableschool.data.model.main.Video

abstract class CourseNoteRepository : BaseRepository() {

    abstract suspend fun retrieveCourses(): LiveData<MutableList<Course>>

    abstract suspend fun retrieveSingleCourse(code: String): Course?

    abstract suspend fun videosOfCourse(code: String): MutableList<Video>?

    abstract suspend fun documentsOfCourse(code: String): MutableList<Document>?

    abstract suspend fun numberOfVideos(code: String): Int

    abstract suspend fun numberOfDocuments(code: String): Int

    abstract fun resetState()

    abstract suspend fun initCourseNotesData()
}