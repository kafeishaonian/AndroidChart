package com.chart.client.utils;

import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;

import com.chart.client.model.CMinute;
import com.chart.client.model.TickDataResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * 分时线，K线图标工具类
 * Created by Hongmingwei on 2017/10/12.
 * Email: 648600445@qq.com
 */

public class LineUtil {

    private static final String TAG = LineUtil.class.getSimpleName();

    /**
     * 获取该画笔写出文字的宽度
     * @param paint 画笔
     * @param content 内容
     * @return 宽度
     */
    public static float getTextWidth(Paint paint, String content){
        return paint.measureText(content);
    }

    /**
     * 获取该画笔写出文字的高度
     * @param paint
     * @return
     */
    public static float getTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     *  是否白盘和夜盘一起显示
     * @param duration
     * @return
     */
    public static boolean hasNight(String duration){
        if (duration.contains("|")){
            return duration.split("\\|").length == 3;
        }
        return false;
    }

    /**
     * 获取分时线下的时间，需要展示的点数目
     * @param duration
     * @return
     */
    public static int getShowCount(String duration){
        Log.e(TAG, "getShowCount: ===========" + duration );
        ArrayList<Integer> mins = getTimesMin(duration);
        switch (mins.size()){
            case 2:
                return mins.get(1) - mins.get(0);
            case 4:
                return (mins.get(3) - mins.get(2)) + (mins.get(1) - mins.get(0));
            case 6:
                return (mins.get(5) - mins.get(4)) + (mins.get(3) - mins.get(2)) + (mins.get(1) - mins.get(0));
        }
        return 242;
    }

    /**
     * 获取开盘收盘时间对应的分钟数
     * @param duration
     * @return
     */
    public static ArrayList<Integer> getTimesMin(String duration){
        ArrayList<String> times = getTimes(duration);
        ArrayList<Integer> mins = new ArrayList<>();
        for (String s : times){
            int min = getMin(s);
            mins.add(min);
        }
        return mins;
    }


    /**
     * 解析开收盘时间点
     * @param duration 9:00-11:30|13:00-15:00（可能谁有1,2,3段时间）
     * @return {"9:30", "11:00", "13:00", "15:00"}
     */
    public static ArrayList<String> getTimes(String duration){
        ArrayList<String> result = new ArrayList<>();
        if (TextUtils.isEmpty(duration)){
            return result;
        }
        if (duration.contains("|")){
            String[] t1 = duration.split("\\|");
            for (String s : t1){
                Log.e(TAG, "getTimes: ====" + s.split("-")[0]+"========" + s.split("-")[1] );
                result.add(s.split("-")[0]);
                result.add(s.split("-")[1]);
            }
        } else {
            result.add(duration.split("-")[0]);
            result.add(duration.split("-")[1]);
        }
        return result;
    }

    /**
     * 通过long时间获取分钟数
     * @param time
     * @return
     */
    public static int getMin(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time * 1000);
        Log.e(TAG, "getMin: ====" + calendar.get(Calendar.HOUR_OF_DAY) + "======" + calendar.get(Calendar.MINUTE));
        return getMin(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
    }

    /**
     * 传入时间和分钟如 15:00， 解析成分钟数
     * @param minStr
     * @return
     */
    public static int getMin(String minStr) {
        return Integer.parseInt(minStr.split(":")[0]) * 60 + Integer.parseInt(minStr.split(":")[1]);
    }

    /**
     * 判断网格是否不规则的网格：即早中晚开盘时间是否一致，不一致的话网格需要重新画
     * @param duration
     * @return
     */
    public static boolean isIrregular(String duration){
        ArrayList<Integer> timeMins = getTimesMin(duration);
        switch (timeMins.size()){
            case 2:
                return false;
            case 4:
                return timeMins.get(3) - timeMins.get(2) != timeMins.get(1) - timeMins.get(0);
            case 6:
                return (timeMins.get(3) - timeMins.get(2) != timeMins.get(1) - timeMins.get(0)) || (timeMins.get(3) - timeMins.get(2) != timeMins.get(5) - timeMins.get(4));
        }
        return false;
    }

    /**
     * 根据开盘时间，计算网格竖线的X轴坐标
     * @param duration
     * @param width
     * @return
     */
    public static float[] getXByDuration(String duration, float width){
        if (TextUtils.isEmpty(duration)){
            return new float[]{};
        }
        int showCount =  getShowCount(duration);
        float xUnit = width / (float) showCount;
        ArrayList<Integer> timeMins = getTimesMin(duration);
        if (timeMins.size() == 6){
            //如果白盘夜盘一起的话，需要画5条竖线（需实虚实虚）
            float[] result = new float[5];
            //实线1
            result[1] = (timeMins.get(1) - timeMins.get(0)) * xUnit;
            //实线2
            result[3] = (timeMins.get(3) - timeMins.get(0) - (timeMins.get(2) - timeMins.get(1))) * xUnit;
            //虚线1
            result[0] = result[1] / 2f;
            //虚线2
            result[2] = (result[3] - result[1]) / 2f + result[1];
            //虚线3
            result[4] = (width - result[3]) / 2f + result[3];
            return result;
        } else {
            //如果只有白盘的话，需要画3条竖线（虚实虚）
            float[] result = new float[3];
            //实线1
            result[1] = (timeMins.get(1) - timeMins.get(0)) * xUnit;
            //虚线1
            result[0] = result[1] / 2f;
            //虚线2
            result[2] = (width - result[1]) / 2f + result[1];
            return result;
        }
    }

