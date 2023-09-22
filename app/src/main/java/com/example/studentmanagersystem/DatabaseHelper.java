package com.example.studentmanagersystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * databasehelper
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        String sql_table_user = "create table IF NOT EXISTS user(" +
                "account text," +
                "password text" +
                ")";
        db.execSQL(sql_table_user);

//        默认帐号密码
        db.execSQL("INSERT INTO user VALUES (?, ?)",
                new Object[]{"admin", "123456"});

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}