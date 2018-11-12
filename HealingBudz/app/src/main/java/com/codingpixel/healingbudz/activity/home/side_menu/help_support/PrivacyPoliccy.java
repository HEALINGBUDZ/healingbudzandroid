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
        web_view.loadUrl("file:///android_asset/privacypolicy.html");


        String Html_text = "<b ><font color=#0083cb>What is a Privacy Policy, Exactly?</font> </b><br>" +
                "A privacy policy is a document that discloses to your website visitors what you will do with any information gathered about them, how you are gathering that information and how the information will be stored, managed and protected. It fulfills a legal requirement in many countries & jurisdictions to protect your user's privacy.<br>" +
                "<br>" +
                "<b ><font color=#0083cb>Why Can't I Use a Template Privacy Policy?</font> </b><br>" +
                "Copying & pasting an example privacy policy might be OK... or it could be worse, far worse, than having no policy at all. What if it contains misleading or inaccurate information?<br>" +
                "<br>" +
                "Your privacy policy needs to be tailored to your website. If you simply \"borrow\" a template or generic privacy policy, it could provide misleading information.<br>" +
                "<br>" +
                "<b><font color=#0083cb>But I Don't Collect Personal Data...<br></font> </b>" +
                "If you run a website in 2017, you almost certainly collect personal data - even if you are unaware of it. And ignorance is no excuse for complying with the law.<br>" +
                "<br>" +
                "As a general \"rule of thumb\", any website that...<br>" +
                "- Tracks visitor numbers (eg, Google Analytics)<br>" +
                "- Collects any personal data (eg, email addresses for a newsletter)<br>" +
                "- Shows advertising (eg, Google AdSense)<br>" +
                "- Takes online payments (Like PayPal or credit cards)<br>" +
                "- Uses cookies<br>" +
                "- Has user accounts<br>" +
                "<br><b ><font color=#0083cb>How to Create a Privacy Policy ASAP</font> </b><br>" +
                "Generating a privacy policy for your website can be confusing and time-consuming. Worse, hiring an attorney to do the same thing will likely cost you $1000+.<br>" +
                "<br>" +
                "There's no one-size-fits-all policy template (no matter what our competitors say!), and your privacy policy needs to be tailored to your website to do the job right.<br>" +
                "<br>" +
                "With our privacy policy generator, you don't need to hire an expensive attorney or roll the dice on \"borrowing\" somebody else's template or copying & pasting a generic privacy policy.<br>" +
                "<br>" +
                "We can help you generate a customized privacy policy in around three minutes.<br>" +
                "<br>" +
                "<b ><font color=#0083cb>Any Questions?</font> </b><br>" +
                "Please check our FAQ";
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Text.append(Html.fromHtml(Html_text, Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            Text.append(Html.fromHtml(Html_text));
//        }
    }
}
