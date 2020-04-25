package com.chart.client.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.OverScroller;

import com.chart.client.model.CrossModel;
import com.chart.client.model.StickModel;
import com.chart.client.utils.ColorUtil;
import com.chart.client.utils.DrawUtil;
import com.chart.client.utils.GridUtil;
import com.chart.client.utils.IndexParseUtil;
import com.chart.client.utils.LineUtil;
import com.chart.client.utils.NumberUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hongmingwei on 2017/10/17.
 * Email: 648600445@qq.com
 */

public class KLineChartView extends ChartView implements ChartConstant {
    /**
     * TAG
     */
    private static final String TAG = KLineChartView.class.getSimpleName();

    private Context mContext;
    //烛形图加空白的宽度和烛形图
    public static final float WIDTH_SCALE = 1.2f;
    //烛形图和右侧空白的宽度
    public float DEFUALT_WIDTH = 19;
    //K线所有数据
    private ArrayList<StickModel> models;
    //K线展示的数据
    private ArrayList<StickModel> showModels;
    //一屏烛形图的数量
    private int drawCount;
    //每两个烛形图X轴的距离
    private float candleXDistance;
    //当前画图偏移量（往右滑动之后）
    private int offset;
    //y轴最大值
    protected double yMax;
    //Y轴最小值
    protected double yMin;

    protected float yUnit;
    protected float xUnit;
    //获取最大滑动阻力
    private int mMaxFlingVelocity;
    //获取手指按下坐标
    private float mDownX;
    //获取当前坐标
    private float mMoveDist;
    //滑动速度跟踪器
    private VelocityTracker mVelocityTracker;
    //滑动渐变动画
    private Animation mAnimation;


    public KLineChartView(Context context) {
        this(context, null);
    }

