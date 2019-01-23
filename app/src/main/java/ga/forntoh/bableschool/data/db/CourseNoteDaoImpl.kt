package ga.forntoh.bableschool.data.db

import com.dbflow5.query.select
import ga.forntoh.bableschool.data.model.main.Course
import ga.forntoh.bableschool.data.model.main.Course_Table

class CourseNoteDaoImpl(private val database: AppDatabase) : CourseNoteDao {

    override fun retrieveCourseNotes(): MutableList<Course> =
            (select from Course::class).queryList(database)

    override fun retrieveSingleCourse(code: String) =
            (select from Course::class where (Course_Table.code eq code)).querySingle(database)

    override fun saveCourseNotes(vararg courses: Course) =
            courses.forEach { it.save(database) }
}