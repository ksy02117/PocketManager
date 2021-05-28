package com.example.pocketmanager.schedule.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pocketmanager.general.DBHelper;
import com.example.pocketmanager.map.LocationData;
import com.example.pocketmanager.general.Time;

import java.util.List;

public class EventDBHelper {


    public static long insert(Event parent, String name, Time startTime, Time endTime, LocationData location, Boolean outdoor, String description, int priority) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.NAME, name);
        values.put(EventContract.EventEntry.START_TIME, startTime.getDt());
        values.put(EventContract.EventEntry.END_TIME, endTime.getDt());
        values.put(EventContract.EventEntry.DESCRIPTION, description);
        values.put(EventContract.EventEntry.OUTDOOR, outdoor ? 1 : 0);
        values.put(EventContract.EventEntry.PRIORITY, priority);
        values.put(EventContract.EventEntry.PARENT_ID, parent == null ? null : parent.getID());
        values.put(EventContract.EventEntry.LOCATION_ID, location == null ? null : location.getID());

        long newRowId = db.insert(EventContract.EventEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    public static synchronized void initEvents() {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

        //define column
        String[] projection = {
                EventContract.EventEntry.ID,
                EventContract.EventEntry.NAME,
                EventContract.EventEntry.START_TIME,
                EventContract.EventEntry.END_TIME,
                EventContract.EventEntry.DESCRIPTION,
                EventContract.EventEntry.OUTDOOR,
                EventContract.EventEntry.PRIORITY,
                EventContract.EventEntry.PARENT_ID,
                EventContract.EventEntry.LOCATION_ID
        };

        //define where it's an event
        String selection = EventContract.EventEntry.PARENT_ID + " IS NULL";

        //define sort order

        //get values
        Cursor cursor = db.query(
                EventContract.EventEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        //load data into map
        long id;
        String name;
        Time startTime, endTime;
        boolean outdoor;
        String description;
        int priority;

        Event parent;
        LocationData location;

        while (cursor.moveToNext()) {
            id = cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry.ID));
            name = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.NAME));
            startTime = new Time(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry.START_TIME)));
            endTime = new Time(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry.END_TIME)));
            outdoor = cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.OUTDOOR)) == 1 ? true : false;
            description = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.DESCRIPTION));
            priority = cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.PRIORITY));

            location = LocationData.findLocationByID(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry.LOCATION_ID)));

            Event.loadEvent(id, name, startTime, endTime, location, outdoor, description, priority);
        }

        //define where it's an event
        selection = EventContract.EventEntry.PARENT_ID + " IS NOT NULL";

        //define sort order

        //get values
        cursor = db.query(
                EventContract.EventEntry.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            id = cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry.ID));
            name = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.NAME));
            startTime = new Time(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry.START_TIME)));
            endTime = new Time(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry.END_TIME)));
            outdoor = cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.OUTDOOR)) == 1 ? true : false;
            description = cursor.getString(cursor.getColumnIndex(EventContract.EventEntry.DESCRIPTION));
            priority = cursor.getInt(cursor.getColumnIndex(EventContract.EventEntry.PRIORITY));

            parent = Event.findEventByID(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry.PARENT_ID)));
            location = LocationData.findLocationByID(cursor.getLong(cursor.getColumnIndex(EventContract.EventEntry.LOCATION_ID)));

            SubEvent.loadEvent(id, parent, name, startTime, endTime, location, outdoor, description, priority);
        }
    }

    public static synchronized void delete(long id) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        String selection = EventContract.EventEntry.ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(EventContract.EventEntry.TABLE_NAME, selection, selectionArgs);
    }

    public static synchronized void clear() {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        db.delete(EventContract.EventEntry.TABLE_NAME, null, null);
    }

    public static synchronized void updateStartTime(long id, Time startTime) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.START_TIME, startTime.getDt());

        // Which row to update, based on the title
        String selection = EventContract.EventEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
    public static synchronized void updateEndTime(long id, Time endTime) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.END_TIME, endTime.getDt());

        // Which row to update, based on the title
        String selection = EventContract.EventEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
    public static synchronized void updateName(long id, String name) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.NAME, name);

        // Which row to update, based on the title
        String selection = EventContract.EventEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
    public static synchronized void updateDescription(long id, String description) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.DESCRIPTION, description);

        // Which row to update, based on the title
        String selection = EventContract.EventEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
    public static synchronized void updatePriority(long id, int priority) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.PRIORITY, priority);

        // Which row to update, based on the title
        String selection = EventContract.EventEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
    public static synchronized void updateLocation(long id, LocationData location) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.LOCATION_ID, location.getID());

        // Which row to update, based on the title
        String selection = EventContract.EventEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }
    public static synchronized void updateOutdoor(long id, boolean outdoor) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(EventContract.EventEntry.OUTDOOR, outdoor ? 1 : 0);

        // Which row to update, based on the title
        String selection = EventContract.EventEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                EventContract.EventEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }



}
