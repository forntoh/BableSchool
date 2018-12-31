package ga.forntoh.bableschool.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.forntoh.EasyRecyclerView.EasyRecyclerView
import com.raizlabs.android.dbflow.kotlinextensions.*
import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.adapters.TermScoresAdapter
import ga.forntoh.bableschool.model.Score
import ga.forntoh.bableschool.model.Score_Table
import ga.forntoh.bableschool.utils.Utils
import ga.forntoh.bableschool.utils.Utils.dealWithData
import ga.forntoh.bableschool.utils.Utils.isConnected
import io.reactivex.schedulers.Schedulers

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

    private fun fetchItems(term: Int) {
        val service = RetrofitBuilder.createService(ApiService::class.java)
        if (isConnected(activity!!))
            service.getTermScores(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), term, Utils.termYear)
                    .subscribeOn(Schedulers.io())
                    .subscribe({ scores ->
                        (delete(Score::class) where Score_Table.term.eq(term)).execute()
                        if (!scores.isNullOrEmpty()) scores.forEach { it.term = term; it.save() }
                        dealWithData(activity!!, (select from Score::class where Score_Table.term.eq(term)).list.apply { add(0, Score()) }, this.scores, termScoresAdapter)
                    }) { dealWithData(activity!!, (select from Score::class where Score_Table.term.eq(term)).list.apply { add(0, Score()) }, this.scores, termScoresAdapter) }
        else {
            dealWithData(activity!!, (select from Score::class where Score_Table.term.eq(term)).list.apply { add(0, Score()) }, this.scores, termScoresAdapter)
        }
    }
}
