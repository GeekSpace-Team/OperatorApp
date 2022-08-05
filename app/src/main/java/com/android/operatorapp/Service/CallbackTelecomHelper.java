package com.android.operatorapp.Service;

import android.content.Context;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;

public class CallbackTelecomHelper extends Call.Callback {

    String TAG = "CallbackTelecomHelper_TAG";

    private Context context;

    public CallbackTelecomHelper(Context context) {
        this.context = context;
    }

    @Override
    public void onStateChanged(Call call, int state) {
        super.onStateChanged(call, state);

        Log.i(TAG, "onStateChanged");
    }

    @Override
    public void onDetailsChanged(Call call, Call.Details details) {
        super.onDetailsChanged(call, details);


        Log.i(TAG, "onDetailsChanged");
    }

    @Override
    public void onCallDestroyed(Call call) {
        super.onCallDestroyed(call);

        Log.i(TAG, "onCallDestroyed");
    }

    @Override
    public void onConnectionEvent(Call call, String event, Bundle extras) {
        super.onConnectionEvent(call, event, extras);


        Log.i(TAG, "onConnectionEvent");
    }

    @Override
    public void onRttRequest(Call call, int id) {
        super.onRttRequest(call, id);


        Log.i(TAG, "onRttRequest");
    }
}
