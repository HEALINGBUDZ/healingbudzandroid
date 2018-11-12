package com.codingpixel.healingbudz.custome_dialog.save_discussion;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;

public class BudzProductAddAlertDialog extends BaseDialogFragment<BudzProductAddAlertDialog.OnDialogFragmentClickListener> {

   static OnDialogFragmentClickListener Listener ;
    public interface OnDialogFragmentClickListener {
        public void onCrossClicked(BudzProductAddAlertDialog dialog);
    }
    public static BudzProductAddAlertDialog newInstance(OnDialogFragmentClickListener listner) {
        BudzProductAddAlertDialog frag = new BudzProductAddAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.product_add_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
//        wmlp.gravity = Gravity.TOP;
//        int top_y = getResources().getDisplayMetrics().heightPixels / 5;
//        wmlp.y = top_y;

        //y position
        dialog.setView(main_dialog);


        TextView msg = main_dialog.findViewById(R.id.msg);

        main_dialog.findViewById(R.id.cross_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                Listener.onCrossClicked(BudzProductAddAlertDialog.this);
            }
        });

        return dialog;
    }

}