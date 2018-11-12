package com.codingpixel.healingbudz.activity.age_verification;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.GetUserLatLng;
import com.codingpixel.healingbudz.activity.Registration.LoginEntrance;
import com.codingpixel.healingbudz.interfaces.UserLocationListner;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.getSavedUser;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;
import static com.codingpixel.healingbudz.static_function.UIModification.FullScreen;

public class AgeVerification extends AppCompatActivity implements UserLocationListner {
    Button Enter, Exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_verification);
        FullScreen(AgeVerification.this);
        new GetUserLatLng().getUserLocation(AgeVerification.this, AgeVerification.this);
        Enter = (Button) findViewById(R.id.enter);
        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedPrefrences.getBool("is_user_login", AgeVerification.this)) {
                    user = getSavedUser(AgeVerification.this);
                    GoToHome(AgeVerification.this, false);
                } else {
                    GoTo(AgeVerification.this, LoginEntrance.class);
                }
                finish();
            }
        });

        Exit = (Button) findViewById(R.id.exit);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onLocationSuccess(Location location) {
        SharedPrefrences.setString("lat_cur", String.valueOf(location.getLatitude()), this);
        SharedPrefrences.setString("lng_cur", String.valueOf(location.getLongitude()), this);
    }

    @Override
    public void onLocationFailed(String Error) {

    }
}
