package com.chart.client.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 每次绘图的时候 如果后面没有数据 直接画直线到这个时间就可以了
 * Created by Hongmingwei on 2017/10/12.
 * Email: 648600445@qq.com
 */

public class TickParam implements Parcelable {

    private double last;
    private String duration;
    private int length;
    private long until;

    public TickParam(){

    }

    protected TickParam(Parcel in) {
        this.last = in.readDouble();
        this.duration = in.readString();
        this.length = in.readInt();
        this.until = in.readLong();
    }

    public static final Creator<TickParam> CREATOR = new Creator<TickParam>() {
        @Override
        public TickParam createFromParcel(Parcel in) {
            return new TickParam(in);
        }

        @Override
        public TickParam[] newArray(int size) {
            return new TickParam[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.last);
        dest.writeString(this.duration);
        dest.writeInt(this.length);
        dest.writeLong(this.until);
    }

    public double getLast() {
        return last;
    }

    public void setLast(double last) {
        this.last = last;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getUntil() {
        return until;
    }

    public void setUntil(long until) {
        this.until = until;
    }
}
