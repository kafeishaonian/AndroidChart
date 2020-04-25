package com.chart.client.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.View;

import com.chart.client.R;
import com.chart.client.ui.ChartConstant;
import com.chart.client.ui.ChartView;

/**
 * Created by Hongmingwei on 2017/10/16.
 * Email: 648600445@qq.com
 */

public abstract class LineBaseFragment extends Fragment implements ChartConstant, TabLayout.OnTabSelectedListener, ChartView.OnDoubleTapListener, View.OnClickListener{

    protected String cid;
    protected boolean isShow;
    //K线类型：取值为ChartConstant的TYPE_RIK等,日k月k周k等,默认为0分时图
    protected int type;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDoubleTap() {

    }
}
