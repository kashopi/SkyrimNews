package com.freecrats.skyrimnews;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SkyrimNewsActivity extends Activity {
    /** Called when the activity is first created. */
	private static boolean serviceStarted=false;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        refreshNewsService();
        refreshNewsList();
    }
    
    private void refreshNewsService(){
    	if(!serviceStarted){
    		serviceStarted=true;
    		startService(new Intent(this, BGService.class));
    	}
    }
    
    private void refreshNewsList(){   	
        SQLDataStore sds = new SQLDataStore(getApplicationContext(), getPackageName(), null, 1);
		SQLiteDatabase db = sds.getReadableDatabase();
		String sql="SELECT * FROM news";
		Cursor cursor = db.rawQuery(sql,null);
		List<String> datos = new ArrayList<String>();
		if(cursor.moveToFirst())
			do{
				datos.add(cursor.getString(1));
			}while(cursor.moveToNext());
		cursor.close();
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,datos);
    	ListView lista = (ListView) findViewById(R.id.listview_news);
    	lista.setAdapter(adapter);
		db.close();
    }
    
    public void refresh_onclick(View v){
    	refreshNewsList();
    }
    
    @Override
    public void onWindowFocusChanged (boolean hasFocus){
    	if(hasFocus){
    		String ns = Context.NOTIFICATION_SERVICE;
    		NotificationManager nManager = (NotificationManager) getSystemService(ns);
    		nManager.cancelAll();
    	}
    }
}