package com.codingpixel.healingbudz.activity.home.side_menu.myrewardz.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codingpixel.healingbudz.activity.home.home_fragment.budz_map_tab.dialog.BaseDialogFragment;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.customeUI.CustomeToast;

import org.json.JSONException;
import org.json.JSONObject;

public class BudzAddressFillDialog extends BaseDialogFragment<BudzAddressFillDialog.OnDialogFragmentClickListener> {

    static OnDialogFragmentClickListener Listener;
    LinearLayout submit;
    EditText et_state, et_city, et_zip, et_address, et_name;

    public static BudzAddressFillDialog newInstance(OnDialogFragmentClickListener listner, String title, String points, int itemPosition) {
        BudzAddressFillDialog frag = new BudzAddressFillDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("points", points);
        args.putInt("pos", itemPosition);
        frag.setArguments(args);
        Listener = listner;
        return frag;
    }

    public interface OnDialogFragmentClickListener {
        public void onSubmitAddress(BudzAddressFillDialog dialog, JSONObject jsonObject, int itemPosition);

        public void onCross(BudzAddressFillDialog dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater factory = LayoutInflater.from(getContext());
        final View main_dialog = factory.inflate(R.layout.budz_reward_address_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        Bundle bundle = getArguments();
        final int itemPosition = bundle.getInt("pos");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;

        ImageView Cross_btn = main_dialog.findViewById(R.id.cross_btn);

        submit = main_dialog.findViewById(R.id.submit);
        et_state = main_dialog.findViewById(R.id.et_state);
        et_zip = main_dialog.findViewById(R.id.et_zip);
        et_address = main_dialog.findViewById(R.id.et_address);
        et_name = main_dialog.findViewById(R.id.et_name);
        et_city = main_dialog.findViewById(R.id.et_city);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (et_name.getText().toString().trim().length() == 0) {
//                    CustomeToast.ShowCustomToast(v.getContext(), "Name required!", Gravity.BOTTOM);
//                } else
                if (et_address.getText().toString().trim().length() == 0) {
                    CustomeToast.ShowCustomToast(v.getContext(), "Address required!", Gravity.TOP);
                }
                else if (et_zip.getText().toString().trim().length() == 0) {
                    CustomeToast.ShowCustomToast(v.getContext(), "Zip Code required!", Gravity.TOP);
                }
//                else if (et_city.getText().toString().trim().length() == 0) {
//                    CustomeToast.ShowCustomToast(v.getContext(), "City Name required!", Gravity.BOTTOM);
//                } else if (et_state.getText().toString().trim().length() == 0) {
//                    CustomeToast.ShowCustomToast(v.getContext(), "State Name required!", Gravity.BOTTOM);
//                }
                else {
                    JSONObject jsonObject = new JSONObject();
                    /*name
state
city
address
zip*/
                    try {
                        jsonObject.put("name", et_name.getText().toString().trim());
                        jsonObject.put("state", et_state.getText().toString().trim());
                        jsonObject.put("city", et_city.getText().toString().trim());
                        jsonObject.put("zip", et_zip.getText().toString().trim());
                        jsonObject.put("address", et_address.getText().toString().trim());
                        Listener.onSubmitAddress(BudzAddressFillDialog.this, jsonObject, itemPosition);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
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