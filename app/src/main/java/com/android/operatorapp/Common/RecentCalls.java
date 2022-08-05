package com.android.operatorapp.Common;

import android.content.Context;
import android.database.Cursor;

import com.android.operatorapp.DataBase.CallDB;
import com.android.operatorapp.Model.CallItem;

import java.util.ArrayList;
import java.util.Optional;

public class RecentCalls {
    public static ArrayList<CallItem> RECENT_CALLS=new ArrayList<>();

    public static CallItem findByPhoneNumber(String phoneNumber,Context context){
        CallItem find=null;
        CallDB callDB=new CallDB(context);
        Cursor cursor=callDB.getByPhoneNumber(phoneNumber+"");
        if(cursor.getCount()==0){
            return null;
        } else {
            while (cursor.moveToNext()){
                find=new CallItem(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        Integer.parseInt(cursor.getString(8)),
                        cursor.getString(7));
            }
        }
        return find;
    }

    public static CallItem findByState(int state, Context context){
        CallItem find=null;
        CallDB callDB=new CallDB(context);
        Cursor cursor=callDB.getByPhoneState(state+"");
        if(cursor.getCount()==0){
            return null;
        } else {
            while (cursor.moveToNext()){
                find=new CallItem(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        Integer.parseInt(cursor.getString(8)),
                        cursor.getString(7));
            }
        }
        return find;
    }


}
