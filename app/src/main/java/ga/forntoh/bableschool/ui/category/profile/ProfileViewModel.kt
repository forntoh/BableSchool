package ga.forntoh.bableschool.ui.category.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ga.forntoh.bableschool.data.repository.ProfileRepository

class ProfileViewModel(private val userRepository: ProfileRepository) : ViewModel() {

    init {
        userRepository.scope = viewModelScope
    }

    suspend fun login(matriculation: String, password: String) =
            userRepository.login(matriculation, password)

    suspend fun updatePassword(matriculation: String, password: String) =
            userRepository.updatePassword(matriculation, password)

    fun logout() = userRepository.logout()

    suspend fun user() = userRepository.getUser()
}