package com.chart.client.chart;

import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.chart.client.MyApplication;
import com.chart.client.R;
import com.chart.client.chartmodel.TickChartModel;
import com.chart.client.utils.DateUtil;
import com.chart.client.utils.DensityUtils;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by Hongmingwei on 2018/3/5.
 * Email: 648600445@qq.com
 */

public class TickChartMarkerView implements IMarker {

    private static final String TAG = TickChartMarkerView.class.getSimpleName();
    /**
     * Params
     */
    private String xValue, yLeftValue, yRightValue;
    private TickChartModel model;

    public TickChartMarkerView(TickChartModel model) {
        this.model = model;
    }

    @Override
    public MPPointF getOffset() {
        return null;
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float v, float v1) {
        return null;
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        int x = (int) entry.getX();
        xValue = DateUtil.getShortDateJustHour(model.getData().get(x).getTime());
        yLeftValue = String.valueOf(model.getData().get(x).getPrice());
        yRightValue =  String.valueOf(model.getData().get(x).getPrice());
    }

    @Override
    public void draw(Canvas canvas, float v, float v1) {
        Context mContext = MyApplication.getInstance().getApplicationContext();
        Paint textPaint = new Paint();
        textPaint.setTextSize(DensityUtils.sp2px(mContext, 10));
        textPaint.setColor(Color.WHITE);

        Paint lablePaint = new Paint();
        lablePaint.setColor(MyApplication.getInstance().getBaseContext().getResources()
                .getColor(R.color.c_0e2947));

        RectF rect;
        //1920*1080进行基准适配
        int px40 = DensityUtils.dip2px(mContext, 13);
        int px90 = DensityUtils.dip2px(mContext, 20);
        int px170 = DensityUtils.dip2px(mContext, 47);
        int px50 = DensityUtils.dip2px(mContext, 17);
        int px10 = DensityUtils.dip2px(mContext, 3);
        int px8 = DensityUtils.dip2px(mContext, 3);
        int px160 = DensityUtils.dip2px(mContext, 60);
        int px180 = DensityUtils.dip2px(mContext, 50);
        int px80 = DensityUtils.dip2px(mContext, 17);
        int px20 = DensityUtils.dip2px(mContext, 7);
        int px12 = DensityUtils.dip2px(mContext, 4);
        int px85 = DensityUtils.dip2px(mContext, 18);
        int px185 = DensityUtils.dip2px(mContext, 57);
        int px190 = DensityUtils.dip2px(mContext, 58);

        if (v - px40 >= px90 && v - px40 <= canvas.getWidth() - px170) {
            rect = new RectF(v - px50, px10, v + px50, px50);
            canvas.drawRoundRect(rect, px8, px8, lablePaint);
            canvas.drawText(xValue, v - px40, px40, textPaint);
        } else if (v - px40 > canvas.getWidth() - px170) {
            rect = new RectF(canvas.getWidth() - px180, px10, canvas.getWidth() - px80, px50);
            canvas.drawRoundRect(rect, px8, px8, lablePaint);
            canvas.drawText(xValue, canvas.getWidth() - px170, px40, textPaint);
        } else {
            rect = new RectF(px80, px10, px180, px50);
            canvas.drawRoundRect(rect, px8, px8, lablePaint);
            canvas.drawText(xValue, px90, px40, textPaint);
        }

        //y轴左侧maker
        rect = new RectF(px80, v1 - px20, px160, v1 + px20);
        canvas.drawRoundRect(rect, px8, px8, lablePaint);
        canvas.drawText(yLeftValue, px90, v1 + px12, textPaint);

        //y轴右侧maker
        rect = new RectF(canvas.getWidth() - px190, v1 - px20, canvas.getWidth() - px85, v1 + px20);
        canvas.drawRoundRect(rect, px8, px8, lablePaint);
        canvas.drawText(yRightValue, canvas.getWidth() - px185, v1 + px12, textPaint);
    }
}