    public KLineChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KLineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context){
        mContext = context;
        final ViewConfiguration vc = ViewConfiguration.get(mContext);
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
    }



    private ScaleGestureDetector mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener(){
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if(models == null) return super.onScale(detector);
            //放大是由1变大，缩小是由1变小
            float scale = detector.getScaleFactor();
            //这个变化太快，把scale变慢一点
            scale = 1 + ((scale - 1) * 0.8f);
            drawCount = (int) (mWidth / DEFUALT_WIDTH);
            if(scale < 1 && drawCount >= models.size()) {
                //缩小时，当缩到一屏显示完时，则不再缩小
            } else if(scale > 1 && drawCount < 30) {
                //放大时，当一屏少于15个时，则不再放大
            } else {
                DEFUALT_WIDTH = DEFUALT_WIDTH * scale;
                invalidate();
            }
            return super.onScale(detector);
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float curX = event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                stopScroll();       //停止滑动
                mDownX = event.getX();
                mMoveDist = curX;

                //取得velocityTracker实例
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);
                break;
            case MotionEvent.ACTION_UP:
                mMoveDist = curX - mMoveDist;
                if (Math.abs(mMoveDist) < ViewConfiguration.get(mContext).getScaledTouchSlop()) {//过滤点击不小心滑动
                    //如果是点击操作
                    //do something
                } else {
                    //手势滑动之后继续滚动
                    sliding();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);//计算速度
                mDownX = curX;
                break;
            default:
                break;
        }

        if (mGestureDetector != null)
            mGestureDetector.onTouchEvent(event);
        if(mScaleGestureDetector != null)
            mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void init() {
        if (models == null){
            return;
        }
        //获取数量
        drawCount = (int) (mWidth / DEFUALT_WIDTH);
        //获取间隔
        candleXDistance = drawCount * WIDTH_SCALE;
        if (models != null && models.size() > 0){
            if (drawCount < models.size()){
                getShowList(offset);
            } else {
                showModels = new ArrayList<>();
                showModels.addAll(models);
            }
        }
        if (showModels == null){
            return;
        }
        float[] low = new float[showModels.size()];
        float[] high = new float[showModels.size()];
        int i = 0;
        for (StickModel model : showModels){
            low[i] = (float) model.getLow();
            high[i] = (float) model.getHigh();
            i++;
        }
        float[] maxAndMin = LineUtil.getMaxAndMin(low, high);
        yMax = maxAndMin[0];
        yMin = maxAndMin[1];
        yUnit = (float) ((yMax - yMin) / mainH);
        xUnit = mWidth / drawCount;
    }

    @Override
    protected void drawGrid(Canvas canvas) {
        GridUtil.drawGrid(canvas, mWidth, mainH);
        GridUtil.drawIndexGrid(canvas, indexStartY, mWidth, indexH);
    }

    @Override
    protected void drawCandles(Canvas canvas) {
        if (models == null && models.size() == 0){
            return;
        }
        float x = 0;
        if (showModels == null || showModels.size() == 0){
            return;
        }
        //计算出页面能显示多少个
        for (int i = 0;i < showModels.size(); i++){
            if (drawCount < models.size()){
                x = mWidth - (mWidth / drawCount * (showModels.size() - i));
            } else {
                x = (mWidth / drawCount * i);
            }
            DrawUtil.drawCandle(canvas,
                    parseNumber(showModels.get(i).getHigh()),
                    parseNumber(showModels.get(i).getLow()),
                    parseNumber(showModels.get(i).getOpen()),
                    parseNumber(showModels.get(i).getClose()),
                    x, parseNumber(showModels.get(i).getHigh()),
                    candleXDistance, mWidth);
        }
    }

    /**
     * SMA5 SMA10 SMA20线
     * @param canvas
     */
    @Override
    protected void drawLines(Canvas canvas) {
        if (models == null || models.size() == 0){
            return;
        }
        float[] sma5 = new float[showModels.size()];
        float[] sma10 = new float[showModels.size()];
        float[] sma20 = new float[showModels.size()];
        int size = showModels.size();
        for (int i = 0; i < showModels.size(); i++){
            if (size > IndexParseUtil.START_SMA5){
                sma5[i] = (float) showModels.get(i).getSma5();
            }
            if (size > IndexParseUtil.START_SMA10){
                sma10[i] = (float) showModels.get(i).getSma10();
            }
            if (size > IndexParseUtil.START_SMA20){
                sma20[i] = (float) showModels.get(i).getSma20();
            }
        }
        DrawUtil.drawLineWithXOffset(canvas, sma5, DEFUALT_WIDTH, mainH, ColorUtil.COLOR_SMA5, (float) yMax, (float) yMin, DEFUALT_WIDTH / 2);
        DrawUtil.drawLineWithXOffset(canvas, sma10, DEFUALT_WIDTH, mainH, ColorUtil.COLOR_SMA10, (float) yMax, (float) yMin, DEFUALT_WIDTH / 2);
        DrawUtil.drawLineWithXOffset(canvas, sma20, DEFUALT_WIDTH, mainH, ColorUtil.COLOR_SMA20, (float) yMax, (float) yMin, DEFUALT_WIDTH / 2);
    }

    @Override
    protected void drawText(Canvas canvas) {
        if (models == null || models.size() == 0){
            return;
        }
        //画X轴时间
        if (showModels.size() <= drawCount){
            DrawUtil.drawKLineXTime(canvas, showModels.get(0).getTime(), showModels.get(showModels.size() - 1).getTime(), mWidth, mainH);
        } else {
            DrawUtil.drawKLineXTime(canvas, showModels.get(0).getTime(), null, mWidth, mainH);
        }
        //Y 轴价格
        DrawUtil.drawKLineYPrice(canvas, yMax, yMin, mainH);
    }

    @Override
    protected void drawVOL(Canvas canvas) {
        if (models == null || models.size() == 0){
            return;
        }
        long max = 0;
        for (StickModel model : showModels){
            max = model.getCount() > max ? model.getCount() : max;
        }
        //如果量全为0， 则不画
        if (max == 0){
            return;
        }
        //画量线，多条竖直线
        DrawUtil.drawVOLRects(canvas, xUnit, indexStartY, indexH, max, showModels);
        //画量  sma5  sma10
        drawCountSma(canvas, (float) max);
    }

    @Override
    protected void drawZJ(Canvas canvas) {
        if(models == null || models.size() == 0) return;
        float[] sup = new float[showModels.size()];
        float[] big = new float[showModels.size()];
        float[] mid = new float[showModels.size()];
        float[] sma = new float[showModels.size()];
        for (int i = 0; i < showModels.size(); i++) {
            sup[i] = (float) showModels.get(i).getSp();
            big[i] = (float) showModels.get(i).getBg();
            mid[i] = (float) showModels.get(i).getMd();
            sma[i] = (float) showModels.get(i).getSm();
        }
        float maxAndMin[] = LineUtil.getMaxAndMin(sup, big, mid, sma);
        DrawUtil.drawLines(canvas, sup, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_ZJ_SUPER, maxAndMin[0], maxAndMin[1], false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawLines(canvas, big, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_ZJ_BIG, maxAndMin[0], maxAndMin[1], false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawLines(canvas, mid, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_ZJ_MIDDLE, maxAndMin[0], maxAndMin[1], false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawLines(canvas, sma, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_ZJ_SMALL, maxAndMin[0], maxAndMin[1], false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawIndexMiddleText(canvas, (maxAndMin[0] - maxAndMin[1]) / 2 + "", indexStartY + indexH / 2);
    }

    @Override
    protected void drawMACD(Canvas canvas) {
        if(models == null || models.size() == 0) return;
        float[] dif = new float[showModels.size()];
        float[] dea = new float[showModels.size()];
        float[] macd = new float[showModels.size()];
        for (int i = 0; i < showModels.size(); i++) {
            if(models.indexOf(showModels.get(i)) > IndexParseUtil.START_DIF)
                dif[i] = (float) showModels.get(i).getDif();
            if(models.indexOf(showModels.get(i)) > IndexParseUtil.START_DEA) {
                dea[i] = (float) showModels.get(i).getDea();
                macd[i] = (float) showModels.get(i).getMacd();
            }
        }
        float maxAndMin[] = LineUtil.getMaxAndMin(dif, dea, macd);
        DrawUtil.drawMACDRects(canvas, macd, maxAndMin[0], maxAndMin[1], indexH - 2, indexStartY + 2, DEFUALT_WIDTH);
        DrawUtil.drawLines(canvas, dif, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_DIF, maxAndMin[0], maxAndMin[1], false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawLines(canvas, dea, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_DEA, maxAndMin[0], maxAndMin[1], false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawIndexMiddleText(canvas, "0", indexStartY + indexH / 2);
    }

    @Override
    protected void drawKDJ(Canvas canvas) {
        if(models == null || models.size() == 0) return;
        float[] kl = new float[showModels.size()];
        float[] dl = new float[showModels.size()];
        float[] jl = new float[showModels.size()];
        int size = showModels.size();
        for (int i = 0; i < showModels.size(); i++) {
            if(size > IndexParseUtil.START_K)
                kl[i] = (float) showModels.get(i).getK();
            if(size > IndexParseUtil.START_DJ) {
                dl[i] = (float) showModels.get(i).getD();
                jl[i] = (float) showModels.get(i).getJ();
            }
        }
        float maxAndMin[] = LineUtil.getMaxAndMin(kl, dl, jl);
        DrawUtil.drawLines(canvas, kl, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_KDJ_K, maxAndMin[0], maxAndMin[1], false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawLines(canvas, dl, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_KDJ_D, maxAndMin[0], maxAndMin[1], false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawLines(canvas, jl, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_KDJ_J, maxAndMin[0], maxAndMin[1], false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawIndexMiddleText(canvas, (maxAndMin[0] - maxAndMin[1]) / 2 + "", indexStartY + indexH / 2);
    }

    @Override
    protected boolean onViewScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
//        && Math.abs(distanceX) > DEFUALT_WIDTH
        if (models != null && drawCount < models.size()){
            int temp = offset + (int)(0 - distanceX * 0.2);
            if (temp < 0 || temp + drawCount > models.size()){

            } else {
                offset = temp;
                postInvalidate();
            }
            return true;
        }
        return false;
    }

    /**
     * 获取页面一页可展示的数据
     * @param offset
     */
    private void getShowList(int offset){
        if (offset != 0 && models.size() - drawCount - offset < 0){
            offset = models.size() - drawCount;
        }
        showModels = new ArrayList<>();
        showModels.addAll(models.subList(models.size() - drawCount - offset, models.size() - offset));
    }

    /**
     * 把传入的参数计算成坐标，直接展示到界面上
     * @param input
     * @return 返回里面的StickData的最高价最低价，都是可以直接显示在坐标上的
     */
    private float parseNumber(double input){
        return mainH - (float)((input - yMin) / yUnit);
    }

    /**
     * 画量的均线
     * @param canvas
     * @param max
     */
    private void drawCountSma(Canvas canvas, float max){
        if(models == null || models.size() == 0) {
            return;
        }
        float[] sma5 = new float[showModels.size()];
        float[] sma10 = new float[showModels.size()];
        int size = showModels.size();
        for (int i = 0; i < showModels.size(); i++) {
            if(size > IndexParseUtil.START_SMA5){
                sma5[i] = (float) showModels.get(i).getCountSma5();
            }
            if(size > IndexParseUtil.START_SMA10){
                sma10[i] = (float) showModels.get(i).getCountSma10();
            }
        }
        Log.e(TAG, "drawCountSma: ======" + indexStartY + "=====" + indexH);
        DrawUtil.drawLines(canvas, sma5, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_SMA5, max, 0f, false, indexStartY + 2, DEFUALT_WIDTH / 2);
        DrawUtil.drawLines(canvas, sma10, DEFUALT_WIDTH, indexH - 2, ColorUtil.COLOR_SMA10, max, 0f, false, indexStartY + 2, DEFUALT_WIDTH / 2);
    }

    public void setDataAndInvalidate(ArrayList<StickModel> models) {
        this.models = models;
        parseData();
        postInvalidate();
    }

    /**
     * 设置K线类型
     */
    public void setType(int type) {
        lineType = type;
    }

    /**
     * 计算各指标
     */
    private void parseData() {
        offset = 0;
        //根据当前显示的指标类型，优先计算指标
        IndexParseUtil.initSma(this.models);
        switch (indexType) {
            case INDEX_MACD:
                IndexParseUtil.initMACD(models);
                break;
            case INDEX_KDJ:
                IndexParseUtil.initKDJ(models);
                break;
        }
        //把暂时不显示的计算，放在线程中去完成，避免阻塞主线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (indexType) {
                    case INDEX_VOL:
                        IndexParseUtil.initMACD(models);
                        IndexParseUtil.initKDJ(models);
                        break;
                    case INDEX_MACD:
                        IndexParseUtil.initKDJ(models);
                        break;
                    case INDEX_KDJ:
                        IndexParseUtil.initMACD(models);
                        break;
                }
            }
        }).start();
    }

    @Override
    public void onCrossMove(float x, float y) {
        super.onCrossMove(x, y);
        if(mCrossView == null || showModels == null) return;
        int position = (int) Math.rint(new Double(x)/ new Double(DEFUALT_WIDTH));
        if(position < showModels.size()) {
            StickModel model = showModels.get(position);
            float xIn = (mWidth / drawCount * position) + (mWidth / candleXDistance / 2);
            CrossModel bean = new CrossModel(xIn, getY(model.getClose()));
            bean.price = model.getClose() + "";
            //注意，这个值用来区分，是否画出均线的小圆圈
            bean.y2 = -1;
            bean.timeYMD = model.getTime();
            setIndexTexts(model, bean);
            mCrossView.drawLine(bean);
            if(mCrossView.getVisibility() == GONE)
                mCrossView.setVisibility(VISIBLE);
            //TODO 这里显示该点数据到屏幕
            msgText.setVisibility(VISIBLE);
            msgText.setText(Html.fromHtml(ColorUtil.getCurPriceInfo(model)));
        }
    }

    @Override
    public void onDismiss() {
        msgText.setVisibility(INVISIBLE);
    }

    //获取价格对应的Y轴
    private float getY(double price) {
        return mainH - (new Float(price) - (float)yMin) / yUnit;
    }

    /**
     * 设置指标左上角的文字
     * @param data
     * @param bean
     */
    private void setIndexTexts(StickModel data, CrossModel bean) {
        switch (indexType) {
            case INDEX_VOL:
                bean.indexText = new String[3];
                bean.indexText[0] = "VOL:" + data.getCount();
                bean.indexText[1] = "SMA5:" + data.getCountSma5();
                bean.indexText[2] = "SMA10:" + data.getCountSma10();
                bean.indexColor = new int[] {
                        data.isRise() ? ColorUtil.COLOR_RED : ColorUtil.COLOR_GREEN,
                        ColorUtil.COLOR_SMA5,
                        ColorUtil.COLOR_SMA10
                };
                break;
            case INDEX_ZJ:
                bean.indexText = new String[4];
                bean.indexText[0] = "超大:" + data.getSp();
                bean.indexText[1] = "大:" + data.getBg();
                bean.indexText[2] = "中:" + data.getMd();
                bean.indexText[3] = "小:" + data.getSm();
                bean.indexColor = new int[]{
                        ColorUtil.COLOR_ZJ_SUPER,
                        ColorUtil.COLOR_ZJ_BIG,
                        ColorUtil.COLOR_ZJ_MIDDLE,
                        ColorUtil.COLOR_ZJ_SMALL
                };
                break;
            case INDEX_MACD:
                bean.indexText = new String[4];
                bean.indexText[0] = "MACD(12,26,9)";
                bean.indexText[1] = "DIF：" + NumberUtil.beautifulDouble(data.getDif());
                bean.indexText[2] = "DEA：" + NumberUtil.beautifulDouble(data.getDea());
                bean.indexText[3] = "MACD：" + NumberUtil.beautifulDouble(data.getMacd());
                bean.indexColor = new int[] {
                        Color.BLACK,
                        ColorUtil.COLOR_DIF,
                        ColorUtil.COLOR_DEA,
                        ColorUtil.COLOR_MACD
                };
                break;
            case INDEX_KDJ:
                bean.indexText = new String[4];
                bean.indexText[0] = "KDJ(9,3,3)";
                bean.indexText[1] = "K：" + NumberUtil.beautifulDouble(data.getK());
                bean.indexText[2] = "D：" + NumberUtil.beautifulDouble(data.getD());
                bean.indexText[3] = "J：" + NumberUtil.beautifulDouble(data.getJ());
                bean.indexColor = new int[] {
                        Color.BLACK,
                        ColorUtil.COLOR_KDJ_K,
                        ColorUtil.COLOR_KDJ_D,
                        ColorUtil.COLOR_KDJ_J
                };
                break;
        }
    }

    /**
     * 滑动操作后的惯性滑动。
     * 利用animation实现
     */
    private void sliding() {
        if (mAnimation == null) {
            mAnimation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    float speed = -(mVelocityTracker.getXVelocity() / 100);
                    speed = (1 - interpolatedTime) * speed;

//                    && Math.abs(speed) > DEFUALT_WIDTH
                    if (models != null && drawCount < models.size()){
                        int temp = offset + (int)(0 - speed * 0.2);
                        if (temp < 0 || temp + drawCount > models.size()){
                        } else {
                            offset = temp;
                            postInvalidate();
                        }
                    }
                }
            };
            mAnimation.setInterpolator(new DecelerateInterpolator());//设置一个减速插值器
        }

        stopScroll();
        mAnimation.setDuration(1000);
        startAnimation(mAnimation);
    }

    /**
     * 停止滑动
     */
    private void stopScroll() {
        if (mAnimation != null && !mAnimation.hasEnded()) {
            mAnimation.cancel();
            clearAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mVelocityTracker != null) {//要记得回收
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}
