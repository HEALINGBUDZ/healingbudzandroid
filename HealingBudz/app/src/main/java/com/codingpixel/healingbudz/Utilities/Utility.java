package com.codingpixel.healingbudz.Utilities;
/*
 * Created by M_Muzammil Sharif on 07-Mar-18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.SparseArray;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Url.UrlValidator;
import com.codingpixel.healingbudz.interfaces.ApiStatusCallBack;

import org.jsoup.Jsoup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    public static final boolean DO_SHOW_NETWORK_NOT_AWAILIBLE_DIALOGS = true;

    public static void launchWebUrl(Context context, String url) {
        if (context == null) {
            return;
        }
        if (url == null || url.trim().isEmpty()) {
            return;
        }
        url.replace(" ", "");
        Uri webpage = Uri.parse(url);
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://" + url);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    public static List<String> extractURL(String s) {
        if (s.trim().isEmpty()) {
            return null;
        }
//        Pattern pattern = Pattern.compile("(http://|https://)?([a-z0-9][a-z0-9\\-]*\\.)+[/a-zA-Z0-9\\-?&$=!;`~@()_+^#%'{}.]*");
        Pattern pattern = Patterns.WEB_URL;


//        (^|[\\s.:;?\\-\\]<\\(])" +
//        "((https?://|www\\.|pic\\.)[-\\w;/?:@&=+$\\|\\_.!~*\\|'()\\[\\]%#,☺]+[\\w/#](\\(\\))?)" +
//                "(?=$|[\\s',\\|\\(\\).:;?\\-\\[\\]>\\)])
        Matcher m = pattern.matcher(s);
        List<String> matches = new ArrayList<>();
        while (m.find()) {
            String s1 = m.group();
            matches.add(s1);
//            if (UrlValidator.getInstance().isValid(s1)) {
//                matches.add(s1);
//            }
        }
        return matches;
    }

    public static void launchEmailApplication(Context context, String email) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, email);
        context.startActivity(intent);
    }
    public static void launchPhoneApplication(Context context, String phone) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }
    public static void shareString(Context context, String string) {
        if (context == null) {
            return;
        }
        ShareCompat.IntentBuilder.
                from((Activity) context).
                setText(string).
                setType("text/plain").
                setChooserTitle("Share With Using:").
                startChooser();
    }

    public static String html2text(String html) {
        return Jsoup.parse(html).text();
    }

    public static void launchActivity(Activity activity, Class<?> mClass, boolean shouldFinishParent, Bundle bundle) {
        Intent intent = new Intent(activity, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
        if (shouldFinishParent) {
            activity.finish();
        }
    }

    public static void launchActivityForResult(Activity activity, Class<?> mClass, int requestCode) {
        Intent intent = new Intent(activity, mClass);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchActivityForResult(Activity activity, Class<?> mClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchActivityForResultFromFragment(Fragment fragment, Activity activity, Class<?> mClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void launchActivity(Activity activity, Class<?> mClass, boolean shouldFinishParent) {
        Intent intent = new Intent(activity, mClass);
        activity.startActivity(intent);
        if (shouldFinishParent) {
            activity.finish();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void finishWithResult(Activity activity, Bundle bundle, int result) {
        Intent i = new Intent();
        if (bundle != null) {
            i.putExtras(bundle);
        }
        activity.setResult(result, i);
        activity.finish();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view = null;
    }

    public static String getDeviceToken(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static boolean emailValidate(String email) {
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void getApiStatusCallBack(String status, String errorMsg, @NonNull ApiStatusCallBack callBack) {
        if (status == null && errorMsg == null) {
            callBack.unKnownError();
            return;
        }
        if (status == null) {
            callBack.knownError(errorMsg);
            return;
        }
        if (status.trim().toLowerCase().equals("success")) {
            callBack.success();
            return;
        }
        if (errorMsg == null) {
            callBack.unKnownError();
            return;
        }
        if (errorMsg.trim().toLowerCase().equals("Session Expired".trim().toLowerCase())) {
            callBack.sessionExpire();
            return;
        }
        callBack.knownError(errorMsg);
    }

    public static String convertDuration(long duration) {
        long hour = ((duration / 1000) / 60 / 60);
        long minutes = ((duration / 60000) % 60);
        long seconds = ((duration / 1000) % 60);
        if (hour <= 0) {
            return (String.format("%d:%02d", minutes, seconds));
        } else {
            return (String.format("%d:%02d:%02d", hour, minutes, seconds));
        }
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int getDeviceWidth(@NonNull Context context) {
        SharedPreferences pref = context.getSharedPreferences("com.muzammil.Dimensions", Context.MODE_PRIVATE);
        if (pref.getInt("screen_width", 0) == 0) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            pref.edit().putInt("screen_width", size.x).apply();
        }
        return pref.getInt("screen_width", 0);
    }

    public static int getDeviceHeight(@NonNull Context context) {
        SharedPreferences pref = context.getSharedPreferences("com.muzammil.Dimensions", Context.MODE_PRIVATE);
        if (pref.getInt("screen_height", 0) == 0) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            pref.edit().putInt("screen_height", size.y).apply();
        }
        return pref.getInt("screen_height", 0);
    }

    /*public static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }*/

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static <T> ArrayList<T> spareArrayToList(@NonNull SparseArray<T> sparseArray) {
        if (sparseArray == null) return null;
        ArrayList<T> arrayList = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            arrayList.add(sparseArray.valueAt(i));
        }
        return arrayList;
    }

    public static String getBudType(int budPoints) {
        if (budPoints < 100) {
            return "Sprout";
        }
        if (budPoints < 200) {
            return "Seedling";
        }
        if (budPoints < 300) {
            return "Young Bud";
        }
        if (budPoints < 400) {
            return "Blooming Bud";
        }
        return "Best Bud";
    }

    public static String getBudColor(int budPoints) {
        if (budPoints < 100) {
            return "#dedede";
        }
        if (budPoints < 200) {
            return "#73ae44";
        }
        if (budPoints < 300) {
            return "#f3c330";
        }
        if (budPoints < 400) {
            return "#df910b";
        }
        return "#cb6acc";
    }

    @DrawableRes
    public static int getProfilePlaceHolder(int budPoints) {
        if (budPoints < 100) {
            return R.drawable.ic_profile_gray;
        }
        if (budPoints < 200) {
            return R.drawable.ic_user_profile_green;
        }
        if (budPoints < 300) {
            return R.drawable.ic_discuss_question_profile;
        }
        if (budPoints < 400) {
            return R.drawable.ic_discuss_question_profile;
        }
        return R.drawable.ic_user_profile;
    }

    @DrawableRes
    public static int getBudColorDrawable(int budPoints) {
        if (budPoints < 100) {
            return R.drawable.ic_user_profile_rating_99;
        }
        if (budPoints < 200) {
            return R.drawable.ic_user_profile_rating_199;
        }
        if (budPoints < 300) {
            return R.drawable.ic_user_profile_rating_299;
        }
        if (budPoints < 400) {
            return R.drawable.ic_user_profile_rating_399;
        }
        return R.drawable.ic_user_profile_rating_400_plus;
    }

    public static String getImageTimeStamp() {
        return android.text.format.DateFormat.format("yyyy_MM_dd-hhmmss", Calendar.getInstance().getTime()).toString();
    }
}

