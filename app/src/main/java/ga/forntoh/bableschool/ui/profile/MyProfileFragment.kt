package ga.forntoh.bableschool.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.ApiService
import ga.forntoh.bableschool.data.model.groupie.ItemProfileData
import ga.forntoh.bableschool.ui.profile.score.ScoreSheetActivity
import ga.forntoh.bableschool.ui.profile.timeTable.TimeTableActivity
import ga.forntoh.bableschool.utils.InsetDecoration
import ga.forntoh.bableschool.utils.StorageUtil
import kotlinx.android.synthetic.main.fragment_my_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MyProfileFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_my_profile, container, false)

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btn_score_sheet.setOnClickListener { activity!!.startActivity(Intent(activity, ScoreSheetActivity::class.java)) }
        btn_time_table.setOnClickListener { activity!!.startActivity(Intent(context, TimeTableActivity::class.java)) }

        val profileDataAdapter = GroupAdapter<ViewHolder>()

        rv_profile_data.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = profileDataAdapter
            addItemDecoration(InsetDecoration(16))
        }

        val profileDataSection = Section().apply { profileDataAdapter.add(this) }

        launch {
            val user = ApiService().getUserProfile(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), StorageUtil.getInstance(context!!).loadPassword()).await()

            profile_username.text = "@${user.username}"
            profile_class.text = user.classe
            if (user.picture.isNullOrEmpty())
                Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(profile_image)
            else
                Picasso.get().load(user.picture).placeholder(R.drawable.placeholder).fit().centerCrop().into(profile_image)
            user.profileData?.map { ItemProfileData(it.key, it.value) }?.let { profileDataSection.update(it) }
        }
    }
}
