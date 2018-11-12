package com.codingpixel.healingbudz.interfaces;
/*
 * Created by M_Muzammil Sharif on 26-Mar-18.
 */

import com.codingpixel.healingbudz.network.BudzFeedModel.MentionTagJsonModel;
import com.luseen.autolinklibrary.AutoLinkMode;

public interface AutoLinkTextLinkClickListener {

    public void onLinkClick(AutoLinkMode autoLinkMode, String matchedText, MentionTagJsonModel extraJson);

}