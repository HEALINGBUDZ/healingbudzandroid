package com.codingpixel.healingbudz.customeUI;
/*
 * Created by M_Muzammil Sharif on 09-Mar-18.
 */

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ZoomProblemViewPager extends ViewPager {
    public ZoomProblemViewPager(Context context) {
        super(context);
    }

    public ZoomProblemViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
