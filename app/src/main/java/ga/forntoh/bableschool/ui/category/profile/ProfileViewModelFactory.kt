package ga.forntoh.bableschool.ui.category.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ga.forntoh.bableschool.data.repository.ProfileRepository

class ProfileViewModelFactory(private val userRepository: ProfileRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(userRepository) as T
    }
}