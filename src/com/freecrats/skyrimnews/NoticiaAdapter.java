package com.freecrats.skyrimnews;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NoticiaAdapter extends ArrayAdapter {

	Activity context;
	
	public NoticiaAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		// TODO Auto-generated constructor stub
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.items_list, null);
		
		TextView labelTitulo = (TextView) item.findViewById(R.id.labelTitle);
		//labelTitulo.setText(text)
	}

}
