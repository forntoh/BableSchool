package ga.forntoh.bableschool.data.db

import ga.forntoh.bableschool.data.model.main.Course

interface CourseNoteDao {

    fun retrieveCourseNotes(): MutableList<Course>

    fun retrieveSingleCourse(code: String): Course?

    fun saveCourseNotes(vararg courses: Course)
}