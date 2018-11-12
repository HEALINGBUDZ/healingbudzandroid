package com.codingpixel.healingbudz.activity.Wall.dialogue;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codingpixel.healingbudz.activity.home.side_menu.profile.dialog.BaseDialogFragment;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.UserLikesDialogueRecylerAdapter;
import com.codingpixel.healingbudz.network.BudzFeedModel.submitPostLike.Like;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.codingpixel.healingbudz.activity.home.home_fragment.qa_tab.DiscussQuestionFragment.isNewScreen;
import static com.codingpixel.healingbudz.static_function.IntentFunction.GoToProfile;

public class UserLikesAlertDialog extends BaseDialogFragment<UserLikesAlertDialog.OnDialogFragmentClickListener> implements UserLikesDialogueRecylerAdapter.ItemClickListener {
    static OnDialogFragmentClickListener Listener;
    static String userID = "";
    static boolean is_Following;
    RecyclerView Emoji_recyler_View;
    LinearLayout Refresh;
    List<Like> likeList = new ArrayList<>();

    @Override
    public void onItemClick(View view, int position) {
        isNewScreen = true;
        GoToProfile(view.getContext(), likeList.get(position).getUserId());

    }


    public interface OnDialogFragmentClickListener {
        public void onCrossListener(UserLikesAlertDialog dialog);
    }

    public static UserLikesAlertDialog newInstance(OnDialogFragmentClickListener listner, List<Like> likeList) {
        UserLikesAlertDialog frag = new UserLikesAlertDialog();
        Bundle args = new Bundle();
        args.putSerializable("likes", (Serializable) likeList);
        frag.setArguments(args);
        Listener = listner;


        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.likes_layout, null);
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
        Bundle bundle = getArguments();
        likeList = (List<Like>) bundle.getSerializable("likes");

        Refresh = main_dialog.findViewById(R.id.refresh);
        Refresh.setVisibility(View.GONE);
        Emoji_recyler_View = main_dialog.findViewById(R.id.follower_recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        Emoji_recyler_View.setLayoutManager(layoutManager);
        if (likeList.size() > 0) {
            UserLikesDialogueRecylerAdapter recyler_adapter = new UserLikesDialogueRecylerAdapter(getContext(), likeList);
            recyler_adapter.setClickListener(this);
            Emoji_recyler_View.setAdapter(recyler_adapter);
        }
        dialog.setView(main_dialog);
        TextView Following_title = main_dialog.findViewById(R.id.following_title);

        final ImageView cross = main_dialog.findViewById(R.id.cross_btn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Listener.onCrossListener(UserLikesAlertDialog.this);
            }
        });
        return dialog;
    }


}