package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.util.Linkify;
import android.util.AttributeSet;


public class HealinBudEditText extends CustomEditText {
    public HealinBudEditText(Context context) {
        super(context);
        applyCustomFont(context);
        setLinksClickable(true);
//        setImportantForAutofill();
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public HealinBudEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public HealinBudEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public HealinBudEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Lato-Light.ttf", context);
        setTypeface(customFont, Typeface.NORMAL);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
    }
}
