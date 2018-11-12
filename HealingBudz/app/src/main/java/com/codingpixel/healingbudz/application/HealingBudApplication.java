package com.codingpixel.healingbudz.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.Request;
import com.codingpixel.healingbudz.Utilities.Constants;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.onesignal.OneSignalNotificationOpenedHandler;
import com.codingpixel.healingbudz.onesignal.OneSignalNotificationReceivedHandler;
import com.crashlytics.android.Crashlytics;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.freshchat.consumer.sdk.Freshchat;
import com.freshchat.consumer.sdk.FreshchatConfig;
import com.google.android.gms.ads.MobileAds;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;

import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.getSavedUser;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;

//import com.google.android.gms.ads.MobileAds;

public class HealingBudApplication extends Application {

    private static Context context;
    public static final String TAG = "APP_HB";
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Fabric.with(this, new Crashlytics());
        Foreground.init(this);
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .setReadTimeout(30000)
                .setConnectTimeout(30000)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);

        context = getApplicationContext();
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, Constants.add_init);
        OneSignal.startInit(this)
                .autoPromptLocation(false) // default call promptLocation later
                .setNotificationReceivedHandler(new OneSignalNotificationReceivedHandler())
                .setNotificationOpenedHandler(new OneSignalNotificationOpenedHandler())
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        FreshchatConfig freshchatConfig = new FreshchatConfig("57a3a6ae-a145-4bb2-a33c-5a8b05cfd6cd", "b9b562e4-ee91-4038-b22c-96758b81bf8b");
        freshchatConfig.setCameraCaptureEnabled(false);
        freshchatConfig.setGallerySelectionEnabled(false);
        Freshchat.getInstance(getApplicationContext()).init(freshchatConfig);
    }

    @Override
    public void onTerminate() {
        callOfflineApi();
        super.onTerminate();
    }

    private void callOfflineApi() {

        try {
            user = getSavedUser(HealingBudApplication.getContext());
            if (user != null && user.getSession_key() != null && user.getSession_key().length() > 5) {
                new VollyAPICall(HealingBudApplication.getContext()
                        , false
                        , URL.offline_user
                        , new JSONObject().put("session_key", Splash.user.getSession_key())
                        , Splash.user.getSession_key(), Request.Method.POST
                        , new APIResponseListner() {
                    @Override
                    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                        Log.d(TAG, response);
                    }

                    @Override
                    public void onRequestError(String response, APIActions.ApiActions apiActions) {
                        Log.d(TAG, response);
                    }
                }, APIActions.ApiActions.offline);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}