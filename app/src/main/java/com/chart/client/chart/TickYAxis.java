package com.chart.client.chart;

import com.github.mikephil.charting.components.YAxis;

/**
 * Created by Hongmingwei on 2018/4/3.
 * Email: 648600445@qq.com
 */

public class TickYAxis extends YAxis{

    protected boolean ismDrawDifferentUpDownColorToLable;
    protected int upTextColor;
    protected int downTextColor;

    public TickYAxis() {
        super();
        this.ismDrawDifferentUpDownColorToLable = false;
        this.upTextColor = -16777216;
        this.downTextColor = -16777216;

    }
    public TickYAxis(AxisDependency axis) {
        super(axis);
        this.ismDrawDifferentUpDownColorToLable = false;
        this.upTextColor = -16777216;
        this.downTextColor = -16777216;

    }

    public void setUpTextColor(int upTextColor) {
        this.upTextColor = upTextColor;
    }

    public int getUpTextColor() {
        return this.upTextColor;
    }

    public void setDownTextColor(int color) {
        this.downTextColor = color;
    }

    public int getDownTextColor() {
        return this.downTextColor;
    }

    public boolean drawDifferentUpDownColorToLableEnabled() {
        return this.ismDrawDifferentUpDownColorToLable;
    }

    public void setDrawDifferentUpDownColorToLableEnabled(boolean ismDrawDifferentUpDownColorToLa7ble) {
        this.ismDrawDifferentUpDownColorToLable = ismDrawDifferentUpDownColorToLa7ble;
    }


}
