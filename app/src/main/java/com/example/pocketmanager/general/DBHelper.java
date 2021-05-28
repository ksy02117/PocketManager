package com.example.pocketmanager.general;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.pocketmanager.map.LocationContract;
import com.example.pocketmanager.map.LocationDBHelper;
import com.example.pocketmanager.schedule.storage.EventContract;
import com.example.pocketmanager.schedule.storage.EventDBHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance = null;

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "PocketManager.db";

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static DBHelper getInstance(Context ctx) {
        if (instance == null)
            instance = new DBHelper(ctx);
        return instance;
    }
    public synchronized static DBHelper getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DBHelper.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LocationContract.SQL_CREATE_ENTRIES);
        db.execSQL(EventContract.SQL_CREATE_ENTRIES);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LocationContract.SQL_DELETE_ENTRIES);
        db.execSQL(EventContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }
}
