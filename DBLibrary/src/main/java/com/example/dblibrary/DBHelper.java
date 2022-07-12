package com.example.dblibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "events.db";
    public static final String EVENTS_TABLE_NAME = "events";
    public static final String EVENTS_COLUMN_EVENTTIME = "EventTime";
    public static final String EVENTS_COLUMN_HOSTID = "HostID";
    public static final String EVENTS_COLUMN_USERID = "UserID";
    public static final String EVENTS_COLUMN_LOCATIONNBR = "LocationNbr";
    public static final String EVENTS_COLUMN_ROUTENBR = "RouteNbr";
    public static final String EVENTS_COLUMN_DAY = "Day";
    public static final String EVENTS_COLUMN_LOGGER = "Logger";
    public static final String EVENTS_COLUMN_EVENTNBR = "EventNbr";
    public static final String EVENTS_COLUMN_ADDTLDESC = "AddtlDesc";
    public static final String EVENTS_COLUMN_ADDTLNBR = "AddtlNbr";
    //public static final String EVENTS_COLUMN_LOGLEVEL = "LogLevel";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table events " +
                        "(id integer primary key, EventTime text,HostID text,UserID text,LocationNbr text,RouteNbr text,Day text,Logger text,EventNbr number,AddtlDesc text, AddtlNbr text)"
        );
    }
    public boolean insertEvent (String eventTime, String hostID, String userId, String locationNbr, String routeNbr, String day, String logger, String eventNbr, String addtlDesc, String addtlNbr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EventTime", eventTime);
        contentValues.put("HostID", hostID);
        contentValues.put("UserID", userId);
        contentValues.put("LocationNbr", locationNbr);
        contentValues.put("RouteNbr", routeNbr);
        contentValues.put("Day", day);
        contentValues.put("Logger", logger);
        contentValues.put("EventNbr", eventNbr);
        contentValues.put("AddtlDesc", addtlDesc);
        contentValues.put("AddtlNbr", addtlNbr);
        db.insert("events", null, contentValues);
        return true;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from events where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EVENTS_COLUMN_EVENTTIME);
        return numRows;
    }
    public Integer deleteEvents (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("events",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllEvents() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from events", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            int index = res.getColumnIndex(EVENTS_COLUMN_EVENTTIME);
            if(index >= 0) {
                array_list.add(res.getString(index));
                res.moveToNext();
            }
        }
        return array_list;
    }
    public boolean updateEvents (Integer id, String eventTime, String hostID, String userId, String locationNbr, String routeNbr, String day, String logger, String eventNbr, String addtlDesc, String addtlNbr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("EventTime", eventTime);
        contentValues.put("HostID", hostID);
        contentValues.put("UserID", userId);
        contentValues.put("LocationNbr", locationNbr);
        contentValues.put("RouteNbr", routeNbr);
        contentValues.put("Day", day);
        contentValues.put("Logger", logger);
        contentValues.put("EventNbr", eventNbr);
        contentValues.put("AddtlDesc", addtlDesc);
        contentValues.put("AddtlNbr", addtlNbr);
        db.update("events", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS events");
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
