package ga.forntoh.bableschool.ui.category.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.groupie.ItemProfileData
import ga.forntoh.bableschool.data.model.main.User
import ga.forntoh.bableschool.internal.InsetDecoration
import ga.forntoh.bableschool.ui.base.ScopedFragment
import ga.forntoh.bableschool.ui.category.CategoryActivity
import ga.forntoh.bableschool.ui.category.profile.score.ScoreSheetActivity
import ga.forntoh.bableschool.ui.category.profile.timeTable.TimeTableActivity
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MyProfileFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: ProfileViewModelFactory by instance()
    private lateinit var viewModel: ProfileViewModel

    lateinit var user: User

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_my_profile, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        buildUI()
    }

    @SuppressLint("SetTextI18n")
    private fun buildUI() = launch {
        (activity as CategoryActivity).srl.isEnabled = false

        btn_score_sheet.setOnClickListener { activity!!.startActivity(Intent(activity, ScoreSheetActivity::class.java)) }
        btn_time_table.setOnClickListener { activity!!.startActivity(Intent(context, TimeTableActivity::class.java)) }

        val profileDataAdapter = GroupAdapter<ViewHolder>()
        profileDataAdapter.setOnItemClickListener(onItemClickListener)

        rv_profile_data.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = profileDataAdapter
            addItemDecoration(InsetDecoration(16))
        }

        val profileDataSection = Section().apply { profileDataAdapter.add(this) }

        user = (viewModel.user.await() ?: return@launch)

        profile_username.text = "@${user.username}"
        profile_class.text = user.classe
        if (user.picture.isNullOrEmpty())
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(profile_image)
        else
            Picasso.get().load(user.picture).placeholder(R.drawable.placeholder).fit().centerCrop().into(profile_image)
        user.profileDataMap().map { ItemProfileData(it.key, it.value) }.let { profileDataSection.update(it) }
    }

    private val onItemClickListener = OnItemClickListener { item, _ ->
        if (item is ItemProfileData && item.key.equals("Password")) {
            MaterialDialog(context!!).show {
                input(inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD) { _, password ->
                    launch { viewModel.updatePassword(user.username!!, password.toString()) }
                }
                title(R.string.change_password)
                positiveButton(R.string.save)
                cornerRadius(8f)
                lifecycleOwner(this@MyProfileFragment)
            }
        }
    }
}
