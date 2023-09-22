package com.example.studentmanagersystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqlHelper extends SQLiteOpenHelper {

	public MySqlHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {


		db.execSQL("create table student(_id integer primary key autoincrement,"
				+ "name varchar(20),"
				+ "sex varchar(20),"
				+ "mingzu varchar(20),"
				+ "StudentClass varchar(20),"
				+ "StudentNumber varchar(20),"
				+ "phone varchar(20),"
				+ "StudentDormitory varchar(20)," + "image blob)");

		db.execSQL("create table score("
				+ "_id integer primary key autoincrement,"
				+ "course varchar(20),"
				+ "score varchar(20),"
				+ "type varchar(20),"
				+ "uid varchar(20)"
				+ ")"
			);


		System.out.println("onCreate 被调用");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {




	}

}
