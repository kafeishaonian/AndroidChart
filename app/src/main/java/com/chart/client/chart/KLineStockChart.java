package com.chart.client.chart;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.chart.client.R;
import com.chart.client.chartmodel.KlineChartModel;
import com.chart.client.utils.TimeUtils;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * K线图封装
 * Created by Hongmingwei on 2018/3/28.
 * Email: 648600445@qq.com
 */

public class KLineStockChart extends CombinedChart {
    /**
     * TAG
     */
    private static final String TAG = KLineStockChart.class.getSimpleName();
    /**
     * Params
     */
    private List<CandleEntry> higlightList = null;
    private Context mContext;


    public KLineStockChart(Context context) {
        super(context);
        mContext = context;
    }

    public KLineStockChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public KLineStockChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    public List<CandleEntry> getHiglightList() {
        return higlightList;
    }

    @Override
    protected void init() {
        super.init();
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics =new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        mRenderer = new KLineStockChartRenderer(this, mAnimator, mViewPortHandler, width);
//        initListener();
    }

    /**
     * 设置数据
     *
     * @param model
     * @param isInitChart
     */
    public void setCandleData(KlineChartModel model, boolean isInitChart) {
        List<CandleEntry> candleValues = new ArrayList<>();
        higlightList = candleValues;
        List<Entry> lineValues1 = new ArrayList<>();
        List<Entry> lineValues2 = new ArrayList<>();
        List<Entry> lineValues3 = new ArrayList<>();
        for (int i = 0; i < model.getOpen().size(); i++) {
            CandleEntry entry = new CandleEntry(i, Float.parseFloat(model.getHi().get(i)),
                    Float.parseFloat(model.getLow().get(i)),
                    Float.parseFloat(model.getOpen().get(i)),
                    Float.parseFloat(model.getClose().get(i)));
            candleValues.add(entry);
        }

        for (int i = 0; i < model.getMa5().size(); i++) {
            lineValues1.add(new Entry(i, Float.parseFloat(model.getMa5().get(i))));
            lineValues2.add(new Entry(i, Float.parseFloat(model.getMa10().get(i))));
            lineValues3.add(new Entry(i, Float.parseFloat(model.getMa15().get(i))));
        }
        showChart(candleValues, lineValues1, lineValues2, lineValues3, model);
        initCandleChart(model, isInitChart);
    }

    /**
     * 初始化K线图数据属性
     *
     * @param candleValues 蜡烛图数据集合
     * @param lineValues1  MD5数据集合
     * @param lineValues2  MD10数据集合
     * @param lineValues3  MD20数据集合
     */
    private void showChart(List<CandleEntry> candleValues, List<Entry> lineValues1, List<Entry> lineValues2, List<Entry> lineValues3, KlineChartModel model) {
        CombinedData combinedData = new CombinedData();
        List<ILineDataSet> sets = new ArrayList<>();
        sets.add(initLineData(lineValues1, Color.CYAN));
        sets.add(initLineData(lineValues2, Color.BLACK));
        sets.add(initLineData(lineValues3, Color.BLUE));
        LineData lineData = new LineData(sets);
        combinedData.setData(lineData);
        combinedData.setData(initCandleStickChartData(candleValues));
        CandleStackChartMarkerView markerView = new CandleStackChartMarkerView(model);
        setMarker(markerView);
        setData(combinedData);
        notifyDataSetChanged();
        postInvalidate();
    }

    /**
     * 初始化MD指标线数据属性
     *
     * @param yValues   MD数据集合
     * @param lineColor MD线颜色
     * @return
     */
    private LineDataSet initLineData(List<Entry> yValues, int lineColor) {
        //创建数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "MA5");
        //设置线宽
        lineDataSet.setLineWidth(1f);
        //设置线的颜色
        lineDataSet.setColor(lineColor);
        //设置数据高亮显示
        lineDataSet.setHighlightEnabled(false);
        //设置不显示指示器
        lineDataSet.setDrawHighlightIndicators(false);
        //设置选中的高亮颜色
        lineDataSet.setHighLightColor(Color.BLACK);
        //数据点圆圈标识开关
        lineDataSet.setDrawCircles(false);
        //数据点数据标识开关
        lineDataSet.setDrawValues(false);
        return lineDataSet;
    }

