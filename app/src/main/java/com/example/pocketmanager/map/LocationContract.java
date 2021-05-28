package com.example.pocketmanager.map;

import android.provider.BaseColumns;

import com.example.pocketmanager.schedule.storage.EventContract;

public class LocationContract {

    private LocationContract() {}

    public static class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "LOCATION";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
        public static final String LATITUDE = "LAT";
        public static final String LONGITUDE = "LON";
        public static final String FAVORITE = "FAV";
        public static final String DESCRIPTION = "DESCRIPTION";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + LocationEntry.TABLE_NAME + "(" +
            LocationEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            LocationEntry.NAME + " TEXT," +
            LocationEntry.LATITUDE + " INTEGER NOT NULL," +
            LocationEntry.LONGITUDE + " INTEGER NOT NULL," +
            LocationEntry.FAVORITE + " INTEGER NOT NULL," +
            LocationEntry.DESCRIPTION + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;
}
