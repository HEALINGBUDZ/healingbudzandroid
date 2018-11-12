package com.codingpixel.healingbudz.Utilities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/*
 * Created by M_Muzammil Sharif on 10-May-17.
 */

public class PermissionHandler {
    public static void checkPermission(@NonNull String[] permissions, @NonNull Context context, @NonNull CheckPermissionResponse response) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            response.permissionGranted();
            return;
        }
        if (checkIsPermissionsGranted((Activity) context, permissions, 0)) {
            response.permissionGranted();
        } else {
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    response.showNeededPermissionDialog();
                    return;
                }
            }
            response.requestPermission();
        }
    }

    public interface CheckPermissionResponse {
        public void permissionGranted();

        public void showNeededPermissionDialog();

        public void requestPermission();
    }

    public interface PermissionResultResponse {
        public void permissionDenied();

        public void permissionGranted();
    }

    private static boolean checkIsPermissionsGranted(Activity activity, String[] permissions, int pos) {
        int permission = ContextCompat.checkSelfPermission(activity,
                permissions[pos]);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            return permissions.length <= (pos + 1) || (checkIsPermissionsGranted(activity, permissions, (pos + 1)));
        }
        // Should we show an explanation?
        return false;
    }

    public static void onPermissionsResult(@NonNull String[] permissions, @NonNull Context context, @NonNull PermissionResultResponse response) {
        if (checkIsPermissionsGranted(((Activity) context), permissions, 0)) {
            response.permissionGranted();
        } else {
            response.permissionDenied();
        }
    }
}
