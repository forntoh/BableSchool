package ga.forntoh.bableschool.ui.profile.score

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.raizlabs.android.dbflow.kotlinextensions.from
import com.raizlabs.android.dbflow.kotlinextensions.list
import com.raizlabs.android.dbflow.kotlinextensions.select
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.data.model.Score
import java.util.*

class YearGraphFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_year_graph, container, false)
        LongOperation(v.findViewById<AnyChartView>(R.id.any_chart_view).apply { setProgressBar(v.findViewById(R.id.chart_progressBar)) }).execute()
        return v
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LongOperation internal constructor(private val anyChartView: AnyChartView) : AsyncTask<Void, Void, Wrapper>() {

        override fun doInBackground(vararg params: Void): Wrapper {
            val seriesData = ArrayList<DataEntry>()
            val allScores = ArrayList<List<Score>>()

            var seq = 0
            val scores = (select from Score::class).list
            for (i in 1..3) {
                scores.filter { it.term == i }.apply {
                    allScores.add(this)
                    map { it.firstSequenceMark }.apply { seriesData.add(CustomDataEntry("Seq ${++seq}", this)) }
                    map { it.secondSequenceMark }.apply {
                        seriesData.add(CustomDataEntry("Seq ${++seq}", this))
                    }
                }
            }
            return Wrapper(seriesData, allScores)
        }

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
                    name(allScores[0][i].course!!.abbr)
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

    private inner class CustomDataEntry internal constructor(x: String, scores: List<Number>) : ValueDataEntry(x, scores[0]) {
        init {
            for (i in 1 until scores.size) setValue("value$i", scores[i])
        }
    }

    inner class Wrapper(var seriesData: List<DataEntry>, var allScores: List<List<Score>>)
}
