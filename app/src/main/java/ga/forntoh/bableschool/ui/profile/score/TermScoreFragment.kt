package ga.forntoh.bableschool.ui.profile.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raizlabs.android.dbflow.kotlinextensions.delete
import com.raizlabs.android.dbflow.kotlinextensions.save
import com.raizlabs.android.dbflow.kotlinextensions.where
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.ApiService
import ga.forntoh.bableschool.data.model.Score
import ga.forntoh.bableschool.data.model.Score_Table
import ga.forntoh.bableschool.data.model.groupie.ItemScore
import ga.forntoh.bableschool.data.model.groupie.ItemScoreSummary
import ga.forntoh.bableschool.utils.InsetDecoration
import ga.forntoh.bableschool.utils.StorageUtil
import ga.forntoh.bableschool.utils.Utils
import kotlinx.android.synthetic.main.fragment_term_score.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class TermScoreFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_term_score, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val term = arguments!!.getInt("term")
        val groupAdapter = GroupAdapter<ViewHolder>()

        rv_term_scores.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = groupAdapter
            addItemDecoration(InsetDecoration(16))
        }

        val sectionHeader = Section().apply { groupAdapter.add(this) }
        val section = Section().apply { groupAdapter.add(this) }

        launch {
            val scores = ApiService().getTermScores(StorageUtil.getInstance(activity!!.baseContext).loadMatriculation(), term, Utils.termYear).await()
            (delete(Score::class) where Score_Table.term.eq(term)).execute()

            var total = 0.0
            scores.forEach { it.term = term; it.save(); total += it.scoreAverage }

            sectionHeader.add(ItemScoreSummary(total / scores.size, scores.last().termRank))
            section.update(scores.map { ItemScore(it.course, it.firstSequenceMark, it.secondSequenceMark, it.rank, it.termRank, it.scoreAverage) })
        }
    }
}
