package com.android.operatorapp.Service;

import static android.provider.CallLog.Calls.LIMIT_PARAM_KEY;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.android.operatorapp.Api.DataSender;
import com.android.operatorapp.CallTracker.CallLogModel;
import com.android.operatorapp.CallTracker.MainActivity;
import com.android.operatorapp.Common.RecentCalls;
import com.android.operatorapp.Common.Utils;
import com.android.operatorapp.DataBase.CallDB;
import com.android.operatorapp.Model.CallItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executor;

public class CallReceiver extends BroadcastReceiver {
    private static final String TAG = CallReceiver.class.getSimpleName();



    @Override
    public void onReceive(Context context, Intent intent) {

        if (!Utils.getSharedPreference(context, "active").equals("1")) {
            return;
        }

        String intentAction = intent.getAction();
        if (intentAction == null || !intentAction.equals("android.intent.action.PHONE_STATE")) {
            return;
        }

        CallChecker.check(context);
        String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        String number2=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.e("State",state+" / "+number+" / "+number2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            registerCustomTelephonyCallback(context,"");
        } else {
//            insertCall(intent,context);
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.listen(new MyPhoneListener(context,""), PhoneStateListener.LISTEN_CALL_STATE);
        }

    }

    private void insertCall(Intent intent, Context context) {

        CallDB callDB = new CallDB(context);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String callDirection = "";

        String number=intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

        String state=intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if(number==null)
        {
            number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            //Outgoing call
            CallItem callItem = new CallItem(
                    number="",
                    number="",
                    1 + "",
                    formattedDate,
                    currentTime,
                    "0",
                    -2,
                    Utils.generateString());
            callDB.insert(callItem);
            callDirection = "Çykyş jaňy";
            DataSender.sendData(callItem, context);
        }
        else if (state.equals("RINGING"))
        {
            //Incoming call
            CallItem callItem = new CallItem(
                    number="",
                    number+"",
                    0 + "",
                    formattedDate,
                    currentTime,
                    "0",
                    -2,
                    Utils.generateString());
            callDB.insert(callItem);
            callDirection = "Giriş jaňy";
            DataSender.sendData(callItem, context);
        }

        try{
            Utils.writeConsole("Jaň(tel: " + number + ", görnüşi: " + callDirection + ")");
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    public static class CustomTelephonyCallback extends TelephonyCallback implements TelephonyCallback.CallStateListener {
        private CallBack mCallBack;

        public CustomTelephonyCallback(CallBack callBack) {
            mCallBack = callBack;
        }

        @Override
        public void onCallStateChanged(int state) {
            mCallBack.callStateChanged(state);
        }


    }

    @RequiresApi(Build.VERSION_CODES.S)
    public void registerCustomTelephonyCallback(Context context,String phoneNumber) {
        TelephonyManager telephony = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        try{
            telephony.registerTelephonyCallback(context.getMainExecutor(), new CustomTelephonyCallback(new CallBack() {
                @Override
                public void callStateChanged(int state) {
                    if(state==TelephonyManager.CALL_STATE_RINGING){
                        CallItem callItem= RecentCalls.findByState(-2,context);
                        if(callItem!=null) {
//                            Toast.makeText(context, ""+callItem.getPhNumber(), Toast.LENGTH_SHORT).show();
                            callItem.setState(TelephonyManager.CALL_STATE_RINGING);
                            CallDB callDB=new CallDB(context);
                            callDB.updateData(callItem,callItem.getUniqueId());
                            DataSender.sendData(callItem,context);
                            Utils.writeConsole("Jaň(tel: "+callItem.getPhNumber()+", görnüşi: ringing)");

                        }
                    }

                    if(state==TelephonyManager.CALL_STATE_OFFHOOK){
                        CallItem callItem= RecentCalls.findByState(-2,context);
                        if(callItem==null)
                            callItem=RecentCalls.findByState(TelephonyManager.CALL_STATE_RINGING,context);
                        if(callItem!=null) {
//                            Toast.makeText(context, ""+callItem.getPhNumber(), Toast.LENGTH_SHORT).show();
                            callItem.setState(TelephonyManager.CALL_STATE_OFFHOOK);
                            CallDB callDB=new CallDB(context);
                            callDB.updateData(callItem,callItem.getUniqueId());
                            DataSender.sendData(callItem,context);
                            Utils.writeConsole("Jaň(tel: "+callItem.getPhNumber()+", görnüşi: OFFHOOK)");
                        }
                    }

                    if(state==TelephonyManager.CALL_STATE_IDLE){
                        CallItem callItem= RecentCalls.findByState(TelephonyManager.CALL_STATE_RINGING,context);
                        if(callItem==null)
                            callItem=RecentCalls.findByState(TelephonyManager.CALL_STATE_OFFHOOK,context);
                        if(callItem!=null) {
                            callItem.setState(TelephonyManager.CALL_STATE_IDLE);
                            CallDB callDB=new CallDB(context);
                            callDB.deleteData(callItem.getPhNumber());


                            CallItem finalCallItem = callItem;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<CallItem> callItems=getCallLogByPhoneNumber(context,finalCallItem.getPhNumber());
                                    String duration="0";
                                    if(callItems.size()>0){
                                        duration=callItems.get(0).getCallDuration();
                                    }
                                    finalCallItem.setCallDuration(duration);
                                    finalCallItem.setState(TelephonyManager.CALL_STATE_IDLE);
                                    DataSender.sendData(finalCallItem,context);
                                    Utils.writeConsole("Jaň(tel: "+ finalCallItem.getPhNumber()+", görnüşi: IDLE, duration:"+ duration +")");
                                }
                            },5000);
                        }
                    }
                }
            }));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    interface CallBack {
        void callStateChanged(int state);
    }


    public static class MyPhoneListener extends PhoneStateListener {
        private Context context;
        private String phoneNumber;

        public MyPhoneListener(Context context, String phoneNumber) {
            this.context = context;
            this.phoneNumber = phoneNumber;
        }


        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            super.onCallStateChanged(state, phoneNumber);
            if(state==TelephonyManager.CALL_STATE_RINGING){
                CallItem callItem= RecentCalls.findByState(-2,context);
                if(callItem!=null) {
                    callItem.setState(TelephonyManager.CALL_STATE_RINGING);
                    CallDB callDB=new CallDB(context);
                    callDB.updateData(callItem,callItem.getUniqueId());
                    Utils.writeConsole("Jaň(tel: "+callItem.getPhNumber()+", görnüşi: ringing)");
                }
            }

            if(state==TelephonyManager.CALL_STATE_OFFHOOK){
                CallItem callItem= RecentCalls.findByState(-2,context);
                if(callItem==null)
                    callItem=RecentCalls.findByState(TelephonyManager.CALL_STATE_RINGING,context);
                if(callItem!=null) {
                    callItem.setState(TelephonyManager.CALL_STATE_OFFHOOK);
                    CallDB callDB=new CallDB(context);
                    callDB.updateData(callItem,callItem.getUniqueId());
                    Utils.writeConsole("Jaň(tel: "+callItem.getPhNumber()+", görnüşi: OFFHOOK)");
                }
            }

            if(state==TelephonyManager.CALL_STATE_IDLE){
                CallItem callItem= RecentCalls.findByState(TelephonyManager.CALL_STATE_RINGING,context);

                if(callItem==null)
                    callItem=RecentCalls.findByState(TelephonyManager.CALL_STATE_OFFHOOK,context);
                if(callItem!=null) {
                    callItem.setState(TelephonyManager.CALL_STATE_IDLE);
                    CallDB callDB=new CallDB(context);
                    callDB.deleteData(callItem.getPhNumber());

                    CallItem finalCallItem = callItem;
                    new Handler().postDelayed(() -> {
                        ArrayList<CallItem> callItems=getCallLogByPhoneNumber(context,finalCallItem.getPhNumber());
                        String duration="0";
                        if(callItems.size()>0){
                            duration=callItems.get(0).getCallDuration();
                        }
                        finalCallItem.setCallDuration(duration);
                        finalCallItem.setState(TelephonyManager.CALL_STATE_IDLE);
                        DataSender.sendData(finalCallItem,context);
                        Utils.writeConsole("Jaň(tel: "+ finalCallItem.getPhNumber()+", görnüşi: IDLE, duration:"+ duration +")");
                    },5000);
                }
            }
        }
    }


