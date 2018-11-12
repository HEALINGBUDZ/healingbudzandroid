package com.codingpixel.healingbudz.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codingpixel.healingbudz.activity.Registration.LoginEntrance;
import com.codingpixel.healingbudz.customeUI.CustomeToast;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.codingpixel.healingbudz.Utilities.Constants.appKey;
import static com.codingpixel.healingbudz.Utilities.FileUtils.haveNetworkConnection;
import static com.codingpixel.healingbudz.Utilities.SetUserValuesInSP.delete_UserValues;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.ClearSharedPrefrences;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setBool;
import static com.codingpixel.healingbudz.sharedprefrences.SharedPrefrences.setString;


public class VollyAPICall {
    public VollyAPICall(final Context context, final boolean isLoading, String url, final JSONObject jsonBody, final String token, int method, final APIResponseListner apiResponseListner, final APIActions.ApiActions apiActions) {
        if (context != null) {
            if (!haveNetworkConnection(context)) {
                CustomeToast.ShowCustomToast(context, "Network not Available!", Gravity.TOP);

            } else {
                if (context != null) {
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    final ProgressDialog pd = ProgressDialog.newInstance();
                    if (isLoading) {
                        try {
                            pd.show(((FragmentActivity) context).getSupportFragmentManager(), "pd");
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                    final String requestBody = jsonBody.toString();
                    Log.d("VollyAPICall: ", url);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("VOLLEY", response + "");

                            apiResponseListner.onRequestSuccess(response.toString(), apiActions);

                            if (isLoading) {
                                try {
                                    pd.dismiss();
                                } catch (IllegalStateException | NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            try {
                                String sesClear = "";
                                String resp = "";
                                if (error != null && error.networkResponse != null) {
                                    if (error.networkResponse.data != null) {
                                        String responseBody = new String(error.networkResponse.data, "utf-8");
                                        JSONObject jsonObject = new JSONObject(responseBody);
                                        if (jsonObject.getString("errorMessage").length() > 0) {

                                            sesClear = jsonObject.getString("errorMessage");

                                        }
                                        resp = jsonObject.toString();
                                    } else {
                                        resp = "{internal server error}";
                                    }
                                } else {
                                    assert error != null;
                                    if (error.getMessage() != null) {
                                        resp = error.getMessage();
                                    } else {
                                        resp = "{internal server error}";
                                    }
                                }
                                if (sesClear.equalsIgnoreCase("Session Expired")) {
                                    if (isLoading) {
                                        try {
                                            pd.dismiss();
                                        } catch (IllegalStateException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    ClearSharedPrefrences(context);
//                                    final ProgressDialog pd = ProgressDialog.newInstance();
//                                    try {
//                                        pd.show(((FragmentActivity) context).getSupportFragmentManager(), "pd");
//                                    } catch (IllegalStateException e) {
//                                        e.printStackTrace();
//                                    }
                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {
                                            try {
//                                                if (isLoading)
//                                                    pd.dismiss();
                                            } catch (IllegalStateException e) {
                                                e.printStackTrace();
                                            }
                                            Collection<String> keys = new ArrayList<>();
                                            keys.add("user_id");
                                            keys.add("device_type");
                                            OneSignal.deleteTags(keys);
                                            setString("user_email", "", context);
                                            setString("user_password", "", context);
                                            setBool("is_user_login", false, context);
                                            delete_UserValues(context);
                                            Intent i = new Intent(context, LoginEntrance.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            context.startActivity(i);
                                            if (context instanceof com.codingpixel.healingbudz.onesignal.OnSignalNotificationExtenderService) {

                                            } else
                                                ((Activity) context).finish();
                                        }
                                    }, 1000);

                                } else {
                                    apiResponseListner.onRequestError(resp, apiActions);
                                    if (isLoading) {
                                        try {
                                            pd.dismiss();
                                        } catch (IllegalStateException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (JSONException | UnsupportedEncodingException e) {
                                if (isLoading) {
                                    try {
                                        pd.dismiss();
                                    } catch (IllegalStateException errorPrint) {
                                        errorPrint.printStackTrace();
                                    }
                                }
                                apiResponseListner.onRequestError(error.networkResponse.statusCode + "", apiActions);
                                //Handle a malformed json response
                            }
                        }
                    }) {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> param = new HashMap<>();
                            param.put("app_key", appKey);
                            if (token != null) {
                                param.put("session_token", token);
                            }
                            return param;
                        }

                        @Override
                        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                            try {
                                String jsonString = new String(response.data,
                                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                                // Check if it is JSONObject or JSONArray
                                Object json = new JSONTokener(jsonString).nextValue();
                                JSONObject jsonObject = new JSONObject();
                                if (json instanceof JSONObject) {
                                    jsonObject = (JSONObject) json;
                                } else if (json instanceof JSONArray) {
                                    jsonObject.put("success", json);
                                } else {
                                    String message = "{\"error\":\"Unknown Error\",\"code\":\"failed\"}";
                                    jsonObject = new JSONObject(message);
                                }
                                return Response.success(jsonObject,
                                        HttpHeaderParser.parseCacheHeaders(response));
                            } catch (UnsupportedEncodingException e) {
                                return Response.error(new ParseError(e));
                            } catch (JSONException e) {
                                return Response.error(new ParseError(e));
                            }
                        }

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            return super.getParams();
                        }
                    };

                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                            50000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    requestQueue.add(jsonObjectRequest);
                }
            }
        }
    }
}