    /**
     * 初始化K线图数据属性
     *
     * @param candleValues
     * @return
     */
    private CandleData initCandleStickChartData(List<CandleEntry> candleValues) {
        //创建数据集合
        CandleDataSet candleDataSet = new CandleDataSet(candleValues, "");
        //设置蜡烛影线颜色
        candleDataSet.setShadowColor(Color.DKGRAY);
        //设置蜡烛影线宽度
        candleDataSet.setShadowWidth(0.5f);
        //设置蜡烛阳线颜色
        candleDataSet.setIncreasingColor(Color.RED);
        //设置蜡烛阳线风格为填充
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        //设置蜡烛阴线颜色
        candleDataSet.setDecreasingColor(Color.GREEN);
        //设置蜡烛阴线风格为填充
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        //设置影线和阴阳线是否保持同色
        candleDataSet.setShadowColorSameAsCandle(true);


        //设置高亮线颜色
        candleDataSet.setHighLightColor(Color.BLACK);
        //设置是否绘制数据
        candleDataSet.setDrawValues(!candleDataSet.isDrawValuesEnabled());
        CandleData candleData = new CandleData(candleDataSet);
        return candleData;
    }

    private void initCandleChart(final KlineChartModel model, boolean isInitChart) {
        setDescription(null);

        //设置x轴属性
        XAxis xAxis = getXAxis();
        //防止x头尾数据不完全显示
        xAxis.setAvoidFirstLastClipping(true);
        //设置显示轴线
        xAxis.setDrawAxisLine(true);
        //设置绘制背景网格
        xAxis.setDrawGridLines(true);
        //设置X轴左侧偏移距离
        xAxis.setAxisMinimum(-0.8f);
        //设置X轴右侧偏移距离
        xAxis.setAxisMaximum(getXChartMax() + 0.8f);
        //设置x轴轴线位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置背景网格虚线密度
        xAxis.enableGridDashedLine(5f, 5f, 0f);
        //设置背景网格虚线颜色
        xAxis.setGridColor(Color.BLACK);
        //设置x轴轴线颜色
        xAxis.setAxisLineColor(Color.BLACK);
        //设置刻度字体大小
        xAxis.setTextSize(11f);
        //设置刻度字体颜色
        xAxis.setTextColor(Color.BLACK);
        //设置刻度显示数量
        xAxis.setLabelCount(5, true);
        //x轴数据格式化
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                if (v < model.getOpen().size()) {
                    return TimeUtils.getTimeMD(model.getTime().get((int) v));
                }
                return "";
            }
        });

        //左侧Y轴设置
        final YAxis leftAxis = getAxisRight();
        //设置背景网格虚线密度
        leftAxis.enableGridDashedLine(5f, 5f, 0f);
        //设置显示轴线
        leftAxis.setDrawAxisLine(true);
        //设置绘制背景网格
        leftAxis.setDrawGridLines(true);
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
        //设置刻度是否从0开始
        leftAxis.setStartAtZero(false);
        //格式化刻度数据
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float v, AxisBase axisBase) {
                return (new DecimalFormat("#0.00").format(v));
            }
        });

        //右侧Y轴设置
        YAxis rightAxis = getAxisLeft();
        //设置不绘制背景网格
        rightAxis.setDrawGridLines(false);
        //设置不显示刻度数据
        rightAxis.setDrawLabels(false);
        //设置显示轴线
        rightAxis.setDrawAxisLine(true);
        //设置轴线颜色
        rightAxis.setAxisLineColor(Color.BLACK);

        //图例设置
        Legend legend=getLegend();
        //设置不显示图例
        legend.setEnabled(false);

        //设置图表边框
        setDrawBorders(true);
        //设置边框宽度
        setBorderWidth(0.4f);

        //设置边框颜色
        setBorderColor(Color.BLACK);
        //s设置惯性滑族
