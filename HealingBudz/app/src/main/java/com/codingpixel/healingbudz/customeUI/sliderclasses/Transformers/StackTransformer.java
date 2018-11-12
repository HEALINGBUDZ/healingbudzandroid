package com.codingpixel.healingbudz.customeUI.sliderclasses.Transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

//import com.codingpixel.healingbudz.customeUI.sliderclasses.Transformers.*;

public class StackTransformer extends com.codingpixel.healingbudz.customeUI.sliderclasses.Transformers.BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		ViewHelper.setTranslationX(view,position < 0 ? 0f : -view.getWidth() * position);
	}

}
