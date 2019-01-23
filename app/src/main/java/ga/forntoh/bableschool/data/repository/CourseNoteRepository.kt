package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.model.main.Course

interface CourseNoteRepository {

    suspend fun retrieveCourses(): MutableList<Course>

    suspend fun retrieveSingleCourse(code: String): Course?
}