package com.chart.client.chart;

import android.graphics.Canvas;
import android.graphics.Color;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.renderer.YAxisRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Hongmingwei on 2018/4/3.
 * Email: 648600445@qq.com
 */

public class TickYAxisRenderer extends YAxisRenderer {
    /**
     * TAG
     */
    private static final String TAG = TickYAxisRenderer.class.getSimpleName();

    protected TickYAxis mYAxis;

    public TickYAxisRenderer(ViewPortHandler viewPortHandler, TickYAxis yAxis, Transformer trans) {
        super(viewPortHandler, yAxis, trans);
        mYAxis = yAxis;
    }

    @Override
    protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
        final int from = mYAxis.isDrawBottomYLabelEntryEnabled() ? 0 : 1;
        final int to = mYAxis.isDrawTopYLabelEntryEnabled()
                ? mYAxis.mEntryCount
                : (mYAxis.mEntryCount - 1);
        int count = to / 2;
        boolean odevityFlag;
        if (to % 2 == 0){
            odevityFlag = true;
        } else {
            odevityFlag = false;
        }
        float offsetNews = 0f;
        for (int i = from; i < to; i++) {
            String text = mYAxis.getFormattedLabel(i);
            if (mYAxis.drawDifferentUpDownColorToLableEnabled()) {
                if (odevityFlag) {
                    if (i < count) {
                        mAxisLabelPaint.setColor(mYAxis.getDownTextColor());
                        offsetNews = 0f;
                    } else {
                        mAxisLabelPaint.setColor(mYAxis.getUpTextColor());

                        offsetNews = offset * 2;
                    }
                } else {
                    if (i < count) {
                        mAxisLabelPaint.setColor(mYAxis.getDownTextColor());
                        offsetNews = 0f;
                    } else if (i > count) {
                        mAxisLabelPaint.setColor(mYAxis.getUpTextColor());
                        offsetNews = offset * 2;
                    } else {
                        mAxisLabelPaint.setColor(mYAxis.getTextColor());
                        offsetNews = 0f;
                    }
                }
            }
            c.drawText(text, fixedPosition, positions[i * 2 + 1] + offsetNews, mAxisLabelPaint);
        }
    }
}
