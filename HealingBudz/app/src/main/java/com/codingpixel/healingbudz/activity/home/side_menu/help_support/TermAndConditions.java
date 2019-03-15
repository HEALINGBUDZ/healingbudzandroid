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

public class TermAndConditions extends AppCompatActivity {
    ImageView Home, Back;
    TextView Text;
    WebView web_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_conditions);
        ChangeStatusBarColor(TermAndConditions.this, "#171717");
        HideKeyboard(TermAndConditions.this);
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
                GoToHome(TermAndConditions.this, true);
                finish();
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
