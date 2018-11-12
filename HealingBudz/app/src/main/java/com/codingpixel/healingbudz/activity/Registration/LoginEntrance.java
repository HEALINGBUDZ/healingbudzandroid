package com.codingpixel.healingbudz.activity.Registration;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.activity.Registration.login.ForgetPasswordStep;
import com.codingpixel.healingbudz.activity.Registration.login.SignInWithEmailFirstStepActivity;
import com.codingpixel.healingbudz.activity.Registration.login.SocialLoginFirstStep;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import static com.codingpixel.healingbudz.Utilities.GetUserLatLng.GetZipcode_AddressFromLatlng;
import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.save_UserValues;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.upload_profile_img;
import static com.codingpixel.healingbudz.network.model.URL.get_keywords;
import static com.codingpixel.healingbudz.network.model.URL.social_login;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setBool;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setString;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;

public class LoginEntrance extends AppCompatActivity implements APIResponseListner
        , UserLocationListner
        , GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String TAG = "ggoogle";
    LinearLayout Sign_in_with_email, Sign_Up_with_email;
    LinearLayout Facebook_Login;
    LinearLayout google_login;
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    String user_Location = "";
    String user_Zipcode = "";
    double user_latitude = 0, user_longitude = 0;

    Button forget_password;
    //    private GoogleSignInClient mGoogleSignInClient;
    int lastGoogleAccounts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_entrance);
        new GetUserLatLng().getUserLocation(LoginEntrance.this, LoginEntrance.this);
        FullScreen(this);
        Init();
    }

    @Override
    protected void onStart() {
        super.onStart();

//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        }
        Account[] googleAccounts = AccountManager.get(this).getAccountsByType("com.google");

        if (googleAccounts != null && googleAccounts.length > 0) {
            if (lastGoogleAccounts == 0) {
                lastGoogleAccounts = googleAccounts.length;
            } else {
                if (googleAccounts.length > lastGoogleAccounts) {
                }
                // do login
            }
        }
    }
