package com.freecrats.skyrimnews;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDataStore extends SQLiteOpenHelper {
	String sqlCreate="CREATE TABLE news(id INTEGER PRIMARY KEY AUTOINCREMENT," +
			"text VARCHAR(50),url VARCHAR(255),date CHAR(10) ,read CHAR(1) DEFAULT 'N')";
	public SQLDataStore(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(sqlCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS news");
		db.execSQL(sqlCreate);
	}
	
	public boolean urlExists(SQLiteDatabase db,String url){
		boolean result=false;
		String sql="SELECT * FROM news WHERE url=?";
		Cursor cursor = db.rawQuery(sql,new String[]{url});
		if(cursor.getCount()>0)
			result=true;
		return(result);
	}

}
