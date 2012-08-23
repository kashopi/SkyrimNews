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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SkyrimNewsActivity extends Activity {
    /** Called when the activity is first created. */
	private static boolean serviceStarted=false;
	private List<Titular> datos = new ArrayList<Titular>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void onResume(){
    	super.onResume();
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
		String sql="SELECT * FROM news ORDER BY id desc";
		Cursor cursor = db.rawQuery(sql,null);
		datos.clear();
		if(cursor.moveToFirst())
			do{
				datos.add(new Titular(cursor.getString(3).replace("&nbsp;",""),cursor.getString(1).replace("&nbsp;",""),cursor.getString(2) + cursor.getString(4)));
			}while(cursor.moveToNext());
		cursor.close();
		db.close();
		
		NoticiaAdapter adaptador = new NoticiaAdapter(this);
		ListView lstNews = (ListView) findViewById(R.id.listview_news);
		lstNews.setAdapter(adaptador);
		
		//Ahora ponemos el listener del onclick en los elementos de la lista
		lstNews.setOnItemClickListener(new OnItemClickListener(){
			
			public void onItemClick(AdapterView<?> a, View v, int position, long id){
				Titular noticia = (Titular) a.getAdapter().getItem(position);
				String url = noticia.getURL();
				Log.d("URLOPEN", url);
				showURL(v,url);

			}
		});
    }
    
    public void showURL(View v,String url){
    	Intent intent = new Intent(this, MyBrowser.class);
    	intent.putExtra("URL", url);
     	startActivity(intent);
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
    
    class NoticiaAdapter extends ArrayAdapter {

    	Activity context;
    	
    	public NoticiaAdapter(Activity context) {
    		super(context, R.layout.items_list, datos);
    		this.context = context;
    		// TODO Auto-generated constructor stub
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent){
    		LayoutInflater inflater = context.getLayoutInflater();
    		View item = inflater.inflate(R.layout.items_list, null);
    		
    		TextView labelTitulo = (TextView) item.findViewById(R.id.labelTitle);
    		labelTitulo.setText(datos.get(position).getTitulo());
    		TextView labelSubtitulo = (TextView) item.findViewById(R.id.labelSubtitle);
    		labelSubtitulo.setText(datos.get(position).getSubtitulo());
    		TextView labelURL = (TextView) item.findViewById(R.id.labelURL);
    		labelURL.setText(datos.get(position).getURL());
    		
    		return(item);

    	}

    }

}