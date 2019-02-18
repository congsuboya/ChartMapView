package com.suboya.chartmaplib.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.suboya.chartmaplib.data.ChartData;
import com.suboya.chartmaplib.render.ChartRender;

import java.util.ArrayList;
import java.util.List;

public class MapChart extends View {


    private ChartData chartData;
    private float viewWidth;           //View的宽度
    private boolean isFirst; //是否是第一次绘制,用于最初的适配
    private float map_scale = 0;

    private ChartRender mRender;

    public MapChart(Context context) {
        super(context);
        mRender = new ChartRender(this);
    }

    public MapChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRender = new ChartRender(this);
    }


    public ChartData getChartData() {
        return chartData;
    }

    public void setChartData(ChartData chartData) {
        this.chartData = chartData;
        isFirst = true;
        invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //不管高度的设置Mode是什么，直接把View的高度按照宽度适配的缩放倍数进行适配
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (chartData != null) {
            map_scale = width / chartData.getMax_x();
        }
        int height = (int) (chartData.getMax_y() * map_scale);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //保证只在初次绘制的时候进行缩放适配
        if (isFirst) {
            viewWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            //首先重置所有点的坐标，使得map适应屏幕大小
            if (chartData != null) {
                map_scale = viewWidth / chartData.getMax_x();
            }
            //缩放所有Path
            mRender.initFirstData(map_scale);
        }
        //关联缩放和平移后的矩阵
//            scrollScaleGestureDetector.connect(canvas);
//            scrollScaleGestureDetector.setScaleMax(3);//最大缩放倍数
//            scrollScaleGestureDetector.setScalemin(1);//最小缩放倍数
        //绘制Map
        mRender.drawAreaView(canvas);
        isFirst = false;
        super.onDraw(canvas);
    }

    public ChartRender getmRender() {
        return mRender;
    }

    public void setmRender(ChartRender mRender) {
        this.mRender = mRender;
        isFirst = true;
        invalidate();
    }

}
