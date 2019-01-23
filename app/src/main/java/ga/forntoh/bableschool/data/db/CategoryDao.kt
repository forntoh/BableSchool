package ga.forntoh.bableschool.data.db

import ga.forntoh.bableschool.data.model.main.Category

interface CategoryDao {

    fun saveCategories(vararg categories: Category)

    fun retrieveCategories(): MutableList<Category>
}