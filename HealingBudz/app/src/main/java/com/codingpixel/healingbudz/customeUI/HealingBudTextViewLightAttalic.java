package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.widget.TextView;

public class HealingBudTextViewLightAttalic extends TextView {

    public HealingBudTextViewLightAttalic(Context context) {
        super(context);
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public HealingBudTextViewLightAttalic(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        attrs.getStyleAttribute();
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public HealingBudTextViewLightAttalic(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public HealingBudTextViewLightAttalic(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Lato-LightItalic.ttf", context);
        setTypeface(customFont, Typeface.NORMAL);

    }
}
