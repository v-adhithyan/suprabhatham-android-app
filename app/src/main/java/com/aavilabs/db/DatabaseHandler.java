package com.aavilabs.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.TreeMap;

/**
 * Created by adhithyan-3592 on 31/01/16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "suprabhatham";
    public static final String ALARM_TABLE_NAME = "alarms";
    public static final String RECURRING_TABLE = "recurring";

    private static final String ID = "id";
    private static final String TIME = "time";
    private static final String SORT_ORDER = "ASC";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createAlarmTable = "create table " + ALARM_TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TIME + " char(30))";
        db.execSQL(createAlarmTable);

        String createRecurringTable = "create table " + RECURRING_TABLE + " (TIME char(30))";
        db.execSQL(createRecurringTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String tableName, String time){
        ContentValues cv = new ContentValues();
        cv.put(TIME, time);

        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(tableName, null, cv);

        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String[] values = {String.valueOf(id)};

        db.delete(ALARM_TABLE_NAME, ID + " = ?", values);

        db.close();
    }

    public TreeMap<Integer, String> getAllTime(){
        TreeMap<Integer, String> timeMap = new TreeMap<Integer, String>();

        String select = "SELECT * FROM " + ALARM_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String time = cursor.getString(1);

                timeMap.put(id, time);

            }while(cursor.moveToNext());
        }

        db.close();

        return timeMap;
    }

    public boolean isRecurringAlarm(){
        String select = "SELECT count(time) from " + RECURRING_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        int count = cursor.getCount();

        db.close();

        if(count > 0){
            return true;
        }
        else{
            return false;
        }

    }
}
