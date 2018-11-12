package com.codingpixel.healingbudz.customeUI.sliderclasses.Transformers;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

//import com.daimajia.slider.library.Transformers.*;

public class CubeInTransformer extends com.codingpixel.healingbudz.customeUI.sliderclasses.Transformers.BaseTransformer {

	@Override
	protected void onTransform(View view, float position) {
		// Rotate the fragment on the left or right edge
        ViewHelper.setPivotX(view,position > 0 ? 0 : view.getWidth());
        ViewHelper.setPivotY(view,0);
        ViewHelper.setRotation(view,-90f * position);
	}

	@Override
	public boolean isPagingEnabled() {
		return true;
	}

}
