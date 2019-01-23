package ga.forntoh.bableschool.ui.category.profile.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.ui.base.ScopedFragment
import ga.forntoh.bableschool.utilities.in2Dp
import kotlinx.android.synthetic.main.fragment_year_score.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class YearScoreFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: ScoreViewModelFactory by instance()
    private lateinit var viewModel: ScoreViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_year_score, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ScoreViewModel::class.java)
        buildUI()
    }

    private fun buildUI() = launch {
        val score = viewModel.annualRank.await()
        student_position.text = score?.position ?: return@launch
        student_average.text = score.average?.toDouble()!!.in2Dp
    }

}
