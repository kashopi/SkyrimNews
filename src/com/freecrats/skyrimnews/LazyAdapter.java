package com.freecrats.skyrimnews;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String,String>> data;
	private static LayoutInflater inflater=null;
	public ImageLoader imageloader;
	
	public LazyAdapter(Activity a, ArrayList<HashMap<String,String>> d){
		this.activity = a;
		this.data = d;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageloader = new ImageLoader(activity.getApplicationContext());
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(convertView == null)
			view = inflater.inflate(R.layout.list_row, null);
		
		TextView title = (TextView) view.findViewById(R.id.title);
		TextView website = (TextView) view.findViewById(R.id.website);
		TextView datescanned = (TextView) view.findViewById(R.id.datescanned);
		ImageView thumb_image = (ImageView) view.findViewById(R.id.list_image);
		
		HashMap<String,String> entrada = new HashMap<String,String>();
		entrada = data.get(position);
		
		title.setText("TITULO ABC");
		website.setText("WEBSITE!!!");
		datescanned.setText("444444");
		imageloader.DisplayImage("http://www.englishforum.ch/customavatars/avatar15347_1.gif", thumb_image);
		
		return null;
	}

}
