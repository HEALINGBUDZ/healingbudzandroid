package com.codingpixel.healingbudz.activity.home.side_menu.help_support;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.Registration.LoginEntrance;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.getSavedUser;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class TermAndConditionsOnIntorduction extends AppCompatActivity {
    ImageView Home, Back;
    TextView Text, Agree, Disagree;
    WebView web_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_conditions_intro);
        ChangeStatusBarColor(TermAndConditionsOnIntorduction.this, "#171717");
        HideKeyboard(TermAndConditionsOnIntorduction.this);
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
                GoToHome(TermAndConditionsOnIntorduction.this, true);
                finish();
            }
        });

//        Text = (TextView) findViewById(R.id.text);
        Agree = (TextView) findViewById(R.id.agree);
        Disagree = (TextView) findViewById(R.id.disagree);
        Agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefrences.getBool("is_user_login", TermAndConditionsOnIntorduction.this)) {
                    SharedPrefrences.setBool("first_launch_overview_screen", false, TermAndConditionsOnIntorduction.this);
                    user = getSavedUser(TermAndConditionsOnIntorduction.this);
                    GoToHome(TermAndConditionsOnIntorduction.this, false);
                    TermAndConditionsOnIntorduction.this.finish();
                } else {
                    GoTo(TermAndConditionsOnIntorduction.this, LoginEntrance.class);
                    TermAndConditionsOnIntorduction.this.finish();
                }
            }
        });
        Disagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
                finishAffinity();
            }
        });
        web_view = findViewById(R.id.web_view);
        WebSettings webSetting = web_view.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        web_view.setWebChromeClient(new WebChromeClient());
//        web_view.loadUrl("file:///android_asset/termcond.html");
        web_view.loadUrl("https://healingbudz.com/terms-conditions-mobile");

    }
}
