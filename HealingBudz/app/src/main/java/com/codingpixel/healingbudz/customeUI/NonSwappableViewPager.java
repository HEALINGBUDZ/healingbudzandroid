package com.codingpixel.healingbudz.customeUI;
/*
 * Created by M_Muzammil Sharif on 09-Mar-18.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwappableViewPager extends ViewPager {
    public NonSwappableViewPager(Context context) {
        super(context);
    }

    public NonSwappableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
