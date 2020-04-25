package com.chart.client.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Hongmingwei on 2017/10/12.
 * Email: 648600445@qq.com
 */

public abstract class ChartView extends View implements ChartConstant, CrossView.OnMoveListener, NestedScrollingChild{
    /**
     * TAG
     */
    private static final String TAG = ChartView.class.getSimpleName();
    private Context mContext;
    //总宽
    protected float mWidth;
    //总高
    protected float mHeight;
    //分时图，K线图高度
    protected float mainH;
    //指标高度
    protected float indexH;
    //指标左上Y坐标
    protected float indexStartY;
    //时间左上Y坐标
    protected float timeStartY;
    //分时线或K线类型， 取值看ChartConstant
    protected int lineType = TYPE_TICK;
    //指标类型
    protected int indexType = INDEX_VOL;
    //十字线最后停下的点，当切换指标的时候，使用这点来计算指标应显示的文字
    private float lastX, lastY;
    //十字线点对应的详情展示
    protected CrossView mCrossView;
    //十字线点对应的详情展示
    protected TextView msgText;
    //双击监听，全屏
    protected OnDoubleTapListener onDoubleTapListener;



    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //关闭硬件加速，不然虚线显示为实线了
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //初始化需要的数据
        initWidthAndHeight();
        //画网格
        drawGrid(canvas);
        //画线 (分时线的价格线，均线或K线的均线)
        drawLines(canvas);
        if (lineType != TYPE_TICK){
            //如果是K线另外画烛形图
            drawCandles(canvas);
        }
        //写X轴 Y轴的文字
        drawText(canvas);
        switch (indexType){
            case INDEX_VOL:
                drawVOL(canvas);
                break;
            case INDEX_ZJ:
                drawZJ(canvas);
                break;
            case INDEX_MACD:
                drawMACD(canvas);
                break;
            case INDEX_KDJ:
                drawKDJ(canvas);
                break;
        }
    }

    /**
     * 画网格包含主网格和指标网格
     * @param canvas
     */
    protected abstract void drawGrid(Canvas canvas);

    /**
     * 分时价格线均线，K线均线
     * @param canvas
     */
    protected void drawLines(Canvas canvas){}

    /**
     * 画烛形图
     * @param canvas
     */
    protected void drawCandles(Canvas canvas){}

    /**
     * 写X， Y轴文字
     * @param canvas
     */
    protected abstract void drawText(Canvas canvas);

    /**
     * 画量
     * @param canvas
     */
    protected abstract void drawVOL(Canvas canvas);

    /**
     * 资金
     * @param canvas
     */
    protected abstract void drawZJ(Canvas canvas);

    /**
     * MACD
     * @param canvas
     */
    protected void drawMACD(Canvas canvas){}

    /**
     * KDJ
     * @param canvas
     */
    protected void drawKDJ(Canvas canvas){}
    /**
     * 初始化宽高
     */
    private void initWidthAndHeight(){
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mainH = mHeight * MAIN_SCALE;
        indexH = mHeight * INDEX_SCALE;
        indexStartY = mHeight - indexH;
        timeStartY = indexStartY - mHeight * TIME_SCALE;
        init();
    }

    /**
     * 在开始画图前，初始化数据
     */
    protected abstract void init();

    protected  abstract boolean onViewScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY);

    public interface OnDoubleTapListener{
        void onDoubleTap();
    }

    @Override
    public void onCrossMove(float x, float y) {
        lastX = x;
        lastY = y;
    }

    /**
     * 点击手势监听
     */
    protected GestureDetector mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapUp(final MotionEvent e) {
            //TODO 延时300毫秒显示，为双击腾出时间
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //单击显示十字线
                    if (mCrossView != null){
                        if (mCrossView.getVisibility() == View.GONE){
                            onCrossMove(e.getX(), e.getY());
                        }
                    }
                }
            }, DOUBLE_TAP_DELAY);
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
//            //单击显示十字线
//            if (mCrossView != null){
//                if (mCrossView.getVisibility() == View.GONE){
//                    onCrossMove(e.getX(), e.getY());
//                }
//            }
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleTapListener.onDoubleTap();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.e(TAG, "onScroll: =========" );
            return onViewScroll(e1, e2, distanceX, distanceY);
        }
    });

    /**
     * 设置需要使用的View
     * @param crossView
     * @param msgText
     */
    public void setUsedViews(CrossView crossView, TextView msgText) {
        this.mCrossView = crossView;
        this.msgText = msgText;
        crossView.setOnMoveListener(this);
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.onDoubleTapListener = onDoubleTapListener;
    }

    public void showVOL() {
        indexType = INDEX_VOL;
        postInvalidate();
        resetIndexText();
    }

    public void showZJ() {
        indexType = INDEX_ZJ;
        postInvalidate();
        resetIndexText();
    }

    public void showMACD() {
        indexType = INDEX_MACD;
        postInvalidate();
        resetIndexText();
    }

    public void showKDJ() {
        indexType = INDEX_KDJ;
        postInvalidate();
        resetIndexText();
    }

    /**
     * 十字线显示的时候，切换指标之后，让十字线显示的指标文字也切换掉
     */
    protected void resetIndexText() {
        if(mCrossView != null && mCrossView.getVisibility() == VISIBLE) {
            onCrossMove(lastX, lastY);
        }
    }
}
