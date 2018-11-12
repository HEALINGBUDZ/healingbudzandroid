package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.widget.TextView;

public class HealingBudTextViewItalic extends TextView {

    public HealingBudTextViewItalic(Context context) {
        super(context);
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public HealingBudTextViewItalic(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        attrs.getStyleAttribute();
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public HealingBudTextViewItalic(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public HealingBudTextViewItalic(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomFont(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("Lato-Italic.ttf", context);
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

