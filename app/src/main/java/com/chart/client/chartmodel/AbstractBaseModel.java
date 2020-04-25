package com.chart.client.chartmodel;

/**
 * Created by Hongmingwei on 2018/2/1.
 * Email: 648600445@qq.com
 */

public abstract class AbstractBaseModel {
    private String resultCode;//返回码
    private String msg;//返回信息

    public AbstractBaseModel() {
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
