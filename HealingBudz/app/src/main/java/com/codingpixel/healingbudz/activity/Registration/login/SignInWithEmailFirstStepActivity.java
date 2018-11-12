package com.codingpixel.healingbudz.activity.Registration.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.customeUI.CustomeToast.ShowCustomToast;
import static com.codingpixel.healingbudz.network.model.URL.baseurl;
import static com.codingpixel.healingbudz.network.model.URL.check_email_availablity;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.StaticObjects.emailPattern;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class SignInWithEmailFirstStepActivity extends AppCompatActivity implements APIResponseListner {
    ImageView Next_Step;
    EditText Email;
    ImageView Back;
    public static JSONObject login_data = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_email_first_step);
//        FullScreen(this);
        StatusBarUtil.setTransparent(this);
        Init();
    }

    public void Init() {
        Email = (EditText) findViewById(R.id.email_text_field);
        String email = SharedPrefrences.getString("user_email", SignInWithEmailFirstStepActivity.this);
        if (email.length() > 0) {
            Email.setText(email);
            Email.setSelection(email.length());
        }
        Next_Step = (ImageView) findViewById(R.id.next_step);
        Next_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Email.getText().length() != 0) {
                    if (Email.getText().toString().trim().matches(emailPattern)) {

                        try {
                            HideKeyboard(SignInWithEmailFirstStepActivity.this);
                            login_data.put("email", Email.getText().toString());
                            new VollyAPICall(SignInWithEmailFirstStepActivity.this, true, baseurl + check_email_availablity, login_data, null, Request.Method.POST, SignInWithEmailFirstStepActivity.this, APIActions.ApiActions.check_emai);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ShowCustomToast(SignInWithEmailFirstStepActivity.this, "Invalid Email Address!", Gravity.TOP);

                    }

                } else {
                    ShowCustomToast(SignInWithEmailFirstStepActivity.this, "Email Field Required!", Gravity.TOP);
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
        Log.d("response", response);
//        GoTo(SignInWithEmailFirstStepActivity.this, SignInWithEmailSecondStep.class);
        ShowCustomToast(SignInWithEmailFirstStepActivity.this, "Invalid Email Address!", Gravity.TOP);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        GoTo(SignInWithEmailFirstStepActivity.this, SignInWithEmailSecondStep.class);
//        ShowCustomToast(SignInWithEmailFirstStepActivity.this, "Invalid Email Address", Gravity.TOP);
    }
}
