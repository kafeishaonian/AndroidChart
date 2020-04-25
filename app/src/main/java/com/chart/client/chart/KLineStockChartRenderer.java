package com.chart.client.chart;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.Log;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.CombinedDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Hongmingwei on 2018/4/6.
 * Email: 648600445@qq.com
 */

public class KLineStockChartRenderer extends ChartXBoundsCandleRenderer {

    private static final String TAG = KLineStockChartRenderer.class.getSimpleName();

    protected CombinedDataProvider mChart;
    private Path mHighlightLinePath = new Path();
    private Paint mPaintLabel = new Paint();
    float hLength = Utils.convertDpToPixel(15f);
    float rect = Utils.convertDpToPixel(8f);
    float textX = Utils.convertDpToPixel(2f);
    float textY = Utils.convertDpToPixel(3f);
    int mWidth;


    public KLineStockChartRenderer(KLineStockChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler, int width) {
        super(chart, animator, viewPortHandler);
        mChart = chart;
        mPaintLabel.setTextSize(12f);
        mWidth = width;
    }

    @Override
    public void drawData(Canvas c) {
        super.drawData(c);
        CandleData candleData = mChart.getCandleData();
        for (ICandleDataSet set : candleData.getDataSets()) {
            if (set.isVisible()) {
                drawDataSet(c, set);
            }
        }
    }

    @SuppressWarnings("ResourceAsColor")
    protected void drawDataSet(Canvas c, ICandleDataSet dataSet) {
        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
        mXBounds.set(mChart, dataSet);
        boolean showCandleBar = dataSet.getShowCandleBar();
        float maxPrice = dataSet.getYMax();
        float minPrice = dataSet.getYMin();
        boolean maxFlag = true;
        boolean minFlag = true;
        for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {
            CandleEntry e = dataSet.getEntryForIndex(j);
            if (e == null)
                continue;
            if (showCandleBar) {
                if (maxPrice == e.getHigh() && maxFlag) {
                    maxFlag = false;
                    drawMaxLabel(c, trans, e.getX(), maxPrice);
                }
                if (minPrice == e.getLow() && minFlag) {
                    minFlag = false;
                    drawMinLabel(c, trans, e.getX(), minPrice);
                }
            }
        }
    }

    /**
     * 绘制最大值的标识
     */
    private void drawMaxLabel(Canvas canvas, Transformer trans, float maxX, float maxY) {
        MPPointD minPoint = trans.getPixelForValues(maxX, maxY);
        float lowX = (float) minPoint.x;
        float lowY = (float) minPoint.y;
        mPaintLabel.setColor(Color.RED);
        float rectLength = Utils.convertDpToPixel((maxY + "").length() * Utils.convertDpToPixel(1.5f));
        if (lowX > mWidth - mWidth / 3) {
            canvas.drawLine(lowX, lowY, lowX - hLength, lowY, mPaintLabel);
            canvas.drawRect(new Rect((int) (lowX - hLength - rectLength), (int) (lowY - rect), (int) (lowX - hLength), (int) (lowY + rect)), mPaintLabel);
            mPaintLabel.setColor(Color.WHITE);
            mPaintLabel.setTextSize(16f);
            canvas.drawText(maxY + "", lowX - rectLength - hLength + textX, lowY + textY, mPaintLabel);
        } else {
            canvas.drawLine(lowX, lowY, lowX + hLength, lowY, mPaintLabel);
            canvas.drawRect(new Rect((int) (lowX + hLength), (int) (lowY - rect), (int) (lowX + hLength + rectLength), (int) (lowY + rect)), mPaintLabel);
            mPaintLabel.setColor(Color.WHITE);
            mPaintLabel.setTextSize(16f);
            canvas.drawText(maxY + "", lowX + hLength + textX, lowY + textY, mPaintLabel);
        }
    }

    /**
     * 绘制最小值的标识
     */
    private void drawMinLabel(Canvas canvas, Transformer trans, float minX, float minY) {
        MPPointD minPoint = trans.getPixelForValues(minX, minY);
        float lowX = (float) minPoint.x;
        float lowY = (float) minPoint.y;
        mPaintLabel.setColor(Color.GREEN);
        float rectLength = Utils.convertDpToPixel((minY + "").length() * Utils.convertDpToPixel(1.5f));
        if (lowX > mWidth - mWidth / 3) {
            canvas.drawLine(lowX, lowY, lowX - hLength, lowY, mPaintLabel);
            canvas.drawRect(new Rect((int) (lowX - hLength - rectLength), (int) (lowY - rect), (int) (lowX - hLength), (int) (lowY + rect)), mPaintLabel);
            mPaintLabel.setColor(Color.WHITE);
            mPaintLabel.setTextSize(16f);
            canvas.drawText(minY + "", lowX - rectLength - hLength + textX, lowY + textY, mPaintLabel);
        } else {
            canvas.drawLine(lowX, lowY, lowX + hLength, lowY, mPaintLabel);
            canvas.drawRect(new Rect((int) (lowX + hLength), (int) (lowY - rect), (int) (lowX + hLength + rectLength), (int) (lowY + rect)), mPaintLabel);
            mPaintLabel.setColor(Color.WHITE);
            mPaintLabel.setTextSize(16f);
            canvas.drawText(minY + "", lowX + hLength + textX, lowY + textY, mPaintLabel);
        }
    }


    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        CandleData candleData = mChart.getCandleData();
        for (Highlight high : indices) {
            ICandleDataSet set = candleData.getDataSetByIndex(high.getDataSetIndex());
            if (set == null || !set.isHighlightEnabled())
                continue;
            CandleEntry e = set.getEntryForXValue(high.getX(), high.getYPx());
            float openValue = e.getOpen() * mAnimator.getPhaseY();
            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), openValue);
            high.setDraw((float) pix.x, (float) pix.y);
            drawHighlightLines(c, (float) pix.x, (float) pix.y, set);
        }
    }

    /**
     * 这里绘制高亮线
     */
    protected void drawHighlightLines(Canvas c, float x, float y, ILineScatterCandleRadarDataSet set) {
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());
        mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());
        // 绘制高亮竖直线
        if (set.isVerticalHighlightIndicatorEnabled()) {
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(x, mViewPortHandler.contentTop());
            mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());
            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }
        // 绘制高亮水平线
        if (set.isHorizontalHighlightIndicatorEnabled()) {
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), y);
            mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), y);
            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }
    }
}
