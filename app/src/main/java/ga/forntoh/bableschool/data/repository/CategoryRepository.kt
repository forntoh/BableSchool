package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.model.main.Category

abstract class CategoryRepository : BaseRepository() {

    abstract suspend fun retrieveCategories(): LiveData<MutableList<Category>>

    abstract fun passwordChanged(): Boolean

    abstract fun setPasswordChanged()
}