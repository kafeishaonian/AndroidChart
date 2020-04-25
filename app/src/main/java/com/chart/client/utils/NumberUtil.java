package com.chart.client.utils;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 格式数字  包括价格  成交额
 * Created by Hongmingwei on 2017/10/13.
 * Email: 648600445@qq.com
 */

public class NumberUtil {
    public static final int DATA_INT = 0;
    public static final int DATA_FLOAT = 1;
    public static final int DATA_DOUBLE = 2;
    public static final int DATA_DEFAULT = -1;
    public static final int DATA_BE_EQUAL_TO = 0;
    public static final int DATA_LESS_THAN = 1;
    public static final int DATA_GREATER_THAN = 2;

    /**
     * 根据价格，获取 万 亿， 万亿
     * @param d
     * @return
     */
    public static String getMoneyString(double d){
        DecimalFormat format = new DecimalFormat("0.00");
        format.setMaximumFractionDigits(2);
        double wan = 10000;
        double fwan = -10000;
        double yi = 100000000; //  一亿以下
        double fyi = -100000000; //  一亿以下
        double wyi = 1000000000000d; //  万亿以下
        double fwyi = -1000000000000d; //  万亿以下

        if (d < wan && d > fwan) {
            return format.format(d);
        } else if (d < yi && d > fyi) {
            return format.format(d / Math.pow(10, 4)) + "万";
        } else if (d < wyi && d > fwyi) {
            return format.format(d / Math.pow(10, 8)) + "亿";
        } else {
            return format.format(d / Math.pow(10, 12)) + "万亿";
        }
    }

    /**
     * 行情图涨跌幅比例
     * @param d
     * @return
     */
    public static String getPercentString(double d){
        DecimalFormat format = new DecimalFormat("0.00");
        format.setMaximumFractionDigits(2);
        return format.format(d * 100) + "%";
    }

    /**
     * 格式化数据，默认两位小数点
     * @param d
     * @return
     */
    public static String beautifulDouble(double d){
        return beautifulDouble(d, 2);
    }

    /**
     * 格式化数据
     * @param d
     * @param scale  小数点位数
     * @return
     */
    public static String beautifulDouble(double d, int scale){
        String str = String.valueOf(d);
        try {
            BigDecimal big = new BigDecimal(d);
            double res = big.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
            return String.format("%." + scale + "f", res);
        } catch (Exception e){
            e.printStackTrace();
        }
        return d + "";
    }

    /**
     * 格式化数据
     * @param d 数字的str
     * @param scale  小数点位数
     * @return
     */
    public static String beautifulDouble(String d, int scale){
        try {
            if (d == null || "null".equalsIgnoreCase(d)){
                d = "0";
            }
            d = d.replace("%", "");
            d = d.replace("--", "");
            Double mDouble = Double.parseDouble(d);
            return beautifulDouble(mDouble, scale);
        } catch (Exception e){
            e.printStackTrace();
        }
        return d;
    }


    public static String getCountString(double d) {

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        double wan = 10000;
        double fwan = -10000;
        double yi = 100000000; //  一亿以下
        double fyi = -100000000; //  一亿以下
        double wyi = 1000000000000d; //  万亿以下
        double fwyi = -1000000000000d; //  万亿以下

        if (d < wan && d > fwan) {
            return String.valueOf((int) d);
        } else if (d < yi && d > fyi) {
            return df.format(d / Math.pow(10, 4)) + "万";
        } else if (d < wyi && d > fwyi) {
            return df.format(d / Math.pow(10, 8)) + "亿";
        } else {
            return df.format(d / Math.pow(10, 12)) + "万亿";
        }

    }


