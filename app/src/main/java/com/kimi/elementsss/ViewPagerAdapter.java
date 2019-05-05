package com.kimi.elementsss;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

//    private int[] layouts;


    private int[] layouts = new int[]{
            R.layout.slide1,
            R.layout.slide2,
            R.layout.slide3};



    //constructor
    public ViewPagerAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {

        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

//        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);

        return view;
    }



    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        View view = (View) object;
        container.removeView(view);
    }



}
