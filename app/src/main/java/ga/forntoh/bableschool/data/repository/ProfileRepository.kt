package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.model.main.User

interface ProfileRepository {

    suspend fun login(matriculation: String, password: String)

    suspend fun getUser(): User?

    fun logout()

    suspend fun updatePassword(matriculation: String, password: String)
}