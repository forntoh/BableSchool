package ga.forntoh.bableschool.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.ApiService
import ga.forntoh.bableschool.data.model.groupie.ItemCategory
import ga.forntoh.bableschool.data.model.main.Category
import ga.forntoh.bableschool.data.model.main.Category_Table
import ga.forntoh.bableschool.utils.InsetDecoration
import ga.forntoh.bableschool.utils.enableWhiteStatusBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : BaseActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        disableFlags(true)
        enableWhiteStatusBar()

        setSupportActionBar(toolbar as Toolbar?)

        initializeUI()
    }

    private fun initializeUI() {

        val categoriesAdapter = GroupAdapter<ViewHolder>().apply {
            setOnItemClickListener(onItemClickListener)
            spanCount = 2
        }

        rv_categories.apply {
            layoutManager = GridLayoutManager(context, categoriesAdapter.spanCount).apply {
                spanSizeLookup = categoriesAdapter.spanSizeLookup
            }
            adapter = categoriesAdapter
            addItemDecoration(InsetDecoration(16))
        }

        val categoriesSection = Section()

        categoriesAdapter.add(categoriesSection)

        val apiService = ApiService()

        launch {
            val categories = apiService.functions.await()

            categories.forEach { it.save() }

            categoriesSection.update(categories.map { ItemCategory(it.title, it.thumbnail, it.color) })
        }
    }

    private val onItemClickListener = OnItemClickListener { item, _ ->
        if (item is ItemCategory)
            startActivity(Intent(this, CategoryActivity::class.java).apply {
                putExtra("category", Gson().toJson((select from Category::class where Category_Table.title.eq(item.title)).querySingle()))
            })
    }
}
