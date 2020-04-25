package com.chart.client.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.Log;

/**
 * 画分时线K线网格
 * Created by Hongmingwei on 2017/10/13.
 * Email: 648600445@qq.com
 */

public class GridUtil {
    /**
     * TAG
     */
    private static final String TAG = GridUtil.class.getSimpleName();

    /**
     * 画网格
     * @param canvas 画布
     * @param width 控件总高
     * @param height 分时图K 线图宽度
     */
    public static void drawGrid(Canvas canvas, float width, float height){
        if (canvas == null){
            Log.w(TAG, "drawGrid: ==== Canvas 为空");
            return;
        }
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 8, 8, 8}, 1));
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        //横虚线
        canvas.drawLine(0, height * 3 / 4, width, height * 3 / 4, paint);
        canvas.drawLine(0, height * 1 / 4, width, height * 1 / 4, paint);
        //竖虚线
        canvas.drawLine(width * 3 / 4, 0, width * 3 / 4, height, paint);
        canvas.drawLine(width * 1 / 4, 0, width * 1 / 4, height, paint);
        paint.reset();
        paint.setColor(Color.GRAY);
        //中间实线（横）
        canvas.drawLine(0, height * 2 / 4, width, height * 2 / 4, paint);
        //中间实线（竖）
        canvas.drawLine(width * 2 / 4, 0, width * 2 / 4, height, paint);
        //四周线
        //下
        canvas.drawLine(0, height - 1, width, height - 1, paint);
        //上
        canvas.drawLine(0, 0, width, 0, paint);
        //右
        canvas.drawLine(width -1, 0, width - 1, height, paint);
        //左
        canvas.drawLine(0, 0, 0, height, paint);
        paint.reset();
    }

    /**
     * 画指标网格
     * @param canvas  画布
     * @param y  指标Y轴坐标
     * @param width  控件总宽度
     * @param height 指标高度
     */
    public static void drawIndexGrid(Canvas canvas, float y, float width, float height){
        if (canvas == null){
            Log.w(TAG, "drawIndexGrid: =====Canvas 为空");
            return;
        }
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{20, 20, 20, 20}, 0));
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        //横虚线
        canvas.drawLine(0, y + height / 2, width, y + height / 2, paint);
        //竖虚线
        canvas.drawLine(width * 3 / 4, y, width * 3 / 4, y + height, paint);
        canvas.drawLine(width * 1 / 4, y, width * 1 / 4, y + height, paint);
        paint.reset();
        paint.setColor(Color.GRAY);
        //中间实线(竖)
        canvas.drawLine(width * 2 / 4, y, width * 2 / 4, y + height, paint);
        //四周线
        //下
        canvas.drawLine(0, y + height - 1, width, y + height - 1, paint);
        //上
        canvas.drawLine(0, y, width, y, paint);
        //右
        canvas.drawLine(width - 1, y, width - 1, y + height, paint);
        //左
        canvas.drawLine(0, y, 0, y + height, paint);
        paint.reset();
    }

    /**
     * 夜间分时段特有的七段线
     * @param canvas 画布
     * @param width 控件宽度
     * @param height 控件高度
     */
    public static void drawNightGrid(Canvas canvas, float width, float height){
        if (canvas == null){
            Log.w(TAG, "drawNightGrid: =====  canvas为空");
            return;
        }
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 1}, 0));
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        //横虚线
        canvas.drawLine(0, height * 3 / 4, width, height * 3 / 4, paint);
        canvas.drawLine(0, height * 1 / 4, width, height * 1 / 4, paint);
        //竖虚线
        canvas.drawLine(width * 1 / 6, 0 ,width * 1 / 6, height, paint);
        canvas.drawLine(width * 3 / 6, 0, width * 3 / 6, height, paint);
        canvas.drawLine(width * 5 / 6, 0, width * 5 / 6, height, paint);
        paint.reset();
        paint.setColor(Color.GRAY);
        //中间实线（横）
        canvas.drawLine(0, height * 2 / 4, width, height * 2 / 4, paint);
        //中间实线（竖）
        canvas.drawLine(width * 2 / 6, 0, width * 2 / 6, height, paint);
        canvas.drawLine(width * 4 / 6, 0, width * 4 / 6, height, paint);
        //四周线
        //下
        canvas.drawLine(0, height - 1, width, height - 1, paint);
        //上
        canvas.drawLine(0, 0, width, 0, paint);
        //右
        canvas.drawLine(width - 1, 0, width - 1, height, paint);
        //左
        canvas.drawLine(0, 0, 0, height, paint);
        paint.reset();
    }

    /**
     * 画指标的网格
     * @param canvas 画布
     * @param y 指标左上Y轴坐标
     * @param width  控件总宽度
     * @param height 指标高度
     */
    public static void drawNightIndexGrid(Canvas canvas, float y, float width, float height){
        if (canvas == null){
            Log.w(TAG, "drawNightIndexGrid: ===== Canvas为空");
            return;
        }
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{10, 1}, 0));
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        //横虚线
        canvas.drawLine(0, y + height / 2, width, y + height / 2, paint);
        //竖虚线
        canvas.drawLine(width * 1 / 6, y, width * 1 / 6, y + height, paint);
        canvas.drawLine(width * 3 / 6, y, width * 3 / 6, y + height, paint);
        canvas.drawLine(width * 5 / 6, y, width * 5 / 6, y + height, paint);
        paint.reset();
        paint.setColor(Color.GRAY);
        //中间实线
