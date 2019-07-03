package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.model.main.User
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepositoryImpl(
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : ProfileRepository {

    init {
        bableSchoolDataSource.downloadedUserProfile.observeForever {
            saveUser(it)
        }
    }

    private fun saveUser(user: User?) {
        user?.let { appStorage.saveUser(it) }
    }

    override suspend fun login(matriculation: String, password: String) =
            bableSchoolDataSource.getUserProfile(matriculation, password)

    override fun logout() = appStorage.clearUser()

    override suspend fun getUser(): User? = withContext(Dispatchers.IO) {
        return@withContext appStorage.loadUser()
    }
}