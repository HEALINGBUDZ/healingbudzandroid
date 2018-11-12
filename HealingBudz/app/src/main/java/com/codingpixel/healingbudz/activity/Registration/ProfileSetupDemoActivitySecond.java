package com.codingpixel.healingbudz.activity.Registration;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.Registration.SignUpWithEmailProfilePhoto.demo_question;
import static com.codingpixel.healingbudz.activity.Registration.SignUpWithEmailProfilePhoto.user_image_drawable;
import static com.codingpixel.healingbudz.activity.Registration.SignUpwithEmailFirstStep.signup_data;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;

public class ProfileSetupDemoActivitySecond  extends AppCompatActivity {
    TextView Location_Heading, Question;
    TextView Answer;
    ImageView Profile_Img;
    Button Countinue_Profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup_demo_second);
        FullScreen(ProfileSetupDemoActivitySecond.this);
        Init();
    }

    public void Init() {
        Location_Heading = (TextView) findViewById(R.id.heading_location);
        Countinue_Profile = (Button) findViewById(R.id.countinue_profile);
        Question = (TextView) findViewById(R.id.question);
        Answer = (TextView) findViewById(R.id.answer);
        Profile_Img = (ImageView) findViewById(R.id.profile_image);
        Profile_Img.setImageDrawable(user_image_drawable);
        String[] str = new String[0];
        try {
            str = signup_data.getString("location").split(",");
            Location_Heading.setText(signup_data.getString("nick_name") + ", from " + str[0] + ", " + str[str.length - 1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JSONObject object = new JSONObject(demo_question);
            Question.setText(object.getString("question"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Answer.setText(Html.fromHtml("I’m using <font color=#6d96ad>cannabis oil</font> for <font color=#6d96ad>muscle pain therapy</font> and I’m unsure if I should warm it before rubbing it on the effected areas.", Html.FROM_HTML_MODE_COMPACT));
            } else {
                Answer.setText(Html.fromHtml("I’m using <font color=#6d96ad>cannabis oil</font> for <font color=#6d96ad>muscle pain therapy</font> and I’m unsure if I should warm it before rubbing it on the effected areas."));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Countinue_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(ProfileSetupDemoActivitySecond.this , AddYourExpertAreasActivity.class);
                finish();
            }
        });

    }
}