//        canvas.drawLine(0, y + height * 2 / 4,width, y + height * 2 / 4, paint);
        canvas.drawLine(width * 2 / 6, y, width * 2 / 6, y + height, paint);
        canvas.drawLine(width * 4 / 6, y, width * 4 / 6, y + height, paint);
        //四周线
        //下
        canvas.drawLine(0, y + height - 1, width, y + height - 1, paint);
        //上
        canvas.drawLine(0, y, width, y, paint);
        //右
        canvas.drawLine(width - 1, y, width - 1, y + height, paint);
        //左
        canvas.drawLine(0, y, 0, y + height, paint);
        paint.reset();
    }

    /**
     * 由于早中晚开盘时间不等，需要画一个竖线不规则的网格
     * @param canvas 画布
     * @param width 控件总宽度
     * @param height 分时图，K线图高度
     * @param duration 时间
     */
    public static void drawIrregularGrid(Canvas canvas, float width, float height, String duration){
        float[] lineX = LineUtil.getXByDuration(duration, width);
        if (canvas == null){
            Log.w(TAG, "drawIrregularGrid: ==== Canvas为空");
            return;
        }
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{8, 8, 8, 8}, 1));
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        //横虚线
        canvas.drawLine(0, height * 3 / 4, width, height * 3 / 4, paint);
        canvas.drawLine(0, height * 1 / 4, width, height * 1 / 4, paint);
        //竖虚线
        canvas.drawLine(lineX[0], 0, lineX[0], height, paint);
        canvas.drawLine(lineX[2], 0, lineX[2], height, paint);
        if (lineX.length == 5){
            canvas.drawLine(lineX[4], 0, lineX[4], height, paint);
        }
        paint.reset();
        paint.setColor(Color.GRAY);
        //中间实线（横）
        canvas.drawLine(0, height * 2 / 4, width, height * 2 / 4, paint);
        //中间实线（竖）
        canvas.drawLine(lineX[1], 0, lineX[1], height, paint);
        if (lineX.length == 5){
            canvas.drawLine(lineX[3], 0, lineX[3], height, paint);
        }
        //TODO 四周线
        //下
        canvas.drawLine(0, height - 1f, width, height - 1f, paint);
        //上
        canvas.drawLine(0, 0, width, 0, paint);
        //右
        canvas.drawLine(width - 1, 0, width - 1, height, paint);
        //左
        canvas.drawLine(0, 0, 0, height, paint);
        paint.reset();
    }

    /**
     * 由于早中晚开盘时间不一样，需要画一个竖线不规则的网格
     * @param canvas 画布
     * @param y 指标左上Y坐标
     * @param width  控件总宽度
     * @param height 指标高度
     * @param duration 时间
     */
    public static void drawIrregularIndexGrid(Canvas canvas, float y, float width, float height, String duration){
        if (canvas == null){
            Log.w(TAG, "drawIrregularIndexGrid: ==== Canvas为空");
            return;
        }
        float[] lineX = LineUtil.getXByDuration(duration, width);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[]{20, 20, 20, 20}, 0));
        paint.setColor(Color.GRAY);
        paint.setAntiAlias(true);
        //横虚线
        canvas.drawLine(0, y + height / 2, width, y + height / 2, paint);
        //竖虚线
        canvas.drawLine(lineX[0], y, lineX[0], y + height, paint);
        canvas.drawLine(lineX[2], y, lineX[2], y + height, paint);
        if (lineX.length == 5){
            canvas.drawLine(lineX[4], y, lineX[4], y + height, paint);
        }
        paint.reset();
        paint.setColor(Color.GRAY);
        //中间实线
        canvas.drawLine(lineX[1], y, lineX[1], y + height, paint);
        if (lineX.length == 5){
            canvas.drawLine(lineX[3], y, lineX[3], y + height, paint);
        }
        //四周线
        //下
        canvas.drawLine(0, y + height - 1, width, y + height - 1, paint);
        //上
        canvas.drawLine(0, y, width, y, paint);
        //右
        canvas.drawLine(width - 1, y, width - 1, y + height, paint);
        //左
        canvas.drawLine(0, y, 0, y + height, paint);
        paint.reset();
    }


}
