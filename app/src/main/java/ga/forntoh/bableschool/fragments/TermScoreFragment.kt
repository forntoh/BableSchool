package ga.forntoh.bableschool.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.adapters.TermScoresAdapter
import ga.forntoh.bableschool.model.Score
import ga.forntoh.bableschool.utils.Utils
import io.reactivex.schedulers.Schedulers
import java.util.*

class TermScoreFragment : Fragment() {

    private val scores = ArrayList<Score>()
    private val termScoresAdapter by lazy { TermScoresAdapter(scores) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_term_score, container, false)
        if (arguments != null) {
            EasyRecyclerView()
                    .setType(EasyRecyclerView.Type.VERTICAL)
                    .setAdapter(termScoresAdapter)
                    .setRecyclerView(v.findViewById(R.id.rv_term_scores))
                    .setItemSpacing(16, null)
                    .initialize()
            fetchItems(arguments!!.getInt("term"))
        }
        return v
    }

    @SuppressLint("CheckResult")
    private fun fetchItems(term: Int) {
        val service = RetrofitBuilder.createService(ApiService::class.java)
        service.getTermScores(StorageUtil.getInstance(activity!!).loadMatriculation(), term, Utils.termYear)
                .subscribeOn(Schedulers.io())
                .subscribe({ scores ->
                    //TODO: Save scores
                    scores.forEach { it.term = term }
                    dealWithScores(scores)
                }) { t -> t.printStackTrace() }
    }

    private fun dealWithScores(o: Collection<Score>) {
        scores.apply { clear(); addAll(o); add(0, Score()) }
        activity!!.runOnUiThread { termScoresAdapter.notifyDataSetChanged() }
    }

}
