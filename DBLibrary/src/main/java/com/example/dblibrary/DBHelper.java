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
    public static final String EVENTS_COLUMN_ID = "id";
    public static final String EVENTS_TABLE_NAME = "events";
    public static final String EVENTS_COLUMN_EVENTTIME = "EventTime";
    public static final String EVENTS_COLUMN_HOSTID = "HostID";
    public static final String EVENTS_COLUMN_APPID = "AppID";
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
                        "(id integer primary key, EventTime text,HostID text,AppId text default 'CPRNT',UserID text,LocationNbr integer(4),RouteNbr integer(2),Day integer(1),Logger text,EventNbr integer(5),AddtlDesc text, AddtlNbr integer)"
        );
    }
    public boolean insertEvent (String eventTime, String hostID, String userId, int locationNbr, int routeNbr, int day, String logger, int eventNbr, String addtlDesc, int addtlNbr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_COLUMN_EVENTTIME, eventTime);
        contentValues.put(EVENTS_COLUMN_HOSTID, hostID);
        contentValues.put(EVENTS_COLUMN_USERID, userId);
        contentValues.put(EVENTS_COLUMN_LOCATIONNBR, locationNbr);
        contentValues.put(EVENTS_COLUMN_ROUTENBR, routeNbr);
        contentValues.put(EVENTS_COLUMN_DAY, day);
        contentValues.put(EVENTS_COLUMN_LOGGER, logger);
        contentValues.put(EVENTS_COLUMN_EVENTNBR, eventNbr);
        contentValues.put(EVENTS_COLUMN_ADDTLDESC, addtlDesc);
        contentValues.put(EVENTS_COLUMN_ADDTLNBR, addtlNbr);

        db.insert("events", null, contentValues);
        return true;
    }
    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from events where id="+id+"", null );
        return res;
    }

    public String getDBAsXML() {
        ArrayList<Integer> ids = getAllEvents();
        StringBuilder xml = new StringBuilder("CPRNT;");
        for(int id:ids) {
            xml.append(getEventAsXMLString(id));
        }
        return xml.toString();
    }

    private String getEventAsXMLString(int id) {
        Cursor rs = getData(id);
        rs.moveToFirst();

        int eventTimeIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_EVENTTIME);
        int hostIDIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_HOSTID);
        int userIDIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_USERID);
        int locationNbrIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_LOCATIONNBR);
        int routeNbrIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_ROUTENBR);
        int dayIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_DAY);
        int loggerIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_LOGGER);
        int eventNbrIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_EVENTNBR);
        int addtlDescIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_ADDTLDESC);
        int addtlNbrIndex = rs.getColumnIndex(DBHelper.EVENTS_COLUMN_ADDTLNBR);

        String eventTime = "";
        String hostID = "";
        String userID = "";
        String locationNbr = "";
        String routeNbr = "";
        String day = "";
        String logger = "";
        String eventNbr = "";
        String addtlDesc = "";
        String addtlNbr = "";
        if(eventTimeIndex >= 0) {
            eventTime = rs.getString(eventTimeIndex);
        }
        if(hostIDIndex >= 0) {
            hostID = rs.getString(hostIDIndex);
        }
        if(userIDIndex >= 0) {
            userID = rs.getString(userIDIndex);
        }
        if(locationNbrIndex >= 0) {
            locationNbr = rs.getString(locationNbrIndex);
        }
        if(routeNbrIndex >= 0) {
            routeNbr = rs.getString(routeNbrIndex);
        }
        if(dayIndex >= 0) {
            day = rs.getString(dayIndex);
        }
        if(loggerIndex >= 0) {
            logger = rs.getString(loggerIndex);
        }
        if(eventNbrIndex >= 0) {
            eventNbr = rs.getString(eventNbrIndex);
        }
        if(addtlDescIndex >= 0) {
            addtlDesc = rs.getString(addtlDescIndex);
        }
        if(addtlNbrIndex >= 0) {
            addtlNbr = rs.getString(addtlNbrIndex);
        }

        if (!rs.isClosed())  {
            rs.close();
        }
        return "<LogEntry EventTime=\""+eventTime+"\" HostId=\""+hostID+"\" UserID=\""+userID+"\" LocationNbr=\""+locationNbr+"\" RouteNbr=\""+routeNbr+"\" Day=\""+day+"\" Logger=\""+logger+"\" EventNbr=\""+eventNbr+"\" AddtlDesc=\""+addtlDesc+"\" AddtlNbr=\""+addtlNbr+"\" />";
    }



    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, EVENTS_COLUMN_ID);
        return numRows;
    }
    public Integer deleteEvents (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("events",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Integer> getAllEvents() {
        ArrayList<Integer> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from events", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            int index = res.getColumnIndex(EVENTS_COLUMN_ID);
            if(index >= 0) {
                array_list.add(res.getInt(index));
                res.moveToNext();
            }
        }
        return array_list;
    }
    public boolean updateEvents (Integer id, String eventTime, String hostID, String userId, int locationNbr, int routeNbr, int day, String logger, int eventNbr, String addtlDesc, int addtlNbr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENTS_COLUMN_EVENTTIME, eventTime);
        contentValues.put(EVENTS_COLUMN_HOSTID, hostID);
        contentValues.put(EVENTS_COLUMN_USERID, userId);
        contentValues.put(EVENTS_COLUMN_LOCATIONNBR, locationNbr);
        contentValues.put(EVENTS_COLUMN_ROUTENBR, routeNbr);
        contentValues.put(EVENTS_COLUMN_DAY, day);
        contentValues.put(EVENTS_COLUMN_LOGGER, logger);
        contentValues.put(EVENTS_COLUMN_EVENTNBR, eventNbr);
        contentValues.put(EVENTS_COLUMN_ADDTLDESC, addtlDesc);
        contentValues.put(EVENTS_COLUMN_ADDTLNBR, addtlNbr);
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
