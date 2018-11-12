package com.codingpixel.healingbudz.activity.Registration.dialogue;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.BaseDialogFragment;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import java.util.ArrayList;

public class IntroHowToAddQuestionDialog extends BaseDialogFragment<IntroHowToAddQuestionDialog.OnDialogFragmentClickListener> implements APIResponseListner {
    static OnDialogFragmentClickListener Listener;
    static String userID = "";
    static boolean is_Following;
    RecyclerView Emoji_recyler_View;
    LinearLayout Refresh;
    ArrayList<UserFollowDataModel> dataModels = new ArrayList<>();

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {

    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Refresh.setVisibility(View.GONE);
        Log.d("resposne", response);
    }

    public interface OnDialogFragmentClickListener {
        public void onCrossClick(IntroHowToAddQuestionDialog dialog);
    }

    public static IntroHowToAddQuestionDialog newInstance(OnDialogFragmentClickListener listner) {
        IntroHowToAddQuestionDialog frag = new IntroHowToAddQuestionDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.intro_add_question_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        int top_y = getResources().getDisplayMetrics().heightPixels / 5;
        wmlp.y = top_y;
//        Refresh = main_dialog.findViewById(R.id.refresh);
//        Refresh.setVisibility(View.VISIBLE);
//        Emoji_recyler_View = main_dialog.findViewById(R.id.follower_recyler_view);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        Emoji_recyler_View.setLayoutManager(layoutManager);
        dialog.setView(main_dialog);
//        TextView Following_title = main_dialog.findViewById(R.id.following_title);
//        if (is_Following) {
//            Following_title.setText("Following");
//            JSONObject jsonObject = new JSONObject();
//            new VollyAPICall(getContext(), false, URL.get_followings + "/" + userID, jsonObject, user.getSession_key(), Request.Method.GET, IntroHowToAddQuestionDialog.this, get_followings);
//        } else {
//            Following_title.setText("Followers");
//            JSONObject jsonObject = new JSONObject();
//            new VollyAPICall(getContext(), false, URL.get_followers + "/" + userID, jsonObject, user.getSession_key(), Request.Method.GET, IntroHowToAddQuestionDialog.this, get_followers);
//        }
        final ImageView cross = main_dialog.findViewById(R.id.cross_btn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                Listener.onFollowFollwoingCrossBtnClink(IntroHowToAddQuestionDialog.this);
            }
        });
        return dialog;
    }

    public static class UserFollowDataModel {
        int id;
        boolean is_follow;
        String name;
        boolean isFollowing_Api;

        public int getId() {
            return id;
        }

        public boolean isFollowing_Api() {
            return isFollowing_Api;
        }

        public void setFollowing_Api(boolean following_Api) {
            isFollowing_Api = following_Api;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isIs_follow() {
            return is_follow;
        }

        public void setIs_follow(boolean is_follow) {
            this.is_follow = is_follow;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}