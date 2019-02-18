package com.suboya.chartmaplib;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.suboya.chartmaplib.charts.MapChart;
import com.suboya.chartmaplib.data.ChartData;
import com.suboya.chartmaplib.utils.SvgUtil;

public class NativeMapActivity extends AppCompatActivity {

    private LinearLayout holder;
    private MapChart mChart;
    private ChartData mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.native_map_layout);
        holder = findViewById(R.id.holder_map);
        initView();
        initMapData();
    }

    private void initView() {
        mChart = new MapChart(this);
        holder.addView(mChart);
    }

    private void initMapData(){
        mData = new SvgUtil(NativeMapActivity.this).getProvinces();
        mChart.setChartData(mData);
    }
}
