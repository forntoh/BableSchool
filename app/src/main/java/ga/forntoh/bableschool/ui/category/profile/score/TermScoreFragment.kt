package ga.forntoh.bableschool.ui.category.profile.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.groupie.ItemScoreSummary
import ga.forntoh.bableschool.data.model.main.toScoreView
import ga.forntoh.bableschool.internal.InsetDecoration
import ga.forntoh.bableschool.ui.base.ScopedFragment
import kotlinx.android.synthetic.main.fragment_term_score.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class TermScoreFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: ScoreViewModelFactory by instance()
    private lateinit var viewModel: ScoreViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_term_score, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ScoreViewModel::class.java)
        buildUI()
    }

    private fun buildUI() = launch {
        val term = arguments?.getInt("term") ?: return@launch

        val groupAdapter = GroupAdapter<ViewHolder>()

        rv_term_scores.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = groupAdapter
            addItemDecoration(InsetDecoration(16))
        }

        val sectionHeader = Section().apply { groupAdapter.add(this) }
        val section = Section().apply { groupAdapter.add(this) }

        val scores = when (term) {
            1 -> viewModel.firstTermScores.await()
            2 -> viewModel.secondTermScores.await()
            3 -> viewModel.thirdTermScores.await()
            else -> return@launch
        }

        if (scores.isNullOrEmpty()) return@launch

        sectionHeader.add(ItemScoreSummary(scores.first().termAvg, scores.last().termRank))
        section.update(scores.map { it.toScoreView() })
    }
}
