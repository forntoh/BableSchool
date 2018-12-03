package ga.forntoh.bableschool.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import ga.forntoh.bableschool.*
import ga.forntoh.bableschool.adapters.ProfileDataAdapter
import ga.forntoh.bableschool.model.User
import io.reactivex.schedulers.Schedulers

/**
 * A simple [Fragment] subclass.
 */
class MyProfileFragment : Fragment() {

    private lateinit var v: View
    private val profileClassV by lazy { v.findViewById<TextView>(R.id.profile_class) }
    private val profileUsernameV by lazy { v.findViewById<TextView>(R.id.profile_username) }
    private val profileImageV by lazy { v.findViewById<CircleImageView>(R.id.profile_image) }
    private val rvProfileDataV by lazy { v.findViewById<RecyclerView>(R.id.rv_profile_data) }

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_my_profile, container, false)

        v.findViewById<View>(R.id.btn_score_sheet).setOnClickListener { activity!!.startActivity(Intent(activity, ScoreSheetActivity::class.java)) }
        v.findViewById<View>(R.id.btn_time_table).setOnClickListener { activity!!.startActivity(Intent(context, TimeTableActivity::class.java)) }

        val service = RetrofitBuilder.createService(ApiService::class.java)
        service.getUserProfile(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), StorageUtil.getInstance(context!!).loadPassword())
                .subscribeOn(Schedulers.io())
                .subscribe({ user -> activity!!.runOnUiThread { showItems(user) } }) { it.printStackTrace() }
        return v
    }

    @SuppressLint("SetTextI18n")
    private fun showItems(user: User) {
        profileUsernameV.text = "@${user.username!!}"
        profileClassV.text = user.classe
        if (TextUtils.isEmpty(user.picture))
            Picasso.get().load(R.drawable.placeholder).fit().centerCrop().into(profileImageV)
        else
            Picasso.get().load(user.picture).placeholder(R.drawable.placeholder).fit().centerCrop().into(profileImageV)

        val adapter = ProfileDataAdapter(user.profileData!!)
        EasyRecyclerView()
                .setType(EasyRecyclerView.Type.VERTICAL)
                .setAdapter(adapter)
                .setRecyclerView(rvProfileDataV)
                .setItemSpacing(16, null)
                .initialize()
    }
}
