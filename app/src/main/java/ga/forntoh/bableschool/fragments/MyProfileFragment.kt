package ga.forntoh.bableschool.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.squareup.picasso.Picasso
import ga.forntoh.bableschool.*
import ga.forntoh.bableschool.adapters.ProfileDataAdapter
import ga.forntoh.bableschool.model.User
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_my_profile.*

/**
 * A simple [Fragment] subclass.
 */
class MyProfileFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_my_profile, container, false)

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_score_sheet.setOnClickListener { activity!!.startActivity(Intent(activity, ScoreSheetActivity::class.java)) }
        btn_time_table.setOnClickListener { activity!!.startActivity(Intent(context, TimeTableActivity::class.java)) }

        val service = RetrofitBuilder.createService(ApiService::class.java)
        service.getUserProfile(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), StorageUtil.getInstance(context!!).loadPassword())
                .subscribeOn(Schedulers.io())
                .subscribe({ user -> activity!!.runOnUiThread { showItems(user) } }) { it.printStackTrace() }
    }

    @SuppressLint("SetTextI18n")
    private fun showItems(user: User) {
        profile_username.text = "@${user.username!!}"
        profile_class.text = user.classe
        if (TextUtils.isEmpty(user.picture))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(profile_image)
        else
            Picasso.get().load(user.picture).placeholder(R.drawable.placeholder).fit().centerCrop().into(profile_image)

        val adapter = ProfileDataAdapter(user.profileData!!)
        EasyRecyclerView()
                .setType(EasyRecyclerView.Type.VERTICAL)
                .setAdapter(adapter)
                .setRecyclerView(rv_profile_data)
                .setItemSpacing(16, null)
                .initialize()
    }
}
