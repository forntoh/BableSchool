package ga.forntoh.bableschool.fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
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
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ga.forntoh.bableschool.R;
import ga.forntoh.bableschool.model.Score;
import ga.forntoh.bableschool.network.ApiService;
import ga.forntoh.bableschool.network.RetrofitBuilder;
import ga.forntoh.bableschool.store.MyStores;
import ga.forntoh.bableschool.store.StorageUtil;
import ga.forntoh.bableschool.store.StoreRepository;
import ga.forntoh.bableschool.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class YearGraphFragment extends Fragment {


    public YearGraphFragment() {
        // Required empty public constructor
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_year_graph, container, false);

        AnyChartView anyChartView = v.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(v.findViewById(R.id.chart_progressBar));

        new LongOperation(anyChartView).execute();
        return v;
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("StaticFieldLeak")
    private class LongOperation extends AsyncTask<Void, Void, Wrapper> {

        private AnyChartView anyChartView;

        LongOperation(AnyChartView anyChartView) {
            this.anyChartView = anyChartView;
        }

        @SuppressLint("CheckResult")
        @Override
        protected Wrapper doInBackground(Void... params) {
            List<DataEntry> seriesData = new ArrayList<>();
            List<List<Score>> allScores = new ArrayList<>();

            ApiService service = RetrofitBuilder.createService(ApiService.class);
            StoreRepository repository = new StoreRepository(Objects.requireNonNull(getActivity()).getApplication());
            for (int i = 1; i <= 3; i++)
                repository
                        .create(service.getTermScores(StorageUtil.with(getActivity()).loadMatriculation(), i, Utils.getTermYear()), new TypeToken<List<Score>>() {
                        }.getType())
                        .get(MyStores.TERM_SCORE(getContext(), i))
                        .subscribe(scores -> allScores.add((List<Score>) scores), t -> {
                        });

            seriesData.add(new CustomDataEntry("Seq 1", Stream.of(allScores.get(0)).map(Score::getFirstSequenceMark).collect(Collectors.toList())));
            seriesData.add(new CustomDataEntry("Seq 2", Stream.of(allScores.get(0)).map(Score::getSecondSequenceMark).collect(Collectors.toList())));
            seriesData.add(new CustomDataEntry("Seq 3", Stream.of(allScores.get(1)).map(Score::getFirstSequenceMark).collect(Collectors.toList())));
            seriesData.add(new CustomDataEntry("Seq 4", Stream.of(allScores.get(1)).map(Score::getSecondSequenceMark).collect(Collectors.toList())));
            seriesData.add(new CustomDataEntry("Seq 5", Stream.of(allScores.get(2)).map(Score::getFirstSequenceMark).collect(Collectors.toList())));
            seriesData.add(new CustomDataEntry("Seq 6", Stream.of(allScores.get(2)).map(Score::getSecondSequenceMark).collect(Collectors.toList())));

            return new Wrapper(seriesData, allScores);
        }

        @Override
        protected void onPostExecute(Wrapper wrapper) {
            List<DataEntry> seriesData = wrapper.getSeriesData();
            List<List<Score>> allScores = wrapper.getAllScores();

            Cartesian cartesian = AnyChart.line();

            cartesian.animation(true);

            cartesian.padding(16d, 0d, 20d, 8d);

            cartesian.crosshair().enabled(true);
            cartesian.crosshair()
                    .yLabel(true)
                    .yStroke((Stroke) null, null, null, (String) null, (String) null);

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

            cartesian.yAxis(0).title("Mark/20");
            cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

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
        }
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, List<Number> scores) {
            super(x, scores.get(0));
            for (int i = 1; i < scores.size(); i++) setValue("value" + i, scores.get(i));
        }

    }

    public class Wrapper {
        List<DataEntry> seriesData;
        List<List<Score>> allScores;

        public Wrapper(List<DataEntry> seriesData, List<List<Score>> allScores) {
            this.seriesData = seriesData;
            this.allScores = allScores;
        }

        public List<DataEntry> getSeriesData() {
            return seriesData;
        }

        public List<List<Score>> getAllScores() {
            return allScores;
        }
    }
}
