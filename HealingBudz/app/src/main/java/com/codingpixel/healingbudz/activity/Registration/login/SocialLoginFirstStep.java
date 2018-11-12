package com.codingpixel.healingbudz.activity.Registration.login;

import android.content.Intent;
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
import com.codingpixel.healingbudz.activity.Registration.AddYourExpertAreasActivity;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;
import com.jaeger.library.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.customeUI.CustomeToast.ShowCustomToast;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part1;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part2;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part3;
import static com.codingpixel.healingbudz.network.model.URL.update_name;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class SocialLoginFirstStep extends AppCompatActivity implements APIResponseListner {
    ImageView Next_Step;
    TextView User_Email, skip;
    EditText Email, ZipCode;
    ImageView Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login_activity);
//        FullScreen(this);

        StatusBarUtil.setTransparent(this);
        Init();
    }

    public void Init() {
        Email = (EditText) findViewById(R.id.email_text_field);
        ZipCode = (EditText) findViewById(R.id.zip_code);
        skip = (TextView) findViewById(R.id.skip);
        Next_Step = (ImageView) findViewById(R.id.next_step);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefrences.setBool("first_launch_overview_screen", false, SocialLoginFirstStep.this);
                Intent i = new Intent(SocialLoginFirstStep.this, AddYourExpertAreasActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        Next_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ZipCode.getText().toString().trim().length() != 0 && Email.getText().toString().trim().length() != 0) {
                    JSONObject object = new JSONObject();
                    HideKeyboard(SocialLoginFirstStep.this);
                    new VollyAPICall(SocialLoginFirstStep.this, true, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + ZipCode.getText().toString() + getLocatioZipcode_part3, object, null, Request.Method.POST, SocialLoginFirstStep.this, APIActions.ApiActions.zip_code);
                } else {
                    if (ZipCode.getText().toString().trim().length() == 0 && Email.getText().toString().trim().length() == 0) {
                        ShowCustomToast(SocialLoginFirstStep.this, "Nick Name and ZipCode Required!", Gravity.TOP);
                    } else if (ZipCode.getText().toString().trim().length() == 0) {
                        ShowCustomToast(SocialLoginFirstStep.this, "ZipCode Required!", Gravity.TOP);
                    } else if (Email.getText().toString().trim().length() == 0) {
                        ShowCustomToast(SocialLoginFirstStep.this, "Nike Name Required!", Gravity.TOP);
                    }
                }
//                if (Email.getText().toString().trim().length() != 0) {
//                    try {
//                        JSONObject jsonObject = new JSONObject();
//                        jsonObject.put("name", Email.getText().toString().trim());
//
//                        new VollyAPICall(SocialLoginFirstStep.this, true, update_name, jsonObject, user.getSession_key(), Request.Method.POST, SocialLoginFirstStep.this, APIActions.ApiActions.login);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    CustomeToast.ShowCustomToast(SocialLoginFirstStep.this, "Nick Name Required!", Gravity.TOP);
//                }
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("respoonse", response);
        if (apiActions == APIActions.ApiActions.zip_code) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                if (jsonArray.length() > 0) {
                    JSONObject object = jsonArray.getJSONObject(0);
                    String Location_Name = object.getString("formatted_address");
                    JSONObject location_object = object.getJSONObject("geometry").getJSONObject("location");
                    Double latitude = location_object.getDouble("lat");
                    Double longitude = location_object.getDouble("lng");
                    Log.d("lat", latitude + "");
                    Log.d("lng", longitude + "");
                    Log.d("location_name", Location_Name + "");
                    //                        JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", Email.getText().toString().trim());
                    jsonObject.put("zip_code", ZipCode.getText().toString().trim());

                    new VollyAPICall(SocialLoginFirstStep.this, true, update_name, jsonObject, user.getSession_key(), Request.Method.POST, SocialLoginFirstStep.this, APIActions.ApiActions.login);
                    // ZipCode.getText().toString()
                } else {
                    CustomeToast.ShowCustomToast(this, "Error , Please provide correct zipcode!", Gravity.TOP);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            user.setFirst_name(Email.getText().toString().trim());
            SharedPrefrences.setBool("first_launch_overview_screen", false, SocialLoginFirstStep.this);
            Intent i = new Intent(SocialLoginFirstStep.this, AddYourExpertAreasActivity.class);
            //SignUpWithSocialProfilePhoto
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
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
