package com.android.operatorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.operatorapp.Api.APIClient;
import com.android.operatorapp.Api.ApiInterface;
import com.android.operatorapp.Common.Constant;
import com.android.operatorapp.Common.Utils;
import com.android.operatorapp.Model.GBody;
import com.android.operatorapp.Model.LoginBody;
import com.android.operatorapp.Model.LoginResponse;
import com.android.operatorapp.databinding.ActivityLoginPageBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {
    private ActivityLoginPageBinding binding;
    private Context context=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(!Utils.getSharedPreference(context,"token").trim().isEmpty()){
            finish();
            startActivity(new Intent(context,MainActivity.class));
        }
        setListener();
    }

    private void setListener() {
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog progressDialog=new ProgressDialog(context);
                progressDialog.setTitle("Barlanyar");
                progressDialog.setMessage("Biraz garasyn...");
                progressDialog.show();
                ApiInterface apiInterface= APIClient.getClient(Utils.urlGenerator(Constant.SERVER_URL)).create(ApiInterface.class);
                Call<GBody<LoginResponse>> call=apiInterface.signIn(new LoginBody(
                        binding.username.getText().toString(),
                        binding.password.getText().toString(),
                        Utils.getSharedPreference(context,"notif_token"),
                        "android"));
                call.enqueue(new Callback<GBody<LoginResponse>>() {
                    @Override
                    public void onResponse(Call<GBody<LoginResponse>> call, Response<GBody<LoginResponse>> response) {
                        if(response.isSuccessful() && response.body()!=null && response.body().getBody()!=null){
                            Utils.setPreference("fullName",response.body().getBody().getFullname(),context);
                            Utils.setPreference("user_id",response.body().getBody().getId(),context);
                            Utils.setPreference("phoneNumber",response.body().getBody().getPhone_number(),context);
                            Utils.setPreference("role_name",response.body().getBody().getRole_name(),context);
                            Utils.setPreference("token",response.body().getBody().getToken(),context);
                            Utils.setPreference("unique_id",response.body().getBody().getUnique_id(),context);
                            Utils.setPreference("sell_point_id",response.body().getBody().getSell_point_id(),context);
                            finish();
                            startActivity(new Intent(context,MainActivity.class));
                        } else {
                            Toast.makeText(LoginPage.this, "Error code: "+response.code(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<GBody<LoginResponse>> call, Throwable t) {
                        Toast.makeText(LoginPage.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
}