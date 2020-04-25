package com.chart.client.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.chart.client.model.CrossModel;
import com.chart.client.utils.ColorUtil;
import com.chart.client.utils.LineUtil;

/**
 * Created by Hongmingwei on 2017/10/10.
 * Email: 648600445@qq.com
 */

public class CrossView extends View {
    /**
     * TAG
     */
    private static final String TAG = CrossView.class.getSimpleName();
    /**
     * params
     */
    private CrossModel model;
    //手势控制
    private GestureDetector mGestureDetector;
    private OnMoveListener mOnMoveListener;
    private Context mContext;

    public CrossView(Context context) {
        super(context);
        init(context);
    }

    public CrossView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CrossView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 设置手势控制
     * @param context
     */
    private void init(Context context){
        mContext = context;
        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //单击隐藏十字线
                setVisibility(GONE);
                if (mOnMoveListener != null){
                    mOnMoveListener.onDismiss();
                }
                return super.onSingleTapUp(e);
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                //滑动时，通知到接口
                if (mOnMoveListener != null){
                    mOnMoveListener.onCrossMove(e2.getX(), e2.getY());
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector != null){
            mGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "onDraw: ===========");
        drawCrossLine(canvas);
    }

    /**
     * 根据X，Y画十字线
     * @param canvas
     */
    private void drawCrossLine(Canvas canvas){
        //当该点没有数据的时候不画
        if (model.x < 0 || model.y < 0){
            return;
        }
        boolean isAverage = model.y2 >= 0;
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(ColorUtil.COLOR_CROSS_LINE);
        linePaint.setStrokeWidth(2f);
        linePaint.setStyle(Paint.Style.FILL);
        //横线
        canvas.drawLine(0, model.y, getWidth(), model.y, linePaint);
        //竖线
        canvas.drawLine(model.x, 0, model.x, getHeight(), linePaint);
        if (isAverage){
            //在均线的时候画出圆点，画十字线和均线价格交汇的圆
            canvas.drawCircle(model.x, model.y, 5, linePaint);
            linePaint.setColor(ColorUtil.COLOR_SMA_LINE);
            canvas.drawCircle(model.x, model.y2, 5, linePaint);
        }
        linePaint.setColor(Color.BLACK);
        linePaint.setTextSize(32f);
        //写价格
        drawPriceTextWithRect(canvas, model.x, model.y, model.price, linePaint);
        //写时间
        drawTimeTextWithRect(canvas, model.x, model.getTime(), linePaint);
        //写指标的文字
        drawIndexTexts(canvas);
        linePaint.reset();
    }

    /**
     * 指标滑动时显示的文字数组
     * @param canvas
     */
    private void drawIndexTexts(Canvas canvas){
        if (model.indexText == null || model.indexColor == null){
            return;
        }
        Paint indexPaint = new Paint();
        indexPaint.setAntiAlias(true);
        indexPaint.setTextSize(26f);
        float x = 0;
        float y = getHeight() * (ChartConstant.MAIN_SCALE + ChartConstant.TIME_SCALE) + 25f;
        for (int i = 0; i < model.indexText.length; i++){
            indexPaint.setColor(model.indexColor[i]);
            canvas.drawText(model.indexText[i], x, y, indexPaint);
            x += LineUtil.getTextWidth(indexPaint, model.indexText[i]) + 30;
        }
    }


    /**
     * 写时间
     * @param canvas  画布
     * @param x  价格X轴
     * @param time  时间
     * @param linePaint  画笔
     */
    private void drawTimeTextWithRect(Canvas canvas, float x, String time, Paint linePaint){
        linePaint.setTextAlign(Paint.Align.LEFT);
        float contentWidth = LineUtil.getTextWidth(linePaint, time) + 20f;
        float y = getHeight() * ChartConstant.MAIN_SCALE;
        Paint timePaint = new Paint();
        timePaint.setColor(Color.WHITE);
        timePaint.setStyle(Paint.Style.FILL);
        timePaint.setAntiAlias(true);
        timePaint.setStrokeWidth(2f);
        //画白底
        float startX = x - contentWidth / 2;
        float endX = x + contentWidth / 2;
        if (startX < 0){
            startX  = 2f;
            endX = startX + contentWidth;
        } else if (endX > getWidth()){
            endX = getWidth() - 2;
            startX = endX - contentWidth;
        }
        canvas.drawRect(startX, y + 2f, endX, y + 30f, timePaint);
        timePaint.setColor(Color.BLACK);
        timePaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(startX, y + 2f, endX, y + 30f, timePaint);
        canvas.drawText(time, startX + 10f, y + 27.5f, linePaint);
    }

    /**
     * 写文字，并且为文字带上背景
     * @param canvas 画布
     * @param x  价格X轴
     * @param y  价格Y轴
     * @param content  价格
     * @param linePaint 画笔
     */
    private void drawPriceTextWithRect(Canvas canvas, float x, float y, String content, Paint linePaint){
        float contentWidth = LineUtil.getTextWidth(linePaint, content) + 10;
        Paint contentPaint = new Paint();
        contentPaint.setColor(Color.WHITE);
        contentPaint.setAntiAlias(true);
        contentPaint.setStyle(Paint.Style.FILL);
        contentPaint.setStrokeWidth(2f);
        float startY = y - 15f;
        float endY = y + 15f;
        //TODO 当文字Y轴在0以下的时候
        if (startY < 0){
            startY = 0f;
            endY = startY + 30f;
        } else if (endY > getHeight()){  //TODO 当文字Y轴超出Y轴高度的时候
            endY = getHeight();
            startY = endY  - 30f;
        }

        if (x < 200){
            //X轴在左，文字在右，画白底
            canvas.drawRect(getWidth() - contentWidth, startY, getWidth(), endY, contentPaint);
            contentPaint.setColor(Color.BLACK);
            contentPaint.setStyle(Paint.Style.STROKE);
            //画黑框
            canvas.drawRect(getWidth() - contentWidth, startY, getWidth(), endY, contentPaint);
            linePaint.setTextAlign(Paint.Align.RIGHT);
            //写文字
            canvas.drawText(content, getWidth() - 5f, endY - 3f, linePaint);
        } else {
            //X轴在右，文字在左
            canvas.drawRect(0, startY, contentWidth, endY, contentPaint);
            contentPaint.setColor(Color.BLACK);
            contentPaint.setStyle(Paint.Style.STROKE);
            //画黑框
            canvas.drawRect(0, startY, contentWidth, endY, contentPaint);
            linePaint.setTextAlign(Paint.Align.LEFT);
            //写文字
            canvas.drawText(content, 5f, endY - 3f, linePaint);
        }

    }




    /**
     * 画分时线的十字线
     * @param model
     */
    public void drawLine(CrossModel model){
        this.model = model;
        postInvalidate();
    }

    /**
     * 设置移动监听
     * @param onMoveListener
     */
    public void setOnMoveListener(OnMoveListener onMoveListener){
        mOnMoveListener = onMoveListener;
    }

    /**
     * 十字线移动的监听
     */
    public interface OnMoveListener{
        /**
         * 十字线移动（回调到数据存放的位置，判断是否需要画线后，在调用本界面方法）
         * @param x  x轴坐标
         * @param y  y轴坐标
         */
        void onCrossMove(float x, float y);

        /**
         * 十字线消失的回调
         */
        void onDismiss();
    }
}
