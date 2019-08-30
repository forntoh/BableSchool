package ga.forntoh.bableschool.data.repository

import androidx.lifecycle.LiveData
import ga.forntoh.bableschool.data.AppStorage
import ga.forntoh.bableschool.data.db.CategoryDao
import ga.forntoh.bableschool.data.model.main.Category
import ga.forntoh.bableschool.data.network.BableSchoolDataSource
import ga.forntoh.bableschool.internal.DataKey
import ga.forntoh.bableschool.utilities.isFetchNeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class CategoryRepositoryImpl(
        private val categoryDao: CategoryDao,
        private val bableSchoolDataSource: BableSchoolDataSource,
        private val appStorage: AppStorage
) : CategoryRepository() {

    init {
        bableSchoolDataSource.downloadedCategories.observeForever {
            scope.launch {
                saveCategories(*it.toTypedArray())
            }
        }
    }

    // Main
    override suspend fun retrieveCategories(): LiveData<MutableList<Category>> {
        return withContext(Dispatchers.IO) {
            initCategoriesData()
            return@withContext categoryDao.retrieveCategories()
        }
    }

    private suspend fun initCategoriesData() {
        if (isFetchNeeded(appStorage.getLastSaved(DataKey.CATEGORIES), 60 * 24 * 7) || categoryDao.numberOfItems() <= 0) {
            bableSchoolDataSource.categories()
            appStorage.setLastSaved(DataKey.CATEGORIES, ZonedDateTime.now())
        }
    }

    private suspend fun saveCategories(vararg categories: Category) = categoryDao.saveCategories(*categories)

    override fun passwordChanged() = appStorage.getChangedPassword()
}