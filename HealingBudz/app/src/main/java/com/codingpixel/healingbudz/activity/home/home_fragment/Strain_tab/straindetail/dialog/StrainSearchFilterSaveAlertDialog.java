package com.codingpixel.healingbudz.activity.home.home_fragment.Strain_tab.straindetail.dialog;

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
import android.widget.EditText;
import android.widget.ImageView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.FeelingJournalDialogRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;

import static com.codingpixel.healingbudz.test_data.GetEmoji.getEmojis;

public class StrainSearchFilterSaveAlertDialog extends BaseDialogFragment<StrainSearchFilterSaveAlertDialog.OnDialogFragmentClickListener> implements FeelingJournalDialogRecylerAdapter.ItemClickListener {
    ImageView Emoji_icon;
    static OnDialogFragmentClickListener Listener;

    @Override
    public void onItemClick(View view, int position) {
        Emoji_icon.setImageResource(getEmojis()[position]);
    }

    public interface OnDialogFragmentClickListener {
        public void onCrossBtnClink(StrainSearchFilterSaveAlertDialog dialog);

        public void onSaveBtnClink(StrainSearchFilterSaveAlertDialog dialog, String saveName);
    }

    public static StrainSearchFilterSaveAlertDialog newInstance(OnDialogFragmentClickListener listner) {
        StrainSearchFilterSaveAlertDialog frag = new StrainSearchFilterSaveAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.strain_filter_search_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        dialog.setView(main_dialog);
        final ImageView cross = main_dialog.findViewById(R.id.cross_btn);
        final ImageView save_search = main_dialog.findViewById(R.id.save_search);
        final EditText et_save_text = main_dialog.findViewById(R.id.et_save_text);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onCrossBtnClink(StrainSearchFilterSaveAlertDialog.this);
            }
        });
        save_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_save_text.getText().toString().trim().length() > 0) {
                    Listener.onSaveBtnClink(StrainSearchFilterSaveAlertDialog.this, et_save_text.getText().toString());
                } else {
                    CustomeToast.ShowCustomToast(v.getContext(), "Empty Field!", Gravity.TOP);
                }
            }
        });
        return dialog;
    }


}