package com.freecrats.skyrimnews;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BGService extends Service{
	private Timer updateTimer=null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate(){
		updateTimer = new Timer("skyrimnewsUpdate");
	}
	
	@Override
	public void onStart(Intent intent,int startId){
		super.onStart(intent, startId);
		updateTimer.cancel();
		updateTimer = new Timer("skyrimnewsUpdate");
		updateTimer.scheduleAtFixedRate(doRefresh, 0, 15*1000);
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
	}
	
	private TimerTask doRefresh = new TimerTask(){
		public void run(){
			doSync();
		}
	};
	
	public void doSync(){
        SQLDataStore sds = new SQLDataStore(getApplicationContext(), getPackageName(), null, 1);
		SQLiteDatabase db = sds.getWritableDatabase();

		try {
			String contenido = HTTPFetch.getStringFromURL("http://n4g.com/channel/the-elder-scrolls-v-skyrim");
			Pattern pattern = Pattern.compile("<a href=\"([^\"]+)\" class=\"shsl-item-title\">([^<]+)</a>");
			Matcher matcher = pattern.matcher(contenido);
			boolean found=false;
			while(matcher.find()){
				String url = matcher.group(1);
				String title = matcher.group(2);
				if(!sds.urlExists(db, url)){
					ContentValues noticia=new ContentValues();
					noticia.put("text", title);
					noticia.put("url", url);
					noticia.put("date","2012-02-28");
					db.insert("news", null, noticia);
					Log.d("SQL","Insertando noticia");
					
					doNotification(new String("Skyrim News"),title);
				}else{
					Log.d("SQL","Noticia existe...");
				}
				//db.execSQL("INSERT INTO news(text,url,date) VALUES(?,?,?)",new String[]=[title,url,new String('2012-02-27')]);
				found=true;
			}
			if(!found)
				Log.d("KSP","NO se han encontrado coincidencias");
			
			Log.d("HTTP",contenido);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.close();
	}
	
	private void doNotification(String title,String msg){
		//Preparamos la notificacion
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nManager = (NotificationManager) getSystemService(ns);
		int icono = android.R.drawable.star_on;
		long hora = System.currentTimeMillis();
		Notification notif = new Notification(icono, msg.subSequence(0, 35), hora);
		
		//Preparamos el intent
		Context contexto = getApplicationContext();
		Intent notIntent = new Intent(contexto,SkyrimNewsActivity.class);
		PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, notIntent, 0);
		notif.setLatestEventInfo(contexto, title, msg.subSequence(0, 25), contIntent);
		
		//Lanzamos la notificacion
		nManager.notify(5555, notif);
	
	}
	
}
