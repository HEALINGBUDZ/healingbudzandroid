package com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import static com.codingpixel.healingbudz.Utilities.Constants.stripeKey;
import static com.codingpixel.healingbudz.static_function.UIModification.HideKeyboard;

//import static com.codingpixel.healingbudz.static_function.StaticObjects.stripeKey;

public class BudzMapStripeDialog extends BaseDialogFragment<BudzMapStripeDialog.OnDialogFragmentClickListener> {
    static OnDialogFragmentClickListener Listener;

    public interface OnDialogFragmentClickListener {
        public void TokenGenrate(Token token);
    }

    public static BudzMapStripeDialog newInstance(OnDialogFragmentClickListener listner, boolean isSubscribe, String pay) {
        BudzMapStripeDialog frag = new BudzMapStripeDialog();
        Bundle args = new Bundle();
        args.putBoolean("isSubscribe", isSubscribe);
        args.putString("pay", pay);
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.budz_map_payment_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;

        final CardInputWidget mCardInputWidget = main_dialog.findViewById(R.id.card_input_widget);
        Button Payy = main_dialog.findViewById(R.id.payy);
        Payy.setText(getArguments().getString("pay"));
        Payy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyboard(getActivity());
                final ProgressDialog pd = ProgressDialog.newInstance();
                pd.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "pd");
                Card card = mCardInputWidget.getCard();
                if (card != null) {
                    Stripe stripe = new Stripe(getContext(), stripeKey);
                    stripe.createToken(
                            card,
                            new TokenCallback() {
                                public void onSuccess(Token token) {
                                    // Send token to your server
                                    Log.d("token", token.getId());
                                    Listener.TokenGenrate(token);
                                    dialog.dismiss();
                                    pd.dismiss();
                                }

                                public void onError(Exception error) {
                                    CustomeToast.ShowCustomToast(getContext(), error.getMessage(), Gravity.TOP);
                                    pd.dismiss();
                                }
                            }
                    );
                } else {
                    CustomeToast.ShowCustomToast(getContext(), "Wrong Card Number!", Gravity.TOP);
                    pd.dismiss();
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
}