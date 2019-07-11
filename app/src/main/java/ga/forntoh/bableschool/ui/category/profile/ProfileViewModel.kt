package ga.forntoh.bableschool.ui.category.profile

import androidx.lifecycle.ViewModel
import ga.forntoh.bableschool.data.repository.ProfileRepository
import ga.forntoh.bableschool.internal.lazyDeferred

class ProfileViewModel(private val userRepository: ProfileRepository) : ViewModel() {

    suspend fun login(matriculation: String, password: String) =
            userRepository.login(matriculation, password)

    suspend fun updatePassword(matriculation: String, password: String) =
            userRepository.updatePassword(matriculation, password)

    fun logout() = userRepository.logout()

    val user by lazyDeferred {
        userRepository.getUser()
    }
}