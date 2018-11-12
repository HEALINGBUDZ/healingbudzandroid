package com.codingpixel.healingbudz.activity.introduction;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.activity.Registration.LoginEntrance;
import com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences;

import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.getSavedUser;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoTo;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToHome;


public class StrainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.intro_activity_five, container, false);


        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
