package com.chart.client.chart;

import android.graphics.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.chart.client.chartmodel.KlineChartModel;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

/**
 * Created by Hongmingwei on 2018/3/27.
 * Email: 648600445@qq.com
 */

public class CoupleChartGestureListener implements OnChartGestureListener {
    /**
     * TAG
     */
    private static final String TAG = CoupleChartGestureListener.class.getSimpleName();
    /**
     * View
     */
    private CombinedChart srcChart;
    private CombinedChart desChart;
    private KlineChartModel mKlineModel;
    private Highlight[] highlight;

    public CoupleChartGestureListener(CombinedChart srcChart, CombinedChart desChart) {
        this.srcChart = srcChart;
        this.desChart = desChart;
        syncCharts();
        highLightSync();
    }

    public CoupleChartGestureListener(CombinedChart srcChart, CombinedChart desChart, KlineChartModel model) {
        this.srcChart = srcChart;
        this.desChart = desChart;
        mKlineModel = model;
        highlight = new Highlight[mKlineModel.getOpen().size()];
        for (int i = 0; i < model.getOpen().size(); i++){
            highlight[i] = new Highlight(i, Float.parseFloat(model.getOpen().get(i)), i);
        }
        syncCharts();
        highLightSync();
    }


    @Override
    public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
        syncCharts();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {
        syncCharts();
    }

    @Override
    public void onChartLongPressed(MotionEvent motionEvent) {
        syncCharts();
        if (!srcChart.isHighlightPerDragEnabled()) {
            lockChart();
            Highlight h = srcChart.getHighlightByTouchPoint(motionEvent.getX(), motionEvent.getY());
            if (h!= null){
                h.setDraw(motionEvent.getX(), motionEvent.getY());
                srcChart.highlightValue(h, true);
                srcChart.disableScroll();
            }
        }
    }

    @Override
    public void onChartDoubleTapped(MotionEvent motionEvent) {
        syncCharts();
    }

    @Override
    public void onChartSingleTapped(MotionEvent motionEvent) {
        syncCharts();
        if (srcChart.isHighlightPerDragEnabled()) {
            unlockChart();
        }
    }

    @Override
    public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        syncCharts();
    }

    @Override
    public void onChartScale(MotionEvent motionEvent, float v, float v1) {
        syncCharts();
    }

    @Override
    public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {
        syncCharts();
    }


    /**
     * 锁住图表
     */
    public void lockChart() {
        if (srcChart instanceof TickChart){
            srcChart.setDragEnabled(true);
            desChart.setDragEnabled(true);
        }else {
            srcChart.setDragEnabled(false);
            desChart.setDragEnabled(false);
        }
        srcChart.setHighlightPerDragEnabled(true);
        desChart.setHighlightPerDragEnabled(true);
        srcChart.setHighlightPerTapEnabled(true);
        desChart.setHighlightPerTapEnabled(true);
    }

    /**
     * 解锁图表
     */
    public void unlockChart() {
        srcChart.setDragEnabled(true);
        srcChart.setHighlightPerDragEnabled(false);
        desChart.setDragEnabled(true);
        desChart.setHighlightPerDragEnabled(false);
        srcChart.highlightValue(null);
        desChart.highlightValue(null);
        srcChart.setHighlightPerTapEnabled(false);
        desChart.setHighlightPerTapEnabled(false);
    }


    /**
     * 同步图表位置
     */
    public void syncCharts() {
        Matrix srcMatrix;
        float[] srcVals = new float[9];
        Matrix dstMatrix;
        float[] dstVals = new float[9];

        srcMatrix = srcChart.getViewPortHandler().getMatrixTouch();
        srcMatrix.getValues(srcVals);

        if (desChart.getVisibility() == View.VISIBLE) {
            dstMatrix = desChart.getViewPortHandler().getMatrixTouch();
            dstMatrix.getValues(dstVals);

            dstVals[Matrix.MSCALE_X] = srcVals[Matrix.MSCALE_X];
            dstVals[Matrix.MSKEW_X] = srcVals[Matrix.MSKEW_X];
            dstVals[Matrix.MTRANS_X] = srcVals[Matrix.MTRANS_X];
            dstVals[Matrix.MSKEW_Y] = srcVals[Matrix.MSKEW_Y];
            dstVals[Matrix.MSCALE_Y] = srcVals[Matrix.MSCALE_Y];
            dstVals[Matrix.MTRANS_Y] = srcVals[Matrix.MTRANS_Y];
            dstVals[Matrix.MPERSP_0] = srcVals[Matrix.MPERSP_0];
            dstVals[Matrix.MPERSP_1] = srcVals[Matrix.MPERSP_1];
            dstVals[Matrix.MPERSP_2] = srcVals[Matrix.MPERSP_2];

            dstMatrix.setValues(dstVals);
            desChart.getViewPortHandler().refresh(dstMatrix, desChart, true);
        }
    }


    /**
     * 同步高亮数据
     */
    private void highLightSync() {
        srcChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                Log.e(TAG, "onValueSelected: ============" + highlight);

                Log.e("TAG",highlight.getDataIndex()+"");
                if (desChart instanceof TickChart) {
                    if (((TickChart) desChart).getHiglightList().size()>entry.getX()) {
                        float y = ((TickChart) desChart).getHiglightList().get((int) entry.getX()).getY();
                        Highlight highlight1 = new Highlight(entry.getX(), y, highlight.getDataSetIndex());

                        highlight1.setDataIndex(highlight.getDataIndex());
                        desChart.highlightValues(new Highlight[]{highlight1});
                    }
                }
                else if (desChart instanceof KLineStockChart){
                    if (((KLineStockChart) desChart).getHiglightList().size()>entry.getX()) {
                        float y = ((KLineStockChart) desChart).getHiglightList().get((int) entry.getX()).getY();
                        Highlight highlight1 = new Highlight(entry.getX(), y, highlight.getDataSetIndex());

                        highlight1.setDataIndex(highlight.getDataIndex());
                        desChart.highlightValues(new Highlight[]{highlight1});
                    }
                }
                else {
                    desChart.highlightValues(new Highlight[]{highlight});
                }

            }

            @Override
            public void onNothingSelected() {
                desChart.highlightValue(null);
            }
        });
    }
}
