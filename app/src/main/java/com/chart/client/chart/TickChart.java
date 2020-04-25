package com.chart.client.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.chart.client.R;
import com.chart.client.chartmodel.TickChartModel;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
 * Created by Hongmingwei on 2018/3/5.
 * Email: 648600445@qq.com
 */

public class TickChart extends CombinedChart {
    /**
     * TAG
     */
    private static final String TAG  = TickChart.class.getSimpleName();

    /**
     * Params
     */
    private Context mContext;
    private List<Entry> higlightList = null;

    public TickChart(Context context) {
        super(context);
        mContext = context;
    }

    public TickChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public TickChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public List<Entry> getHiglightList() {
        return higlightList;
    }

    @Override
    protected void init() {
        super.init();
        mXAxisRenderer = new ChartXAxisRenderer(mViewPortHandler, mXAxis, mRightAxisTransformer);
        mAxisLeft = new TickYAxis(YAxis.AxisDependency.LEFT);
        mAxisRendererLeft = new TickYAxisRenderer(mViewPortHandler, (TickYAxis) mAxisLeft, mLeftAxisTransformer);
        mAxisRight = new TickYAxis(YAxis.AxisDependency.RIGHT);
        mAxisRendererRight = new TickYAxisRenderer(mViewPortHandler, (TickYAxis) mAxisRight, mRightAxisTransformer);
    }

    @Override
    public TickYAxis getAxisLeft() {
        return (TickYAxis) super.getAxisLeft();
    }

    @Override
    public TickYAxis getAxisRight() {
        return (TickYAxis) super.getAxisRight();
    }


    /**
     * 设置数据
     * @param model   数据Model
     * @param xCount  X轴一共要显示多少个点
     * @param isInitChart  是否第一个加载
     */
    public void setLineData(TickChartModel model, int xCount, boolean isInitChart){
        List<Entry> yValues = new ArrayList<>();
        higlightList = yValues;
        List<Entry> yExtValues = new ArrayList<>();
        List<Entry> yAverageValues = new ArrayList<>();
        List<TickChartModel.DataBean> list = model.getData();
        for (int i = 0; i < list.size(); i++){
            yValues.add(new Entry(i, Float.parseFloat(String.valueOf(list.get(i).getPrice()))));
            yAverageValues.add(new Entry(i, Float.parseFloat(String.valueOf(list.get(i).getAverage()))));
        }

        for (int i = 0; i < xCount; i++){
            yExtValues.add(new Entry(i, Float.parseFloat("0")));
        }
        showChart(yValues, yAverageValues, yExtValues, model);
        if (isInitChart){
            initTickChart(model);
        }
    }

    /**
     * 绘制分时图
     * @param yValues
     * @param yAverageValues
     * @param yExtValues
     * @param model
     */
    private void showChart(List<Entry> yValues, List<Entry> yAverageValues, List<Entry> yExtValues, TickChartModel model){
        CombinedData combinedData = new CombinedData();
        //添加数据集合
        List<ILineDataSet> sets = new ArrayList<>();
        //添加行情图数据
        sets.add(initTickChartData(yValues, Color.GRAY, true, true));
        //添加辅助线数据
        sets.add(initTickChartData(yExtValues, Color.TRANSPARENT, false, false));
        //添加均价数据
        sets.add(initTickChartData(yAverageValues, Color.RED, true, false));
        //设置线数
        LineData lineData = new LineData(sets);
        combinedData.setData(lineData);
        //添加数据
        setData(combinedData);
        //设置高亮显示
        TickChartMarkerView markerView = new TickChartMarkerView(model);
        setMarker(markerView);
        //更新图标
        postInvalidate();
    }


