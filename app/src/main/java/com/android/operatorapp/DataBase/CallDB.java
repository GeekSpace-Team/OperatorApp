package com.android.operatorapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.android.operatorapp.Model.CallItem;

public class CallDB extends SQLiteOpenHelper {
    private static final String DBNAME="callHistory";
    private static final String TBNAME="callHistory";
    public CallDB(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TBNAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "phone TEXT," +
                "contactName TEXT," +
                "callType TEXT," +
                "callDate TEXT," +
                "callTime TEXT," +
                "callDuration TEXT," +
                "uniqueId TEXT," +
                "state TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
        onCreate(db);
    }

    public Cursor getAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT * FROM "+TBNAME+" ORDER BY ID DESC",null);
        return cursor;
    }

    public Cursor getByPhoneNumber(String phoneNumber){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TBNAME+" WHERE phone = '"+phoneNumber+"' ORDER BY ID DESC LIMIT 1",null);
    }

    public Cursor getByPhoneState(String state){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TBNAME+" WHERE state = '"+state+"' ORDER BY ID DESC LIMIT 1",null);
    }

    public Cursor getByUniqueId(String uniqueId){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+TBNAME+" WHERE uniqueId = '"+uniqueId+"' ORDER BY ID DESC",null);
    }

    public boolean insert(CallItem item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("phone",item.getPhNumber());
        contentValues.put("contactName",item.getContactName());
        contentValues.put("callType",item.getCallType());
        contentValues.put("callDate",item.getCallDate());
        contentValues.put("callTime",item.getCallTime());
        contentValues.put("callDuration",item.getCallDuration());
        contentValues.put("uniqueId",item.getUniqueId());
        contentValues.put("state",item.getState());
        long result=db.insert(TBNAME,null,contentValues);
        if(result==-1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateData(CallItem item,String uniqueId){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("phone",item.getPhNumber());
        contentValues.put("contactName",item.getContactName());
        contentValues.put("callType",item.getCallType());
        contentValues.put("callDate",item.getCallDate());
        contentValues.put("callTime",item.getCallTime());
        contentValues.put("callDuration",item.getCallDuration());
        contentValues.put("uniqueId",item.getUniqueId());
        contentValues.put("state",item.getState());
        db.update(TBNAME,contentValues,"uniqueId=?",new String[]{uniqueId});
        return true;
    }

    public Integer deleteData(String phoneNumber){
        SQLiteDatabase db=this.getWritableDatabase();
        return db.delete(TBNAME,"phone=?",new String[]{phoneNumber});
    }

    public void truncate(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS "+TBNAME);
        onCreate(db);
    }


}
