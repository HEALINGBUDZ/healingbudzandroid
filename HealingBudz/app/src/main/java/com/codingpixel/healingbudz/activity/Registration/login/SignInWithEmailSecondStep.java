package com.codingpixel.healingbudz.activity.Registration.login;

import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.activity.Registration.LoginEntrance;
import com.codingpixel.healingbudz.activity.splash.Splash;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.jaeger.library.StatusBarUtil;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.save_UserValues;
import static com.codingpixel.healingbudz.activity.Registration.login.SignInWithEmailFirstStepActivity.login_data;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.CustomeToast.ShowCustomToast;
import static com.codingpixel.healingbudz.network.model.URL.login;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setBool;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setString;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class SignInWithEmailSecondStep extends AppCompatActivity implements APIResponseListner, UserLocationListner {
    ImageView Next_Step, show_pass;
    TextView User_Email;
    EditText Password;
    Button forget_password, forget_password_show;
    ImageView Back;
    double user_latitude = 0, user_longitude = 0;
    boolean isPass = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_email_password_activity);
//        FullScreen(this);
        StatusBarUtil.setTransparent(this);
        Init();
        new GetUserLatLng().getUserLocation(SignInWithEmailSecondStep.this, SignInWithEmailSecondStep.this);
    }


    public void Init() {
        Password = (EditText) findViewById(R.id.password_text_field);
        show_pass = findViewById(R.id.show_pass);
        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Password.getText().toString().length() > 0) {
                    if (isPass) {
                        isPass = false;
                        show_pass.setImageResource(R.drawable.ic_hide_pass);
                        Password.setTransformationMethod(null);
                    } else {
                        show_pass.setImageResource(R.drawable.ic_show_pass);
                        isPass = true;
                        Password.setTransformationMethod(new PasswordTransformationMethod());
                    }
                }
            }
        });

        forget_password_show = findViewById(R.id.forget_password_show);
        forget_password_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo(SignInWithEmailSecondStep.this, ForgetPasswordStep.class);
            }
        });
        forget_password = (Button) findViewById(R.id.forget_password);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoTo(SignInWithEmailSecondStep.this, ForgetPasswordStep.class);
            }
        });
        String password = SharedPrefrences.getString("user_password", SignInWithEmailSecondStep.this);
        if (password.length() > 0) {
            String email = SharedPrefrences.getString("user_email", SignInWithEmailSecondStep.this);
            try {
                if (login_data.getString("email").equalsIgnoreCase(email)) {
                    Password.setText(password);
                    Password.setSelection(Password.length());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        User_Email = (TextView) findViewById(R.id.user_email);
        try {
            User_Email.setText(login_data.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Next_Step = (ImageView) findViewById(R.id.next_step);
        Next_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Password.getText().toString().trim().length() != 0 && Password.getText().toString().trim().length() >= 6) {
                    try {
                        login_data.put("password", Password.getText().toString());
                        String android_id = Settings.Secure.getString(SignInWithEmailSecondStep.this.getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        login_data.put("device_id", android_id);
                        login_data.put("device_type", "Android");
                        if (user_latitude != 0 && user_longitude != 0) {
//                            login_data.put("lat", user_latitude);
//                            login_data.put("lng", user_longitude);
                        }
                        login_data.put("lat", "");
                        login_data.put("lng", "");
                        HideKeyboard(SignInWithEmailSecondStep.this);
                        new VollyAPICall(SignInWithEmailSecondStep.this, true, login, login_data, null, Request.Method.POST, SignInWithEmailSecondStep.this, APIActions.ApiActions.login);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (Password.getText().toString().trim().length() == 0) {
                        ShowCustomToast(SignInWithEmailSecondStep.this, "Enter Password!", Gravity.TOP);
                    }
                    if (Password.getText().toString().trim().length() < 6) {
                        ShowCustomToast(SignInWithEmailSecondStep.this, "Enter Password must be 6 character!", Gravity.TOP);
                    }

                }
            }
        });
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("respoonse", response);
        try {
            JSONObject obj = new JSONObject(response);
            JSONObject session_object = obj.getJSONObject("successData").getJSONObject("session");
            JSONObject user_object = obj.getJSONObject("successData").getJSONObject("user");
            user.setId(session_object.getInt("id"));
            user.setUser_id(session_object.getInt("user_id"));
            user.setDevice_type(session_object.getString("device_type"));
            user.setDevice_type(session_object.getString("device_type"));
            user.setDevice_id(session_object.getString("device_id"));
            user.setLat(user_object.optDouble("lat"));
            user.setLng(user_object.optDouble("lng"));
            user.setSession_key(session_object.getString("session_key"));
            user.setTime_zone(session_object.optString("time_zone"));
            user.setFb_id(session_object.optString("fb_id"));
            user.setG_id(session_object.optString("g_id"));
            user.setCreated_at(session_object.optString("created_at"));
            user.setUpdated_at(session_object.optString("updated_at"));
            user.setSpecial_icon(user_object.getString("special_icon"));
            user.setFirst_name(user_object.getString("first_name"));
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
//            if()
            new VollyAPICall(this,
                    false
                    , URL.get_keywords
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
            JSONObject keys = new JSONObject();
            keys.put("user_id", user.getUser_id() + "");
            keys.put("device_type", "android");
            OneSignal.sendTags(keys);
            SharedPrefrences.setBool("first_launch_overview_screen", false, SignInWithEmailSecondStep.this);
//            OneSignal.sendTag("user_id", user.getUser_id() + "");
            setString("user_email", user.getEmail(), SignInWithEmailSecondStep.this);
            setString("user_password", Password.getText().toString().trim(), SignInWithEmailSecondStep.this);
            setBool("is_user_login", true, SignInWithEmailSecondStep.this);
            save_UserValues(user, SignInWithEmailSecondStep.this);
            GoToHome(SignInWithEmailSecondStep.this, true);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getApplicationContext(), object.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationSuccess(Location location) {
        user_latitude = location.getLatitude();
        user_longitude = location.getLongitude();
    }

    @Override
    public void onLocationFailed(String Error) {
        Log.d("latlng", Error);
    }
}
