package com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.side_menu.profile.UserProfileActivity;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.UserFollowFollwingDialogRecylerAdapter;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_followers;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.get_followings;

public class UserFollowFollwingAlertDialog extends BaseDialogFragment<UserFollowFollwingAlertDialog.OnDialogFragmentClickListener> implements APIResponseListner {
    static OnDialogFragmentClickListener Listener;
    static String userID = "";
    static boolean is_Following;
    RecyclerView Emoji_recyler_View;
    LinearLayout Refresh;
    ArrayList<UserFollowDataModel> dataModels = new ArrayList<>();
    boolean isGoneOther = false;

    @Override
    public void onResume() {
        super.onResume();
        if(isGoneOther){
            isGoneOther = false;
            if(getActivity() instanceof UserProfileActivity){
                ((UserProfileActivity)getActivity()).onBackDataCalled();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isGoneOther = true;
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Refresh.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (apiActions == get_followings) {
                dataModels.clear();
                JSONArray jsonArray = jsonObject.getJSONArray("successData");
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject object = jsonArray.getJSONObject(x);
                    UserFollowDataModel dataModel = new UserFollowDataModel();
                    dataModel.setId(object.getInt("id"));
                    dataModel.setIs_follow(true);
                    dataModel.setFollowing_Api(true);
                    dataModel.setName(object.getString("first_name"));
                    dataModels.add(dataModel);
                }
                if (getContext() != null) {
                    UserFollowFollwingDialogRecylerAdapter recyler_adapter = new UserFollowFollwingDialogRecylerAdapter(getContext(), dataModels);
                    Emoji_recyler_View.setAdapter(recyler_adapter);
                }
            } else if (apiActions == get_followers) {
                dataModels.clear();
                JSONArray jsonArray = jsonObject.getJSONArray("successData");
                for (int x = 0; x < jsonArray.length(); x++) {
                    JSONObject object = jsonArray.getJSONObject(x);
                    UserFollowDataModel dataModel = new UserFollowDataModel();
                    dataModel.setId(object.getInt("user_id"));
                    if (object.getInt("is_following_count") == 1) {
                        dataModel.setIs_follow(true);
                    } else {
                        dataModel.setIs_follow(false);
                    }
                    dataModel.setFollowing_Api(false);
                    dataModel.setName(object.getJSONObject("user").getString("first_name"));
                    dataModels.add(dataModel);
                }
                if (getContext() != null) {
                    UserFollowFollwingDialogRecylerAdapter recyler_adapter = new UserFollowFollwingDialogRecylerAdapter(getContext(), dataModels);
                    Emoji_recyler_View.setAdapter(recyler_adapter);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Refresh.setVisibility(View.GONE);
        Log.d("resposne", response);
    }

    public interface OnDialogFragmentClickListener {
        public void onFollowFollwoingCrossBtnClink(UserFollowFollwingAlertDialog dialog);
    }

    public static UserFollowFollwingAlertDialog newInstance(OnDialogFragmentClickListener listner, String user_id, boolean isFollowing) {
        UserFollowFollwingAlertDialog frag = new UserFollowFollwingAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        userID = user_id;
        is_Following = isFollowing;
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.followers_journal_dialog_layout, null);
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
        Refresh = main_dialog.findViewById(R.id.refresh);
        Refresh.setVisibility(View.VISIBLE);
        Emoji_recyler_View = main_dialog.findViewById(R.id.follower_recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        Emoji_recyler_View.setLayoutManager(layoutManager);
        dialog.setView(main_dialog);
        TextView Following_title = main_dialog.findViewById(R.id.following_title);
        if (is_Following) {
            Following_title.setText("Following");
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(getContext(), false, URL.get_followings + "/" + userID, jsonObject, user.getSession_key(), Request.Method.GET, UserFollowFollwingAlertDialog.this, get_followings);
        } else {
            Following_title.setText("Followers");
            JSONObject jsonObject = new JSONObject();
            new VollyAPICall(getContext(), false, URL.get_followers + "/" + userID, jsonObject, user.getSession_key(), Request.Method.GET, UserFollowFollwingAlertDialog.this, get_followers);
        }
        final ImageView cross = main_dialog.findViewById(R.id.cross_btn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onFollowFollwoingCrossBtnClink(UserFollowFollwingAlertDialog.this);
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