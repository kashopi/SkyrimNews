package com.freecrats.skyrimnews;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class SkyrimNewsActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        try {
			String contenido = HTTPFetch.getStringFromURL("http://www.bethblog.com");
			Pattern pattern = Pattern.compile("<title>([^<]+)</title>");
			Matcher matcher = pattern.matcher(contenido);
			if(matcher.find()){
				String titulo = matcher.group();
				Log.d("MATCH",titulo);
			}
			
			Log.d("HTTP",contenido);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}