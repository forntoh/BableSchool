package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.CourseNoteDao
import ga.forntoh.bableschool.data.model.main.Course
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class CourseNoteRepositoryImpl(
        private val courseNoteDao: CourseNoteDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : CourseNoteRepository {

    init {
        bableSchoolDataSource.downloadedCourseNotes.observeForever {
            saveCourseNotes(*it.toTypedArray())
        }
    }

    // Main
    override suspend fun retrieveCourses(): MutableList<Course> {
        return withContext(Dispatchers.IO) {
            initCourseNotesData()
            val data = courseNoteDao.retrieveCourseNotes()
            if (data.isNullOrEmpty()) {
                appStorage.clearLastSaved(DataKey.COURSES)
                initCourseNotesData()
            }
            return@withContext data
        }
    }

    // Main
    override suspend fun retrieveSingleCourse(code: String): Course? {
        return withContext(Dispatchers.IO) {
            return@withContext courseNoteDao.retrieveSingleCourse(code)
        }
    }

    private suspend fun initCourseNotesData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.COURSES), 60)) {
            bableSchoolDataSource.getCourseNotes(appStorage.loadUser()?.profileData?.matriculation
                    ?: return)
            appStorage.setLastSaved(DataKey.COURSES, ZonedDateTime.now())
            delay(200)
        }
    }

    private fun saveCourseNotes(vararg courses: Course) =
            courseNoteDao.saveCourseNotes(*courses)
}