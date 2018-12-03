package ga.forntoh.bableschool

import android.os.Bundle
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.Delete
import ga.forntoh.bableschool.adapters.CategoryAdapter
import ga.forntoh.bableschool.model.Category
import ga.forntoh.bableschool.utils.Utils.dealWithData
import ga.forntoh.bableschool.utils.Utils.isConnected
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : BaseActivity() {

    private val adapter by lazy { CategoryAdapter(categoriesList) }
    private val categoriesList = ArrayList<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disableFlags(true)
        enableWhiteStatusBar()

        setSupportActionBar(findViewById(R.id.toolbar))

        EasyRecyclerView().apply {
            setType(EasyRecyclerView.Type.GRID)
            setAdapter(adapter)
            setRecyclerView(findViewById(R.id.rv_categories))
            setItemSpacing(16, null)
            setSpan(2)
            initialize()
        }

        fetchItems()
    }

    private fun fetchItems() {
        val service = RetrofitBuilder.createService(ApiService::class.java)

        if (isConnected(this))
            service.functions
                    .subscribeOn(Schedulers.io())
                    .subscribe({ categories ->
                        Delete.table(Category::class.java)
                        categories.forEach { it.save() }
                        dealWithData(this, categories, categoriesList, adapter)
                    }, { dealWithData(this, (select from Category::class).list, categoriesList, adapter) })
        else dealWithData(this, (select from Category::class).list, categoriesList, adapter)
    }
}
