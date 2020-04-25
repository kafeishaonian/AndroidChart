package com.chart.client.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.chart.client.chartmodel.KlineChartModel;
import com.chart.client.chartmodel.TickChartModel;
import com.chart.client.utils.TimeUtils;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * VOL
 * Created by Hongmingwei on 2018/3/21.
 * Email: 648600445@qq.com
 */

public class BarChart extends CombinedChart {
    /**
     * TAG
     */
    private static final String TAG = BarChart.class.getSimpleName();

    //图标类型
    public static final String MACD = "MACD";
    public static final String KDJ = "KSJ";

    public BarChart(Context context) {
        super(context);
    }

    public BarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 行情图辅图
     */
    public void setTickBarData(TickChartModel tickChartModel, int xCount, boolean isInitChart) {
        List<BarEntry> barValues = new ArrayList<>();
        List<Entry> lineValues = new ArrayList<>();
        List<TickChartModel.DataBean> datas = tickChartModel.getData();
        int[] colors = new int[datas.size()];
        for (int i = 0; i < datas.size(); i++) {
            BarEntry entry = new BarEntry(i, Float.parseFloat(String.valueOf(datas.get(i).getCount())));
            barValues.add(entry);

            if (i == 0) {
                if (datas.get(i).getPrice() > tickChartModel.getParam().getLast()) {
                    colors[i] = Color.RED;
                } else {
                    colors[i] = Color.GREEN;
                }
            } else {
                if (datas.get(i).getPrice() > datas.get(i - 1).getPrice()) {
                    colors[i] = Color.RED;
                } else {
                    colors[i] = Color.GREEN;
                }
            }
        }
        for (int i = 0; i < xCount; i++) {
            lineValues.add(i, new Entry(i, 0f));
        }
        showTickChart(barValues, lineValues, colors);
        if (isInitChart) {
            initTickChart(xCount);
        }
    }

    /**
     * 绘制BarChart
     *
     * @param list
     * @param lineValue
     * @param colors
     */
    private void showTickChart(List<BarEntry> list, List<Entry> lineValue, int[] colors) {
        CombinedData combinedData = new CombinedData();
        combinedData.setData(initBarData(list, colors));
        combinedData.setData(new LineData(initLineData(lineValue, Color.TRANSPARENT, true, true)));
        setData(combinedData);
        notifyDataSetChanged();
        postInvalidate();
    }