    /**
     * 计算出Y轴显示价格的最大最小值
     * @param max  最大值
     * @param min  最小值
     * @param yd   昨收
     * @return
     */
    public static double[] getMaxAndMinByYd(double max, double min, double yd){
        double limit = Math.abs(max - yd) > Math.abs(yd - min) ? Math.abs(max - yd) : Math.abs(yd - min);
        double[] result = new double[2];
        result[0] = yd + limit;
        result[1] = yd - limit;
        return result;
    }


    /**
     * 补全分时图中缺失的点，如某个时间段没有数据，则该时间段都取前一条数据的值
     * @param response
     * @return
     */
    public static ArrayList<CMinute> getAllTickData(TickDataResponse response){
        ArrayList<CMinute> tickTempDatas = new ArrayList<>();
        if (response.getData() == null || response.getData().size() == 0){
            return tickTempDatas;
        }
        int nightStartMin = 0;
        int nightStopMin = 0;
        int morningStartMin = 0;
        int morningStopMin = 0;
        int afternoonStartMin = 0;
        int afternoonStopMin = 0;
        //停盘的时间  这里都切换成分钟
        String duration = response.getParam().getDuration();
        if (duration.contains("|")){
            String ds[] = duration.split("\\|");
            if (ds[0].contains("-")){
                String mins[] =ds[0].split("-");
                morningStartMin = getMin(mins[0]);
                morningStopMin = getMin(mins[1]);
            }
            if (ds[1].contains("-")){
                String mins[] = ds[1].split("-");
                afternoonStartMin = getMin(mins[0]);
                afternoonStopMin = getMin(mins[1]);
            }
            if (ds.length == 3){
                String mins[] = ds[2].split("-");
                nightStartMin = getMin(mins[0]);
                nightStopMin = getMin(mins[1]);
            }
        } else {
            if (duration.contains("-")){
                String mins[] = duration.split("-");
                morningStartMin = getMin(mins[0]);
                morningStopMin = getMin(mins[1]);
            }
        }
        int drawCount = 0;
        //是否有夜盘
        boolean hasNight = nightStartMin > 0;
        if (!hasNight){
            drawCount = afternoonStopMin - morningStartMin - (afternoonStartMin - morningStopMin);
        } else {
            drawCount = nightStopMin - morningStartMin - (nightStartMin - afternoonStopMin) - (afternoonStartMin - morningStopMin);
        }
        tickTempDatas.addAll(response.getData());
        int firstMin = getMin(tickTempDatas.get(0).getTime());
        while (firstMin < morningStartMin && tickTempDatas != null && tickTempDatas.size() > 1){
            tickTempDatas.remove(0);
            response.getData().remove(0);
            firstMin = getMin(tickTempDatas.get(0).getTime());
        }

        //服务器返回数据的第一个数据
        long firstLongTime = tickTempDatas.get(0).getTime();
        for (int i = 0; i < response.getData().size(); i++){
            if (i == 0){
                //先补全第一点到开盘时间中间的点
                int div = firstMin - morningStartMin - 1;
                if (div >= 1){
                    for (int j = 0; i <= div; j++){
                        int min = getMin(firstLongTime - (j + 1) * 60);
                        if (hasNight){
                            if ((min > morningStartMin && min < afternoonStartMin) || (min > afternoonStopMin && min < nightStartMin)){
                                continue;
                            }
                        } else {
                            if (min > morningStopMin && min < afternoonStartMin && afternoonStartMin != 0){
                                continue;
                            }
                        }
                        CMinute temp = new CMinute();
                        temp.setTime(firstLongTime - (j + 1) * 60);
                        temp.setPrice(response.getParam().getLast());
                        tickTempDatas.add(0, temp);
                    }
                }
            } else {
                CMinute currentObject = response.getData().get(i);
                CMinute beforeObject = response.getData().get(i - 1);
                int currentMin = getMin(currentObject.getTime());
                int beforeMin = getMin(beforeObject.getTime());
                //当前时间 比上一次的时间要大2分钟   正常数据的时候是1分钟
                int div = currentMin - beforeMin - 1;
                //没有休盘时间或者是在休盘时间外
                if (morningStopMin == 0 || currentMin <= morningStartMin || currentMin >= afternoonStartMin){
                    for (int j = 0; j < div; j++){
                        CMinute temp = (CMinute) beforeObject.clone();
                        Log.e(TAG, "getAllTickData: ]====" + temp.toString());
                        temp.setTime(beforeObject.getTime() + ((j + 1) * 60));
                        temp.setCount(0);
                        temp.setMoney(0);
                        int tempMin = beforeMin + (j + 1);
                        if (morningStopMin > 0){
                            if (!hasNight){
                                if (tempMin > morningStopMin && tempMin < afternoonStartMin){
                                    //正好在停盘时间内
                                } else {
                                    tickTempDatas.add(i + tickTempDatas.size() - response.getData().size() + 1, temp);
                                }
                            } else {
                                //有夜盘
                                if ((tempMin > morningStopMin && tempMin < afternoonStartMin) || (tempMin > afternoonStopMin && tempMin < nightStartMin)){
                                    //正好在停盘时间内
                                } else {
                                    tickTempDatas.add(i + tickTempDatas.size() - response.getData().size() + 1, temp);
                                }
                            }
                        } else {
                            //没有中间停盘时间
                            tickTempDatas.add(i + tickTempDatas.size() - response.getData().size() + 1, temp);
                        }
                    }
                }
            }
        }
        //until 画线最后到达的位置
        int until = getMin(response.getParam().getUntil());
        if (until <= (hasNight ? nightStopMin : afternoonStopMin) && tickTempDatas.size() < drawCount) {
            CMinute lasteObject = tickTempDatas.get(tickTempDatas.size() - 1);
            int lasteMin = getMin(lasteObject.getTime());
            int div = until - lasteMin - 1;
            if (div >= 1) {
                for (int j = 0; j <= div; j++) {
                    CMinute temp = new CMinute();
                    temp.setTime((lasteObject.getTime() + (j + 1) * 60));
                    temp.setPrice(lasteObject.getPrice());
                    temp.setAverage(lasteObject.getAverage());
                    if (morningStopMin > 0) {
                        //有停盘点
                        int tempMin = lasteMin + (j + 1);
                        if (hasNight) {
                            if ((tempMin > morningStopMin && tempMin < afternoonStartMin) || (tempMin > afternoonStopMin && tempMin < nightStartMin)) {
                                //正好在停盘时间内  不添加
                            } else {
                                tickTempDatas.add(tickTempDatas.size(), temp);
                            }
                        } else {
                            if (tempMin > morningStopMin && tempMin < afternoonStartMin) {
                                //正好在停盘时间内  不添加
                            } else {
                                tickTempDatas.add(tickTempDatas.size(), temp);
                            }
                        }

                    }

                }
            }
        }
        //20160801 解析后的时间有时错乱了，需要对时间进行排序
        //TODO 此处需要优化，应该从循环中发现哪里添加错误，而不是简单的进行排序，排序反而会消耗内存、时间，需要修改
        Collections.sort(tickTempDatas, new Comparator<CMinute>() {
            @Override
            public int compare(CMinute cMinute, CMinute t1) {
                Log.e(TAG, "compare: ===========" +  new Long(cMinute.getTime()) + "==========" +(new Long(t1.getTime())) );
                return new Long(cMinute.getTime()).compareTo(new Long(t1.getTime()));
            }
        });
        return tickTempDatas;

    }

