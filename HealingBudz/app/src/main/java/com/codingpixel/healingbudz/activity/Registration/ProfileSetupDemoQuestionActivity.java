package com.codingpixel.healingbudz.activity.Registration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ProfileSetupDemoQuestionActivity extends AppCompatActivity {
    TextView Location_Heading, Question;
    EditText Description_Details;
    ImageView Profile_Img;
    Button Ask_Your_Budz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup_demo_question);
        FullScreen(ProfileSetupDemoQuestionActivity.this);
        Init();
    }

    public void Init() {
        Location_Heading = (TextView) findViewById(R.id.heading_location);
        Ask_Your_Budz = (Button) findViewById(R.id.ask_yout_budz);
        Question = (TextView) findViewById(R.id.question);
        Description_Details = (EditText) findViewById(R.id.description_details);
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
            Description_Details.setText(object.getString("answer"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Ask_Your_Budz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoTo(ProfileSetupDemoQuestionActivity.this , ProfileSetupDemoActivitySecond.class);
                finish();
            }
        });

    }
}
