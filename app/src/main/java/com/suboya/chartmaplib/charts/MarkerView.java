package com.suboya.chartmaplib.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.suboya.chartmaplib.R;

public class MarkerView extends RelativeLayout {

    private MapChart mapChart;

    public MarkerView(Context context) {
        super(context);
        setupLayoutResource(R.layout.map_marker);
    }


    private void setupLayoutResource(int layoutResource){
        View inflated = LayoutInflater.from(getContext()).inflate(layoutResource, this);
        inflated.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        inflated.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

        // measure(getWidth(), getHeight());
        inflated.layout(0, 0, inflated.getMeasuredWidth(), inflated.getMeasuredHeight());
    }


    public void draw(Canvas canvas, float posX, float posY) {

        int saveId = canvas.save();
        // translate to the correct position and draw
        canvas.translate(posX, posY  - 50);
        draw(canvas);
//        drawPoints[0] = posX;
//        drawPoints[1] = posY - tableChart.getRowHeight();
        canvas.restoreToCount(saveId);
    }

    public MapChart getMapChart() {
        return mapChart;
    }

    public void setMapChart(MapChart mapChart) {
        this.mapChart = mapChart;
    }

}
