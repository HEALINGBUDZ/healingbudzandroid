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
import com.jaeger.library.StatusBarUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.Registration.SignUpwithEmailFirstStep.signup_data;
import static com.codingpixel.healingbudz.customeUI.CustomeToast.ShowCustomToast;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part1;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part2;
import static com.codingpixel.healingbudz.network.model.URL.getLocatioZipcode_part3;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class SignUpwithEmailNickName extends AppCompatActivity implements APIResponseListner {
    ImageView Next_Step, Back;
    EditText NickName, ZipCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_with_email_second_step);
//        FullScreen(SignUpwithEmailNickName.this);
        StatusBarUtil.setTransparent(this);
        Init();
    }

    public void Init() {
        NickName = (EditText) findViewById(R.id.nick_name);
        ZipCode = (EditText) findViewById(R.id.zip_code);
        Next_Step = (ImageView) findViewById(R.id.next_step);
        Next_Step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ZipCode.getText().toString().trim().length() != 0 && NickName.getText().toString().trim().length() != 0) {
                    JSONObject object = new JSONObject();
                    HideKeyboard(SignUpwithEmailNickName.this);
                    new VollyAPICall(SignUpwithEmailNickName.this, true, getLocatioZipcode_part1 + "USA" + getLocatioZipcode_part2 + ZipCode.getText().toString() + getLocatioZipcode_part3, object, null, Request.Method.POST, SignUpwithEmailNickName.this, APIActions.ApiActions.testAPI);
                } else {
                    if (ZipCode.getText().toString().trim().length() == 0 && NickName.getText().toString().trim().length() == 0) {

                        ShowCustomToast(SignUpwithEmailNickName.this, "Nick Name and ZipCode Required!", Gravity.TOP);

                    } else if (ZipCode.getText().toString().trim().length() == 0) {
                        ShowCustomToast(SignUpwithEmailNickName.this, "ZipCode Required!", Gravity.TOP);
                    } else if (NickName.getText().toString().trim().length() == 0) {
                        ShowCustomToast(SignUpwithEmailNickName.this, "Nike Name Required!", Gravity.TOP);
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
        Log.d("response", response);
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
                signup_data.put("nick_name", NickName.getText().toString());
                signup_data.put("zip_code", ZipCode.getText().toString());
                signup_data.put("location", Location_Name);
                signup_data.put("lat", latitude);
                signup_data.put("lng", longitude);
                GoTo(SignUpwithEmailNickName.this, SignUpWithEmailProfilePhoto.class);
            } else {
                CustomeToast.ShowCustomToast(SignUpwithEmailNickName.this, "Error , Please provide correct zipcode!", Gravity.TOP);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response", response);
        Log.d("respinse", response);
        try {
            JSONObject object = new JSONObject(response);
            CustomeToast.ShowCustomToast(SignUpwithEmailNickName.this, "Error , getting locaiton! try again later!", Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
            CustomeToast.ShowCustomToast(SignUpwithEmailNickName.this, "Error , getting locaiton! try again later!", Gravity.TOP);
        }

    }
}
