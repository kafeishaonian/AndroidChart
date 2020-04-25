package com.chart.client.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Hongmingwei on 2018/3/28.
 * Email: 648600445@qq.com
 */

public class TimeUtils {

    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    public static String getTimeMD(long timeInMillis) {
        SimpleDateFormat defaultFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
        return getTime(timeInMillis, defaultFormat);
    }

    public static String getTimeMD(String timeMillis){
        return timeMillis.substring(timeMillis.indexOf(" "), timeMillis.length());
    }
}
