package com.codingpixel.healingbudz.Utilities;
/*
 * Created by M_Muzammil Sharif on 07-Mar-18.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Flags {
    public static final int ACTIVITIES_COMMUNICATION_FLAG = 5319;
    public static final int ACTIVITY_COMMUNICATION_FOR_UPDATE_FLAG = 5318;
    public static final int ACTIVITY_COMMUNICATION_FOR_UPDATE_RESULT = 5320;
    public static final int ACTIVITY_LAUNCH_NEW_ONE = 5317;
    public static final int NOTIFY = 555;
    public static final int NOTIFY_NEW_ELEMIENT = 556;
    public static final int NOTIFY_DELETE = 558;

    public static final int SESSION_OUT = 401;
    public static final int LOG_OUT = 402;

    public static final int STORAGE_PERMISSION_REQUEST = 8000;
    public static final int STORAGE_PERMISSION_RESULT = 8001;

    public static final int CAMERA_PERMISSION_REQUEST = 7000;
    public static final int CAMERA_PERMISSION_RESULT = 7001;

    public static final int PICTURE_CAPTUREING_REQUEST = 7005;
    public static final int PICTURE_CAPTUREING_RESULT = 7006;

    public static final int VIDEO_CAPTUREING_REQUEST = 7010;
    public static final int VIDEO_CAPTUREING_RESULT = 7011;

    public static boolean openMap(Context context, String address) {
        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme("geo")
                .path("0,0")
                .appendQueryParameter("q", address);
        Intent intent = new Intent(Intent.ACTION_VIEW, uriBuilder.build());
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }
}
