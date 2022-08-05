package com.android.operatorapp.Service;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.telecom.InCallService;
import android.util.Log;

public class MyCallService extends InCallService {
    public static final String TAG = "MyService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Log.d(TAG, "On Start");

        return START_STICKY;
    }




    @Override
    public void onCallAdded(Call call) {
        super.onCallAdded(call);


        call.registerCallback(new CallbackTelecomHelper(this));

        Log.d(TAG, "onCallAdded");
        Log.d(TAG, "onCallAdded details" + call.getDetails());
    }


    @Override
    public void onCallRemoved(Call call) {
        super.onCallRemoved(call);
        Log.d(TAG, "onCallRemoved");
        Log.d(TAG, "onCallRemoved details" + call.getDetails());
    }

    @Override
    public void onConnectionEvent(Call call, String event, Bundle extras) {
        super.onConnectionEvent(call, event, extras);

        Log.d(TAG, "getDisconnect code: " + call.getDetails().getDisconnectCause().getCode());
        Log.d(TAG, "getDisconnect reason: " + call.getDetails().getDisconnectCause().getReason());
        Log.d(TAG, "getDisconnect description: " + call.getDetails().getDisconnectCause().getDescription());
        Log.d(TAG, "event : " + event);

    }
}
