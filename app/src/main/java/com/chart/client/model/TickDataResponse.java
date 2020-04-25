package com.chart.client.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Hongmingwei on 2017/10/12.
 * Email: 648600445@qq.com
 */

public class TickDataResponse implements Parcelable {

    private int success;
    private String error_code;
    private String msg;
    List<CMinute> data;
    private TickParam param;

    public TickDataResponse(){

    }

    protected TickDataResponse(Parcel in) {
        this.success = in.readInt();
        this.error_code = in.readString();
        this.msg = in.readString();
        this.data = in.createTypedArrayList(CMinute.CREATOR);
        this.param = in.readParcelable(TickParam.class.getClassLoader());
    }

    public static final Creator<TickDataResponse> CREATOR = new Creator<TickDataResponse>() {
        @Override
        public TickDataResponse createFromParcel(Parcel in) {
            return new TickDataResponse(in);
        }

        @Override
        public TickDataResponse[] newArray(int size) {
            return new TickDataResponse[size];
        }
    };



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.success);
        dest.writeString(this.error_code);
        dest.writeString(this.msg);
        dest.writeTypedList(this.data);
        dest.writeParcelable(this.param, flags);
    }


    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CMinute> getData() {
        return data;
    }

    public void setData(List<CMinute> data) {
        this.data = data;
    }

    public TickParam getParam() {
        return param;
    }

    public void setParam(TickParam param) {
        this.param = param;
    }
}