    public static ArrayList<CallItem> getCallLogByPhoneNumber(Context context, String phoneNumber) {
        String str_number, str_contact_name, str_call_type, str_call_full_date,
                str_call_date, str_call_time, str_call_time_formatted, str_call_duration;
        ArrayList<CallItem> callLogModelArrayList = new ArrayList<>();
        // reading all data in descending order according to DATE
        String sortOrder = CallLog.Calls.DATE + " DESC";

        String SELECTION = CallLog.Calls.NUMBER+" = '"+phoneNumber+"' ";

        Cursor cursor = context.getContentResolver().query(
                CallLog.Calls.CONTENT_URI.buildUpon().appendQueryParameter(LIMIT_PARAM_KEY, "1")
                        .build(),
                null,
                SELECTION,
                null,
                sortOrder);

        //clearing the arraylist
        callLogModelArrayList.clear();

        //looping through the cursor to add data into arraylist
        while (cursor.moveToNext()) {
            str_number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            str_contact_name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            str_contact_name = str_contact_name == null || str_contact_name.equals("") ? "Unknown" : str_contact_name;
            str_call_type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
            str_call_full_date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
            str_call_duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));

            SimpleDateFormat dateFormatter = new SimpleDateFormat(
                    "dd MMM yyyy");
            str_call_date = dateFormatter.format(new Date(Long.parseLong(str_call_full_date)));

