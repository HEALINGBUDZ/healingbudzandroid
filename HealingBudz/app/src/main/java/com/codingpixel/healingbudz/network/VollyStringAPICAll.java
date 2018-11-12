package com.codingpixel.healingbudz.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.network.model.APIResponseListner;
import com.codingpixel.healingbudz.network.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VollyStringAPICAll {
    public VollyStringAPICAll(Context context , boolean isLoading,String  url,final String  token, JSONObject jsonBody , int method , final APIResponseListner apiResponseListner){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        final ProgressDialog pd = ProgressDialog.newInstance();
        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(method, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("VOLLEY", response+"");
                Response resp = new Response();
                resp.message = response;
                    apiResponseListner.onRequestSuccess(resp.message , APIActions.ApiActions.testAPI);

                pd.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Response resp = new Response();
                    if(error != null && error.networkResponse != null ){
                        if(error.networkResponse.data != null){
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            resp.message = jsonObject.toString();
                        }else {
                            resp.message = "{internal server error}";
                        }
                    }else {
                        assert error != null;
                        if(error.getMessage() != null){
                            resp.message = error.getMessage() ;
                        }else {
                            resp.message = "{internal server error}";
                        }

                    }

                    apiResponseListner.onRequestError(resp.message , APIActions.ApiActions.testAPI);
                        pd.dismiss();
                } catch ( JSONException | UnsupportedEncodingException e ) {
                        pd.dismiss();
                    Response resp = new Response();
                    resp.message = "Error";
                    apiResponseListner.onRequestError(resp.message , APIActions.ApiActions.testAPI);
                    //Handle a malformed json response
                }
            }
        }){
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
                HashMap<String , String > param = new HashMap<>();
                if(token != null){
                    param.put("Authorization" ,"bearer "+token);
                }
                return param;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("VOLLEY", response+"");
                Response resp = new Response();
                resp.message = response.toString();
                apiResponseListner.onRequestSuccess(resp.message , APIActions.ApiActions.testAPI);
                pd.dismiss();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    Response resp = new Response();
                    if(error != null && error.networkResponse != null ){
                        if(error.networkResponse.data != null){
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            resp.message = jsonObject.toString();
                        }else {
                            resp.message = "{internal server error}";
                        }
                    }else {
                        assert error != null;
                        if(error.getMessage() != null){
                            resp.message = error.getMessage() ;
                        }else {
                            resp.message = "{internal server error}";
                        }

                    }

                    apiResponseListner.onRequestError(resp.message , APIActions.ApiActions.testAPI);
                        pd.dismiss();
                } catch ( JSONException | UnsupportedEncodingException e ) {
                        pd.dismiss();
                    Response resp = new Response();
                    resp.message = "Error";
                    apiResponseListner.onRequestError(resp.message , APIActions.ApiActions.testAPI);
                    //Handle a malformed json response
                }
            }
        }){

        };
        requestQueue.add(jsonObjectRequest);
    }

}
