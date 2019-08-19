package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.model.main.User

abstract class ProfileRepository : BaseRepository() {

    abstract suspend fun login(matriculation: String, password: String)

    abstract suspend fun getUser(): User?

    abstract fun logout()

    abstract suspend fun updatePassword(matriculation: String, password: String)
}