            SimpleDateFormat timeFormatter = new SimpleDateFormat(
                    "HH:mm:ss");
            str_call_time = timeFormatter.format(new Date(Long.parseLong(str_call_full_date)));

            //str_call_time = getFormatedDateTime(str_call_time, "HH:mm:ss", "hh:mm ss");

//            str_call_duration = DurationFormat(str_call_duration);

            CallItem callLogItem = new CallItem(str_number, str_contact_name, str_call_type,
                    str_call_date, str_call_time, str_call_duration,Integer.parseInt(str_call_type),Utils.generateString());

            switch (Integer.parseInt(str_call_type)) {
                case CallLog.Calls.INCOMING_TYPE:
                    str_call_type = "Incoming";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    str_call_type = "Outgoing";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    str_call_type = "Missed";
                    break;
                case CallLog.Calls.VOICEMAIL_TYPE:
                    str_call_type = "Voicemail";
                    break;
                case CallLog.Calls.REJECTED_TYPE:
                    str_call_type = "Rejected";
                    break;
                case CallLog.Calls.BLOCKED_TYPE:
                    str_call_type = "Blocked";
                    break;
                case CallLog.Calls.ANSWERED_EXTERNALLY_TYPE:
                    str_call_type = "Externally Answered";
                    break;
                default:
                    str_call_type = "NA";
            }



            callLogModelArrayList.add(callLogItem);

        }

        return callLogModelArrayList;
    }

    private static String DurationFormat(String duration) {
        String durationFormatted = null;
        if (Integer.parseInt(duration) < 60) {
            durationFormatted = duration + " sec";
        } else {
            int min = Integer.parseInt(duration) / 60;
            int sec = Integer.parseInt(duration) % 60;

            if (sec == 0)
                durationFormatted = min + " min";
            else
                durationFormatted = min + " min," + sec + " sec";

        }
        return durationFormatted;
    }

    private void SendDataToServer(CallLogModel callLogItem) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("CallLog")
//                .child(getDeviceName())
//                .child(callLogItem.getCallDate())
//                .child(callLogItem.getCallTime());
//        myRef.setValue(callLogItem);
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


}
