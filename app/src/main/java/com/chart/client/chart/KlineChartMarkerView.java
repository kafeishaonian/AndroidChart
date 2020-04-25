package com.chart.client.chart;

import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

import com.chart.client.R;
import com.chart.client.chartmodel.ChartKlineMakerModel;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class KlineChartMarkerView extends MarkerView {
    private static final String TAG = KlineChartMarkerView.class.getSimpleName();

    /**
     * view
     */
    private TextView tvCurTime;
    private TextView tvOpen;
    private TextView tvClose;
    private TextView tvHigh;
    private TextView tvLow;
    private Context mContext;

    public KlineChartMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        mContext = context;

        tvCurTime = (TextView) findViewById(R.id.maker_time);
        tvOpen = (TextView) findViewById(R.id.maker_open);
        tvClose = (TextView) findViewById(R.id.maker_close);
        tvHigh = (TextView) findViewById(R.id.maker_high);
        tvLow = (TextView) findViewById(R.id.maker_low);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {
            CandleEntry ce = (CandleEntry) e;
            ChartKlineMakerModel model = (ChartKlineMakerModel) e.getData();
            //LogUtils.d(TAG, "tick chart =============  " + e.getVal());
            if (model != null) {
                tvCurTime.setText(TextUtils.isEmpty(model.getTime()) ? "-.--" : model.getTime());
                tvOpen.setText(TextUtils.isEmpty(model.getOpen()) ? "-.--" : model.getOpen());
                tvClose.setText(TextUtils.isEmpty(model.getClose()) ? "-.--" : model.getClose());
                tvHigh.setText(TextUtils.isEmpty(model.getHi()) ? "-.--" : model.getHi());
                tvLow.setText(TextUtils.isEmpty(model.getLow()) ? "-.--" : model.getLow());
            } else {
                tvCurTime.setText("-.--");
                tvOpen.setText("-.--");
                tvClose.setText("-.--");
                tvHigh.setText("-.--");
                tvLow.setText("-.--");
            }
        } else {
        }
    }

    @Override
    public MPPointF getOffsetForDrawingAtPoint(float posX, float posY) {
        // this will center the marker-view horizontally
        final int screenWidth = getScreenWidth(mContext);
        final int screenHeight = getScreenHeight(mContext);
        float x=0;
        float y=0;
        if (screenWidth > screenHeight) {//横屏
            if (posX > (screenWidth * 0.8)) {
                x= -(getWidth()) - 20;
            } else if (posX < (screenWidth * 0.2)) {
                x= 20;
            }
        } else {//竖屏
            if (posX > (screenWidth * 0.8)) {
                x= -(getWidth()) - 20;
            } else if (posX < (screenWidth * 0.2)) {
                x= 20;
            }
        }

        if (x==0){
            x=-(getWidth() / 2);
        }

        if (posY<getHeight()){
            y=-getHeight()/4;
        }else{
            y=-getHeight()*3/4;
        }
        return new MPPointF(x,y);
    }

    public static int getScreenHeight(Context context) {
        Display display = getDisplay(context);
        int screenHeight = display.getHeight();
        return screenHeight;
    }
    /**
     * 获取屏幕宽度大小，单位px
     *
     * @param context
     * @return
     */
    public int getScreenWidth(Context context) {
        Display display = getDisplay(context);
        int screenWidth = display.getWidth();
        return screenWidth;
    }

    public static Display getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

    //TODO    @Override
//    public int getXOffset(float xpos) {
//        // this will center the marker-view horizontally
//        final int screenWidth = DisplayUtils.getScreenWidth(mContext);
//        final int screenHeight = DisplayUtils.getScreenHeight(mContext);
//        if (screenWidth > screenHeight) {//横屏
//            if (xpos > (screenWidth * 0.8)) {
//                return -(getWidth()) - 20;
//            } else if (xpos < (screenWidth * 0.2)) {
//                return 20;
//            }
//        } else {//竖屏
//            if (xpos > (screenWidth * 0.5)) {
//                return -(getWidth()) - 20;
//            } else if (xpos < (screenWidth * 0.2)) {
//                return 20;
//            }
//        }
//
//        return -(getWidth() / 2);
//    }
//
//    @Override
//    public int getYOffset(float ypos) {
//        // this will cause the marker-view to be above the selected value
//        return -((getHeight()/4)*3);
//    }
}
