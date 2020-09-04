package ga.forntoh.bableschool.ui.category.profile.score

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.main.ScoreCoursePair
import ga.forntoh.bableschool.ui.base.ScopedFragment
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.util.*

class YearGraphFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: ScoreViewModelFactory by instance()
    private lateinit var viewModel: ScoreViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_year_graph, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

        val seriesData = ArrayList<DataEntry>()
        val allScores = ArrayList<List<ScoreCoursePair>>()

        launch {
            v.findViewById<AnyChartView>(R.id.any_chart_view).setProgressBar(v.findViewById(R.id.chart_progressBar))

            val firstTermScores = viewModel.firstTermScoresAsync.await()
            val secondTermScores = viewModel.secondTermScoresAsync.await()
            val thirdTermScores = viewModel.thirdTermScoresAsync.await()

            allScores.add(firstTermScores)
            allScores.add(secondTermScores)
            allScores.add(thirdTermScores)

            if (firstTermScores.isNotEmpty()) {
                firstTermScores.map { it.score.firstSequenceMark }.apply { seriesData.add(CustomDataEntry("Seq 1", this)) }
                firstTermScores.map { it.score.secondSequenceMark }.apply { seriesData.add(CustomDataEntry("Seq 2", this)) }
            }
            if (secondTermScores.isNotEmpty()) {
                secondTermScores.map { it.score.firstSequenceMark }.apply { seriesData.add(CustomDataEntry("Seq 3", this)) }
                secondTermScores.map { it.score.secondSequenceMark }.apply { seriesData.add(CustomDataEntry("Seq 4", this)) }
            }
            if (thirdTermScores.isNotEmpty()) {
                thirdTermScores.map { it.score.firstSequenceMark }.apply { seriesData.add(CustomDataEntry("Seq 5", this)) }
                thirdTermScores.map { it.score.secondSequenceMark }.apply { seriesData.add(CustomDataEntry("Seq 6", this)) }
            }

            LongOperation(v.findViewById(R.id.any_chart_view), seriesData, allScores).execute()
        }
        return v
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LongOperation(
            private val anyChartView: AnyChartView,
            val seriesData: ArrayList<DataEntry>,
            val allScores: ArrayList<List<ScoreCoursePair>>
    ) : AsyncTask<Void, Void, Wrapper>() {

        override fun doInBackground(vararg params: Void): Wrapper = Wrapper(seriesData, allScores)

        override fun onPostExecute(wrapper: Wrapper) {
            val allScores = wrapper.allScores

            val cartesian = AnyChart.line().apply {
                animation(true)
                padding(16.0, 0.0, 20.0, 8.0)
                crosshair().enabled(true)
                crosshair().yLabel(true).yStroke(null as Stroke?, null, null, null as String?, null as String?)
                tooltip().positionMode(TooltipPositionMode.POINT)
                yAxis(0).title("Mark/20")
                xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)
            }

            for (i in 0 until allScores[0].size) {
                val mapping = with(Set.instantiate()) { data(wrapper.seriesData); mapAs("{ x: 'x', value: 'value${if (i == 0) "" else i}' }") }
                cartesian.line(mapping).apply {
                    name(allScores[0][i].course.abbr)
                    hovered().markers().enabled(true)
                    hovered().markers().type(MarkerType.CIRCLE).size(4.0)
                    tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5.0).offsetY(5.0)
                }
            }

            anyChartView.setChart(cartesian.apply {
                legend().enabled(true)
                legend().fontSize(13.0)
                legend().padding(0.0, 16.0, 10.0, 16.0)
            })
        }
    }

    private class CustomDataEntry(x: String, scores: List<Number>) : ValueDataEntry(x, scores[0]) {
        init {
            for (i in 1 until scores.size) setValue("value$i", scores[i])
        }
    }

    class Wrapper(var seriesData: List<DataEntry>, var allScores: List<List<ScoreCoursePair>>)
}
