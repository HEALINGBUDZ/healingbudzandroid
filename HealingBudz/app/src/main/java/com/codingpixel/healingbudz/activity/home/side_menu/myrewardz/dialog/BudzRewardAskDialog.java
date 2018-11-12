package com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BaseDialogFragment;
import com.codingpixel.healingbudz.R;

public class BudzRewardAskDialog extends BaseDialogFragment<BudzRewardAskDialog.OnDialogFragmentClickListener> {

    static OnDialogFragmentClickListener Listener;

    public interface OnDialogFragmentClickListener {
        public void onClaimRewardClick(BudzRewardAskDialog dialog, int itemPosition);

    }

    public static BudzRewardAskDialog newInstance(OnDialogFragmentClickListener listner, String title, String points, int itemPosition) {
        BudzRewardAskDialog frag = new BudzRewardAskDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("points", points);
        args.putInt("pos", itemPosition);
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.budz_reward_ask_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
//        int top_y = getResources().getDisplayMetrics().heightPixels / 5;
//        wmlp.y = top_y;   //y position
        String title = "", points = "";
        Bundle bundle = getArguments();
        title = bundle.getString("title");
        points = bundle.getString("points");
        final int itemPosition = bundle.getInt("pos");
        ImageView Cross_btn = main_dialog.findViewById(R.id.cross_btn);
        TextView text_title = main_dialog.findViewById(R.id.text_title);
        text_title.setText(Html.fromHtml("\"" + title + "\" worth " + "<font color=#82BB2B>" + points + "</font>"));
        Cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button claim_reward_btn = main_dialog.findViewById(R.id.claim_reward_btn);
        claim_reward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog.dismiss();
                Listener.onClaimRewardClick(BudzRewardAskDialog.this, itemPosition);
            }
        });
        dialog.setView(main_dialog);
        return dialog;
    }


}