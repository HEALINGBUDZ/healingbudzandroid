package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class HealingBubCheckBok extends CheckBox {

    public HealingBubCheckBok(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public HealingBubCheckBok(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        attrs.getStyleAttribute();
        applyCustomFont(context);
    }

    public HealingBubCheckBok(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    public HealingBubCheckBok(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Lato-Regular.ttf", context);
        setTypeface(customFont, Typeface.NORMAL);
//        int style = getTypeface().getStyle();
//        if (style == Typeface.NORMAL) {
//            Typeface customFont = FontCache.getTypeface("Lato-Light.ttf", context);
//            setTypeface(customFont, Typeface.NORMAL);
//        } else if (style == Typeface.BOLD) {
//            Typeface customFont_bold = FontCache.getTypeface("Lato-Light.ttf", context);
//            setTypeface(customFont_bold, Typeface.BOLD);
//        }
        }
    }
