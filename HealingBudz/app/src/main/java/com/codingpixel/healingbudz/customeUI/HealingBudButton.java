package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by codingpixel on 11/08/2017.
 */

public class HealingBudButton extends Button {
    public HealingBudButton(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public HealingBudButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public HealingBudButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    public HealingBudButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Lato-Light.ttf", context);
        setTypeface(customFont, Typeface.NORMAL);
    }
}