//        setDragDecelerationFrictionCoef(0.5f);

        if (isInitChart) {
            //设置图表当前显示位置
            moveViewToX(model.getOpen().size() - 1);
            //设置图表手势
            //设置拖动
            setDragEnabled(true);
            //打开缩放功能
            setScaleEnabled(true);
            //设置禁止x轴上的缩放
            setScaleXEnabled(true);
            //设置禁止y轴上的缩放
            setScaleYEnabled(false);
            //设置惯性滚动
            setDragDecelerationEnabled(true);
            //设置捏缩功能
            setPinchZoom(true);
            //设置双击缩放图表
            setDoubleTapToZoomEnabled(false);
            //设置滑动高亮显示
            setHighlightPerDragEnabled(false);
            //设置点击高亮显示
            setHighlightPerTapEnabled(false);
            //设置自动匹配Y轴高度
            setAutoScaleMinMaxEnabled(true);

            //设置缩放显示最小K线数
            setVisibleXRangeMinimum(15f);
            //设置缩放显示最大K线数
            setVisibleXRangeMaximum(80f);

            Matrix save = new Matrix();
            save.setScale(getXRange() / 60f, 1f);
            mViewPortHandler.refresh(save, this, false);
            calculateOffsets();
            //设置x轴绘制动画的时间
            animateX(400);
        } else {
            animateX(0);
        }
    }

    /**
     * K线图放大
     */
    public void setCandleAmplify() {
        Log.e(TAG, "setCandleAmplify ==========" + Math.ceil(getVisibleXRange()));
        if (Math.ceil(getVisibleXRange()) <= 15) {
            Toast.makeText(mContext, "已经放大到最大", Toast.LENGTH_SHORT).show();
        } else {
            getViewPortHandler().getMatrixTouch().postScale(1.5f, 1f);
            setVisibleXRangeMinimum(15f);
            notifyDataSetChanged();
            postInvalidate();
        }
    }

    /**
     * K线图缩小
     */
    public void setCandleSkrink() {
        Log.e(TAG, "setCandleSkrink ==========" + Math.ceil(getVisibleXRange()));
        if (Math.ceil(getVisibleXRange()) >= 80) {
            Toast.makeText(mContext, "已经缩放到最小", Toast.LENGTH_SHORT).show();
        } else {
            getViewPortHandler().getMatrixTouch().postScale(0.5f, 1f);
            setVisibleXRangeMaximum(80f);
            notifyDataSetChanged();
            postInvalidate();
        }
    }

    /**
     * K线图左移
     */
    public void setCandleLeft() {
        Log.e(TAG, "setCandleLeft ==========" + Math.ceil(getLowestVisibleX()));
        if (Math.ceil(getLowestVisibleX()) > 10) {
            getViewPortHandler().getMatrixTouch().postTranslate(70f, 0f);
            notifyDataSetChanged();
            invalidate();
            animateX(0);
        } else if (Math.ceil(getLowestVisibleX()) > 0) {
            moveViewToX(-1);
            notifyDataSetChanged();
            invalidate();
            animateX(0);
        } else {
            Toast.makeText(mContext, "已经移动到最左边", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * K线图右移
     *
     * @param model
     */
    public void setCandleRight(KlineChartModel model) {
        Log.e(TAG, "setCandleRight ==========" + Math.ceil(getHighestVisibleX()));
        if (Math.ceil(getHighestVisibleX()) < (model.getOpen().size() - 10)) {
            getViewPortHandler().getMatrixTouch().postTranslate(-70f, 0f);
            notifyDataSetChanged();
            postInvalidate();
            animateX(0);
        } else if (Math.ceil(getHighestVisibleX()) < model.getOpen().size()) {
            moveViewToX(model.getOpen().size() - 1);
            notifyDataSetChanged();
            postInvalidate();
            animateX(0);
        } else {
            Toast.makeText(mContext, "已经移动到最右边", Toast.LENGTH_SHORT).show();
        }
    }


    private void initListener() {
        this.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, Highlight highlight) {
                highlightValue(highlight);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        this.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent motionEvent, ChartTouchListener.ChartGesture chartGesture) {

            }

            @Override
            public void onChartLongPressed(MotionEvent motionEvent) {
                setDragEnabled(false);
                setHighlightPerDragEnabled(true);
            }

            @Override
            public void onChartDoubleTapped(MotionEvent motionEvent) {

            }

            @Override
            public void onChartSingleTapped(MotionEvent motionEvent) {
                setDragEnabled(true);
                setHighlightPerDragEnabled(false);
                highlightValue(null);
            }

            @Override
            public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

            }

            @Override
            public void onChartScale(MotionEvent motionEvent, float v, float v1) {

            }

            @Override
            public void onChartTranslate(MotionEvent motionEvent, float v, float v1) {

            }
        });
    }
}
