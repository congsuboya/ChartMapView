package com.suboya.chartmaplib.data;

import java.util.List;

public class ChartData {

    private List<BaseData> chartData;
    private float Max_x;
    private float Min_x;
    private float Max_y;
    private float Min_y;

    public List<BaseData> getChartData() {
        return chartData;
    }

    public void setChartData(List<BaseData> chartData) {
        this.chartData = chartData;
    }

    public float getMax_x() {
        return Max_x;
    }

    public void setMax_x(float max_x) {
        Max_x = max_x;
    }

    public float getMin_x() {
        return Min_x;
    }

    public void setMin_x(float min_x) {
        Min_x = min_x;
    }

    public float getMax_y() {
        return Max_y;
    }

    public void setMax_y(float max_y) {
        Max_y = max_y;
    }

    public float getMin_y() {
        return Min_y;
    }

    public void setMin_y(float min_y) {
        Min_y = min_y;
    }
}
