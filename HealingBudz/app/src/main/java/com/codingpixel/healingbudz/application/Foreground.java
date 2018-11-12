package com.codingpixel.healingbudz.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.getSavedUser;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;

public class Foreground implements Application.ActivityLifecycleCallbacks {

    private static Foreground instance;
    public static final String TAG = "FORGROUND";

    public static void init(Application app) {
        if (instance == null) {
            instance = new Foreground();
            app.registerActivityLifecycleCallbacks(instance);
        }
    }

    public static Foreground get() {
        return instance;
    }

    private Foreground() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d("res", "reas");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.d("res", "reas");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.d("Resume Application", "false");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.d("res", "reas");
//        callOfflineApi();
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

    @Override
    public void onActivityStopped(Activity activity) {
        Log.d("Stoped Application", "true");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        Log.d("res", "reas");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Log.d("Destroy Applicatio", "true");
    }


}