package com.codingpixel.healingbudz.activity.Registration.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.Registration.SignUPWithEmailEnterPassword;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.customeUI.CustomeToast.ShowCustomToast;
import static com.codingpixel.healingbudz.network.model.URL.forget_password;
import static com.codingpixel.healingbudz.static_function.StaticObjects.emailPattern;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class ForgetPasswordStep extends AppCompatActivity implements APIResponseListner {
    ImageView Next_Step;
    TextView User_Email;
    EditText Email;
    ImageView Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_activity);
//        FullScreen(this);

        StatusBarUtil.setTransparent(this);
        Init();
    }

    public void Init() {
        Email = (EditText) findViewById(R.id.email_text_field);
        Next_Step = (ImageView) findViewById(R.id.next_step);
        Next_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Email.getText().toString().trim().matches(emailPattern)) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("email", Email.getText().toString());
                        HideKeyboard(ForgetPasswordStep.this);
                        new VollyAPICall(ForgetPasswordStep.this, true, forget_password, object, null, Request.Method.POST, ForgetPasswordStep.this, APIActions.ApiActions.forget_password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (Email.getText().toString().trim().length() == 0) {
                        ShowCustomToast(ForgetPasswordStep.this, "Please Enter Email!", Gravity.TOP);
                    } else {
                        ShowCustomToast(ForgetPasswordStep.this, "Invalid Email Address!", Gravity.TOP);
                    }
                }
            }
        });
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgetPasswordStep.this.onBackPressed();
            }
        });
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("respoonse", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getApplicationContext(),"Email has been sent to you!", Gravity.TOP);
            ForgetPasswordStep.this.onBackPressed();
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


}
