package com.codingpixel.healingbudz.Utilities;
/*
 * Created by M_Muzammil Sharif on 09-Mar-18.
 */

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import java.math.RoundingMode;
import java.nio.IntBuffer;
import java.text.DecimalFormat;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

public class Constants {
    public static final DecimalFormat D_FORMAT_ONE;
    public static final int UPGRADED = 55656;
    //    public static final String[] stateList = {"Alaska", "Arkansas"
//            , "Arizona"
//            , "West Virginia"
//            , "Washington, DC"
//            , "Washington"
//            , "Vermont"
//            , "Rhode Island"
//            , "Pennsylvania"
//            , "Oregon"
//            , "Ohio"
//            , "North Dakota"
//            , "New York"
//            , "New Mexico"
//            , "New Jersey"
//            , "New Hampshire"
//            , "Nevada"
//            , "Montana"
//            , "Minnesota"
//            , "Michigan"
//            , "Massachusetts"
//            , "Maryland"
//            , "Maine"
//            , "Illinois"
//            , "Hawaii", "Florida"
//            , "Delaware"
//            , "Connecticut"
//            , "Colorado", "California"
//    };
    public static final String[] stateList = {"Idaho",
            "Wyoming",
            "Utah",
            "South Dakota",
            "Nebraska",
            "Kansas",
            "Oklahoma",
            "Texas",
            "Missouri",
            "Iowa", "Wisconsin", "Indiana"
            , "Kentucky"
            , "Tennessee"
            , "Mississippi"
            , "Alabama", "Virginia", "Georgia"
            , "North Carolina"
            , "South Carolina"
    };
    public static final String[] stateListIni = {"ID", "WY", "UT", "SD", "NE", "KS", "OK", "TX", "MO", "IA", "WI", "IN",
            "KY", "TN", "MS", "AL", "VA", "GA", "NC", "SC"};

    //            {"AK",
//            "AZ",
//            "AR",
//            "CA",
//            "CO",
//            "CT",
//            "DE",
//            "FL",
//            "HI",
//            "IL",
//            "ME",
//            "MD",
//            "MA",
//            "MI",
//            "MN",
//            "MT",
//            "NV",
//            "NH",
//            "NJ",
//            "NM",
//            "NY",
//            "ND",
//            "OH",
//            "OR",
//            "PA",
//            "RI",
//            "VT",
//            "VA",
//            "WA",
//            "WV"};
    public static final String appKey = "4m9Nv1nbyLoaZAMyAhQri9BUXBxlD3yQxbAiHsn2hwQ=";
    public static final String POST_EXTRA = "com.muzammil.post";
    public static final String POST_ID_EXTRA = "com.healing_post_id";
    public static final String POST_EXTRA_WITH_NEW = "com.muzammil.posts.with.new";
    public static final String POST_FILE_POS = "com.muzammil.post.file_pos";
    public static final String CAMERA_ONLY_VIDEO = "camera_Type_only_video";
    public static final String[] STORAGE_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static final String[] LOCATION_PERMISSIONS = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    public static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    public static final String[] POST_FILTERS = new String[]{"Newest", "Followers First", "Most Liked"};
    public static String add_init = "ca-app-pub-3940256099942544~3347511713";
    public static String stripeKey = "pk_test_oJX1vAPXtFBkeX6mZuOestxD";//Client Key Test
    //pk_test_oJX1vAPXtFBkeX6mZuOestxD
//    public static String stripeKey = "pk_test_kBD5CZDk3MBZNRLeWqrfvhew";//In House Key Test
    public static String stripeKeySecret = "sk_test_LxzI3dFzzcvYJ1kXNQhJjsVQ";//Client secret Key Test
    public static String stripeKeyLive = "Not Found Yet";//Client Key Live

    static {
        D_FORMAT_ONE = new DecimalFormat("0.0");
        D_FORMAT_ONE.setRoundingMode(RoundingMode.CEILING);
    }

    public static float distance(Double latRec, Double lngRec, Double latLoc, Double lngLoc) {
        double earthRadius = 3958.756;
        double latDiff = Math.toRadians(latLoc - latRec);
        double lngDiff = Math.toRadians(lngLoc - lngRec);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(latRec)) * Math.cos(Math.toRadians(latLoc)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;
        Double miless = 0.000621371;
        return Double.valueOf((distance * meterConversion) * miless).floatValue();
    }

    public static Bitmap takeScreenShotView(View view, Bitmap bb) {
//        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();//
        Rect frame = new Rect();

//        photoViewAttacher.get
//        return photoViewAttacher.getVisibleRectangleBitmap();
        view.getWindowVisibleDisplayFrame(frame);
//        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = (int) view.getWidth();
        int height = (int) view.getHeight();

        Bitmap b = Bitmap.createBitmap(bb, 0, 0, width, height);
        view.destroyDrawingCache();
        return b;
    }

    public static Bitmap SavePixels(int x, int y, int w, int h) {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
        int b[] = new int[w * (y + h)];
        int bt[] = new int[w * h];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);
        gl.glReadPixels(x, 0, w, y + h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);
        for (int i = 0, k = 0; i < h; i++, k++) {
            for (int j = 0; j < w; j++) {
                int pix = b[i * w + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0x00ff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(h - k - 1) * w + j] = pix1;
            }
        }

        Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
        return sb;
    }

}
