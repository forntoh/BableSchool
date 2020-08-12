package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.CourseNoteDao
import ga.forntoh.bableschool.data.model.main.Course
import ga.forntoh.bableschool.data.model.main.Document
import ga.forntoh.bableschool.data.model.main.Video
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class CourseNoteRepositoryImpl(
        private val courseNoteDao: CourseNoteDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : CourseNoteRepository() {

    init {
        bableSchoolDataSource.downloadedCourseNotes.observeForever {
            scope.launch {
                for (item in it) {
                    saveCourseNotes(Course(item.code, item.title))
                    saveVideos(item.vids.map { video -> video.apply { courseCode = item.code } })
                    saveDocuments(item.docs.map { document -> document.apply { courseCode = item.code } })
                }
            }
        }
    }

    override fun resetState() {
        appStorage.clearLastSaved(DataKey.COURSES)
        scope.launch { courseNoteDao.deleteAll() }
    }

    // Main
    override suspend fun retrieveCourses() = withContext(Dispatchers.IO) {
        initCourseNotesData()
        return@withContext courseNoteDao.retrieveCourseNotes()
    }

    // Main
    override suspend fun retrieveSingleCourse(code: String) = withContext(Dispatchers.IO) {
        return@withContext courseNoteDao.retrieveSingleCourse(code)
    }

    override suspend fun videosOfCourse(code: String) = courseNoteDao.videosOfCourse(code)

    override suspend fun documentsOfCourse(code: String) = courseNoteDao.documentsOfCourse(code)

    override suspend fun numberOfVideos(code: String) = courseNoteDao.numberOfVideos(code)

    override suspend fun numberOfDocuments(code: String) = courseNoteDao.numberOfDocuments(code)

    override suspend fun initCourseNotesData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.COURSES), 60) || courseNoteDao.numberOfItems() <= 0) {
            bableSchoolDataSource.getCourseNotes(appStorage.loadUser()?.profileData?.matriculation
                    ?: return)
            appStorage.setLastSaved(DataKey.COURSES, ZonedDateTime.now())
            delay(200)
        }
    }

    private suspend fun saveCourseNotes(vararg courses: Course) = courseNoteDao.saveCourseNotes(*courses)

    private suspend fun saveVideos(videos: List<Video>) = courseNoteDao.saveVideos(*videos.toTypedArray())

    private suspend fun saveDocuments(documents: List<Document>) = courseNoteDao.saveDocuments(*documents.toTypedArray())
}