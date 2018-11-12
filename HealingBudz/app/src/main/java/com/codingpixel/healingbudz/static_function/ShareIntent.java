package com.codingpixel.healingbudz.static_function;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;

import com.android.volley.Request;
import com.codingpixel.healingbudz.customeUI.customalerts.SweetAlertDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.VollyAPICall;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.URL;

import org.json.JSONException;
import org.json.JSONObject;

import static com.codingpixel.healingbudz.activity.splash.Splash.user;
import static com.codingpixel.healingbudz.data_structure.APIActions.ApiActions.save_share;

public class ShareIntent implements APIResponseListner {

    public static void ShareHBContent(final Activity context, final JSONObject data_object) {

        String Text = null;
        String content = "";
        String url = "";

        try {
            if (data_object.has("url")) {
                url = data_object.getString("url");
            }
            if (data_object.has("content")) {
                content = data_object.getString("content");
            }
            Text = data_object.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ShareCallApi(context, data_object);
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, Text);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // image link
//        shareIntent.putExtra(Intent.EXTRA_TEXT, image_link);
        if(data_object.has("BudzCome")){
            context.startActivity(Intent.createChooser(shareIntent, "Select App to Share Healing Budz Content"));
        }else {
            if (url.length() > 0 && content.length() > 0) {
                SweetAlertDialog abc = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText("Share In The Buzz")
                        .setContentText(Html.fromHtml(content).toString())
                        .setConfirmText("Share In App")

                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                ShareCallWithInApi(context, data_object);
                            }
                        })
                        .setCancelText("Social Share")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();
                                context.startActivity(Intent.createChooser(shareIntent, "Select App to Share Healing Budz Content"));
                            }
                        });
                abc.setCanceledOnTouchOutside(true);
                abc.show();

            } else {
                context.startActivity(Intent.createChooser(shareIntent, "Select App to Share Healing Budz Content"));
            }
        }

    }

//    public static void ShareHBContent(Activity context, JSONObject data_object) {
//        String Text = null;
//        try {
//            Text = data_object.getString("msg");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        ShareCallApi(context, data_object);
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, Text);
//        shareIntent.setType("text/plain");
//        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//        context.startActivity(Intent.createChooser(shareIntent, "Select App to Share Healing Budz Content"));
//    }

    public static void ShareCallApi(Context context, JSONObject object) {
        new VollyAPICall(context, false, URL.save_share, object, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
            @Override
            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                if (apiActions == save_share) {
                    Log.d("onRequestSuccess: ", response);
                }
            }

            @Override
            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                if (apiActions == save_share) {
                    Log.d("onRequestSuccess: ", response);
                }
            }
        }, save_share);

    }

    public static void ShareCallWithInApi(final Context context, JSONObject object) {
        new VollyAPICall(context, true, URL.share_post_in_app, object, user.getSession_key(), Request.Method.POST, new APIResponseListner() {
            @Override
            public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
                if (apiActions == save_share) {
                    Log.d("onRequestSuccess: ", response);
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Shared Successfully")
                            .setContentText("")
                            .setConfirmText("Okay")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();

                                }
                            }).show();
                }
            }

            @Override
            public void onRequestError(String response, APIActions.ApiActions apiActions) {
                if (apiActions == save_share) {
                    Log.d("onRequestSuccess: ", response);
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Failed!")
                            .setContentText("")
                            .setConfirmText("Okay")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();

                                }
                            }).show();
                }
            }
        }, save_share);

    }

    @Override
    public void onRequestSuccess(String response, APIActions.ApiActions apiActions) {
        if (apiActions == save_share) {
            Log.d("onRequestSuccess: ", response);
        }


    }

    @Override
    public void onRequestError(String response, APIActions.ApiActions apiActions) {
        if (apiActions == save_share) {
            Log.d("onRequestSuccess: ", response);
        }
    }
}
