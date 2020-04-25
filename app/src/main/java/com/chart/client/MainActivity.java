package com.chart.client;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chart.client.fragment.KLineFragment;
import com.chart.client.fragment.TickFragment;
import com.chart.client.ui.NoScrollViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String[] TITLES = {"分时线", "日K"};
    private TabLayout tabLayout;
    private NoScrollViewPager viewPager;

    private TextView chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }


    private void initView(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new TickFragment());
        fragments.add(new KLineFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, TITLES);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        chart = (TextView) findViewById(R.id.chart);

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChartActivity.class));
            }
        });
    }
}



// 两日
///api/hq/hismtdata2.do?sid=JG.263
//分时
//GET https://api.baidao.com/api/hq/mtdata3.do?sid=JG.263&updatetime=20170925%2015:25
/**

 Host: api.baidao.com
 Connection: Keep-Alive
 Accept-Encoding: gzip
 User-Agent: okhttp/3.4.1


 /api/hq/mkdata.do?limit=300&sid=JG.263&quotationType=5
 GET /api/hq/mkdata.do?limit=300&sid=JG.263&quotationType=60 HTTP/1.1
 GET /api/hq/mtdata3.do?sid=JG.263 HTTP/1.1
 GET /api/hq/hismtdata2.do?sid=JG.263 HTTP/1.1

 ET /api/hq/mkdata.do?limit=300&sid=JG.263&quotationType=240 HTTP/1.1
 GET /api/hq/dkdata.do?sid=JG.263&quotationType=7 HTTP/1.1
 GET /api/hq/dkdata.do?sid=JG.263&quotationType=30 HTTP/1.1

 */
//https://api.baidao.com/api/hq/mkdata.do?limit=300&sid=JG.263&quotationType=5