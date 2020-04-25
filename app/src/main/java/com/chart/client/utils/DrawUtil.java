package com.chart.client.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.Log;

import com.chart.client.model.CMinute;
import com.chart.client.model.StickModel;
import com.chart.client.ui.KLineChartView;

import java.util.ArrayList;

/**
 * Created by Hongmingwei on 2017/10/13.
 * Email: 648600445@qq.com
 */

public class DrawUtil {
    /**
     * TAG
     */
    private static final String TAG = DrawUtil.class.getSimpleName();

    //分时图时间、价格、涨幅文字大小
    public static final float TICK_TEXT_SIZE = 26f;
    //时间文字大小
    public static final float TICK_TIME_SIZE = 30f;
    //当文字在最上方的时候，需要往下偏移，如高的涨幅
    public static final float TICK_TEXT_OFFSET = 25f;


    /**
     * 分时线： Y轴涨跌幅
     * @param canvas 画布
     * @param max 最大值
     * @param min 最小值
     * @param yd 昨收
     * @param width 控件总宽度
     * @param height  行情图高度
     */
    public static void drawYPercentAndPrice(Canvas canvas, double max, double min, double yd, float width, float height){
        //计算出最大涨跌幅
        double upPercent = (max - yd) / yd; //最大值减去昨收 然后除以昨收为做大涨幅
        double downPercent = (min - yd) / yd; // 最小值减去昨收， 然后除以昨收为最大跌幅
        double maxPercent = Math.abs(upPercent) > Math.abs(downPercent) ? upPercent : downPercent;  //获取最大涨幅或者是跌幅
        double halfPercent = maxPercent / 2d;
        //计算出做大价格
        double diff = Math.abs(max - yd) > Math.abs(min - yd) ? Math.abs(max - yd) : Math.abs(min - yd);
        String p1 = NumberUtil.getMoneyString(yd + diff);
        String p2 = NumberUtil.getMoneyString(yd + diff / 2);
        String p3 = NumberUtil.getMoneyString(yd - diff / 2);
        String p4 = NumberUtil.getMoneyString(yd - diff);

        Paint paint = new Paint();
        paint.setColor(ColorUtil.getTextColorAsh(1, 0));
        paint.setTextSize(TICK_TEXT_SIZE);
        paint.setTextAlign(Paint.Align.RIGHT);
        //最大涨幅（价格）
        canvas.drawText(NumberUtil.getPercentString(Math.abs(maxPercent)), width, TICK_TEXT_OFFSET, paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(p1, 0, TICK_TEXT_OFFSET, paint);
        //一般涨幅
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(NumberUtil.getPercentString(Math.abs(halfPercent)), width, height * 1 / 4, paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(p2, 0, height * 1 / 4, paint);
        //中间
        paint.setColor(ColorUtil.getTextColorAsh(0, 0));
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("0.00%", width, height / 2, paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(NumberUtil.getMoneyString(yd), 0, height / 2, paint);
        //跌幅一半
        paint.setColor(ColorUtil.getTextColorAsh(0, 1));
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("-" + NumberUtil.getPercentString(Math.abs(halfPercent)), width, height * 3 / 4, paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(p3, 0, height * 3 / 4, paint);
        //最大跌幅
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("-" + NumberUtil.getPercentString(Math.abs(maxPercent)), width, height - 4,paint);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(p4, 0, height - 4, paint);
        paint.reset();
    }

    /**
     * 分时图： X轴时间
     * @param canvas 画布
     * @param duration  服务器返回的时间
     * @param time   时间戳
     * @param width 控件宽度
     * @param height  行情图高度
     */
    public static void drawXTime(Canvas canvas, String duration, long time, float width, float height){
        String leftTime = DateUtil.getShortDateJustMonthAndDay(time);
        ArrayList<String> times = LineUtil.getTimes(duration);
        float[] textX = null;
        if (LineUtil.isIrregular(duration)){
            textX = LineUtil.getXByDuration(duration, width);
        }
        if (times.size() == 0){
            return;
        }
        float x1 = 0;
        float x2 = 0;
        float x3 = 0;
        float x4 = 0;
        float x5 = 0;
        float x6 = 0;
        if (times.size() == 2){
            x2 = width;
        } else if (times.size() == 4){
            if (textX != null){
                x2 = textX[1] - 5;
            } else {
                x2 = width / 2f - 5;
            }
            x3 = x2;
            x4 = width;
        } else if (times.size() == 6){
            if (textX != null){
                x2 = textX[1] - 3;
                x4 = textX[3] - 3;
            } else {
                x2 = width / 3f - 5;
                x4 = width * 2f / 3f - 5;
            }
            x3 = x2;
            x5 = x4;
            x6 = width;
        }
        //上面偏移一点
        height = height + 25;
        Paint paint = new Paint();
        paint.setTextSize(TICK_TIME_SIZE);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        //左侧日期
        canvas.drawText(leftTime, x1, height, paint);
        if (times.size() == 2){
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(times.get(1), x2, height, paint);
        } else if (times.size() == 4){
            canvas.drawText("|" + times.get(2), x3, height, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(times.get(1), x2, height, paint);
            canvas.drawText(times.get(3), x4, height, paint);
        } else if (times.size() == 6){
            canvas.drawText("|" + times.get(2), x3, height, paint);
            canvas.drawText("|" + times.get(4), x5, height, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(times.get(1), x2, height, paint);
            canvas.drawText(times.get(3), x4, height, paint);
            canvas.drawText(times.get(5), x6, height, paint);
        }
        paint.reset();
    }

    /**
     * 画线
     * @param canvas  画布
     * @param prices  价格
     * @param xUnit   X轴每两点距离
     * @param height  控件高度
     * @param color   颜色
     * @param max   价格最大值
     * @param min   价格最小值
     * @param fromZero   是否从0开始，意思是，假如Y轴为0是否画出该点（比如在SMA5， SMA10中，并不是0开始的）
     */
    public static void drawLines(Canvas canvas, float[] prices, float xUnit, float height, int color, float max, float min, boolean fromZero){
        drawLines(canvas, prices, xUnit, height, color, max, min, fromZero, 0, 0);
    }


    /**
     * 画线
     * @param canvas  画布
     * @param prices 价格
     * @param xUnit x轴每两点距离
     * @param height 控件高度
     * @param color  颜色
     * @param max   价格最大值
     * @param min   价格最小值
     * @param fromZero  是否从0开始，意思是，假如Y轴为0是否画出该点（比如在SMA5， SMA10中，并不是0开始的）
     * @param y   左上角Y轴，指标并不是从左上角开始画的
     * @param xOffset
     */
    public static void drawLines(Canvas canvas, float[] prices, float xUnit, float height, int color, float max, float min, boolean fromZero, float y, float xOffset){
        if (canvas == null){
            Log.w(TAG, "drawLines: ==== Canvas为空");
            return;
        }
        float tmax = 0f;
        float tmin = 0f;
        for (float price : prices){
            tmax = tmax > price ? tmax : price;
            tmin = tmin < price ? tmin : price;
        }
        //如果数组中值全为0，则不画该线
        if (tmax == 0 && tmin == 0){
            return;
        }
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStrokeWidth(4.0f);
        canvas.drawLines(getLines(prices, xUnit, height, max, min, fromZero, y, xOffset), paint);
        paint.reset();
    }

    /**
     * 画阴影
     * @param canvas  画布
     * @param prices  价格
     * @param xUnit   X轴每两点距离
     * @param height  控件高度
     * @param color   颜色
     * @param max   价格最大值
     * @param min   价格最小值
     * @param fromZero   是否从0开始，意思是，假如Y轴为0是否画出该点（比如在SMA5， SMA10中，并不是0开始的）
     */
    public static void drawBackShade(Canvas canvas, float[] prices, float xUnit, float height, int color, float max, float min, boolean fromZero){
        drawBackShade(canvas, prices, xUnit, height, color, max, min, fromZero, 0, 0);
    }
    /**
     * 画阴影
     * @param canvas  画布
     * @param prices 价格
     * @param xUnit x轴每两点距离
     * @param height 控件高度
     * @param color  颜色
     * @param max   价格最大值
     * @param min   价格最小值
     * @param fromZero  是否从0开始，意思是，假如Y轴为0是否画出该点（比如在SMA5， SMA10中，并不是0开始的）
     * @param y   左上角Y轴，指标并不是从左上角开始画的
     * @param xOffset
     */
    public static void drawBackShade(Canvas canvas, float[] prices, float xUnit, float height, int color, float max, float min, boolean fromZero, float y, float xOffset){
        LinearGradient linearGradient=new LinearGradient(0, height, 0, 0, new int[]{Color.WHITE, color, color}, new float[]{0.0f,0.7f,1.0f}, Shader.TileMode.CLAMP);
//        Shader linearGradient = new LinearGradient(0, 0, 0, height, new int[]{Color.parseColor("#aa3483e9"), Color.parseColor("#303483e9")}, null, Shader.TileMode.CLAMP);
        if (canvas == null){
            Log.w(TAG, "drawLines: ==== Canvas为空");
            return;
        }
        float tmax = 0f;
        float tmin = 0f;
        for (float price : prices){
            tmax = tmax > price ? tmax : price;
            tmin = tmin < price ? tmin : price;
        }
        //如果数组中值全为0，则不画该线
        if (tmax == 0 && tmin == 0){
            return;
        }
        Paint backPaint = new Paint();
        backPaint.setAntiAlias(true);
        backPaint.setStyle(Paint.Style.FILL);
//        backPaint.setColor(Color.BLUE);
        backPaint.setAlpha(100);
        backPaint.setShader(linearGradient);

        float[] points = getLines(prices, xUnit, height, max, min, fromZero, y, xOffset);
        Path path = new Path();
        path.moveTo(0, height);
        for (int i = 0; i < prices.length - 1; i++){
            path.lineTo(points[i * 4 + 0], points[i * 4 + 1]);
            if ((prices.length - 2) == i){
                path.lineTo(points[i * 4 + 2], points[i * 4 + 3]);
            }
        }
        path.lineTo(points[(prices.length - 2) * 4 + 2], height);
        canvas.drawPath(path, backPaint);
    }


    /**
     * 传入价格和
     * @param prices 价格
     * @param xUnit  X轴每两点之间距离
     * @param height  控件高度
     * @param max 价格最大值
     * @param min 价格最小值
     * @param fromZero  是否从0开始，意思是，假如y轴为0是否画出该点（比如在SMA5、SMA10中，并不是由0开始）
     * @return
     */
    public static float[] getLines(float[] prices, float xUnit, float height, float max, float min, boolean fromZero, float y, float xOffset){
        float[] result = new float[prices.length * 4];
        //每个点大小
        float yUnit = (max - min) / height;
        for (int i = 0; i < prices.length - 1; i++){
            //排除起点为0的点
            if (!fromZero && prices[i] == 0){
                continue;
            }
            result[i * 4 + 0] = xOffset + i * xUnit;
            result[i * 4 + 1] = y + height - (prices[i] - min) / yUnit ;
            result[i * 4 + 2] = xOffset + (i + 1) * xUnit;
            result[i * 4 + 3] = y + height - (prices[i + 1] - min) / yUnit;

//            Log.e(TAG, "getLines: =="+xUnit+"===" + result[i * 4 + 0] +"====="+ result[i * 4 + 1] +"======"+result[i * 4 + 2] +"========"+result[i * 4 + 3] );
        }
        return result;
    }

    /**
     * VOL线  指标1
     * @param canvas  画布
     * @param xUint X轴单位距离
     * @param y  左上Y坐标
     * @param height 绘画区域高度
     * @param max  最大量
     * @param yd   昨收
     * @param minutes   量集合
     */
    public static void drawVOLRects(Canvas canvas, float xUint, float y, float height, long max, float yd, ArrayList<CMinute> minutes){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float yUnit = height / max;
        paint.setTextSize(TICK_TEXT_SIZE);
        paint.setColor(Color.BLACK);
        for (int i = 0; i < minutes.size(); i++){
            if (minutes.get(i).getCount() != 0){
                int color = 0;
                if (i == 0){
                    color = ColorUtil.getTextColorAsh(minutes.get(i).getPrice() > yd ? 1 : -1, 0);
                } else {
                    color = ColorUtil.getTextColorAsh(minutes.get(i).getPrice() > minutes.get(i - 1).getPrice() ? 1 : -1, 0);
                }
                paint.setColor(color);
                paint.setStrokeWidth(3f);
                canvas.drawLine(xUint * i, y + (height - minutes.get(i).getCount() * yUnit), xUint * i, y + height, paint);
            }
        }
        canvas.drawText(max / 2 + "", 0, y + height / 2, paint);
    }

    /**
     * 指标1 VOL
     * @param canvas  画布
     * @param xUint X轴单位距离
     * @param y  左上y坐标
     * @param height   绘画区域高度
     * @param max   最大量
     * @param models   量集合
     */
    public static void drawVOLRects(Canvas canvas, float xUint, float y, float height, long max, ArrayList<StickModel> models){
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float yUnit = height / max;
        paint.setTextSize(TICK_TEXT_SIZE);
        paint.setColor(Color.BLACK);
        float diff = xUint - xUint / KLineChartView.WIDTH_SCALE;
        canvas.drawText(max / 2 + "", 0, y + height / 2, paint);
        for (int i = 0; i < models.size(); i++) {
            if (models.get(i).getCount() != 0) {
                StickModel model = models.get(i);
                int color = ColorUtil.getTextColorAsh(model.getClose() > model.getOpen() ? 1 : -1, 0);
                paint.setColor(color);
                canvas.drawRect(xUint * i, y + (height - model.getCount() * yUnit), xUint * (i + 1) - diff, y + height, paint);
            }
        }
    }

    /**
     * 画烛形图
     * @param canvas 画布
     * @param maxY 最高 传入计算完成的值（对应X，Y轴） max/yUnit
     * @param minY 最低 传入计算完成的值（对应x,y轴）
     * @param openY 开盘 传入计算完成的值（对应x,y轴）
     * @param closeY  收盘 传入计算完成的值（对应x,y轴）
     * @param x  x轴 烛形图左上x坐标
     * @param y   y轴坐标 烛形图左上y坐标
     * @param drawCount  显示数量
     * @param width  屏幕宽度
     */
    public static void drawCandle(Canvas canvas, float maxY, float minY, float openY, float closeY, float x, float y, float drawCount, float width){
        if (canvas == null){
            Log.w(TAG, "drawCandle:  canvas is null");
            return;
        }
        //当在坐标系之外，不画
        if (x < 0 || y < 0){
            return;
        }
        float xUnit = width / drawCount;
        float diff = xUnit - xUnit / KLineChartView.WIDTH_SCALE;
        //是否上涨，由于计算成了Y轴坐标，所有上面的小，下面的大
        boolean isRise = closeY <= openY;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ColorUtil.getTextColorAsh(isRise ? 1 : -1, 0));
        paint.setStrokeWidth(2.0f);
        canvas.drawLine(x + xUnit / 2, y, x + xUnit / 2, y + (minY - maxY) + 1, paint);
        canvas.drawRect(x+ diff / 2, y + ((!isRise ? closeY : openY) -maxY), x + xUnit - diff / 2, y + ((!isRise ? openY : closeY) - maxY) + 1, paint);
        paint.reset();
    }

    /**
     * 画线，并且线整体在X轴有偏移
     * 使用：画K线均线时，X轴需要加一点
     * @param canvas 画布
     * @param prices 价格
     * @param xUnit X轴间隔
     * @param height 控件高度
     * @param color 颜色
     * @param max 最大值
     * @param min 最小值
     * @param xOffset
     */
    public static void drawLineWithXOffset(Canvas canvas, float[] prices, float xUnit, float height, int color, float max, float min, float xOffset){
        drawLines(canvas, prices, xUnit, height, color, max, min, false, 0, xOffset);
    }

    /**
     * K线 ：下面的时间
     * @param canvas
     * @param s1
     * @param s2
     * @param width
     * @param height
     */
    public static void drawKLineXTime(Canvas canvas, String s1, String s2, float width, float height){
        //上面偏移一点
        height = height + 25;
        Paint paint = new Paint();
        paint.setTextSize(TICK_TIME_SIZE);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(s1, 0, height, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        if (s2 != null){
            canvas.drawText(s2, width, height, paint);
        }
    }

    /**
     * K线：Y轴价格
     * @param canvas
     * @param max
     * @param min
     * @param height
     */
    public static void drawKLineYPrice(Canvas canvas, double max, double min, float height){
        double diff = max - min;
        String p1 = NumberUtil.getMoneyString(max);
        String p2 = NumberUtil.getMoneyString(min + diff * 3 / 4);
        String p3 = NumberUtil.getMoneyString(min + diff * 2 / 4);
        String p4 = NumberUtil.getMoneyString(min + diff * 1 / 4);
        String p5 = NumberUtil.getMoneyString(min);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(ColorUtil.getTextColorAsh(1, 0));
        paint.setTextSize(TICK_TEXT_SIZE);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(p1, 0, TICK_TEXT_OFFSET, paint);
        canvas.drawText(p2, 0, height * 1 / 4, paint);
        canvas.drawText(p3, 0, height * 2 / 4, paint);
        canvas.drawText(p4, 0, height * 3 / 4, paint);
        canvas.drawText(p5, 0, height, paint);
    }

    public static void drawIndexMiddleText(Canvas canvas, String text, float y) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setTextSize(TICK_TEXT_SIZE);
        canvas.drawText(text, 0, y, p);
    }

    /**
     * 指标MACD中的MACD值
     *
     * @param canvas
     * @param macds
     * @param max
     * @param min
     * @param height
     * @param y
     * @param xUint
     */
    public static void drawMACDRects(Canvas canvas, float[] macds, float max, float min, float height, float y, float xUint) {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        float yUnit = height / (max - min);
        float halfY = y + height / 2f;
        float diff = xUint - xUint / KLineChartView.WIDTH_SCALE;
        int i = 0;
        for (float f : macds) {
            p.setColor(f > 0 ? ColorUtil.COLOR_RED : ColorUtil.COLOR_GREEN);
            if (f != 0)
                canvas.drawRect(xUint * i + diff / 2, halfY, xUint * (i + 1) - diff / 2,halfY - f * yUnit - 1, p);
            i++;
        }
    }

    public static void drawPriceShader(Canvas canvas, float[] prices, float xUnit, float height, float max, float min) {
        float[] points = getLines(prices, xUnit, height, max, min, false, 0, 0);
        if(points == null || points.length == 0) return;
        Shader mShasder = new LinearGradient(0, 0, 0, height, new int[]{Color.parseColor("#aa3483e9"), Color.parseColor("#303483e9")}, null, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        //去锯齿
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(mShasder);
        for(int i = 1; i < points.length / 4; i++) {
            float x1 = points[i * 4];
            float y1 = points[i * 4 + 1];
            float x2 = points[i * 4 + 2];
            float y2 = points[i * 4 + 3];
            Path path = new Path();
            path.moveTo(x1 , height);
            path.lineTo(x1, y1);
            path.lineTo(x2, y2);
            path.lineTo(x2, height);
            path.close();
            canvas.drawPath(path,paint);
        }

    }

    public static float getY(float mainH, float input, float yMin, float yUnit) {
        return mainH - ( input - yMin) / yUnit;
    }

}
