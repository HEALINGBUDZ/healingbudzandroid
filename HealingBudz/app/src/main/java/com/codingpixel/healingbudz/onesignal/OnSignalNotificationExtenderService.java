package com.codingpixel.healingbudz.onesignal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.RemoteViews;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.activity.home.HomeActivity;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;
import com.onesignal.shortcutbadger.ShortcutBadger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.getSavedUser;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.network.model.URL.refresh_user_data;

public class OnSignalNotificationExtenderService extends NotificationExtenderService implements APIResponseListner {
    RemoteViews contentViewBig, contentViewSmall;

    @Override
    protected boolean onNotificationProcessing(final OSNotificationReceivedResult receivedResult) {
//        Log.d("OneSignalExample", "Notification displayed with id: " + receivedResult.payload.additionalData);
//        Log.d("OneSignalExample", SharedPrefrences.getBool("isAppOpen", getApplicationContext()) + "    =======>>>");
//        boolean isAppOpeenn = SharedPrefrences.getBool("isAppOpen", getApplicationContext());
//        boolean foregroud = false;
//        try {
//            foregroud = new ForegroundCheckTask().execute(getBaseContext()).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        final JSONObject jsonObject = receivedResult.payload.additionalData;
        String activityToBeOpened = "";
        user = getSavedUser(HealingBudApplication.getContext());
        callBadgeCount();
        EventBus.getDefault().post(new MessageEvent(false));
        if (receivedResult.isAppInFocus) {
            if (jsonObject != null) {
                activityToBeOpened = jsonObject.optString("activityToBeOpened", null);
                if (activityToBeOpened != null && activityToBeOpened.equals("group_invitation")) {
                    ShowCustomeNotification(receivedResult.payload.title, receivedResult.payload.body, jsonObject.toString());
                }
            }
            return true;
        } else {
            OverrideSettings overrideSettings = new OverrideSettings();
            overrideSettings.extender = new NotificationCompat.Extender() {
                @Override
                public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                    // Sets the background notification color to Red on Android 5.0+ devices.
                    Bitmap icon = BitmapFactory.decodeResource(HealingBudApplication.getContext().getResources(),
                            R.mipmap.ic_app_logo);
//                builder.setLargeIcon(icon);
                    String activityToBeOpened = jsonObject.optString("activityToBeOpened", null);
                    if (activityToBeOpened != null && activityToBeOpened.equalsIgnoreCase("admin")) {
                        builder.setSmallIcon(R.mipmap.admin);
                        contentViewSmall = new RemoteViews(getPackageName(), R.layout.custom_notification_admin);
                    } else {
                        contentViewSmall = new RemoteViews(getPackageName(), R.layout.custom_notification);
                        builder.setSmallIcon(R.mipmap.ic_app_logo);
                    }
//                    contentViewBig = new RemoteViews(getPackageName(), R.layout.custom_notification);
//                    contentViewSmall = new RemoteViews(getPackageName(), R.layout.custom_notification_small);
                    if (receivedResult.payload.title != null && !receivedResult.payload.title.isEmpty()) {
//                        contentViewBig.setTextViewText(R.id.title, receivedResult.payload.title);
                        contentViewSmall.setTextViewText(R.id.title, receivedResult.payload.title);
                    }

                    if (receivedResult.payload.body != null && !receivedResult.payload.body.isEmpty()) {
//                        contentViewBig.setTextViewText(R.id.text, receivedResult.payload.body);
                        contentViewSmall.setTextViewText(R.id.text, receivedResult.payload.body);

                    }
                    builder.setCustomContentView(contentViewSmall);
//                    builder.setCustomBigContentView(contentViewBig);
//                    builder.setCus
                    return builder.setColor(new BigInteger("222222", 16).intValue());
                }
            };
            OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
            Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);
            return false;
        }
    }

    void callBadgeCount() {
        try {
            JSONObject login_data = new JSONObject();
            String android_id = Settings.Secure.getString(OnSignalNotificationExtenderService.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            login_data.put("device_id", android_id);
            login_data.put("user_id", user.getUser_id());
            new VollyAPICall(OnSignalNotificationExtenderService.this, false, refresh_user_data, login_data, user.getSession_key(), Request.Method.POST, OnSignalNotificationExtenderService.this, APIActions.ApiActions.login);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ShowCustomeNotification(String title, String msg, String data) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext())
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentTitle(title)
                        .setContentText(msg);
        Intent resultIntent = new Intent(this, HomeActivity.class);
        resultIntent.putExtra("activityToBeOpened", "group_invitation");
        resultIntent.putExtra("data", String.valueOf(data));
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == APIActions.ApiActions.login) {
            try {
                JSONObject obj = new JSONObject(response);
                JSONObject session_object = obj.getJSONObject("successData").getJSONObject("session");
                JSONObject user_object = obj.getJSONObject("successData").getJSONObject("user");
                if (obj.getJSONObject("successData").getInt("notifications_count") > 0) {
                    ShortcutBadger.applyCount(this, obj.getJSONObject("successData").getInt("notifications_count"));

                } else {
                    ShortcutBadger.removeCount(this);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {

    }
}
