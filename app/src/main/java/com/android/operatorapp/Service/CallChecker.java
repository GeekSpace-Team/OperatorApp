package com.android.operatorapp.Service;

import android.content.Context;
import android.database.Cursor;

import com.android.operatorapp.DataBase.CallDB;

public class CallChecker {
    public static void check(Context context){
        CallDB callDB = new CallDB(context);
        Cursor cursor=callDB.getAll();
        if(cursor.getCount()>2){
            String[] args=new String[cursor.getCount()-2];
            int i=0;
            while (cursor.moveToNext()){
                args[i]=cursor.getString(7);
            }
            callDB.deleteIn(args);
        }
    }
}
