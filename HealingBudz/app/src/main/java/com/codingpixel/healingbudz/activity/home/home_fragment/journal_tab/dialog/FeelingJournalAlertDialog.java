package com.codingpixel.healingbudz.activity.home.home_fragment.journal_tab.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.adapter.FeelingJournalDialogRecylerAdapter;

import static com.codingpixel.healingbudz.test_data.GetEmoji.getEmojis;
import static com.codingpixel.healingbudz.test_data.GetEmoji.getEmojisName;

public class FeelingJournalAlertDialog extends BaseDialogFragment<FeelingJournalAlertDialog.OnDialogFragmentClickListener> implements FeelingJournalDialogRecylerAdapter.ItemClickListener {
    ImageView Emoji_icon;
    TextView feeling_Text;
    static OnDialogFragmentClickListener Listener;
    @Override
    public void onItemClick(View view, int position) {
        Emoji_icon.setImageResource(getEmojis()[position]);
        feeling_Text.setText(getEmojisName(position));
//        MakeKeywordClickableText(feeling_Text.getContext(), getEmojisName(position), feeling_Text);
    }

    public interface OnDialogFragmentClickListener {
         void onCrossBtnClink(FeelingJournalAlertDialog dialog);
         void onPickEmojiClick(FeelingJournalAlertDialog dialog , Drawable image_resoure  , String feeling_Text);
    }

    public static FeelingJournalAlertDialog newInstance(OnDialogFragmentClickListener listner) {
        FeelingJournalAlertDialog frag = new FeelingJournalAlertDialog();
        Bundle args = new Bundle();
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.feeling_journal_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;              //        int top_y = getResources().getDisplayMetrics().heightPixels / 5;  wmlp.y = top_y;   //y position
        final RecyclerView Emoji_recyler_View = main_dialog.findViewById(R.id.emoji_recyler_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 6);
        Emoji_recyler_View.setLayoutManager(layoutManager);
        FeelingJournalDialogRecylerAdapter recyler_adapter = new FeelingJournalDialogRecylerAdapter(getContext(), getEmojis());
        Emoji_recyler_View.setAdapter(recyler_adapter);
        recyler_adapter.setClickListener(FeelingJournalAlertDialog.this);
        dialog.setView(main_dialog);
        final ImageView cross = main_dialog.findViewById(R.id.cross_btn);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onCrossBtnClink(FeelingJournalAlertDialog.this);
            }
        });
        final Button save = main_dialog.findViewById(R.id.save_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Listener.onPickEmojiClick(FeelingJournalAlertDialog.this , Emoji_icon.getDrawable() , feeling_Text.getText().toString());
            }
        });
        Emoji_icon = main_dialog.findViewById(R.id.emoji_icon);
        feeling_Text = main_dialog.findViewById(R.id.feeling_Text);
        return dialog;
    }


}