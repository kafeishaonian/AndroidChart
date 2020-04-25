package com.chart.client.chart;

import android.graphics.Canvas;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Hongmingwei on 2018/4/24.
 * Email: 648600445@qq.com
 */

public class TickChartRenderer extends ChartXBoundsCandleRenderer {

    private static final String TAG = TickChartRenderer.class.getSimpleName();

    protected CombinedDataProvider mChart;

    public TickChartRenderer(TickChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        mChart = chart;
    }

    @Override
    public void drawData(Canvas c) {
        super.drawData(c);
        LineData lineData = mChart.getLineData();
        for (ILineDataSet set : lineData.getDataSets()) {
            if (set.isVisible())
                drawDataSet(c, set);
        }
    }

    protected void drawDataSet(Canvas c, ILineDataSet dataSet) {
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        mXBounds.set(mChart, dataSet);
        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {
            Entry e = dataSet.getEntryForIndex(j);
            if (e == null) continue;
            if (j == (mXBounds.range + mXBounds.min)){
                MPPointD minPoint = trans.getPixelForValues(e.getX(), e.getY());
                float lowX = (float) minPoint.x;
                float lowY = (float) minPoint.y;
//                c.drawBitmap(display, lowX, lowY, paint);
            }
        }
    }
}