    /**
     * 金额格式化
     *
     * @param moneyStr 金额
     * @param scale    小数点位数
     * @return
     */
    public static String formatNumberStr(String moneyStr, int scale) {
        if (TextUtils.isEmpty(moneyStr)) {
            return "";
        }
        double money = 0;
        try {
            money = Float.valueOf(moneyStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return beautifulDouble(money, scale);
    }

    /**
     * 成交量  成交额
     * 根据double  获取 万  亿  万亿
     *
     * @param d
     * @return
     */
    public static String formartBigNumber(double d) {
        //万以下 直接返回
        if (d < 10000)
            return beautifulDouble(d, 2);

        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);

        double wan = Math.pow(10, 8); //  一亿以下
        double yi = Math.pow(10, 12); //  万亿以下

        if (d < wan) {
            return df.format(d / Math.pow(10, 4)) + "万";
        } else if (d < yi) {
            return df.format(d / Math.pow(10, 8)) + "亿";
        } else {
            return df.format(d / Math.pow(10, 12)) + "万亿";
        }

    }



    public static String getPercentStringThree(double d) {
        DecimalFormat df = new DecimalFormat("0.000");
        df.setMaximumFractionDigits(3);
        return df.format(d * 100) + "%";
    }

    public static String getKeepTwoString(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setMaximumFractionDigits(2);
        return df.format(d);
    }

    public static String getAdd(String firstValue, String secondValue) {
        BigDecimal first = new BigDecimal(firstValue);
        BigDecimal second = new BigDecimal(secondValue);
        first = first.add(second);
        return first.toString();
    }


    public static double convertNumberString(String valueStr) {
        double number = 0;
        if (!TextUtils.isEmpty(valueStr)) {
            //匹配是否可转换
            if (valueStr.matches("-?(0|[1-9]\\d*)(\\.\\d+)?")) {
                number = Double.valueOf(valueStr);
            }
        }
        return number;
    }

    /**
     * 整型转化string封装
     *
     * @param value
     * @return
     */
    public static String integerToString(int value) {
        return String.valueOf(value);
    }

    /**
     * 格式化小数点
     *
     * @param targer
     * @return
     */
    public static double doubleDecimal(double targer) {
        try {
            int count = 2;

            if (targer < 10) {
                count = 4;
            } else if (targer < 100) {
                count = 3;
            }
            BigDecimal bigDecimal = new BigDecimal(targer);
            return bigDecimal.setScale(count, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            // TODO: handle exception
            return 0;
        }

    }

    /**
     * 比较大小
     */
    public static int compareBigAndSmall(String firstValue, String secondValue, int dataType) {
        switch (dataType) {
            case DATA_INT:
                if (Integer.parseInt(firstValue) < Integer.parseInt(secondValue)) {
                    return DATA_LESS_THAN;
                }
                if (Integer.parseInt(firstValue) < Integer.parseInt(secondValue)) {
                    return DATA_GREATER_THAN;
                }
                if (Integer.parseInt(firstValue) == Integer.parseInt(secondValue)) {
                    return DATA_BE_EQUAL_TO;
                }
                break;
            case DATA_FLOAT:
                if (Float.parseFloat(firstValue) < Float.parseFloat(secondValue)) {
                    return DATA_LESS_THAN;
                }
                if (Float.parseFloat(firstValue) < Float.parseFloat(secondValue)) {
                    return DATA_GREATER_THAN;
                }
                if (Float.parseFloat(firstValue) == Float.parseFloat(secondValue)) {
                    return DATA_BE_EQUAL_TO;
                }
                break;
            case DATA_DOUBLE:
                if (Double.parseDouble(firstValue) < Double.parseDouble(secondValue)) {
                    return DATA_LESS_THAN;
                }
                if (Double.parseDouble(firstValue) < Double.parseDouble(secondValue)) {
                    return DATA_GREATER_THAN;
                }
                if (Double.parseDouble(firstValue) == Double.parseDouble(secondValue)) {
                    return DATA_BE_EQUAL_TO;
                }
                break;
        }
        return DATA_DEFAULT;
    }


    public static int compareBigAndSmall(float firstValue, float secondValue) {
        if (firstValue < secondValue) {
            return DATA_LESS_THAN;
        }
        if (firstValue < secondValue) {
            return DATA_GREATER_THAN;
        }
        if (firstValue == secondValue) {
            return DATA_BE_EQUAL_TO;
        }
        return DATA_DEFAULT;
    }

    public static int compareBigAndSmall(int firstValue, int secondValue) {
        if (firstValue < secondValue) {
            return DATA_LESS_THAN;
        }
        if (firstValue < secondValue) {
            return DATA_GREATER_THAN;
        }
        if (firstValue == secondValue) {
            return DATA_BE_EQUAL_TO;
        }
        return DATA_DEFAULT;
    }

    public static int compareBigAndSmall(double firstValue, double secondValue) {
        if (firstValue < secondValue) {
            return DATA_LESS_THAN;
        }
        if (firstValue < secondValue) {
            return DATA_GREATER_THAN;
        }
        if (firstValue == secondValue) {
            return DATA_BE_EQUAL_TO;
        }
        return DATA_DEFAULT;
    }

}
