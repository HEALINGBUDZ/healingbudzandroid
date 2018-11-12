package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Switch;


public class APPSwitch extends Switch {

    public APPSwitch(Context context) {
        super(context);
    }

    public APPSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public APPSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
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
            if (isChecked) {
                thumbColor = Color.parseColor("#ffffff");
                trackColor = Color.parseColor("#bcbcbc");
            } else {
                thumbColor = Color.parseColor("#ffffff");
                trackColor = Color.parseColor("#6e6e6e");
            }

            try {
                this.getThumbDrawable().setColorFilter(thumbColor, PorterDuff.Mode.MULTIPLY);
                this.getTrackDrawable().setColorFilter(trackColor, PorterDuff.Mode.DARKEN);
                this.getTrackDrawable().setAlpha(1);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}