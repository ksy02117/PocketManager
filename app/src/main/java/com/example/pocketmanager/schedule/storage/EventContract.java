package com.example.pocketmanager.schedule.storage;

import android.provider.BaseColumns;

import com.example.pocketmanager.map.LocationContract;

public final class EventContract {

    private EventContract() {}

    public static class EventEntry implements BaseColumns {
        public static final String TABLE_NAME = "EVENT";
        public static final String ID = "ID";
        public static final String NAME = "NAME";
        public static final String START_TIME = "START";
        public static final String END_TIME = "END";
        public static final String DESCRIPTION = "DESCRIPTION";
        public static final String OUTDOOR = "OUTDOOR";
        public static final String PRIORITY = "PRIORITY";
        public static final String PARENT_ID = "PARENT_ID";
        public static final String LOCATION_ID = "LOCATION";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + EventEntry.TABLE_NAME + " (" +
            EventEntry.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            EventEntry.NAME + " TEXT NOT NULL," +
            EventEntry.START_TIME + " INTEGER NOT NULL," +
            EventEntry.END_TIME + " INTEGER NOT NULL," +
            EventEntry.DESCRIPTION + " TEXT," +
            EventEntry.OUTDOOR + " INTEGER NOT NULL," +
            EventEntry.PRIORITY + " INTEGER NOT NULL," +
            EventEntry.PARENT_ID + " INTEGER REFERENCES "+EventEntry.TABLE_NAME+"("+EventEntry.ID+")" +
            "ON UPDATE CASCADE ON DELETE CASCADE," +
            EventEntry.LOCATION_ID + " INTEGER REFERENCES "+ LocationContract.LocationEntry.TABLE_NAME+"("+LocationContract.LocationEntry.ID+")" +
            "ON UPDATE CASCADE ON DELETE SET NULL);";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME;


}
