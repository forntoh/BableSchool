package ga.forntoh.bableschool.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.forntoh.viewstatusmanager.Status
import com.forntoh.viewstatusmanager.StatusManager

import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.model.AnnualRank
import ga.forntoh.bableschool.utils.Utils
import io.reactivex.schedulers.Schedulers

class YearScoreFragment : Fragment() {

    private lateinit var v: View
    private lateinit var average: TextView
    private lateinit var position: TextView
    private val statusManager by lazy { StatusManager.from(v) }

    @SuppressLint("CheckResult")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_year_score, container, false)

        position = v.findViewById(R.id.student_average)
        average = v.findViewById(R.id.student_position)

        statusManager.setStatus(R.id.student_average, Status.PROGRESS)
        statusManager.setStatus(R.id.student_position, Status.PROGRESS)

        val service = RetrofitBuilder.createService(ApiService::class.java)
        service.annualRank(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), Utils.termYear)
                .subscribeOn(Schedulers.io())
                .subscribe({ dealWithRank(it) }, { dealWithError(it) })
        return v
    }

    private fun dealWithError(o: Throwable) {
        o.printStackTrace()
        statusManager.setStatus(R.id.student_average, Status.FAILURE)
        statusManager.setStatus(R.id.student_position, Status.FAILURE)
    }

    private fun dealWithRank(o: AnnualRank?) {
        statusManager.setStatus(R.id.student_position, Status.SUCCESS)
        statusManager.setStatus(R.id.student_average, Status.SUCCESS)
        position.post { position.text = o?.position ?: "N/A" }
        average.post {
            average.text = Utils.formatScore(java.lang.Double.parseDouble(o?.average ?: "0"))
        }
    }

}
