package com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BaseDialogFragment;
import com.codingpixel.healingbudz.R;

public class BudzThanksDialog extends BaseDialogFragment<BudzThanksDialog.OnDialogFragmentClickListener> {

    static OnDialogFragmentClickListener Listener;

    public interface OnDialogFragmentClickListener {

    }

    public static BudzThanksDialog newInstance(OnDialogFragmentClickListener listner, String title) {
        BudzThanksDialog frag = new BudzThanksDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.budz_reward_thanks_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        Bundle bundle = getArguments();
        ImageView Cross_btn = main_dialog.findViewById(R.id.cross_btn);
        TextView title = main_dialog.findViewById(R.id.title);
        title.setText("for redeeming product “" + bundle.getString("title") + "”");
        Cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setView(main_dialog);
        return dialog;
    }


}