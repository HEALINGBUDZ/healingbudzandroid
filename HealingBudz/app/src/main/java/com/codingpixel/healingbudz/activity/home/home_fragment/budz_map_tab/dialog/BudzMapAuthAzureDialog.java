package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.DateConverter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.FontCache;

import static com.codingpixel.healingbudz.static_function.StaticObjects.emailPattern;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.static_function.StaticObjects.stripeKey;

public class BudzMapAuthAzureDialog extends BaseDialogFragment<BudzMapAuthAzureDialog.OnDialogFragmentClickListener> {
    static OnDialogFragmentClickListener Listener;

    public interface OnDialogFragmentClickListener {
        public void TokenGenrate(PaymentModel token);
    }

    PaymentModel model;

    public static BudzMapAuthAzureDialog newInstance(OnDialogFragmentClickListener listner, boolean isSubscribe, String pay, PaymentModel model) {
        BudzMapAuthAzureDialog frag = new BudzMapAuthAzureDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSubscribe", isSubscribe);
        args.putString("pay", pay);
        if (model != null)
            args.putSerializable("model", model);
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    EditText date_edit_text, cvv_edit_text, card_edit_text, email_edit_text;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.budz_map_auth_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        date_edit_text = main_dialog.findViewById(R.id.date_edit_text);
        cvv_edit_text = main_dialog.findViewById(R.id.cvv_edit_text);
        card_edit_text = main_dialog.findViewById(R.id.card_edit_text);
        email_edit_text = main_dialog.findViewById(R.id.email_edit_text);
        Typeface st = FontCache.getTypeface("Lato-Light.ttf", getContext());
        date_edit_text.setTypeface(st);
        Button Payy = main_dialog.findViewById(R.id.payy);
        if (getArguments() != null && getArguments().containsKey("model")) {
            model = (PaymentModel) getArguments().getSerializable("model");
            if (model != null) {
                date_edit_text.setText(model.getExpDate());
                cvv_edit_text.setText(model.getCvc());
                card_edit_text.setText(model.getCardName());
                email_edit_text.setText(model.getEmail());
            }
        }
        Payy.setText(getArguments().getString("pay"));
        Payy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(getActivity());
                if (isValid()) {
                    //DateConverter.checkDatePay(date_edit_text.getText().toString().trim())
                    PaymentModel token = new PaymentModel(email_edit_text.getText().toString().trim()
                            , date_edit_text.getText().toString().trim()
                            , card_edit_text.getText().toString().trim()
                            , cvv_edit_text.getText().toString().trim());
                    Listener.TokenGenrate(token);
                    dialog.dismiss();
                }
            }
        });

        ImageView Cross_btn = main_dialog.findViewById(R.id.cross_btn);
        Cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setView(main_dialog);
        return dialog;
    }

    public boolean isValid() {
        if (email_edit_text.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(getContext(), "Email Required!", Gravity.TOP);
            return false;
        }
        if (!email_edit_text.getText().toString().trim().matches(emailPattern)) {
            CustomeToast.ShowCustomToast(getContext(), "Email is not valid!", Gravity.TOP);
            return false;
        }
        if (card_edit_text.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(getContext(), "Card Number Required!", Gravity.TOP);
            return false;
        }
        if (date_edit_text.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(getContext(), "Date Required!", Gravity.TOP);
            return false;
        }

        if (DateConverter.checkDatePay(date_edit_text.getText().toString().trim()).equalsIgnoreCase("Date Format Error")) {
            CustomeToast.ShowCustomToast(getContext(), "Expiry Date is not valid!", Gravity.TOP);
            return false;
        }
        if (cvv_edit_text.getText().toString().trim().length() == 0) {
            CustomeToast.ShowCustomToast(getContext(), "CVC Required!", Gravity.TOP);
            return false;
        }
        return true;
    }
}