package com.codingpixel.healingbudz.customeUI;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.codingpixel.healingbudz.R;


public class CustomeToast {
    public static void ShowCustomToast(Context context, String message, int Toast_Gravity) {
        if (context != null) {
            Toast toast = new Toast(context);
            TextView textView = new TextView(context);
            textView.setBackgroundResource(R.drawable.toast_bg);
            textView.setText(message);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(14);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(26, 26, 26, 26);
            toast.setView(textView);
            toast.setGravity(Gravity.TOP, 0, 100);
            toast.setDuration(Toast.LENGTH_LONG);
//            toast.setDuration(17000);
            toast.show();
        }
    }
}
