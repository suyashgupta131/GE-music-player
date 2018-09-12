package com.example.user.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaylistAdapter extends BaseAdapter {
	
		  Context mContext;
		  ArrayList<Playlist> plays=new ArrayList<Playlist>();
		 int playlistcount[];
		 LayoutInflater playlistlay;
		 
			  public PlaylistAdapter(Context c, ArrayList<Playlist> plays) {
		        mContext = c;
		        playlistlay= LayoutInflater.from(c);
		        this.plays=plays;		       
		    } 
			  @Override
			public int getCount() {
				// TODO Auto-generated method stub
				 return plays.size();
				
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@SuppressLint("ResourceType")
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
								
				convertView = (LinearLayout)playlistlay.inflate(R.layout.album, parent, false);
				TextView songView = (TextView)convertView.findViewById(R.id.albname1);
				ImageView AlbumArt=(ImageView)convertView.findViewById(R.id.albart1);
				Typeface t= Typeface.createFromAsset(mContext.getAssets(), "fonts/constanz.ttf");
				songView.setTypeface(t);
				songView.setText(plays.get(position).getName());				
			    AlbumArt.setImageResource(R.drawable.defalbart);
				convertView.setTag(position);
				return convertView;				
			    }			 				
			}