    /**
     * 获取数组中最大最小值
     * @param list
     * @return
     */
    public static float[] getMaxAndMin(float[] list){
        if (list == null || list.length == 0){
            return new float[]{0, 0};
        }
        float max = 0;
        float min = 0;
        float[] temp = list.clone();
        Arrays.sort(temp);
        max = temp[temp.length - 1];
        min = temp[0];
        return new float[]{max, min};
    }

    /**
     * 获取数组中最大最小值
     * @param list1
     * @param list2
     * @return
     */
    public static float[] getMaxAndMin(float[] list1, float[] list2){
        float max = 0;
        float min = 0;
        float[] f1 = getMaxAndMin(list1);
        float[] f2 = getMaxAndMin(list2);
        max = f1[0] > f2[0] ? f1[0] : f2[0];
        min = f1[1] < f2[1] ? f1[1] : f2[1];
        return new float[]{max, min};
    }

    /**
     * 获取数组中最大最小值
     * @param list1
     * @return
     */
    public static float[] getMaxAndMin(float[] list1, float[] list2, float[] list3) {
        float max = 0, min = 0;
        float[] f1 = getMaxAndMin(list1);
        float[] f2 = getMaxAndMin(list2);
        float[] f3 = getMaxAndMin(list3);
        max = f1[0] > f2[0] ? f1[0] : f2[0];
        max = max > f3[0] ? max : f3[0];
        min = f1[1] < f2[1] ? f1[1] : f2[1];
        min = min < f3[1] ? min : f3[1];
        return new float[] {max, min};
    }

    /**
     * 获取数组中最大最小值
     * @param list1
     * @return
     */
    public static float[] getMaxAndMin(float[] list1, float[] list2, float[] list3, float[] list4) {
        float max = 0, min = 0;
        float[] f1 = getMaxAndMin(list1);
        float[] f2 = getMaxAndMin(list2);
        float[] f3 = getMaxAndMin(list3);
        float[] f4 = getMaxAndMin(list4);
        float[] f123 = getMaxAndMin(f1, f2, f3);
        max = f123[0] > f4[0] ? f123[0] : f4[0];
        min = f123[1] < f4[1] ? f123[1] : f4[1];
        return new float[] {max, min};
    }

}
