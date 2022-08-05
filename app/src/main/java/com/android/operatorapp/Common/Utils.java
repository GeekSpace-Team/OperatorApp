package com.android.operatorapp.Common;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.util.Log;

import com.android.operatorapp.MainActivity;

import java.util.Date;
import java.util.UUID;

public class Utils {
    public static void setPreference(String name, String value, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(name, MODE_PRIVATE).edit();
        editor.putString(name, value);
        editor.apply();
    }

    public static String getSharedPreference(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(name, MODE_PRIVATE);
        String value = prefs.getString(name, "");
        return value;
    }

    public static String urlGenerator(String ip){
        return "http://"+ip+":6415";
    }

    public static void writeConsole(String message){
        MainActivity.get().getConsoleView().w(message);
    }

    public static String generateString() {
        String uuid = UUID.randomUUID().toString()+"-"+UUID.randomUUID().toString();
        return "uuid = " + uuid;
    }
}
