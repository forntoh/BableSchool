package ga.forntoh.bableschool.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.model.AnnualRank
import ga.forntoh.bableschool.utils.Utils
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_year_score.*

class YearScoreFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_year_score, container, false)

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val service = RetrofitBuilder.createService(ApiService::class.java)
        service.annualRank(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), Utils.termYear)
                .subscribeOn(Schedulers.io())
                .subscribe({ dealWithRank(it) }, { })
    }

    private fun dealWithRank(o: AnnualRank?) {
        student_position.post { student_average.text = o?.position ?: "N/A" }
        student_average.post {
            student_average.text = Utils.formatScore(java.lang.Double.parseDouble(o?.average
                    ?: "0"))
        }
    }

}
