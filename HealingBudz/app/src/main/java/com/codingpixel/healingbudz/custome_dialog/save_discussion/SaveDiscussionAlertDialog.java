package com.codingpixel.healingbudz.custome_dialog.save_discussion;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;

public class SaveDiscussionAlertDialog extends BaseDialogFragment<SaveDiscussionAlertDialog.OnDialogFragmentClickListener> {

   static OnDialogFragmentClickListener Listener ;
    public interface OnDialogFragmentClickListener {
        public void onOkClicked(SaveDiscussionAlertDialog dialog);
        public void onCancelClicked(SaveDiscussionAlertDialog dialog);
    }
    public static SaveDiscussionAlertDialog newInstance(String Heading, String title, String message , OnDialogFragmentClickListener listner) {
        SaveDiscussionAlertDialog frag = new SaveDiscussionAlertDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("heading", Heading);
        args.putString("msg", message);
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.save_discussion_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        int top_y = getResources().getDisplayMetrics().heightPixels / 5;
        wmlp.y = top_y;

        //y position
        dialog.setView(main_dialog);

        TextView heading = main_dialog.findViewById(R.id.heading);
        heading.setText(getArguments().getString("heading"));
        TextView title = main_dialog.findViewById(R.id.title_d);
        title.setText(getArguments().getString("title"));
        TextView msg = main_dialog.findViewById(R.id.msg);
        msg.setText(getArguments().getString("msg"));
        main_dialog.findViewById(R.id.cross_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listener.onCancelClicked(SaveDiscussionAlertDialog.this);
            }
        });
        CheckBox checkBox = main_dialog.findViewById(R.id.save_option);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Listener.onOkClicked(SaveDiscussionAlertDialog.this);
                        }
                    }, 800);
                }
            }
        });
        return dialog;
    }

}