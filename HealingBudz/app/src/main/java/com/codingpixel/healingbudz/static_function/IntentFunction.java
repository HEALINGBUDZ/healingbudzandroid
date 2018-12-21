package com.codingpixel.healingbudz.static_function;

import android.content.Context;
import android.content.Intent;

import com.codingpixel.healingbudz.activity.Registration.login.ChangePasswordStep;
import com.codingpixel.healingbudz.activity.home.HomeActivity;
import com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.MyAnswersEditShowActivity;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity;
import com.codingpixel.healingbudz.activity.splash.Splash;

import java.io.Serializable;

public class IntentFunction {
    public static void GoTo(Context context, Class next_class) {
        Intent intent = new Intent(context, next_class);
        context.startActivity(intent);
    }

    public static void GoToWithBundle(Context context, Class next_class, Serializable bundle) {
        Intent intent = new Intent(context, next_class);
        intent.putExtra("object", bundle);
        context.startActivity(intent);
    }

    public static void GoToWithBundle(Context context, Class next_class, Serializable bundle, int Answerid, boolean isOnlyId, String name) {
        Intent intent = new Intent(context, next_class);
        intent.putExtra("object", bundle);
        intent.putExtra("Answerid", Answerid);
        intent.putExtra(name, isOnlyId);
        context.startActivity(intent);
    }

    public static void GoToHome(Context context, boolean isActive) {
        Intent i = new Intent(context, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("isActive", isActive);
        context.startActivity(i);
    }

    public static void GoToSplash(Context context) {
        Intent i = new Intent(context, Splash.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }

    public static void GoToPassword(Context context, String isActive) {
        Intent i = new Intent(context, ChangePasswordStep.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.putExtra("token", isActive);
        context.startActivity(i);
    }

    public static void GoToProfile(Context context, int user_id) {
        Intent i = new Intent(context, UserProfileActivity.class);
        i.putExtra("user_id", user_id);
        context.startActivity(i);
    }

    public static void GoToEditHistory(Context context, int id) {
        Intent i = new Intent(context, MyAnswersEditShowActivity.class);
        i.putExtra("answer_id", id);
        context.startActivity(i);
    }
}
