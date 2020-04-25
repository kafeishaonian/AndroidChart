package com.chart.client.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * 使用k线和分时图页面，
 * 当十字线出现的时候，则不可以滑动，其他地方使用无效
 * Created by Hongmingwei on 2017/10/16.
 * Email: 648600445@qq.com
 */

public class NoScrollViewPager extends ViewPager {


    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        return true;
    }
}
