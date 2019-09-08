package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.model.main.User
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class ProfileRepositoryImpl(
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : ProfileRepository() {

    init {
        bableSchoolDataSource.downloadedUserProfile.observeForever {
            saveUser(it)
        }
    }

    private fun saveUser(user: User?) {
        user?.let { if (user.profileData != null) appStorage.saveUser(it) }
    }

    override suspend fun login(matriculation: String, password: String) {
        bableSchoolDataSource.getUserProfile(matriculation, password)
        delay(200)
        bableSchoolDataSource.getCourseNotes(matriculation)
        appStorage.setLastSaved(DataKey.COURSES, ZonedDateTime.now())
    }

    override fun logout() {
        appStorage.clearUser()
    }

    override suspend fun getUser(): User? = withContext(Dispatchers.IO) {
        return@withContext appStorage.loadUser()
    }

    override suspend fun updatePassword(matriculation: String, password: String) =
            bableSchoolDataSource.updatePassword(matriculation, appStorage.loadUser()?.profileDataMap()?.get("Password")!!, password)
}