package com.codingpixel.healingbudz.activity.Registration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.customeUI.CustomeToast.ShowCustomToast;
import static com.codingpixel.healingbudz.network.model.URL.baseurl;
import static com.codingpixel.healingbudz.network.model.URL.check_email_availablity;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.StaticObjects.emailPattern;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class SignUpwithEmailFirstStep extends AppCompatActivity implements APIResponseListner {
    ImageView Next_Step, Back;
    EditText Email;
    public static JSONObject signup_data = new JSONObject();
    public static boolean isSpecialBud = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_with_email_first_step);
//        FullScreen(this);

        StatusBarUtil.setTransparent(this);
        HideKeyboard(SignUpwithEmailFirstStep.this);
        Init();
    }

    public void Init() {
        Next_Step = (ImageView) findViewById(R.id.next_step);
        Next_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Email.getText().toString().trim().length() != 0) {
                    if (!Email.getText().toString().trim().matches(emailPattern)) {
                        ShowCustomToast(SignUpwithEmailFirstStep.this, "Invalid Email Address!", Gravity.TOP);
                    } else {
                        try {
                            HideKeyboard(SignUpwithEmailFirstStep.this);
                            signup_data.put("email", Email.getText().toString());
                            new VollyAPICall(SignUpwithEmailFirstStep.this, true, baseurl + check_email_availablity, signup_data, null, Request.Method.POST, SignUpwithEmailFirstStep.this, APIActions.ApiActions.check_emai);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    ShowCustomToast(SignUpwithEmailFirstStep.this, "Email Field Required!", Gravity.TOP);
                }
            }
        });
        Email = (EditText) findViewById(R.id.email_text_field);

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
        Log.d("respinse", response);
        if (apiActions == APIActions.ApiActions.check_emai_special) {
            try {
                JSONObject object = new JSONObject(response);
                isSpecialBud = true;
//                CustomeToast.ShowCustomToast(getApplicationContext(), object.getString("errorMessage"), Gravity.TOP);
                GoTo(SignUpwithEmailFirstStep.this, SignUPWithEmailEnterPassword.class);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            GoTo(SignUpwithEmailFirstStep.this, SignUPWithEmailEnterPassword.class);

        } else {
            try {
                signup_data.put("email", Email.getText().toString().trim());
                new VollyAPICall(SignUpwithEmailFirstStep.this, true, baseurl + URL.check_special_email, signup_data, null, Request.Method.POST, SignUpwithEmailFirstStep.this, APIActions.ApiActions.check_emai_special);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
//check_special_email
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("respinse", response);
        if (apiActions == APIActions.ApiActions.check_emai_special) {
            try {
                JSONObject object = new JSONObject(response);
                if (object.has("errorMessage") && object.getString("errorMessage").equalsIgnoreCase("Email does not exist.")) {
                    isSpecialBud = false;
                    GoTo(SignUpwithEmailFirstStep.this, SignUPWithEmailEnterPassword.class);
                } else {
                    CustomeToast.ShowCustomToast(getApplicationContext(), object.getString("errorMessage"), Gravity.TOP);
                }
//                CustomeToast.ShowCustomToast(getApplicationContext(), object.getString("errorMessage"), Gravity.TOP);
            } catch (JSONException e) {
                e.printStackTrace();
                CustomeToast.ShowCustomToast(getApplicationContext(), "Server Error!", Gravity.TOP);
            }

        } else {

            try {
                JSONObject object = new JSONObject(response);
                CustomeToast.ShowCustomToast(getApplicationContext(), object.getString("errorMessage"), Gravity.TOP);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
