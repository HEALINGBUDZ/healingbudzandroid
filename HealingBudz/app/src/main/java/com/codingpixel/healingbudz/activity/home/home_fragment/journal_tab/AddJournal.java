package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_groups;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class AddJournal extends AppCompatActivity implements APIResponseListner {
    ImageView Back, Home;
    public  static  boolean isGroupClose  = false ;
    boolean isPublic = false, isPrivate = true;
    Button btn_private, btn_public;
    EditText entry_title;
    Button Save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_journal);
        ChangeStatusBarColor(AddJournal.this, "#0a0a0a");
        HideKeyboard(AddJournal.this);
        Back = (ImageView) findViewById(R.id.back_btn);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Home = (ImageView) findViewById(R.id.home_btn);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToHome(AddJournal.this, true);
                finish();
            }
        });
        entry_title = (EditText) findViewById(R.id.entry_title);
        btn_private = (Button) findViewById(R.id.private_btn);
        btn_public = (Button) findViewById(R.id.public_btn);
        btn_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPrivate) {
                    isPublic = false;
                    isPrivate = true;
                    btn_private.setBackgroundResource(R.drawable.toggle_btn_private);
                    btn_public.setBackground(null);
                }
            }
        });

        btn_public.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isPublic) {
                    isPrivate = false;
                    isPublic = true;
                    btn_public.setBackgroundResource(R.drawable.toggle_btn_public);
                    btn_private.setBackground(null);
                }
            }
        });

        Save = (Button) findViewById(R.id.save_btn);
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entry_title.getText().length() > 0 ){
                    JSONObject object = new JSONObject();
                    try {
                        object.put("title" , entry_title.getText().toString());
                        if(isPublic){
                            object.put("is_public" ,1);
                        }else {
                            object.put("is_public" , 0  );
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new VollyAPICall(AddJournal.this, true, URL.create_journal, object, user.getSession_key(), Request.Method.POST, AddJournal.this, get_groups);
                }else{
                    CustomeToast.ShowCustomToast(v.getContext(),"Title Field Required!", Gravity.TOP);
                }
            }
        });
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("response" , response);
        finish();
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("response" , response);
    }
}