    /**
     * 初始化分时图数据属性
     * @param yValues  数据
     * @param lineColor 颜色
     * @param isShow 是否显示
     * @param isFill 数据高亮
     * @return
     */
    private ILineDataSet initTickChartData(List<Entry> yValues, int lineColor, boolean isShow, boolean isFill){
        //创建数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "");
        //设置线宽
        lineDataSet.setLineWidth(1f);
        //设置线的颜色
        lineDataSet.setColor(lineColor);
        //设置数据高亮显示
        if (isFill){
            lineDataSet.setHighlightEnabled(true);
        } else {
            lineDataSet.setHighlightEnabled(false);
        }
        //设置选中的高亮颜色
        lineDataSet.setHighLightColor(Color.BLACK);
        //数据点圆圈标识开关
        lineDataSet.setDrawCircles(false);
        //数据点数据标识开关
        lineDataSet.setDrawValues(false);
        //线下填充颜色设置
        if (isFill){
            Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.bg_fade_tick);
            lineDataSet.setFillDrawable(drawable);
            lineDataSet.setDrawFilled(true);
        }
        //设置是否显示
        lineDataSet.setVisible(isShow);
        return lineDataSet;
    }

    /**
     * 初始化分时图图表属性
     * @param model
     */
    private void initTickChart(final TickChartModel model) {
        //设置标题
        setDescription(null);
        double yMax = 0;
        double yMin = 0;
        boolean first = true;
        for (TickChartModel.DataBean c : model.getData()){
            if (first){
                first = false;
                yMax = c.getPrice();
                yMin = c.getPrice();
            }
            yMax = c.getPrice() > yMax ? c.getPrice() : yMax;
            if (c.getPrice() != 0){
                yMin = c.getPrice() < yMin ? c.getPrice() : yMin;
            }
        }
        //昨结或今开
        final float averageValue = (float) model.getParam().getLast();
        final float value1 = (float) Math.abs(yMax - averageValue);
        final float value2 = (float) Math.abs(yMin - averageValue);
        //获取最高最低值与差值
        final float buffer = value1 > value2 ? value1 / 10 + value1 : value2 / 10 + value2;

        //设置x轴属性
        XAxis xAxis = getXAxis();
        //防止x头尾数据不完全显示
        xAxis.setAvoidFirstLastClipping(true);
        //设置x轴轴线位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置不显示x轴网格线
        xAxis.setDrawGridLines(true);
        //设置背景网格虚线密度
        xAxis.enableGridDashedLine(5f, 5f, 0f);
        //设置背景网格虚线颜色
        xAxis.setGridColor(Color.BLACK);
        //设置x轴轴线颜色
        xAxis.setAxisLineColor(Color.BLACK);
        //设置刻度字体大小
        xAxis.setTextSize(8f);
        //设置刻度字体颜色
        xAxis.setTextColor(Color.BLACK);
        //设置显示刻度数量
        xAxis.setLabelCount(5, true);
        //设置显示轴线
        xAxis.setDrawAxisLine(true);
        //设置x轴绘制动画的时间
        animateX(400);
        //x轴数据格式化
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if ((int)v== 120){
                    return "11:30/13:00";
                } else if ((int) v == 240){
                    return "15:00";
                } else if ((int) v == 0){
                    return "09:30";
                } else if ((int) v == 60){
                    return "10:30";
                } else if ((int) v == 180){
                    return "14:00";
                } else {
                    return "";
                }
            }
        });

        //左侧Y轴设置
        TickYAxis leftAxis = getAxisLeft();
        //设置背景网格虚线密度
        leftAxis.enableGridDashedLine(5f, 5f, 0f);
        //设置网格虚线的颜色
        leftAxis.setGridColor(Color.BLACK);
        //设置显示刻度数量
        leftAxis.setLabelCount(7, true);
        //设置刻度字体大小
        leftAxis.setTextSize(8f);
        //设置刻度字体颜色
        leftAxis.setTextColor(Color.BLACK);
        //设置刻度值显示位置
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //设置轴线颜色
        leftAxis.setAxisLineColor(Color.BLACK);
        //设置开启Y轴颜色区分
        leftAxis.setDrawDifferentUpDownColorToLableEnabled(true);
        //设置轴线上半区颜色
        leftAxis.setUpTextColor(Color.RED);
        //设置轴线下半区颜色
        leftAxis.setDownTextColor(Color.GREEN);
        //撤销先前设置的最小值
        leftAxis.resetAxisMinimum();
        //设置最大刻度值
        leftAxis.setAxisMaximum(averageValue + buffer);
        //设置最小刻度值
        leftAxis.setAxisMinimum(averageValue - buffer);
        //格式化刻度数据
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return (new DecimalFormat("#0.00").format(v));
            }
        });

        //右侧Y轴设置
        TickYAxis rightAxis = getAxisRight();
        //设置背景网格虚线密度
        rightAxis.enableGridDashedLine(5f, 5f, 0f);
        //设置网格虚线的颜色
        rightAxis.setGridColor(Color.BLACK);
        //设置显示刻度数量
        rightAxis.setLabelCount(7, true);
        //设置刻度字体大小
        rightAxis.setTextSize(8f);
        //设置刻度字体颜色
        rightAxis.setTextColor(Color.BLACK);
        //设置刻度值显示位置
        rightAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //设置轴线颜色
        rightAxis.setAxisLineColor(Color.BLACK);
        //设置开启Y轴颜色区分
        rightAxis.setDrawDifferentUpDownColorToLableEnabled(true);
        //设置轴线上半区颜色
        rightAxis.setUpTextColor(Color.RED);
        //设置轴线下半区颜色
        rightAxis.setDownTextColor(Color.GREEN);
        //撤销先前设置的最小值
        rightAxis.resetAxisMinimum();
        //设置最大刻度值
        rightAxis.setAxisMaximum(averageValue + buffer);
        //设置最小刻度值
        rightAxis.setAxisMinimum(averageValue - buffer);
        //格式化刻度数据
        rightAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return (new DecimalFormat("#0.00").format(v));
            }
        });

        //图例设置
        Legend legend = getLegend();
        //设置不显示图例
        legend.setEnabled(false);
        //设置图表边框
        setDrawBorders(true);
        //设置边框宽度
        setBorderWidth(0.4f);
        //设置边框颜色
        setBorderColor(Color.BLACK);
        //设置图表最小偏移量
//        setMinOffset(0f);

        setNoDataText("暂无数据");
        //设置图表手势
        //设置拖动
        setDragEnabled(true);
        //设置禁止x轴上的缩放
        setScaleXEnabled(false);
        //设置禁止y轴上的缩放
        setScaleYEnabled(false);
        //设置关闭捏缩功能
        setPinchZoom(false);
        //设置双击缩放图表
        setDoubleTapToZoomEnabled(false);
        //设置滑动高亮显示
        setHighlightPerDragEnabled(false);
        //设置点击高亮显示
        setHighlightPerTapEnabled(false);
        //设置图表当前显示位置
        moveViewToX(0);
    }



}
