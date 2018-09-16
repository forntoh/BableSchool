package ga.forntoh.bableschool.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.List;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.Score;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearGraphFragment extends Fragment {


    public YearGraphFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_year_graph, container, false);

        AnyChartView anyChartView = v.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(v.findViewById(R.id.chart_progressBar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(0d, 0d, 20d, 8d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.yAxis(0).title("Mark/20");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<List<Score>> allScores = new ArrayList<>();
        allScores.add(Score.getDummyScores());
        allScores.add(Score.getDummyScores());
        allScores.add(Score.getDummyScores());

        List<DataEntry> seriesData = new ArrayList<>();

        seriesData.add(new CustomDataEntry("Seq 1", Stream.of(allScores.get(0)).map(Score::getFirstSequenceMark).collect(Collectors.toList())));
        seriesData.add(new CustomDataEntry("Seq 2", Stream.of(allScores.get(0)).map(Score::getSecondSequenceMark).collect(Collectors.toList())));
        seriesData.add(new CustomDataEntry("Seq 3", Stream.of(allScores.get(1)).map(Score::getFirstSequenceMark).collect(Collectors.toList())));
        seriesData.add(new CustomDataEntry("Seq 4", Stream.of(allScores.get(1)).map(Score::getSecondSequenceMark).collect(Collectors.toList())));
        seriesData.add(new CustomDataEntry("Seq 5", Stream.of(allScores.get(2)).map(Score::getFirstSequenceMark).collect(Collectors.toList())));
        seriesData.add(new CustomDataEntry("Seq 6", Stream.of(allScores.get(2)).map(Score::getSecondSequenceMark).collect(Collectors.toList())));

        Set set = Set.instantiate();
        set.data(seriesData);

        Mapping mapping0 = set.mapAs("{ x: 'x', value: 'value' }");
        Line series0 = cartesian.line(mapping0);
        series0.name(allScores.get(0).get(0).getCourse().getAbbr());
        series0.hovered().markers().enabled(true);
        series0.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series0.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        for (int i = 1; i < allScores.get(0).size(); i++) {
            Mapping mapping = set.mapAs("{ x: 'x', value: 'value" + i + "' }");
            Line series = cartesian.line(mapping);
            series.name(allScores.get(0).get(i).getCourse().getAbbr());
            series.hovered().markers().enabled(true);
            series.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);
        }

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 16d, 10d, 16d);

        anyChartView.setChart(cartesian);

        return v;
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, List<Number> scores) {
            super(x, scores.get(0));
            for (int i = 1; i < scores.size(); i++) setValue("value" + i, scores.get(i));
        }

    }

}
