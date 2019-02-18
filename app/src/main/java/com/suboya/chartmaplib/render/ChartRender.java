package com.suboya.chartmaplib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;

import com.suboya.chartmaplib.charts.MapChart;
import com.suboya.chartmaplib.data.BaseData;
import com.suboya.chartmaplib.data.ChartData;

import java.util.ArrayList;
import java.util.List;

public class ChartRender {
    private Paint innerPaint, outerPaint;//画省份的内部画笔和外圈画笔
    private int selectPosition;

    public ChartRender(MapChart mapChart) {
        this.mapChart = mapChart;
        //初始化省份内部画笔
        innerPaint = new Paint();
        innerPaint.setColor(Color.BLUE);
        innerPaint.setAntiAlias(true);
        //初始化省份外框画笔
        outerPaint = new Paint();
        outerPaint.setColor(Color.GRAY);
        outerPaint.setAntiAlias(true);
        outerPaint.setStrokeWidth(1);
        outerPaint.setStyle(Paint.Style.STROKE);
    }

    private MapChart mapChart;

    public void drawAreaView(Canvas canvas) {
        ChartData chartData = mapChart.getChartData();
        if (chartData.getChartData().size() > 0) {
            outerPaint.setStrokeWidth(1);
            //首先记录下点击的省份的下标，先把其他的省份绘制完，
            for (int i = 0; i < chartData.getChartData().size(); i++) {
                if (chartData.getChartData().get(i).isSelect()) {
                    selectPosition = i;
                } else {
                    //此时绘制其他省份，边框画笔的宽度为1
                    innerPaint.setColor(chartData.getChartData().get(i).getColor());
                    outerPaint.setColor(chartData.getChartData().get(i).getLineColor());
                    for (Path p : chartData.getChartData().get(i).getListPath()) {
                        canvas.drawPath(p, innerPaint);
                        canvas.drawPath(p, outerPaint);
                    }
                }
            }
            //再绘制点击所在的省份,此时画笔宽度设为2.5，以达到着重显示的效果
            innerPaint.setColor(chartData.getChartData().get(selectPosition).getColor());
            outerPaint.setColor(chartData.getChartData().get(selectPosition).getLineColor());
            outerPaint.setStrokeWidth(2.5f);
            for (Path p : chartData.getChartData().get(selectPosition).getListPath()) {
                canvas.drawPath(p, innerPaint);
                canvas.drawPath(p, outerPaint);
            }
        }
    }

    public void initFirstData(float scale){
        ChartData chartData = mapChart.getChartData();
        if (chartData.getChartData().size() > 0)
            //map的左右上下4个临界点
            chartData.setMax_x(chartData.getMax_x() * scale);
        chartData.setMin_x(chartData.getMin_x() * scale);
        chartData.setMax_y(chartData.getMax_y() * scale);
        chartData.setMin_y(chartData.getMin_y() * scale);
        for (BaseData province : chartData.getChartData()) {
            innerPaint.setColor(province.getColor());
            List<Region> regionList = new ArrayList<>();
            List<Path> pathList = new ArrayList<>();
            for (Path p : province.getListPath()) {
                //遍历Path中的所有点，重置点的坐标
                Path newpath = resetPath(p, scale, regionList);
                pathList.add(newpath);
            }
            province.setListPath(pathList);
            //判断点是否在path画出的区域内
            province.setRegionList(regionList);
        }
    }

    private Path resetPath(Path path, float scale, List<Region> regionList) {
        List<PointF> list = new ArrayList<>();
        PathMeasure pathmesure = new PathMeasure(path, true);
        float[] s = new float[2];
        //按照缩放倍数重置Path内的所有点
        for (int i = 0; i < pathmesure.getLength(); i = i + 2) {
            pathmesure.getPosTan(i, s, null);
            PointF p = new PointF(s[0] * scale, s[1] * scale);
            list.add(p);
        }
        //重绘缩放后的Path
        Path path1 = new Path();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                path1.moveTo(list.get(i).x, list.get(i).y);
            } else {
                path1.lineTo(list.get(i).x, list.get(i).y);
            }
        }
        path1.close();
        //构造Path对应的Region,用于判断点击的点是否在Path内
        RectF rf = new RectF();
        Region re = new Region();
        path1.computeBounds(rf, true);
        re.setPath(path1, new Region((int) rf.left, (int) rf.top, (int) rf.right, (int) rf.bottom));
        regionList.add(re);
        return path1;
    }
}
