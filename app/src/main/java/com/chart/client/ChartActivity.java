package com.chart.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chart.client.chart.BarChart;
import com.chart.client.chart.ChartUtils;
import com.chart.client.chart.CoupleChartGestureListener;
import com.chart.client.chart.KLineStockChart;
import com.chart.client.chart.TickChart;
import com.chart.client.chartmodel.KlineChartModel;
import com.chart.client.chartmodel.TickChartModel;
import com.chart.client.data.TickData;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.utils.Utils;
import com.google.gson.Gson;


/**
 * Created by Hongmingwei on 2018/3/28.
 * Email: 648600445@qq.com
 */

public class ChartActivity extends Activity {
    /**
     * View
     */
    private TickChart mTick;
    private BarChart mTickBar;
    private KLineStockChart mKline;
    private BarChart mKlineBar;

    private TextView ma5;
    private TextView ma10;
    private TextView ma20;
    /**
     * Params
     */
    private TickChartModel data;
    private KlineChartModel klines;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        initView();
        initTickData();
        initKLineData();
    }

    private void initView(){
        mTick = (TickChart) findViewById(R.id.tick);
        mTickBar = (BarChart) findViewById(R.id.tick_bar);

        mKline = (KLineStockChart) findViewById(R.id.kline);
        mKlineBar = (BarChart) findViewById(R.id.kline_bar);

        ma5 = (TextView) findViewById(R.id.ma5);
        ma10 = (TextView) findViewById(R.id.ma10);
        ma20 = (TextView) findViewById(R.id.ma20);
    }


    private void initTickData(){
        data = new Gson().fromJson(TickData.sb.toString(), TickChartModel.class);
        mTick.setLineData(data, 4 * 60 + 1, true);
        mTickBar.setTickBarData(data, 4 * 60, true);
        mTick.setOnChartGestureListener(new CoupleChartGestureListener(mTick, mTickBar));
        mTickBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTick.dispatchTouchEvent(event);
                return true;
            }
        });
    }


    private void initKLineData(){
        klines = new Gson().fromJson(TickData.kline.toString(), KlineChartModel.class);
        mKline.setCandleData(klines, true);
        mKlineBar.setBarData(klines, "MACD", true);
        mKline.setOnChartGestureListener(new CoupleChartGestureListener(mKline, mKlineBar, klines));
        mKlineBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mKline.dispatchTouchEvent(event);
                return true;
            }
        });
        if (true) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChartUtils.initOffset(mKline, mKlineBar);
                }
            }, 100);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChartUtils.initOffset(mKlineBar, mKline);
                }
            }, 100);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChartUtils.initOffset(mTick, mTickBar);
                }
            }, 100);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChartUtils.initOffset(mTickBar, mTick);
                }
            }, 100);
        }
    }

    public void initOffset() {
        float lineLeft = mKline.getViewPortHandler().offsetLeft();
        float barLeft = mKlineBar.getViewPortHandler().offsetLeft();
        float lineRight = mKline.getViewPortHandler().offsetRight();
        float barRight = mKlineBar.getViewPortHandler().offsetRight();
        float offsetLeft, offsetRight;
        if (barLeft < lineLeft) {
            offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            mKlineBar.setExtraLeftOffset(offsetLeft);
        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            mKline.setExtraLeftOffset(offsetLeft);
        }
        if (barRight < lineRight) {
            offsetRight = Utils.convertPixelsToDp(barRight);
            mKlineBar.setExtraRightOffset(offsetRight);
        } else {
            offsetRight = Utils.convertPixelsToDp(lineRight);
            mKline.setExtraRightOffset(offsetRight);
        }
    }
}
