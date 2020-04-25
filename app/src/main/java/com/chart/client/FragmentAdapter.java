package com.chart.client;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Hongmingwei on 2017/10/16.
 * Email: 648600445@qq.com
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private String[] titles;
    private ArrayList<Fragment> fragmentList;

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragmentList, String[] titles) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titles = titles;
    }


    public void setFragmentList(ArrayList<Fragment> fragmentList){
        this.fragmentList = fragmentList;
    }

    public ArrayList<Fragment> getFragmentList() {
        return fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(titles != null && fragmentList != null && titles.length == fragmentList.size()) {
            return titles[position];
        }
        return super.getPageTitle(position);
    }
}
