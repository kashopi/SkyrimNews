package com.freecrats.skyrimnews;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;
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
		updateTimer.scheduleAtFixedRate(doRefresh, 0, 60*30*1000);
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
	}
	
	private TimerTask doRefresh = new TimerTask(){
		public void run(){
			doSync();
		}
	};
	
	class Sites{
		String sitename;
		String baseurl;
		String url;
		String regexp;
		
		public Sites(String _sitename,String _baseurl,String _url,String _regexp){
			this.sitename=_sitename;
			this.baseurl=_baseurl;
			this.url=_url;
			this.regexp=_regexp;
		}
	}
	
	public void doSync(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fecha;
        SQLDataStore sds = new SQLDataStore(getApplicationContext(), getPackageName(), null, 1);
		SQLiteDatabase db = sds.getWritableDatabase();

        List<Sites> sites=new ArrayList<Sites>();
        sites.add(new Sites(
        		"N4G",
        		"http://n4g.com/",
        		"http://n4g.com/channel/the-elder-scrolls-v-skyrim",
        		"<a href=\"([^\"]+)\" class=\"shsl-item-title\">([^<]+)</a>"));
        sites.add(new Sites(
        		"NexusMods",
        		"http://skyrim.nexusmods.com/",
        		"http://skyrim.nexusmods.com/",
        		"<h2><a href=\"([^\"]+)\"><span>([^<]+)</span></a></h2>"));

        try {
			boolean notify=false;
			for(Sites site: sites){
				String siteurl=site.url;
				String regexp=site.regexp;
				String sitename=site.sitename;
				String baseurl=site.baseurl;
				boolean found=false;
				Stack<ContentValues> stack = new Stack<ContentValues>();
				String contenido = HTTPFetch.getStringFromURL(siteurl);
				Pattern pattern = Pattern.compile(regexp);
				Matcher matcher = pattern.matcher(contenido);
				while(matcher.find()){
					String url = matcher.group(1);
					String title = matcher.group(2);
					if(!sds.urlExists(db, url)){
						ContentValues noticia=new ContentValues();
						noticia.put("baseurl", baseurl);
						noticia.put("sitename", sitename);
						noticia.put("text", title);
						noticia.put("url", url);
						fecha = sdf.format(cal.getTime());
						noticia.put("date",fecha);
						//db.insert("news", null, noticia);
						stack.push(noticia);
						Log.d("SQL","Insertando noticia");
						notify=true;
					}else{
						Log.d("SQL","Noticia existe...");
					}
					//db.execSQL("INSERT INTO news(text,url,date) VALUES(?,?,?)",new String[]=[title,url,new String('2012-02-27')]);
					found=true;
				}

				//Guardamos en BD
				while(!stack.empty()){
					ContentValues noticia=stack.pop();
					db.insert("news", null, noticia);
				}
				
				if(!found)
					Log.d("KSP","NO se han encontrado coincidencias");
			}
			if(notify)
				doNotification(new String("Skyrim News"),new String("Hay noticias nuevas"));
			
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
		int icono = android.R.drawable.stat_notify_more;
		long hora = System.currentTimeMillis();
		Notification notif = new Notification(icono, msg, hora);
		
		//Preparamos el intent
		Context contexto = getApplicationContext();
		Intent notIntent = new Intent(contexto,SkyrimNewsActivity.class);
		PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, notIntent, 0);
		notif.setLatestEventInfo(contexto, title, msg, contIntent);
		
		//Lanzamos la notificacion
		nManager.notify(5555, notif);
	
	}
	
}