    /**
     * 初始化分时图BarChart
     *
     * @param xCount
     */
    private void initTickChart(int xCount) {
        setDescription(null);

        XAxis xAxis = getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.enableGridDashedLine(5f, 5f, 0f);
        xAxis.setGridColor(Color.BLACK);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setLabelCount(5, true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(false);
        xAxis.setXOffset(10);
        animateX(400);

        YAxis leftAxis = getAxisLeft();
        leftAxis.enableGridDashedLine(5f, 5f, 0f);
        leftAxis.setGridColor(Color.BLACK);
        leftAxis.setLabelCount(3, true);
        leftAxis.setTextSize(8f);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisLineColor(Color.BLACK);
        leftAxis.setStartAtZero(true);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return (new DecimalFormat("#0.00").format(v));
            }
        });

        YAxis rightAxis = getAxisRight();
        rightAxis.setDrawLabels(true);
        rightAxis.setTextSize(8f);
        leftAxis.setLabelCount(3, true);
        rightAxis.setTextColor(Color.TRANSPARENT);
        rightAxis.setDrawAxisLine(true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        rightAxis.setAxisLineColor(Color.BLACK);
        rightAxis.setDrawGridLines(false);
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return (new DecimalFormat("#0.00").format(v));
            }
        });

        Legend legend = getLegend();
        legend.setEnabled(false);
        setDrawBorders(true);
        setBorderWidth(0.4f);
        setBorderColor(Color.BLACK);

        setDragEnabled(true);
        setScaleXEnabled(false);
        setScaleYEnabled(false);
        setDragDecelerationEnabled(true);
        setPinchZoom(true);
        setDoubleTapToZoomEnabled(false);
        setHighlightPerDragEnabled(true);
        setHighlightPerTapEnabled(false);
        setAutoScaleMinMaxEnabled(true);
        setVisibleXRange(xCount, xCount);
    }


    /**
     * 初始化数据属性
     *
     * @param yValues
     * @param lineColor
     * @param isShow
     * @param isHighLight
     * @return
     */
    private LineDataSet initLineData(List<Entry> yValues, int lineColor, boolean isShow, boolean isHighLight) {
        LineDataSet lineDataSet = new LineDataSet(yValues, "");
        lineDataSet.setLineWidth(1f);
        lineDataSet.setColor(lineColor);
        lineDataSet.setHighlightEnabled(isHighLight);
        lineDataSet.setHighLightColor(Color.BLACK);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawVerticalHighlightIndicator(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setVisible(isShow);
        return lineDataSet;
    }

    /**
     * 初始化数据属性
     *
     * @param barEntries
     * @param colors
     * @return
     */
    private BarData initBarData(List<BarEntry> barEntries, int[] colors) {
        //创建数据集合
        BarDataSet dataSet = new BarDataSet(barEntries, "");
        dataSet.setDrawValues(false);
        dataSet.setBarBorderWidth(0f);
        dataSet.setColors(colors);
        dataSet.setBarShadowColor(Color.TRANSPARENT);
        dataSet.setBarBorderColor(Color.TRANSPARENT);
        dataSet.setHighlightEnabled(false);
        BarData barData = new BarData(dataSet);
        return barData;
    }

    //=================================
    public void setBarData(KlineChartModel kLineModel, String type, boolean isInitChart) {
        List<CandleEntry> candleValues = new ArrayList<>();
        List<Entry> lineValues1 = new ArrayList<>();
        List<Entry> lineValues2 = new ArrayList<>();
        List<Entry> lineValues3 = new ArrayList<>();

//        List<List<String>> datas = kLineModel.getItem().getLineExt();
        for (int i = 0; i < kLineModel.getMacd().size(); i++) {
//            List<String> list = datas.get(i);
            lineValues1.add(new Entry(i, Float.parseFloat(kLineModel.getDif().get(i))));
            lineValues2.add(new Entry(i, Float.parseFloat(kLineModel.getDea().get(i))));
//            lineValues3.add(new Entry(i, Float.parseFloat(kLineModel.getJ().get(i))));
            CandleEntry entry = new CandleEntry(i, 0,
                    Float.parseFloat(kLineModel.getMacd().get(i)),
                    0,
                    Float.parseFloat(kLineModel.getMacd().get(i)));
            candleValues.add(entry);
        }

        showChart(candleValues, lineValues1, lineValues2, lineValues3, kLineModel, type);
        if (isInitChart) {
            initCandleChart(kLineModel);
        }
    }

    /**
     * 绘制barchart
     */
    private void showChart(List<CandleEntry> candleValues, List<Entry> lineValues1, List<Entry> lineValues2,
                           List<Entry> lineValues3, KlineChartModel model, String type) {
        CombinedData combinedData = new CombinedData();
        boolean line3IsShow = false;
        List<ILineDataSet> sets = new ArrayList<>();
        switch (type) {
            case MACD:
                combinedData.setData(initCandleStickChartData(candleValues, true));
                sets.add(initLineData(lineValues1, Color.CYAN, true, false));
                line3IsShow = false;
                break;
            case KDJ:
                combinedData.setData(initCandleStickChartData(candleValues, false));
                sets.add(initLineData(lineValues1, Color.CYAN, true, true));
                line3IsShow = true;
                break;
        }
        sets.add(initLineData(lineValues2, Color.BLACK, true, false));
        sets.add(initLineData(lineValues3, Color.BLUE, line3IsShow, false));
        LineData lineData = new LineData(sets);
        combinedData.setData(lineData);
        setData(combinedData);
        notifyDataSetChanged();
        postInvalidate();
    }

    /**
     * 初始化K线图数据属性
     */
    private CandleData initCandleStickChartData(List<CandleEntry> candleVaues, boolean isShow) {
        CandleDataSet candleDataSet=new CandleDataSet(candleVaues,"");
        candleDataSet.setShadowColor(Color.DKGRAY);
        candleDataSet.setShadowWidth(0.5f);
        candleDataSet.setIncreasingColor(Color.RED);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setDecreasingColor(Color.GREEN);
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setShadowColorSameAsCandle(true);
        candleDataSet.setHighLightColor(Color.BLACK);
        candleDataSet.setDrawValues(!candleDataSet.isDrawValuesEnabled());
        candleDataSet.setDrawHorizontalHighlightIndicator(false);
        candleDataSet.setVisible(isShow);
        CandleData candleData=new CandleData(candleDataSet);
        return candleData;
    }

    /**
     * 初始化蜡烛图图表属性
     */
    private void initCandleChart(final KlineChartModel kLineModel) {
        setDescription(null);

        XAxis xAxis=getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.enableGridDashedLine(5f,5f,0f);
        xAxis.setGridColor(Color.BLACK);
        xAxis.setDrawGridLines(true);
        xAxis.setAxisMinimum(-0.8f);
        xAxis.setAxisMaximum(getXChartMax() + 0.8f);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setTextSize(8f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setLabelCount(5,true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(false);
        xAxis.setXOffset(10);
        animateX(400);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v<kLineModel.getTime().size()) {
                    return TimeUtils.getTimeMD(kLineModel.getTime().get((int) v));
                }
                return "";
            }
        });

        YAxis leftAxis=getAxisRight();
        leftAxis.enableGridDashedLine(5f,5f,0f);
        leftAxis.setGridColor(Color.BLACK);
        leftAxis.setLabelCount(2,true);
        leftAxis.setTextSize(8f);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setAxisLineColor(Color.BLACK);
        leftAxis.setStartAtZero(false);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return (new DecimalFormat("#0.00").format(v));
            }
        });

        YAxis rightAxis=getAxisLeft();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);

        Legend legend=getLegend();
        legend.setEnabled(false);

        setDrawBorders(true);
        setBorderWidth(0.4f);
        setBorderColor(Color.BLACK);
//        setDragDecelerationFrictionCoef(0.5f);

        setDragEnabled(true);
        setScaleEnabled(true);
        setScaleXEnabled(true);
        setScaleYEnabled(false);
        setDragDecelerationEnabled(true);
        setPinchZoom(true);
        setDoubleTapToZoomEnabled(false);
        setHighlightPerDragEnabled(false);
        setHighlightPerTapEnabled(false);
        setAutoScaleMinMaxEnabled(true);
        setVisibleXRangeMinimum(15f);
        setVisibleXRangeMaximum(80f);
        moveViewToX(kLineModel.getTime().size()-1);

    }

}
