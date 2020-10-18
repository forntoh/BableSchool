package ga.forntoh.bableschool.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ga.forntoh.bableschool.data.model.main.Course
import ga.forntoh.bableschool.data.model.main.Document
import ga.forntoh.bableschool.data.model.main.Video

@Dao
interface CourseNoteDao {

    @Query("""
        Select c.* FROM Course as c
        LEFT OUTER JOIN Video as v on v.courseCode = c.code
        LEFT OUTER JOIN Document as d on d.courseCode = c.code
        WHERE d.url IS NOT NULL OR v.url IS NOT NULL
    """)
    fun retrieveCourseNotes(): LiveData<MutableList<Course>>

    @Query("SELECT * FROM Course WHERE Course.code LIKE :code LIMIT 1")
    fun retrieveSingleCourse(code: String): Course?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCourseNotes(vararg courses: Course)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveVideos(vararg videos: Video)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDocuments(vararg videos: Document)

    @Query("SELECT * FROM Video WHERE Video.courseCode LIKE :code")
    suspend fun videosOfCourse(code: String): MutableList<Video>?

    @Query("SELECT * FROM Document WHERE Document.courseCode LIKE :code")
    suspend fun documentsOfCourse(code: String): MutableList<Document>?

    @Query("SELECT COUNT(courseCode) FROM Video WHERE courseCode LIKE :code")
    suspend fun numberOfVideos(code: String): Int

    @Query("SELECT COUNT(courseCode) FROM Document WHERE courseCode LIKE :code")
    suspend fun numberOfDocuments(code: String): Int

    @Query("SELECT COUNT(code) FROM Course")
    suspend fun numberOfItems(): Int

    @Query("DELETE FROM Course")
    fun deleteAllCourses()

    @Query("DELETE FROM Video")
    fun deleteAllVideos()

    @Query("DELETE FROM Document")
    fun deleteAllDocuments()
}