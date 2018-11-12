package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Typeface;
import android.text.util.Linkify;
import android.util.AttributeSet;


public class HealinBudEditTextBold extends CustomEditText {
    public HealinBudEditTextBold(Context context) {
        super(context);
        applyCustomFont(context);
        setAutoLinkMask( Linkify.WEB_URLS);
    }

    public HealinBudEditTextBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        setAutoLinkMask( Linkify.WEB_URLS);
    }

    public HealinBudEditTextBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
        setAutoLinkMask( Linkify.WEB_URLS);
    }

    public HealinBudEditTextBold(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
        setAutoLinkMask( Linkify.WEB_URLS);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Lato-Bold.ttf", context);
        setTypeface(customFont, Typeface.NORMAL);
    }
}
