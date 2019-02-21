package com.suboya.chartmaplib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;

import com.suboya.chartmaplib.charts.MapChart;
import com.suboya.chartmaplib.data.BaseData;
import com.suboya.chartmaplib.data.ChartData;
import com.suboya.chartmaplib.data.HighlightData;

import java.util.ArrayList;
import java.util.List;

public class ChartRender {

    private Paint innerPaint, outerPaint,paintText;//画省份的内部画笔和外圈画笔 还有画名称标题

    public int getSelectPosition() {
        return selectPosition;
    }

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


        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setStrokeWidth(5);
        paintText.setTextSize(20);
        paintText.setColor(Color.BLACK);
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
//                    drawCenterText(canvas,chartData.getChartData().get(i));
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


    public void  drawCenterText(Canvas canvas,BaseData data){
        PathMeasure measure = new PathMeasure(data.getListPath().get(0), false);
        float[] pos1 = new float[2];
        measure.getPosTan(measure.getLength() / 2, pos1, null);

        /**
         * 计算字体的宽高
         */
        Rect rect = new Rect();
        paintText.getTextBounds(data.getName(), 0, data.getName().length(), rect);

        int w = rect.width();
        int h = rect.height();


        canvas.drawText(data.getName(), pos1[0] - w / 2, pos1[1] + h, paintText);


        /**
         * 两点求中点
         */
        float[] pos2 = new float[2];
        measure.getPosTan(0, pos2, null);

        float[] point = new float[2];

        point[0] = (pos1[0] + pos2[0]) / 2;
        point[1] = (pos1[1] + pos2[1]) / 2;


//        points.add(point);
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
