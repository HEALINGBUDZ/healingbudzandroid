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

import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.ChangeStatusBarColor;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

public class PrivacyPoliccy extends AppCompatActivity {
    ImageView Home, Back;
    TextView Text;
    WebView web_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policcy);
        ChangeStatusBarColor(PrivacyPoliccy.this, "#171717");
        HideKeyboard(PrivacyPoliccy.this);
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
                GoToHome(PrivacyPoliccy.this, true);
                finish();
            }
        });

//        Text = (TextView) findViewById(R.id.text);
        web_view = findViewById(R.id.web_view);
        WebSettings webSetting = web_view.getSettings();
        webSetting.setBuiltInZoomControls(true);
        webSetting.setDisplayZoomControls(false);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        web_view.setWebChromeClient(new WebChromeClient());
//        case termLink = "https://healingbudz.com/terms-conditions-mobile"
//    case privacyLink = "https://healingbudz.com/signup-privacy-mobile"
//        web_view.loadUrl("file:///android_asset/privacypolicy.html");
        web_view.loadUrl("https://healingbudz.com/signup-privacy-mobile");


    }
}
