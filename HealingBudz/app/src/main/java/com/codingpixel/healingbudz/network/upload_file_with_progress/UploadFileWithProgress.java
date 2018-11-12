package com.codingpixel.healingbudz.network.upload_file_with_progress;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.codingpixel.healingbudz.customeUI.ProgressDialog;
import com.codingpixel.healingbudz.data_structure.APIActions;
import com.codingpixel.healingbudz.interfaces.FileUploadProgressListner;
import com.codingpixel.healingbudz.network.model.APIResponseListner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import static com.codingpixel.healingbudz.Utilities.Constants.appKey;
import static com.codingpixel.healingbudz.activity.splash.Splash.user;

public class UploadFileWithProgress extends AsyncTask<String, Integer, String> {
    private String Api_url;
    private String File_path;
    private String File_Type;
    private long totalSize = 0;
    private final ProgressDialog pd;
    private FileUploadProgressListner progressListener;
    private APIResponseListner apiResponseListner;
    private JSONArray parameters;
    private JSONArray parameters_key;
    private Context context;
    private Boolean isLoading;
    private APIActions.ApiActions apiActions;

    public UploadFileWithProgress(Context context, Boolean isLoading, String api_url, String file_path, String file_type, JSONArray parameters, JSONArray parameters_key, FileUploadProgressListner progressListener, APIResponseListner apiResponseListner, APIActions.ApiActions apiActions) {
        this.Api_url = api_url;
        this.File_path = file_path;
        this.File_Type = file_type;
        this.progressListener = progressListener;
        this.apiResponseListner = apiResponseListner;
        this.parameters = parameters;
        this.parameters_key = parameters_key;
        pd = ProgressDialog.newInstance();
        this.apiActions = apiActions;
        this.context = context;
        this.isLoading = isLoading;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isLoading) {
            pd.show(((FragmentActivity) context).getSupportFragmentManager(), "pd");
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        if (progressListener != null) {
            progressListener.UploadedProgress(progress[0]);
        }
    }

    @Override
    protected String doInBackground(String... params) {
        return uploadFile(File_path);
    }

    @SuppressWarnings("deprecation")
    private String uploadFile(String path) {
        String responseString = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(Api_url);
            postRequest.setHeader("session_token", user.getSession_key());
            postRequest.setHeader("app_key", appKey);
            File file = new File(path);
            AndroidMultiPartEntity reqEntity = new AndroidMultiPartEntity(
                    new AndroidMultiPartEntity.ProgressListener() {
                        @Override
                        public void transferred(long num) {
                            publishProgress(((int) ((num / (float) totalSize) * 100)));
                        }
                    });
            if (File_Type.equalsIgnoreCase("image")) {
                String mime = getMimeType(file);
                ContentType contentType = ContentType.create(mime);
                FileBody cbFile = new FileBody(file, contentType.getMimeType(), file.getName());
                reqEntity.addPart("image", cbFile);
            } else if (File_Type.equalsIgnoreCase("video")) {
                ContentType contentType = ContentType.create("video/mp4");
                FileBody cbFile = new FileBody(file, contentType.getMimeType(), file.getName());
                reqEntity.addPart("video", cbFile);
            }
            for (int x = 0; x < parameters.length(); x++) {
                reqEntity.addPart(parameters_key.getString(x), new StringBody(parameters.getString(x)));
            }
            totalSize = reqEntity.getContentLength();
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            int code = response.getStatusLine().getStatusCode();
            String sResponse;
            StringBuilder s = new StringBuilder();
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            reader.close();
            System.out.println("Response: " + s);
            System.out.println("CODE: " + "" + code);
            return "" + s;

        } catch (Exception e) {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        try {
            pd.dismiss();
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.getString("status").equalsIgnoreCase("success")) {

                apiResponseListner.onRequestSuccess(result, apiActions);
            } else {
                apiResponseListner.onRequestError(result, apiActions);
            }

        } catch (IllegalArgumentException | JSONException e) {
            e.printStackTrace();

        }
    }

    @NonNull
    static String getMimeType(@NonNull File file) {
        String type = null;
        final String url = file.toString();
        final String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        if (type == null) {
            type = "image/*"; // fallback type. You might set it to */*
        }
        return type;
    }

}