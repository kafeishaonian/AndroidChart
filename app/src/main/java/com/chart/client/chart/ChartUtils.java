package com.chart.client.chart;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.utils.Utils;
/**
 * Created by Hongmingwei on 2018/3/21.
 * Email: 648600445@qq.com
 */
public class ChartUtils {

//    public static void initOffset(CombinedChart mCombinedChart, CombinedChart desChart) {
//        float lineLeft = mCombinedChart.getViewPortHandler().offsetLeft();
//        float barLeft = desChart.getViewPortHandler().offsetLeft();
//        float lineRight = mCombinedChart.getViewPortHandler().offsetRight();
//        float barRight = desChart.getViewPortHandler().offsetRight();
//        float offsetLeft, offsetRight;
//        if (barLeft < lineLeft) {
//            offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
//            desChart.setExtraLeftOffset(offsetLeft);
//        } else {
//            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
//            mCombinedChart.setExtraLeftOffset(offsetLeft);
//        }
//        if (barRight < lineRight) {
//            offsetRight = Utils.convertPixelsToDp(barRight);
//            mCombinedChart.setExtraRightOffset(offsetRight);
//        } else {
//            offsetRight = Utils.convertPixelsToDp(lineRight);
//            desChart.setExtraRightOffset(offsetRight);
//        }
//    }

    public static void initOffset(CombinedChart combinedchart, CombinedChart barChart){
        float lineLeft = combinedchart.getViewPortHandler().offsetLeft();
        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float lineRight = combinedchart.getViewPortHandler().offsetRight();
        float barRight = barChart.getViewPortHandler().offsetRight();
        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
        float transLeft = 0, transRight = 0;
        /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
           /* offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            barChart.setExtraLeftOffset(offsetLeft);*/
            transLeft = lineLeft;
        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            combinedchart.setExtraLeftOffset(offsetLeft);
            transLeft = barLeft;
        }
        /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
          /*  offsetRight = Utils.convertPixelsToDp(lineRight);
            barChart.setExtraRightOffset(offsetRight);*/
            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            combinedchart.setExtraRightOffset(offsetRight);
            transRight = barRight;
        }
        barChart.setViewPortOffsets(transLeft, 10, transRight, barBottom);
    }
}
