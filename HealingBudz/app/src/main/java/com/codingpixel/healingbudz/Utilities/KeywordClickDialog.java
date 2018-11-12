package com.codingpixel.healingbudz.Utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.codingpixel.healingbudz.activity.home.HomeActivity;
import com.codingpixel.healingbudz.R;
import com.codingpixel.healingbudz.Utilities.eventbus.MessageEvent;
import com.codingpixel.healingbudz.adapter.KeywordDialogRecylerAdapter;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.codingpixel.healingbudz.activity.home.HomeActivity.getKeywordDialogItemClickListner;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;

public class KeywordClickDialog implements KeywordDialogRecylerAdapter.ItemClickListener, APIResponseListner {
    AlertDialog dialog;
    String keyword;
    Context context;
    ArrayList<String> data = new ArrayList<>();
    public static Boolean isKeywordSearch = false;
    public static String isKeywordSearch_string = "";


    @Override
    public void onItemClick(View view, int position) {
        dialog.dismiss();
        if (!((Activity) view.getContext() instanceof HomeActivity)) {
            ((Activity) view.getContext()).finish();
        } else {
            ((HomeActivity) view.getContext()).bottomBar(false);
        }
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new MessageEvent(true));
            }
        }, 200);
        switch (position) {

            case 0:
                getKeywordDialogItemClickListner.ShowQuestionsForKeyword(this.keyword);
                break;
            case 1:
                getKeywordDialogItemClickListner.ShowAnswersForKeyword(this.keyword);
                break;
//            case 2:
//                getKeywordDialogItemClickListner.ShowGroupsForKeyword(this.keyword);
//                break;
//            case 3:
//                getKeywordDialogItemClickListner.ShowJournalsForKeyword(this.keyword);
//                break;
            case 2:
                getKeywordDialogItemClickListner.ShowStrainForKeyword(this.keyword);
                break;
            case 3:
                getKeywordDialogItemClickListner.ShowBudzMapForKeyword(this.keyword);
                break;
        }
    }

    public KeywordClickDialog(String Keyword, Context c) {
        super();
        this.keyword = Keyword;
        this.context = c;
        data.add("Questions");
        data.add("Answers");
//        data.add("Groups");
//        data.add("Journals");
        data.add("Strain");
        data.add("Budz Adz");
        ShowDialog();
    }

    public void ShowDialog() {
        LayoutInflater factory = LayoutInflater.from(this.context);
        final View main_dialog = factory.inflate(R.layout.keyword_ckicked_dialog_layout, null);
        dialog = new AlertDialog.Builder(context).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        final TextView Keyword_title = main_dialog.findViewById(R.id.keyword_title);
        ImageView Cross = main_dialog.findViewById(R.id.cross_btn);
        Button follow_keyword = main_dialog.findViewById(R.id.follow_keyword);
        follow_keyword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    new VollyAPICall(view.getContext(), false, URL.follow_keyword, new JSONObject().put("keyword", Keyword_title.getText().toString()), user.getSession_key(), Request.Method.POST, KeywordClickDialog.this, APIActions.ApiActions.get_key);
                    dialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }
        });
        Cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        Keyword_title.setText(keyword);
        RecyclerView recyclerView = main_dialog.findViewById(R.id.recyler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        KeywordDialogRecylerAdapter recyler_adapter = new KeywordDialogRecylerAdapter(context, data);
        recyler_adapter.setClickListener(this);
        recyclerView.setAdapter(recyler_adapter);
        dialog.setView(main_dialog);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog.show();
    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        Log.d("onRequestSuccess: ", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            CustomeToast.ShowCustomToast(context, jsonObject.getString("successMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        CustomeToast.ShowCustomToast(context, "Keyword Followed successfully!", Gravity.BOTTOM);
    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        Log.d("onRequestError: ", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            CustomeToast.ShowCustomToast(context, jsonObject.getString("errorMessage"), Gravity.TOP);
        } catch (JSONException e) {
            e.printStackTrace();
            CustomeToast.ShowCustomToast(context, "Error Following Keyword!", Gravity.TOP);
        }
//        CustomeToast.ShowCustomToast(context, "Error Following Keyword!", Gravity.BOTTOM);
    }
}
