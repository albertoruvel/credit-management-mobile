package com.albertoruvel.credit.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.albertoruvel.credit.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class SummaryFragment extends Fragment {

    @BindView(R.id.summaryPieChart)
    PieChartView pieChart;

    private View currentView;

    public SummaryFragment() {
        // Required empty public constructor
    }

    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(currentView == null){
            currentView = inflater.inflate(R.layout.fragment_summary, container, false);
            ButterKnife.bind(this, currentView);
            init();
        }
        return currentView;
    }

    private void init() {
        List<SliceValue> values = new ArrayList<>();
        SliceValue value = new SliceValue(10, ChartUtils.pickColor());
        value.setLabel("RED");
        SliceValue value1 = new SliceValue(20, ChartUtils.pickColor());
        value1.setLabel("GREEN");
        SliceValue value2 = new SliceValue(30, ChartUtils.pickColor());
        value2.setLabel("GRAY");
        values.add(value);
        values.add(value1);
        values.add(value2);
        PieChartData data = new PieChartData(values);
        data.setHasLabels(true);
        data.setSlicesSpacing(8);
        pieChart.setPieChartData(data);
    }

}
