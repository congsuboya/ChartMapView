package com.suboya.chartmaplib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.suboya.chartmaplib.charts.MapChart;
import com.suboya.chartmaplib.data.ChartData;
import com.suboya.chartmaplib.utils.SvgUtil;

import java.util.ArrayList;
import java.util.List;

public class NativeMapActivity extends AppCompatActivity {

    private LinearLayout holder;
    private MapChart mChart;
    private ChartData mData;

    private List<Integer> colorList = new ArrayList<>();

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
        colorList.clear();
        colorList.add(Color.parseColor("#3D55CC"));
        colorList.add(Color.parseColor("#1DB8AB"));
        colorList.add(Color.parseColor("#FF8106"));
        colorList.add(Color.parseColor("#FCC565"));
        colorList.add(Color.parseColor("#8150C7"));
        colorList.add(Color.parseColor("#5AAAFA"));
        colorList.add(Color.parseColor("#F06060"));
        colorList.add(Color.parseColor("#FFA5B4"));
        colorList.add(Color.parseColor("#2468A2"));
        colorList.add(Color.parseColor("#54D1D1"));
        mData = new SvgUtil(NativeMapActivity.this).getProvinces();
        for (int i =0;i<mData.getChartData().size();i++){
            mData.getChartData().get(i).setColor(colorList.get(i%colorList.size()));
        }

        mChart.setChartData(mData);
    }
}
