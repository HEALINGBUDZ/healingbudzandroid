package com.codingpixel.healingbudz.Utilities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by macmini on 20/12/2017.
 */

public class LinksUtils {
    public static void GotoWebLink(String link, Activity activity) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        activity.startActivity(browserIntent);
    }

    public static void PlayStore(Activity activity, String link) {
        final String appPackageName = "com.facebook.katana&hl=en"; // getPackageName() from Context or Activity object
//        try {
//            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
//        } catch (android.content.ActivityNotFoundException anfe) {
//            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//        }
        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/")));
    }
}
