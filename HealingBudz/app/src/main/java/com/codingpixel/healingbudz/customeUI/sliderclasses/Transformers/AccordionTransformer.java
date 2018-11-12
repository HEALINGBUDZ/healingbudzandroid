package com.codingpixel.healingbudz.customeUI.sliderclasses.Transformers;

/**
 * Created by daimajia on 14-5-29.
 */

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

//import com.daimajia.slider.library.Transformers.*;

public class AccordionTransformer extends com.codingpixel.healingbudz.customeUI.sliderclasses.Transformers.BaseTransformer {

    @Override
    protected void onTransform(View view, float position) {
        ViewHelper.setPivotX(view,position < 0 ? 0 : view.getWidth());
        ViewHelper.setScaleX(view,position < 0 ? 1f + position : 1f - position);
    }

}