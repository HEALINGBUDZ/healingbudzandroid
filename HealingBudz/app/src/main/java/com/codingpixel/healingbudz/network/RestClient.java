package com.codingpixel.healingbudz.network;

import android.content.Context;

import com.codingpixel.healingbudz.network.model.URL;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by M_Muzammil Sharif on 23-Jan-17.
 */

public class RestClient {
    private static RestClient instance = null;
    private ApiServices service;

    public RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.
                        create()).baseUrl(URL.baseurl).
                        client(new OkHttpClient.Builder()
                                .readTimeout(60, TimeUnit.SECONDS)
                                .connectTimeout(60, TimeUnit.SECONDS)
                                .build()).
                        build();
        service = retrofit.create(ApiServices.class);
    }

    /**
     * @param context context is added because if server get secure by ssl
     *                certificate then for ssl secure request we need context
     */
    public static RestClient getInstance(Context context) {
        /*context is never use if api server is not ssl secure*/
        if (instance == null) {
            instance = new RestClient();
        }
        return instance;
    }

    public ApiServices getApiService() {
        return service;
    }
}
