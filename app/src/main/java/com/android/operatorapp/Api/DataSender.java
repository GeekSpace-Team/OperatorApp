package com.android.operatorapp.Api;

import android.content.Context;
import android.widget.Toast;

import com.android.operatorapp.Common.Utils;
import com.android.operatorapp.DataBase.CallDB;
import com.android.operatorapp.Model.CallItem;
import com.android.operatorapp.Model.GBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSender {
    public static void sendData(CallItem item, Context context) {
        if (Utils.getSharedPreference(context, "active").equals("1")) {
            String ip = Utils.getSharedPreference(context, "ip");
            String host = Utils.urlGenerator(ip);
            ApiInterface apiInterface = APIClient.getClient(host).create(ApiInterface.class);
            Call<GBody<String>> call = apiInterface.showCall(item, "Bearer " + Utils.getSharedPreference(context, "token"));
            call.enqueue(new Callback<GBody<String>>() {
                @Override
                public void onResponse(Call<GBody<String>> call, Response<GBody<String>> response) {
                    if (response.isSuccessful()) {
//                        if(item.getState()==-2){
//                            CallDB callDB=new CallDB(context);
//                            callDB.insert(item);
//                        }
//                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(context, "" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GBody<String>> call, Throwable t) {
//                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
