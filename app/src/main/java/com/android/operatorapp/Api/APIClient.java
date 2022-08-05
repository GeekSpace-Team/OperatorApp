package com.android.operatorapp.Api;



import com.android.operatorapp.Common.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200,TimeUnit.SECONDS)
            .connectTimeout(200, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClient(String url) {
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }
}