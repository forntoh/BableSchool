package ga.forntoh.bableschool.data.repository

import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.CategoryDao
import ga.forntoh.bableschool.data.model.main.Category
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class CategoryRepositoryImpl(
        private val categoryDao: CategoryDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : CategoryRepository {

    init {
        bableSchoolDataSource.downloadedCategories.observeForever {
            saveCategories(*it.toTypedArray())
        }
    }

    // Main
    override suspend fun retrieveCategories(): MutableList<Category> {
        return withContext(Dispatchers.IO) {
            initCategoriesData()
            return@withContext categoryDao.retrieveCategories()
        }
    }

    private suspend fun initCategoriesData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.CATEGORIES), 60 * 24 * 7)) {
            bableSchoolDataSource.categories()
            appStorage.setLastSaved(DataKey.CATEGORIES, ZonedDateTime.now())
        }
    }

    private fun saveCategories(vararg categories: Category) = categoryDao.saveCategories(*categories)
}