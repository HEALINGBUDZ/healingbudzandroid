package com.codingpixel.healingbudz.activity.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.android.volley.Request;
import com.codingpixel.healingbudz.DataModel.User;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.Utility;
import com.codingpixel.healingbudz.activity.Registration.login.SocialLoginFirstStep;
import com.codingpixel.healingbudz.activity.Wall.WallNewPostActivity;
import com.codingpixel.healingbudz.activity.age_verification.AgeVerification;
import com.codingpixel.healingbudz.activity.introduction.CustomBackgroundIntro;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.onesignal.OnClearFromRecentService;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.codingpixel.healingbudz.static_function.IntentFunction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.getSavedUser;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToPassword;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;


public class Splash extends AppCompatActivity {
    public static String otherName = "";
    public static String image_path = "";
    public static String NameBusiness = "";
    ImageView icon;
    TextView text;
    public static int widthDis = 0;
    public static User user = new User();
    public static boolean isLock = true;
    public static FragmentManager main_fragmentManager = null;
    public static boolean isKeywordClicked = false;
    boolean isGoToSetting = false;
    public static List<String> keywordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
        /*to save device width and height in preferances so can be get from any type of view other then activity*/
        Utility.getDeviceHeight(Splash.this);
        widthDis = Utility.getDeviceWidth(Splash.this);

        PrintHashKey();
        isLock = true;
        FullScreen(Splash.this);
        icon = (ImageView) findViewById(R.id.icon);
        text = findViewById(R.id.text);
        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinking);
        icon.startAnimation(startAnimation);
        text.startAnimation(startAnimation);
        getFragmentMenager();
        Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();
        String receivedType = receivedIntent.getType();
        if (receivedAction != null) {
            if (receivedAction.equals(Intent.ACTION_SEND)) {
                if (receivedType.startsWith("text/")) {
                    String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                    if (receivedText != null) {
                        Log.d("URLSHARE", receivedText);
                        if (SharedPrefrences.getBool("is_user_login", this)) {
                            User user = getSavedUser(this);
                            Splash.user = user;
                            Bundle bundleTxt = new Bundle();
                            bundleTxt.putString("urlhas", receivedText);
                            Utility.launchActivity(this, WallNewPostActivity.class, true, bundleTxt);
                        } else {
                            callNext();
                        }

                        //set the text

                    } else {
                        callNext();
                    }
                    //handle sent text
                } else {
                    callNext();
                }
                //content is being shared
            } else if (receivedAction.equals(Intent.ACTION_MAIN)) {
                callNext();
            }else {
                callNext();
            }
        } else {
            callNext();
        }

//        NextActivity();

    }

    public void callNext() {
        if (checkLocationPermission()) {
            Intent intent = getIntent();
            if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
                String recipeId = "";
                Bundle parameters = intent.getExtras();
                if (parameters.containsKey("token")) {
                    if (!SharedPrefrences.getBool("is_user_login", this)) {
                        GoToPassword(this, parameters.getString("token"));
                        finish();
                        return;
                    } else {
                        IntentFunction.GoToSplash(this);
                        finish();
                        return;
                    }
                }
                // You can pass a query parameter with the URI, and it's also in parameters, like
                // dld://classDeepLink?qp=12
            } else
                NextActivity();
        } else {


        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.title_location_permission))
                        .setContentText(getString(R.string.text_location_permission))
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                ActivityCompat.requestPermissions(Splash.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .show();
//                new AlertDialog.Builder(this)
//                        .setTitle(R.string.title_location_permission)
//                        .setMessage(R.string.text_location_permission)
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(Splash.this,
//                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                        MY_PERMISSIONS_REQUEST_LOCATION);
//                            }
//                        })
//                        .create()
//                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.REQUEST_INSTALL_PACKAGES},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isGoToSetting) {
            if (checkLocationPermission()) {
                NextActivity();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//
//                        //Request location updates:
//                        locationManager.requestLocationUpdates(provider, 400, 1, this);
//                    }
                    NextActivity();

                } else {
                    if (!isGoToSetting) {
                        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(getString(R.string.title_location_permission))
                                .setContentText(getString(R.string.text_location_permission))
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        isGoToSetting = true;
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        finish();
                                    }
                                })
                                .show();
//                        new AlertDialog.Builder(this)
//                                .setTitle(R.string.title_location_permission)
//                                .setMessage(R.string.text_location_permission)
//                                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        //Prompt the user once explanation has been shown
//                                        isGoToSetting = true;
//                                        Intent intent = new Intent();
//                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
//                                        intent.setData(uri);
//                                        startActivity(intent);
//                                    }
//                                })
//                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
//                                    @Override
//                                    public void onCancel(DialogInterface dialog) {
//                                        finish();
//                                    }
//                                })
//                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                                    @Override
//                                    public void onDismiss(DialogInterface dialog) {
//
//                                    }
//                                })
//                                .create()
//                                .show();

                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    } else {
                        finish();
                    }
                }

            }

        }
    }

    private void NextActivity() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (SharedPrefrences.getBoolWithTrueDefault("first_launch_overview_screen", Splash.this)) {
                    GoTo(Splash.this, CustomBackgroundIntro.class);
                    finish();
//                    GoTo(Splash.this, HealingBudzIntroActivity.class);
                } else {
                    if (SharedPrefrences.getBool("is_user_login", Splash.this)) {
                        user = getSavedUser(Splash.this);
                        if (Splash.user.getFirst_name().equalsIgnoreCase("")) {
                            GoTo(Splash.this, SocialLoginFirstStep.class);
                            finish();
                        } else {
                            new VollyAPICall(Splash.this,
                                    true
                                    , URL.get_keywords, new JSONObject(), user.getSession_key(), Request.Method.GET, new APIResponseListner() {
                                @Override
                                public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                                    try {
                                        if (new JSONObject(response).getString("status").equalsIgnoreCase("success")) {
                                            JSONArray jsonObject = new JSONObject(response).getJSONArray("successData");
                                            keywordList = new ArrayList<>();
                                            for (int i = 0; i < jsonObject.length(); i++) {
                                                JSONObject object = jsonObject.getJSONObject(i);
                                                keywordList.add(object.getString("title"));
                                            }
                                            GoToHome(Splash.this, false);
                                            finish();
                                        } else {

                                            GoToHome(Splash.this, false);
                                            finish();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        finish();
                                        GoToHome(Splash.this, false);
                                    }
                                }

                                @Override
                                public void onRequestError(String response, APIActions.ApiActions apiActions) {
                                    GoToHome(Splash.this, false);
                                    finish();
                                }
                            }, APIActions.ApiActions.key_words);
                        }


                    } else {
                        GoTo(Splash.this, AgeVerification.class);
                        finish();
                    }
                }


            }
        }, 2000);
    }

    private void PrintHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.healingbudz.android",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public void getFragmentMenager() {
        main_fragmentManager = this.getSupportFragmentManager();
    }
}
