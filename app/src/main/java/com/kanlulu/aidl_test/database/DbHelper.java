package com.kanlulu.aidl_test.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Created by kanlulu
 * DATE: 2018/12/12 9:52
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ipc_contentProvider_test.db";
    public static final String TABLE_NAME = "book";
    private static final int DB_VERSION = 1;

    private final String SQL_CREATE_TABLE = "create table if not exists " + TABLE_NAME + " (_id integer primary key, name TEXT, description TEXT)";

    public  DbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
