package com.chart.client.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.chart.client.model.CMinute;
import com.chart.client.model.CrossModel;
import com.chart.client.model.TickDataResponse;
import com.chart.client.utils.ColorUtil;
import com.chart.client.utils.DrawUtil;
import com.chart.client.utils.GridUtil;
import com.chart.client.utils.LineUtil;

import java.util.ArrayList;

/**
 * Created by Hongmingwei on 2017/10/12.
 * Email: 648600445@qq.com
 */

public class TickChartView extends ChartView {
    /**
     * TAG
     */
    private static final String TAG = TickChartView.class.getSimpleName();
    /**
     * params
     */
    //分时数据
    private TickDataResponse tickData;
    //分时线昨收
    private double tickYD;
    //是否白盘夜盘一起显示
    private boolean hasNight;
    //单位
    private float xUnit;
    private double yMax;
    private double yMin;
    //所有价格
    private float[] prices;
    //所有均线数据
    private float[] averages;
    //补全后的所有点
    private ArrayList<CMinute> minutes;

    public TickChartView(Context context) {
        super(context);
    }

    public TickChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TickChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent: =======");
        if (mGestureDetector != null){
            mGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    @Override
    protected void drawGrid(Canvas canvas) {
        //TODO 画网格
        if (tickData != null && tickData.getParam() != null && LineUtil.isIrregular(tickData.getParam().getDuration())){
            //如果是不规则网格画不规则网格
            GridUtil.drawIrregularGrid(canvas, mWidth, mainH, tickData.getParam().getDuration());
            GridUtil.drawIrregularIndexGrid(canvas, indexStartY, mWidth, indexH, tickData.getParam().getDuration());
        } else {
            if (hasNight){
                GridUtil.drawNightGrid(canvas, mWidth, mainH);
                GridUtil.drawNightIndexGrid(canvas, indexStartY, mWidth, indexH);
            } else {
                GridUtil.drawGrid(canvas, mWidth, mainH);
                GridUtil.drawIndexGrid(canvas, indexStartY, mWidth, indexH);
            }
        }
    }



    @Override
    protected void drawText(Canvas canvas) {
        if (tickData == null){
            return;
        }
        DrawUtil.drawYPercentAndPrice(canvas, yMax, yMin, tickYD, mWidth, mainH);
        DrawUtil.drawXTime(canvas, tickData.getParam().getDuration(), tickData.getParam().getUntil(), mWidth, mainH);
    }

    @Override
    protected void drawLines(Canvas canvas) {
        if (tickData == null){
            return;
        }
        drawAverageLine(canvas);
        drawPriceLine(canvas);
    }

    @Override
    protected void drawVOL(Canvas canvas) {
        if (tickData == null){
            return;
        }
        long max = 0;
        for (CMinute minute : minutes){
            max = minute.getCount() > max ? minute.getCount() : max;
        }
        //如果量全为0， 则不画
        if (max != 0){
            //画量线，多条竖直线
            DrawUtil.drawVOLRects(canvas, xUnit, indexStartY, indexH, max, (float) tickData.getParam().getLast(), minutes);
        }
    }

    @Override
    protected void drawZJ(Canvas canvas) {

    }

    @Override
    protected void init() {
        if (tickData == null){
             return;
        }
        tickYD = tickData.getParam().getLast();
        hasNight = LineUtil.hasNight(tickData.getParam().getDuration());
        xUnit = mWidth / LineUtil.getShowCount(tickData.getParam().getDuration());
        //计算最大最小值
        boolean first = true;
        for (CMinute c : tickData.getData()){
            if (first){
                first = false;
                yMax = c.getPrice();
                yMin = c.getPrice();
            }
            yMax = c.getPrice() > yMax ? c.getPrice() : yMax;
            yMax = c.getAverage() > yMax ? c.getAverage() : yMax;
            if (c.getPrice() != 0){
                yMin = c.getPrice() < yMin ? c.getPrice() : yMin;
            }
            if (c.getAverage() != 0 && c.getAverage() != 0.01){
                yMin = c.getAverage() < yMin ? c.getAverage() : yMin;
            }
        }
    }

    @Override
    protected boolean onViewScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onDismiss() {
        msgText.setVisibility(INVISIBLE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mCrossView != null){
            mCrossView.setVisibility(GONE);
        }
    }

    /**
     * 行情图均线
     * @param canvas
     */
    private void drawAverageLine(Canvas canvas){
        prices = new float[minutes.size()];
        averages = new float[minutes.size()];
        for (int i = 0; i < minutes.size(); i++){
            prices[i] = (float) minutes.get(i).getPrice();
            averages[i] = (float) minutes.get(i).getAverage();
        }
        float[] maxAndMin1 = LineUtil.getMaxAndMin(averages);
        //如果均线值全为0.01则不画该线，否则会影响价格线展示
        if (maxAndMin1[0] == 0.01 && maxAndMin1[1] == 0.01){
            return;
        }
        //乘以1.001是为了让上下分别空出一点出来
        double[] maxAndMin = LineUtil.getMaxAndMinByYd(yMax, yMin, tickYD);
        DrawUtil.drawLines(canvas, averages, xUnit, mainH, ColorUtil.COLOR_SMA_LINE, (float) maxAndMin[0], (float) maxAndMin[1], false);
    }

    /**
     * 行情图
     * @param canvas
     */
    private void drawPriceLine(Canvas canvas){
        //乘以1.001是为了让上下分别空出一点出来
        double[] maxAndMin = LineUtil.getMaxAndMinByYd(yMax, yMin, tickYD);
        DrawUtil.drawLines(canvas, prices, xUnit, mainH, ColorUtil.COLOR_PRICE_LINE, (float) maxAndMin[0], (float) maxAndMin[1], false);
        DrawUtil.drawBackShade(canvas, prices, xUnit, mainH, ColorUtil.COLOR_PRICE_LINE, (float) maxAndMin[0], (float) maxAndMin[1], false);
//        DrawUtil.drawPriceShader(canvas, prices, xUnit, mainH, (float) maxAndMin[0], (float) maxAndMin[1]);
    }

    @Override
    public void onCrossMove(float x, float y) {
        super.onCrossMove(x, y);
        if (mCrossView == null || minutes == null){
            return;
        }
        int position = (int) Math.rint(new Double(x) / new Double(xUnit));
        if (position < minutes.size()){
            CMinute cMinute = minutes.get(position);
            float cy = (float) getY(cMinute.getPrice());
            CrossModel model = new CrossModel(position * xUnit, cy);
            model.y2 = (float) getY(cMinute.getAverage());
            model.price = cMinute.getPrice() + "";
            model.time = cMinute.getTime();
            setIndexTextAndColor(position, cMinute, model);
            mCrossView.drawLine(model);
            if (mCrossView.getVisibility() == GONE){
                mCrossView.setVisibility(VISIBLE);
            }
            //TODO 此处把该点的数据写到界面上
            msgText.setVisibility(VISIBLE);
            msgText.setText(Html.fromHtml(getCurPriceInfo(cMinute)));
        }
    }



    /**
     * 获取价格对应的Y轴
     * @param price
     * @return
     */
    private double getY(double price){
        double[] maxAndMin = LineUtil.getMaxAndMinByYd(yMax, yMin, tickYD);
        if (price == maxAndMin[0]){
            return 0;
        }
        if (price == maxAndMin[1]){
            return mainH;
        }
        return mainH - (new Float(price) - maxAndMin[1]) / ((maxAndMin[0] - maxAndMin[1]) / mainH);
    }

    /**
     * 计算指标左上角应该显示的文字
     * @param position
     * @param cMinute
     * @param model
     */
    private void setIndexTextAndColor(int position, CMinute cMinute, CrossModel model){
        switch (indexType){
            case INDEX_VOL:
                model.indexText = new String[]{"VOL:" + cMinute.getCount()};
                model.indexColor = new int[]{cMinute.getPrice() > tickYD ? ColorUtil.COLOR_RED : ColorUtil.COLOR_GREEN};
                break;
            case INDEX_ZJ:
                break;
        }
    }

    /**
     * 价格信息
     * @param cMinute
     * @return
     */
    public String getCurPriceInfo(CMinute cMinute){
        return ColorUtil.getCurPriceInfo(cMinute, tickYD);
    }

    /**
     * 重新画分时图
     * @param data
     */
    public void setDataAndInvalidate(TickDataResponse data) {
        this.tickData = data;
        minutes = LineUtil.getAllTickData(data);
        postInvalidate();
    }
}
