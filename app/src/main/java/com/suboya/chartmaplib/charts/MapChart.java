package com.suboya.chartmaplib.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.suboya.chartmaplib.data.BaseData;
import com.suboya.chartmaplib.data.ChartData;
import com.suboya.chartmaplib.data.HighlightData;
import com.suboya.chartmaplib.render.ChartRender;
import com.suboya.chartmaplib.utils.ScrollScaleGestureDetector;


public class MapChart extends View {


    private ChartData chartData;
    private MarkerView markerView;
    public HighlightData mHighlightData;
    private Context context;
    private float viewWidth;           //View的宽度
    private boolean isFirst; //是否是第一次绘制,用于最初的适配
    private float map_scale = 0;
    private ScrollScaleGestureDetector scrollScaleGestureDetector;//自定义的缩放拖拽手势帮助类
    private ChartRender mRender;

    private ScrollScaleGestureDetector.OnScrollScaleGestureListener onScrollScaleGestureListener = new ScrollScaleGestureDetector.OnScrollScaleGestureListener() {
        @Override
        public void onClick(float x, float y) {
            mHighlightData = null;
            //只有点击在某一个省份内才会触发省份选择接口
            for (BaseData p : chartData.getChartData()) {
                for (Region region : p.getRegionList()) {
                    if (region.contains((int) x, (int) y)) {
                        //重置上一次选中省份的状态
                        chartData.getChartData().get(mRender.getSelectPosition()).setSelect(false);
                        chartData.getChartData().get(mRender.getSelectPosition()).setLineColor(Color.GRAY);
                        setHighLight(x, y, p);
                        //设置新的选中的省份
                        p.setSelect(true);
                        p.setLineColor(Color.BLACK);
                        //暴露到Activity中的接口，把省的名字传过去
//                        onProvinceClickLisener.onChose(p.getName());
                        invalidate();
                        return;
                    }
                }
            }
        }
    };

    private void setHighLight(float x, float y, BaseData p) {
        mHighlightData = new HighlightData();
        mHighlightData.setTouchX(x);
        mHighlightData.setTouchY(y);
        mHighlightData.setName(p.getName());

        if (markerView == null) {
            markerView = new MarkerView(context);
        }
        markerView.setMapChart(this);
    }

    public MapChart(Context context) {
        super(context);
        this.context = context;
        mRender = new ChartRender(this);
        scrollScaleGestureDetector = new ScrollScaleGestureDetector(this, onScrollScaleGestureListener);
    }

    public MapChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mRender = new ChartRender(this);
        scrollScaleGestureDetector = new ScrollScaleGestureDetector(this, onScrollScaleGestureListener);
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
        scrollScaleGestureDetector.connect(canvas);
        scrollScaleGestureDetector.setScaleMax(3);//最大缩放倍数
        scrollScaleGestureDetector.setScalemin(1);//最小缩放倍数
        //绘制Map
        mRender.drawAreaView(canvas);
        isFirst = false;

        if (mHighlightData != null) {
            markerView.draw(canvas, mHighlightData.getTouchX(), mHighlightData.getTouchY());
        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return scrollScaleGestureDetector.onTouchEvent(event);
    }
}
