package com.example.pocketmanager.map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

import com.example.pocketmanager.general.DBHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class LocationDBHelper {

    public static long insert(String name, Double latitude, Double longitude, Boolean favorite, String description) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.NAME, name);
        values.put(LocationContract.LocationEntry.LATITUDE, latitude);
        values.put(LocationContract.LocationEntry.LONGITUDE, longitude);
        values.put(LocationContract.LocationEntry.FAVORITE, favorite ? 1 : 0);
        values.put(LocationContract.LocationEntry.DESCRIPTION, description);

        long newRowId = db.insert(LocationContract.LocationEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    public static synchronized void initLocations() {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();

        //define column
        String[] projection = {
                LocationContract.LocationEntry.ID,
                LocationContract.LocationEntry.NAME,
                LocationContract.LocationEntry.LATITUDE,
                LocationContract.LocationEntry.LONGITUDE,
                LocationContract.LocationEntry.FAVORITE,
                LocationContract.LocationEntry.DESCRIPTION,
        };

        //define where

        //define sort order

        //get values
        Cursor cursor = db.query(
                LocationContract.LocationEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        //load data into map
        long id;
        String name;
        Double latitude, longitude;
        boolean favorite;
        String description;

        while (cursor.moveToNext()) {
            id = cursor.getLong(cursor.getColumnIndex(LocationContract.LocationEntry.ID));
            name = cursor.getString(cursor.getColumnIndex(LocationContract.LocationEntry.NAME));
            latitude = cursor.getDouble(cursor.getColumnIndex(LocationContract.LocationEntry.LATITUDE));
            longitude = cursor.getDouble(cursor.getColumnIndex(LocationContract.LocationEntry.LONGITUDE));
            favorite = cursor.getInt(cursor.getColumnIndex(LocationContract.LocationEntry.FAVORITE)) == 1 ? true : false;
            description = cursor.getString(cursor.getColumnIndex(LocationContract.LocationEntry.DESCRIPTION));

            LocationData.loadLocationData(id, name, latitude, longitude, favorite, description);
        }
    }

    public static synchronized void delete(long id) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        String selection = LocationContract.LocationEntry.ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        db.delete(LocationContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
    }



    public static synchronized void updateFavorite(long id, boolean favorite) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.FAVORITE, favorite ? 1 : 0);

        // Which row to update, based on the title
        String selection = LocationContract.LocationEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        db.update(
                LocationContract.LocationEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }
    public static synchronized void updateName(long id, String name) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.NAME, name);

        // Which row to update, based on the title
        String selection = LocationContract.LocationEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        db.update(
                LocationContract.LocationEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }
    public static synchronized void updateDescription(long id, String description) {
        SQLiteDatabase db = DBHelper.getInstance().getWritableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(LocationContract.LocationEntry.DESCRIPTION, description);

        // Which row to update, based on the title
        String selection = LocationContract.LocationEntry.ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        db.update(
                LocationContract.LocationEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }




}