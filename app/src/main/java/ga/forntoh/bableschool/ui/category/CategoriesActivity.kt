package ga.forntoh.bableschool.ui.category

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.groupie.ItemCategory
import ga.forntoh.bableschool.data.model.main.toCategoryView
import ga.forntoh.bableschool.internal.InsetDecoration
import ga.forntoh.bableschool.ui.base.BaseActivity
import ga.forntoh.bableschool.utilities.enableWhiteStatusBar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class CategoriesActivity : BaseActivity(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: CategoryViewModelFactory by instance()
    private lateinit var viewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        disableFlags(true)
        enableWhiteStatusBar()
        setSupportActionBar(toolbar as Toolbar?)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CategoryViewModel::class.java)
        buildUI()
    }

    private fun buildUI() = launch {
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

        categoriesSection.update(viewModel.categories.await().map { it.toCategoryView() })

        if (!viewModel.isPasswordChanged) {
            MaterialDialog(this@CategoriesActivity).show {
                title(R.string.change_pass_title)
                message(R.string.change_pass_message)
                positiveButton {
                    viewModel.setPasswordChanged()
                    startActivity(Intent(this@CategoriesActivity, CategoryActivity::class.java).apply {
                        putExtra("id", 4)
                        putExtra("title", "My Profile")
                    })
                }
                negativeButton { dismiss() }
                cornerRadius(8f)
            }
        }
    }

    private val onItemClickListener = OnItemClickListener { item, _ ->
        if (item is ItemCategory)
            startActivity(Intent(this, CategoryActivity::class.java).apply {
                putExtra("id", item.identity)
                putExtra("title", item.title)
            })
    }
}
