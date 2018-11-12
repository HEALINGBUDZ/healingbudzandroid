package com.codingpixel.healingbudz.activity.Registration;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONException;

import static com.codingpixel.healingbudz.activity.Registration.SignUpwithEmailFirstStep.signup_data;
import static com.codingpixel.healingbudz.customeUI.CustomeToast.ShowCustomToast;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class SignUPWithEmailEnterPassword extends AppCompatActivity {
    ImageView Next_Step, Back, show_pass, show_pass_con;
    TextView User_Email;
    EditText Password, ConfirmPAssword;
    ScrollView scroll_view;
    int time = 1000;
    boolean isFirst = true;

    boolean isPass = true, isPassCon = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_email_second_step);
//        FullScreen(this);
        StatusBarUtil.setTransparent(this);
        Init();
    }

    public void Init() {
        Password = (EditText) findViewById(R.id.password_text_field);
        scroll_view = findViewById(R.id.scroll_view);
        show_pass = findViewById(R.id.show_pass);
        show_pass_con = findViewById(R.id.show_pass_con);
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
        show_pass_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConfirmPAssword.getText().toString().length() > 0) {
                    if (isPassCon) {
                        isPassCon = false;
                        show_pass_con.setImageResource(R.drawable.ic_hide_pass);
                        ConfirmPAssword.setTransformationMethod(null);
                    } else {
                        show_pass_con.setImageResource(R.drawable.ic_show_pass);
                        isPassCon = true;
                        ConfirmPAssword.setTransformationMethod(new PasswordTransformationMethod());
                    }
                }
            }
        });
        ConfirmPAssword = (EditText) findViewById(R.id.confirm_password_text_field);
        Password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scroll_view.fullScroll(View.FOCUS_DOWN);
                            if (isFirst) {
                                isFirst = false;
                                time = 100;
                            }
                        }
                    }, time);

                }
            }
        });

        User_Email = (TextView) findViewById(R.id.user_email);
        User_Email.setAutoLinkMask(0);
        try {
            User_Email.setText(signup_data.getString("email"));
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//                if (signup_data.getString("email").length() > 20) {
//                    User_Email.setTextSize(Utility.convertDpToPixel(12.0F, this));
//                }
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Next_Step = (ImageView) findViewById(R.id.next_step);
        Next_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Password.getText().toString().trim().length() != 0 && Password.getText().toString().trim().length() >= 6 && ConfirmPAssword.getText().toString().trim().equalsIgnoreCase(Password.getText().toString().trim())) {
                    try {
                        HideKeyboard(SignUPWithEmailEnterPassword.this);
                        signup_data.put("password", Password.getText().toString());
                        GoTo(SignUPWithEmailEnterPassword.this, SignUpwithEmailNickName.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (Password.getText().toString().trim().length() == 0) {
                        ShowCustomToast(SignUPWithEmailEnterPassword.this, "Enter Password!", Gravity.TOP);
                    } else if (Password.getText().toString().trim().length() < 6) {
                        ShowCustomToast(SignUPWithEmailEnterPassword.this, "Enter Password must be 6 character!", Gravity.TOP);
                    } else if (!ConfirmPAssword.getText().toString().trim().equalsIgnoreCase(Password.getText().toString())) {
                        ShowCustomToast(SignUPWithEmailEnterPassword.this, "Password not match..", Gravity.TOP);
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
}
