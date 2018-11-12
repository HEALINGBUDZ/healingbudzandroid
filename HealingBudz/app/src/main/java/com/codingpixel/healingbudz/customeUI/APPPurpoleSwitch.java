package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Switch;

public class APPPurpoleSwitch  extends Switch {

    public APPPurpoleSwitch(Context context) {
        super(context);
    }

    public APPPurpoleSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public APPPurpoleSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        changeColor(checked);
    }

    private void changeColor(boolean isChecked) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            int thumbColor;
            int trackColor;

            if(isChecked) {
                thumbColor = Color.parseColor("#ffffff");
                trackColor = Color.parseColor("#ebaae8");
            } else {
                thumbColor = Color.parseColor("#ffffff");
                trackColor = Color.parseColor("#bcbcbc");
            }

            try {
                getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
                getTrackDrawable().setColorFilter(trackColor, PorterDuff.Mode.DARKEN);
                getTrackDrawable().setAlpha(1);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}