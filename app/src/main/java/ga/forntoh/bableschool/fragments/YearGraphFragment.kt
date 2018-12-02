package ga.forntoh.bableschool.fragments


import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.annimon.stream.Collectors
import com.annimon.stream.Stream
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import ga.forntoh.bableschool.ApiService
import ga.forntoh.bableschool.R
import ga.forntoh.bableschool.RetrofitBuilder
import ga.forntoh.bableschool.StorageUtil
import ga.forntoh.bableschool.model.Score
import ga.forntoh.bableschool.utils.Utils
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class YearGraphFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_year_graph, container, false)

        val anyChartView = v.findViewById<AnyChartView>(R.id.any_chart_view)
        anyChartView.setProgressBar(v.findViewById(R.id.chart_progressBar))

        LongOperation(anyChartView).execute()
        return v
    }

    @SuppressLint("StaticFieldLeak")
    private inner class LongOperation internal constructor(private val anyChartView: AnyChartView) : AsyncTask<Void, Void, Wrapper>() {

        @SuppressLint("CheckResult")
        override fun doInBackground(vararg params: Void): Wrapper {
            val seriesData = ArrayList<DataEntry>()
            val allScores = ArrayList<List<Score>>()

            val service = RetrofitBuilder.createService(ApiService::class.java)
            for (i in 1..3)
                service.getTermScores(StorageUtil.getInstance(activity!!).loadMatriculation(), i, Utils.termYear)
                        .subscribe({ scores ->
                            //TODO: Load Scores from local db
                            allScores.add(scores)
                        }) { t -> t.printStackTrace() }

            seriesData.add(CustomDataEntry("Seq 1", Stream.of(allScores[0]).map { it.firstSequenceMark }.collect(Collectors.toList())))
            seriesData.add(CustomDataEntry("Seq 2", Stream.of(allScores[0]).map { it.secondSequenceMark }.collect(Collectors.toList())))
            seriesData.add(CustomDataEntry("Seq 3", Stream.of(allScores[1]).map { it.firstSequenceMark }.collect(Collectors.toList())))
            seriesData.add(CustomDataEntry("Seq 4", Stream.of(allScores[1]).map { it.secondSequenceMark }.collect(Collectors.toList())))
            seriesData.add(CustomDataEntry("Seq 5", Stream.of(allScores[2]).map { it.firstSequenceMark }.collect(Collectors.toList())))
            seriesData.add(CustomDataEntry("Seq 6", Stream.of(allScores[2]).map { it.secondSequenceMark }.collect(Collectors.toList())))

            return Wrapper(seriesData, allScores)
        }

        override fun onPostExecute(wrapper: Wrapper) {
            val seriesData = wrapper.seriesData
            val allScores = wrapper.allScores

            val cartesian = AnyChart.line()

            cartesian.animation(true)

            cartesian.padding(16.0, 0.0, 20.0, 8.0)

            cartesian.crosshair().enabled(true)
            cartesian.crosshair()
                    .yLabel(true)
                    .yStroke(null as Stroke?, null, null, null as String?, null as String?)

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

            cartesian.yAxis(0).title("Mark/20")
            cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)

            val set = Set.instantiate()
            set.data(seriesData)

            val mapping0 = set.mapAs("{ x: 'x', value: 'value' }")
            val series0 = cartesian.line(mapping0)
            series0.name(allScores[0][0].course!!.abbr)
            series0.hovered().markers().enabled(true)
            series0.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4.0)
            series0.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5.0)
                    .offsetY(5.0)

            for (i in 1 until allScores[0].size) {
                val mapping = set.mapAs("{ x: 'x', value: 'value$i' }")
                val series = cartesian.line(mapping)
                series.name(allScores[0][i].course!!.abbr)
                series.hovered().markers().enabled(true)
                series.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4.0)
                series.tooltip()
                        .position("right")
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetX(5.0)
                        .offsetY(5.0)
            }

            cartesian.legend().enabled(true)
            cartesian.legend().fontSize(13.0)
            cartesian.legend().padding(0.0, 16.0, 10.0, 16.0)

            anyChartView.setChart(cartesian)
        }
    }

    private inner class CustomDataEntry internal constructor(x: String, scores: List<Number>) : ValueDataEntry(x, scores[0]) {

        init {
            for (i in 1 until scores.size) setValue("value$i", scores[i])
        }

    }

    inner class Wrapper(seriesData: List<DataEntry>, allScores: List<List<Score>>) {
        var seriesData: List<DataEntry>
            internal set
        var allScores: List<List<Score>>
            internal set

        init {
            this.seriesData = seriesData
            this.allScores = allScores
        }
    }
}// Required empty public constructor
