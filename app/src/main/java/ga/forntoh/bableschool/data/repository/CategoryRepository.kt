package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.model.main.Category

interface CategoryRepository {

    suspend fun retrieveCategories(): MutableList<Category>

    fun passwordChanged(): Boolean

    fun setPasswordChanged()
}