package com.codingpixel.healingbudz.fragment_back_button_listner;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;

/**
 * Created by codingpixel on 10/08/2017.
 */

public class InitFragmentBackButoonListner {

    public static void InitFragmentBackbtnListner(Activity activity , final BackButtonClickListner backButtonClickListner){
        activity.getWindow().getCurrentFocus().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    backButtonClickListner.onBackButtonClick();
                    return true;
                }
                return false;
            }
        });
    }
}
