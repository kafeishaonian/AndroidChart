package com.chart.client.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.chart.client.MyApplication;
import com.chart.client.chartmodel.KlineChartModel;
import com.chart.client.utils.DensityUtils;
import com.chart.client.utils.TimeUtils;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by Hongmingwei on 2018/3/28.
 * Email: 648600445@qq.com
 */

public class CandleStackChartMarkerView implements IMarker{

    private static final String TAG = CandleStackChartMarkerView.class.getSimpleName();

    private String xValue,yLeftValue;
    private KlineChartModel model;

    public CandleStackChartMarkerView(KlineChartModel model) {
        this.model = model;
    }

    @Override
    public MPPointF getOffset() {
        return null;
    }


    @Override
    public MPPointF getOffsetForDrawingAtPoint(float v, float v1) {
        return new MPPointF(v, v1);
    }

    @Override
    public void refreshContent(Entry entry, Highlight highlight) {
        int x=(int)entry.getX();
        xValue= TimeUtils.getTimeMD(model.getTime().get(x));
        yLeftValue=model.getHi().get(x);
    }

    @Override
    public void draw(Canvas canvas, float v, float v1) {
        Context mContext= MyApplication.getInstance().getApplicationContext();
        //文字画笔
        Paint textPaint=new Paint();
        textPaint.setTextSize(DensityUtils.sp2px(mContext,10));
        textPaint.setColor(Color.WHITE);

        //lable标签画笔
        Paint lablePaint=new Paint();
        lablePaint.setColor(Color.BLACK);

        RectF rect;
        //x轴maker
        //1920*1080进行基准适配
        int px40= DensityUtils.dip2px(mContext,13);
        int px90=DensityUtils.dip2px(mContext,20);
        int px100=DensityUtils.dip2px(mContext,43);
        int px50=DensityUtils.dip2px(mContext,17);
        int px10=DensityUtils.dip2px(mContext,13);
        int px8=DensityUtils.dip2px(mContext,3);
        int px180=DensityUtils.dip2px(mContext,50);
        int px80=DensityUtils.dip2px(mContext,17);
        int px20=DensityUtils.dip2px(mContext,7);
        int px12=DensityUtils.dip2px(mContext,4);
        int px15=DensityUtils.dip2px(mContext,5);
        int px55=DensityUtils.dip2px(mContext,18);
        int px45=DensityUtils.dip2px(mContext,15);
        int px110=DensityUtils.dip2px(mContext,43);

        if (v-px40>=px90&&v-px40<=canvas.getWidth()-px100){
            rect = new RectF(v-px50,px15, v+px50, px55);
            canvas.drawRoundRect(rect,px8,px8,lablePaint);
            canvas.drawText(xValue,v-px40,px45,textPaint);
        }else if (v-px40>canvas.getWidth()-px100){
            rect = new RectF(canvas.getWidth()-px110,px15, canvas.getWidth()-px10, px55);
            canvas.drawRoundRect(rect,px8,px8,lablePaint);
            canvas.drawText(xValue,canvas.getWidth()-px100,px45,textPaint);
        }else {
            rect = new RectF(px80,px15, px180, px55);
            canvas.drawRoundRect(rect,px8,px8,lablePaint);
            canvas.drawText(xValue,px90,px45,textPaint);
        }

        //y轴左侧maker
        rect = new RectF(px80,v1-px20,px180, v1+px20);
        canvas.drawRoundRect(rect,px8,px8,lablePaint);
        canvas.drawText(yLeftValue,px90,v1+px12,textPaint);

//        //右上角图例
//        rect=new RectF(px90,px45,550,px90);
//        canvas.drawRect(rect,lablePaint);
//        canvas.drawText("  数据1"+xValue+"  数据2"+yLeftValue+"  数据3"+yLeftValue,px90,65,textPaint);
    }
}