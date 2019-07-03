package ga.forntoh.bableschool.data.db

import com.dbflow5.query.select
import com.dbflow5.structure.save
import ga.forntoh.bableschool.data.model.main.Category

class CategoryDaoImpl(val database: AppDatabase) : CategoryDao {

    override fun saveCategories(vararg categories: Category) =
            categories.forEach { it.save(database) }

    override fun retrieveCategories() =
            (select from Category::class).queryList(database)

}