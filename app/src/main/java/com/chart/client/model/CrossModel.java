package com.chart.client.model;

import com.chart.client.utils.DateUtil;

/**
 * Created by Hongmingwei on 2017/10/10.
 * Email: 648600445@qq.com
 */

public class CrossModel {
    //价格X轴
    public float x;
    //价格Y轴
    public float y;
    //均线Y轴
    public float y2;
    //价格
    public String price;
    //时间
    public long time;
    //年月日时间（有则先取）
    public String timeYMD;
    //十字线显示时，指标左上的文字数组
    public String[] indexText;
    //对应indexText的颜色
    public int[] indexColor;

    public CrossModel(float x, float y){
        this.x = x;
        this.y = y;
    }

    public String getTime(){
        if (timeYMD == null){
            return DateUtil.getShortDateJustHour(time);
        }
        return timeYMD;
    }
}
