package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Color;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by incubasyss on 15/01/2018.
 */

public class CustomEditText extends EditText {


    public CustomEditText(Context context, KeyImeChange keyImeChangeListener) {
        super(context);
        this.keyImeChangeListener = keyImeChangeListener;
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    private KeyImeChange keyImeChangeListener;

    public interface KeyImeChange {
        public void onKeyIme(int keyCode, KeyEvent event);
    }

    public CustomEditText(Context context) {
        super(context);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public void setKeyImeChangeListener(KeyImeChange listener) {
        keyImeChangeListener = listener;
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLinksClickable(true);
        setAutoLinkMask( Linkify.WEB_URLS);
        setLinkTextColor(Color.parseColor("#6d96ad"));
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            this.clearFocus();

            if (keyImeChangeListener != null) {
                keyImeChangeListener.onKeyIme(keyCode, event);
            }

            // User has pressed Back key. So hide the keyboard

            // TODO: Hide your view as you do it in your activity
        }
        return false;
    }
}