//
//    SmartLogin smartLogin;
//    SmartLoginConfig config;

    public void Init() {
//        config = new SmartLoginConfig(this /* Context */, this /* SmartLoginCallbacks */);
//        config.setFacebookAppId(getString(R.string.facebook_app_id));
//
//        smartLogin = SmartLoginFactory.build(LoginType.Google);
//        smartLogin.login(config);
        forget_password = (Button) findViewById(R.id.forget_password);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo(LoginEntrance.this, ForgetPasswordStep.class);
            }
        });
        Sign_in_with_email = (LinearLayout) findViewById(R.id.sign_in_with_email);
        Sign_in_with_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(LoginEntrance.this, SignInWithEmailFirstStepActivity.class);
            }
        });

        Sign_Up_with_email = (LinearLayout) findViewById(R.id.sign_up_with_email);
        Sign_Up_with_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(LoginEntrance.this, SignUpwithEmailFirstStep.class);
            }
        });

        Facebook_Login = (LinearLayout) findViewById(R.id.facebook_login);
        Facebook_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FBLOgin();
            }
        });

        google_login = (LinearLayout) findViewById(R.id.google_login);
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleLogin();
//                smartLogin.login(config);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();


    }

    public void FBLOgin() {

        FacebookSdk.clearLoggingBehaviors();
        FacebookSdk.sdkInitialize(LoginEntrance.this);
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();
//publish_actions ,
//        LoginManager.getInstance().logInWithPublishPermissions(LoginEntrance.this, Collections.singletonList("public_profile, email, user_birthday, user_friends"));
        LoginManager.getInstance().logInWithReadPermissions(LoginEntrance.this, Arrays.asList("public_profile", "user_friends", "email"));
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        try {
                            AccessToken aToken = loginResult.getAccessToken();
                            Log.d("fb_access_tocken", aToken.getToken());
                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            if (response != null) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject();
                                                    String android_id = Settings.Secure.getString(LoginEntrance.this.getContentResolver(),
                                                            Settings.Secure.ANDROID_ID);
                                                    jsonObject.put("device_id", android_id);
                                                    jsonObject.put("device_type", "Android");
                                                    jsonObject.put("user_type", "1");
                                                    if (response.getJSONObject().has("email")) {
                                                        jsonObject.put("email", response.getJSONObject().getString("email"));
                                                    } else {

                                                        jsonObject.put("email", response.getJSONObject().getString("id"));
                                                    }
                                                    jsonObject.put("nick_name", "");
//                                                    jsonObject.put("image", response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url"));
                                                    jsonObject.put("image", response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url"));
                                                    jsonObject.put("image_path", response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url"));
//                                                    jsonObject.put("image", "https://graph.facebook.com/" + response.getJSONObject().getString("id") + "/picture?type=large&return_ssl_resources=1");
                                                    jsonObject.put("fb_id", response.getJSONObject().getString("id"));
                                                    jsonObject.put("location", user_Location);
                                                    if (!user_Zipcode.equalsIgnoreCase("null")) {
                                                        jsonObject.put("zip_code", user_Zipcode);
                                                    } else {
                                                        jsonObject.put("zip_code", "54000");
                                                    }
                                                    if (user_latitude != 0 && user_longitude != 0) {
                                                        jsonObject.put("lat", user_latitude);
                                                        jsonObject.put("lng", user_longitude);
                                                    }

                                                    new VollyAPICall(LoginEntrance.this, true, social_login, jsonObject, null, Request.Method.POST, LoginEntrance.this, APIActions.ApiActions.login);

                                                    if (LoginManager.getInstance() != null) {
                                                        LoginManager.getInstance().logOut();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,last_name,email,gender, birthday, picture.width(300)");
                            request.setParameters(parameters);
                            request.executeAsync();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onCancel() {
                        LoginManager.getInstance().logOut();
                        Log.d(TAG, "onCancel: ");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "onError: ", exception);
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        smartLogin.onActivityResult(requestCode, resultCode, data, config);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
//                handleSignInResult(task);
                handleSignInResult(result);
            }
            if (resultCode == 0) {

//                new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Google Sign in Error")
//                        .setContentText("Please tap on sign in with google again!")
//                        .setConfirmText("Ok")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismissWithAnimation();
//
//                            }
//                        }).show();

            }
        } else {
            if (callbackManager != null) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acct = completedTask.getResult(ApiException.class);
//            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "Email: " + acct.getEmail());
            Log.e(TAG, "G_ID: " + acct.getId());
            try {
                JSONObject jsonObject = new JSONObject();
                String android_id = Settings.Secure.getString(LoginEntrance.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                jsonObject.put("device_id", android_id);
                jsonObject.put("device_type", "Android");
                jsonObject.put("user_type", "1");
                if (acct.getEmail() != null) {
                    jsonObject.put("email", acct.getEmail());
                } else {

                    jsonObject.put("email", acct.getId());

                }
                jsonObject.put("g_id", acct.getId());
                jsonObject.put("nick_name", acct.getDisplayName());
                jsonObject.put("location", user_Location);
                if (acct.getPhotoUrl() != null) {
                    jsonObject.put("image", acct.getPhotoUrl());
                }
                if (!user_Zipcode.equalsIgnoreCase("null")) {
                    jsonObject.put("zip_code", user_Zipcode);
                } else {
                    jsonObject.put("zip_code", "54000");
                }
                if (user_latitude != 0 && user_longitude != 0) {
                    jsonObject.put("lat", user_latitude);
                    jsonObject.put("lng", user_longitude);
                }
                new VollyAPICall(LoginEntrance.this, true, social_login, jsonObject, null, Request.Method.POST, LoginEntrance.this, APIActions.ApiActions.login);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);

//            mGoogleSignInClient.signOut();
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    public void GoogleLogin() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
//        signInIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    // Google Sign In Intent Handle
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "Email: " + acct.getEmail());
            Log.e(TAG, "G_ID: " + acct.getId());
            try {
                JSONObject jsonObject = new JSONObject();
                String android_id = Settings.Secure.getString(LoginEntrance.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                jsonObject.put("device_id", android_id);
                jsonObject.put("device_type", "Android");
                jsonObject.put("user_type", "1");
                if (acct.getEmail() != null) {
                    jsonObject.put("email", acct.getEmail());
                } else {

                    jsonObject.put("email", acct.getId());

                }
                jsonObject.put("g_id", acct.getId());
                jsonObject.put("nick_name", "");
                jsonObject.put("location", user_Location);
                if (acct.getPhotoUrl() != null) {
                    jsonObject.put("image", acct.getPhotoUrl());
                }
                if (!user_Zipcode.equalsIgnoreCase("null")) {
                    jsonObject.put("zip_code", user_Zipcode);
                } else {
                    jsonObject.put("zip_code", "54000");
                }
                if (user_latitude != 0 && user_longitude != 0) {
                    jsonObject.put("lat", user_latitude);
                    jsonObject.put("lng", user_longitude);
                }
                new VollyAPICall(LoginEntrance.this, true, social_login, jsonObject, null, Request.Method.POST, LoginEntrance.this, APIActions.ApiActions.login);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            }
        } else {
//            if (mGoogleApiClient.isConnected()) {
//                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
//                GoogleLogin();
//            }

        }
    }


    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == upload_profile_img) {

        } else {


            Log.d("respoonse", response);
            try {
                JSONObject obj = new JSONObject(response);
                JSONObject session_object = obj.getJSONObject("successData").getJSONObject("session");
                user.setId(session_object.getInt("id"));
                user.setUser_id(session_object.getInt("user_id"));
                user.setDevice_type(session_object.getString("device_type"));
                user.setDevice_type(session_object.getString("device_type"));
                user.setDevice_id(session_object.getString("device_id"));
                user.setLat(session_object.optDouble("lat"));
                user.setLng(session_object.optDouble("lng"));
                user.setSession_key(session_object.getString("session_key"));
                user.setTime_zone(session_object.optString("time_zone"));
                user.setFb_id(session_object.optString("fb_id"));
                user.setG_id(session_object.optString("g_id"));
                user.setCreated_at(session_object.optString("created_at"));
                user.setUpdated_at(session_object.optString("updated_at"));
                JSONObject user_object = obj.getJSONObject("successData").getJSONObject("user");
                user.setFirst_name(user_object.getString("first_name"));
                user.setSpecial_icon(user_object.getString("special_icon"));
                user.setLast_name(user_object.getString("last_name"));
                user.setEmail(user_object.getString("email"));
                user.setZip_code(user_object.getString("zip_code"));
                user.setImage_path(user_object.getString("image_path"));
                user.setUser_type(user_object.getString("user_type"));
                user.setAvatar(user_object.getString("avatar"));
                user.setCover(user_object.getString("cover"));
                user.setBio(user_object.getString("bio"));
                user.setLocation(user_object.getString("location"));
                user.setPoints(user_object.getInt("points"));
                if (user_object.getInt("show_budz_popup") == 0) {
                    user.setShow_budz_popup(false);
                } else {
                    user.setShow_budz_popup(true);
                }
                if (obj.getJSONObject("successData").getInt("sub_user") == 0) {
                    user.setPaidBudz(false);
                } else {
                    user.setPaidBudz(true);
                }
//            OneSignal.sendTag("user_id", user.getUser_id() + "");
                SharedPrefrences.setBool("first_launch_overview_screen", false, LoginEntrance.this);
                JSONObject keys = new JSONObject();
                keys.put("user_id", user.getUser_id() + "");
                keys.put("device_type", "android");
                OneSignal.sendTags(keys);
                setString("user_email", user.getEmail(), LoginEntrance.this);
                setBool("is_user_login", true, LoginEntrance.this);
                save_UserValues(user, LoginEntrance.this);
                new VollyAPICall(this,
                        false
                        , get_keywords
                        , new JSONObject()
                        , user.getSession_key()
                        , Request.Method.GET
                        , new APIResponseListner() {
                    @Override
                    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                        try {
                            if (new JSONObject(response).getString("status").equalsIgnoreCase("success")) {
                                JSONArray jsonObject = new JSONObject(response).getJSONArray("successData");
                                Splash.keywordList = new ArrayList<>();
                                for (int i = 0; i < jsonObject.length(); i++) {
                                    JSONObject object = jsonObject.getJSONObject(i);
                                    Splash.keywordList.add(object.getString("title"));
                                }

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onRequestError(String response, APIActions.ApiActions apiActions) {

                    }
                }
                        , APIActions.ApiActions.key_words);


                if (!obj.getJSONObject("successData").isNull("is_new_login")) {
                    if (obj.getJSONObject("successData").getInt("is_new_login") == 0)// && !user.getFirst_name().equalsIgnoreCase(""))
                    {
                        if (Splash.user.getFirst_name().equalsIgnoreCase("")){
                            GoTo(this, SocialLoginFirstStep.class);
                            finish();
                        }else {
                            GoToHome(LoginEntrance.this, true);
                            finish();
                        }


                    } else {

                        if (Splash.user.getFirst_name().equalsIgnoreCase("")){
                            GoTo(this, SocialLoginFirstStep.class);
                            finish();
                        }else {
                            GoToHome(LoginEntrance.this, true);
                            finish();
                        }

                    }
                } else {

                    if (Splash.user.getFirst_name().equalsIgnoreCase("")){
                        GoTo(this, SocialLoginFirstStep.class);
                        finish();
                    }else {
                        GoToHome(LoginEntrance.this, true);
                        finish();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
    }

    @Override
    public void onLocationSuccess(Location location) {
        user_latitude = location.getLatitude();
        user_longitude = location.getLongitude();
        SharedPrefrences.setString("lat_cur", String.valueOf(user_latitude), this);
        SharedPrefrences.setString("lng_cur", String.valueOf(user_longitude), this);
        String loc = GetZipcode_AddressFromLatlng(LoginEntrance.this, location.getLatitude(), location.getLongitude());
        if (loc.contains("&&&---&&&")) {
            if (loc.split("&&&---&&&").length > 0) {
                user_Location = loc.split("&&&---&&&")[0];
                user_Zipcode = loc.split("&&&---&&&")[1];
            }
        }

        if (loc.contains("&&&---&&&")) {
            if (loc.split("&&&---&&&").length > 0) {
                if (!loc.split("&&&---&&&")[1].equalsIgnoreCase("null")) {
                    user_Zipcode = loc.split("&&&---&&&")[1];

                } else {
                    user_Zipcode = "54000";
                }
            }
        }

    }

    @Override
    public void onLocationFailed(String Error) {
        Log.d("latlng", Error);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult != null)
            if (connectionResult.isSuccess()) {
                Log.d(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
            } else {
                Log.d(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
            }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (bundle != null)
            Log.d(TAG, "onConnected: " + bundle.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnected: " + i);
    }

}
