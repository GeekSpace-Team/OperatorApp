package com.android.operatorapp.Service;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.CallLog;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.operatorapp.Api.DataSender;
import com.android.operatorapp.Common.RecentCalls;
import com.android.operatorapp.Common.Utils;
import com.android.operatorapp.DataBase.CallDB;
import com.android.operatorapp.MainActivity;
import com.android.operatorapp.Model.CallItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class CallScreenReceiver extends CallScreeningService {
    private final String TAG = CallScreenReceiver.class.getSimpleName();
    private TelephonyManager mTelephonyManager;
    TelephonyManager telephony;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onScreenCall(@NonNull Call.Details details) {

        if (!Utils.getSharedPreference(this, "active").equals("1")) {
            return;
        }

        CallDB callDB = new CallDB(this);

        CallChecker.check(this);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        CallResponse.Builder response = new CallResponse.Builder();
        Log.i("CallScreeningService", getNumberFromDetails(details));
        String callDirection = "";

        if (details.getCallDirection() == Call.Details.DIRECTION_OUTGOING) {
            Log.e(TAG, "OUTGOING CALL");
            CallItem callItem = new CallItem(
                    getNumberFromDetails(details),
                    getNumberFromDetails(details),
                    Call.Details.DIRECTION_OUTGOING + "",
                    formattedDate,
                    currentTime,
                    "0",
                    -2,
                    Utils.generateString());
            callDB.insert(callItem);
            callDirection = "Çykyş jaňy";
            DataSender.sendData(callItem, this);
        } else if (details.getCallDirection() == Call.Details.DIRECTION_INCOMING) {
            Log.e(TAG, "INCOMING CALL");
            CallItem callItem = new CallItem(
                    getNumberFromDetails(details),
                    getNumberFromDetails(details),
                    Call.Details.DIRECTION_INCOMING + "",
                    formattedDate,
                    currentTime,
                    "0",
                    -2,
                    Utils.generateString());
            callDB.insert(callItem);
            callDirection = "Giriş jaňy";
            DataSender.sendData(callItem, this);
        }

        try{
            Utils.writeConsole("Jaň(tel: " + getNumberFromDetails(details) + ", görnüşi: " + callDirection + ")");
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void startCallListener() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private String getNumberFromDetails(@NonNull Call.Details details) {
        Uri handle = details.getHandle();
        if (handle == null) {
            Log.e(TAG, "No handle on incoming call");
            return null;
        }

        String scheme = handle.getScheme();
        if (scheme != null && scheme.equals("tel")) {
            return handle.getSchemeSpecificPart();
        }

        Log.e(TAG, "Unhandled scheme");
        return null;
    }


}
