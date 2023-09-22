package com.example.studentmanagersystem;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

//查询数据工具类
public class Query {
    public ArrayList<String> query_user(SQLiteDatabase db, String column) {
        ArrayList<String> arr = new ArrayList<String>();
        Cursor cursor = db.query("user", new String[]{column},
                null, null, null,
                null, null);
        //利用游标遍历所有数据对象
        while (cursor.moveToNext()) {
            arr.add(cursor.getString(cursor.getColumnIndex(column)));
        }
        return arr;
    }

}
