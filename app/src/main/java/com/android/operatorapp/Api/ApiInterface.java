package com.android.operatorapp.Api;


import com.android.operatorapp.Model.CallItem;
import com.android.operatorapp.Model.GBody;
import com.android.operatorapp.Model.LoginBody;
import com.android.operatorapp.Model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("/api/operator/test-connection")
    Call<GBody<String>> testConnection();

    @POST("/api/operator/show-call")
    Call<GBody<String>> showCall(@Body CallItem body,@Header("Authorization") String token);

    @POST("/api/operator/auth/sign-in")
    Call<GBody<LoginResponse>> signIn(@Body LoginBody body);

}
