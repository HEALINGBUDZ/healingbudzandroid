package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.sliderclasses.SliderTypes.BaseSliderView;
//import com.daimajia.slider.library.SliderTypes.BaseSliderView;

/**
 * Created by incubasyss on 20/03/2018.
 */

public class SliderViewStrainDetail extends BaseSliderView {
    public SliderViewStrainDetail(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.pager_strain, null);
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView) v.findViewById(R.id.photo_powered_by);
        TextView date = (TextView) v.findViewById(R.id.photo_date);
        date.setText(getBundle().getString("date"));
        description.setTextColor(getBundle().getInt("color"));
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
