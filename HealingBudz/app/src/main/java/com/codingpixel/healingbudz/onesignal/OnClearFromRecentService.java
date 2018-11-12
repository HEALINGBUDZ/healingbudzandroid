package com.codingpixel.healingbudz.onesignal;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.application.HealingBudApplication;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.getSavedUser;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;

public class OnClearFromRecentService extends Service {
    public static final String TAG = "SERVICE";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("ClearFromRecentService", "Service Started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ClearFromRecentService", "Service Destroyed");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        callOfflineApi();
        stopSelf();
    }

    private void callOfflineApi() {

        try {
            if (SharedPrefrences.getBool("is_user_login", this)) {
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}