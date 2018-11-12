package com.codingpixel.healingbudz.activity.Registration.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.static_function.IntentFunction;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.customeUI.CustomeToast.ShowCustomToast;
import static com.codingpixel.healingbudz.network.model.URL.reset_password;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class ChangePasswordStep extends AppCompatActivity implements APIResponseListner {
    ImageView Next_Step, show_pass, show_pass_new;
    TextView User_Email;
    EditText Email;
    EditText EmailNew;
    ImageView Back;
    String token = "";
    boolean isPass = true, isPassCon = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_change_activity);
//        FullScreen(this);
        token = getIntent().getExtras().getString("token", "");
        StatusBarUtil.setTransparent(this);
        Init();
        //http://139.162.37.73/healingbudz/changepassword/W8Uy36jHeMflAsK9u+XvYA==
    }

    public void Init() {
        Email = (EditText) findViewById(R.id.email_text_field);
        show_pass = findViewById(R.id.show_pass);
        show_pass_new = findViewById(R.id.show_pass_new);
        EmailNew = (EditText) findViewById(R.id.email_text_field_new);
        Next_Step = (ImageView) findViewById(R.id.next_step);
        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Email.getText().toString().length() > 0) {
                    if (isPass) {
                        isPass = false;
                        show_pass.setImageResource(R.drawable.ic_hide_pass);
                        Email.setTransformationMethod(null);
                    } else {
                        show_pass.setImageResource(R.drawable.ic_show_pass);
                        isPass = true;
                        Email.setTransformationMethod(new PasswordTransformationMethod());
                    }
                }
            }
        });
        show_pass_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmailNew.getText().toString().length() > 0) {
                    if (isPassCon) {
                        isPassCon = false;
                        show_pass_new.setImageResource(R.drawable.ic_hide_pass);
                        EmailNew.setTransformationMethod(null);
                    } else {
                        show_pass_new.setImageResource(R.drawable.ic_show_pass);
                        isPassCon = true;
                        EmailNew.setTransformationMethod(new PasswordTransformationMethod());
                    }
                }
            }
        });
        Next_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Email.getText().toString().trim().length() >= 6 && EmailNew.getText().toString().trim().length() > 0
                        && Email.getText().toString().trim().equals(EmailNew.getText().toString().trim())) {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("password", Email.getText().toString().trim());
                        object.put("token", token);
                        HideKeyboard(ChangePasswordStep.this);
                        new VollyAPICall(ChangePasswordStep.this, true, reset_password, object, null, Request.Method.POST, ChangePasswordStep.this, APIActions.ApiActions.forget_password);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (Email.getText().toString().trim().length() == 0) {
                        ShowCustomToast(ChangePasswordStep.this, "New Password Required!", Gravity.TOP);
                    } else if (Email.getText().toString().trim().length() < 6) {
                        ShowCustomToast(ChangePasswordStep.this, "Password must be at least 6 letter!", Gravity.TOP);
                    } else if (EmailNew.getText().toString().trim().length() == 0) {
                        ShowCustomToast(ChangePasswordStep.this, "Please Enter Repeat Password!", Gravity.TOP);
                    } else if (Email.getText().toString().trim().equalsIgnoreCase(EmailNew.getText().toString().trim())) {
                        ShowCustomToast(ChangePasswordStep.this, "Password don't match!", Gravity.TOP);
                    }
                }
            }
        });
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentFunction.GoToSplash(view.getContext());
                finish();
            }
        });
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("respoonse", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(getApplicationContext(), "Password Reset Successfully!", Gravity.TOP);
            IntentFunction.GoToSplash(this);
            finish();
            //TODO FOR NEXT SCREEN
            ChangePasswordStep.this.onBackPressed();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        IntentFunction.GoToSplash(this);
        finish();
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
