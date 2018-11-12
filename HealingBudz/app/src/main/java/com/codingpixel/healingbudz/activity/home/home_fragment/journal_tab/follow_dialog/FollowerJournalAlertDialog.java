package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.follow_dialog;

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

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.FollowerJournalDialogRecylerAdapter;

import static com.codingpixel.healingbudz.test_data.FollowerJournalData.getFolloweData;

public class FollowerJournalAlertDialog extends BaseDialogFragment<FollowerJournalAlertDialog.OnDialogFragmentClickListener> {
    static OnDialogFragmentClickListener Listener;

    public interface OnDialogFragmentClickListener {
        public void onCrossBtnClink(FollowerJournalAlertDialog dialog);
    }

    public static FollowerJournalAlertDialog newInstance(OnDialogFragmentClickListener listner) {
        FollowerJournalAlertDialog frag = new FollowerJournalAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
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
        LinearLayout Refresh = main_dialog.findViewById(R.id.refresh);
        Refresh.setVisibility(View.VISIBLE);
        final RecyclerView Emoji_recyler_View = main_dialog.findViewById(R.id.follower_recyler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        Emoji_recyler_View.setLayoutManager(layoutManager);
        FollowerJournalDialogRecylerAdapter recyler_adapter = new FollowerJournalDialogRecylerAdapter(getContext(), getFolloweData());
        Emoji_recyler_View.setAdapter(recyler_adapter);
        dialog.setView(main_dialog);
        final ImageView cross = main_dialog.findViewById(R.id.cross_btn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onCrossBtnClink(FollowerJournalAlertDialog.this);
            }
        });
        Refresh.setVisibility(View.GONE);
        return dialog;
    }


}