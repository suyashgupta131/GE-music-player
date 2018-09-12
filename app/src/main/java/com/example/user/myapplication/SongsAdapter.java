package com.example.user.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;

public class SongsAdapter extends BaseAdapter {
	
	//song list and layout
	String str1[] ;
	String songnames[];
	String playlistselsected;
	String playlistNames[];
	private TreeSet<Song> songs;
	private ArrayList<Song> sg=new ArrayList<Song>();
	private LayoutInflater songInf;
	ArrayList<String> addp;
	ArrayList<Integer> songchecked=new ArrayList<Integer>();
	Context m;
	public SongsAdapter(Context c, TreeSet<Song> theSongs){
		m=c;
		songs=theSongs;
		songInf= LayoutInflater.from(c);
		sg=new ArrayList<Song>(songs);
	}
	
	@Override
	public int getCount() {
		return songs.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	static class ViewHolderItem { 
	    TextView songView;
	    TextView artistView;
	    ImageView AlbumArt;
	    ImageButton ib;
	} 

	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
				
	final Song currSong = sg.get(position);
	final	ViewHolderItem viewHolder;
	if(convertView==null){
	    	convertView = (RelativeLayout)songInf.inflate(R.layout.song, parent, false);
	        viewHolder = new ViewHolderItem();
	        viewHolder.songView = (TextView)convertView.findViewById(R.id.song_title);
	        viewHolder.artistView = (TextView)convertView.findViewById(R.id.song_artist);
	        viewHolder.AlbumArt=    (ImageView)convertView.findViewById(R.id.albart1);
			viewHolder.ib=(ImageButton)convertView.findViewById(R.id.playprev);
	        viewHolder.ib.setTag(position);
	        viewHolder.ib.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 
						String sharePath = sg.get(Integer.parseInt(((View)v.getParent()).getTag(R.string.id1).toString())).getPath();
					    Uri uri = Uri.parse(sharePath);
					    Intent share = new Intent(Intent.ACTION_SEND);
					    share.setType("audio/*");
					    share.putExtra(Intent.EXTRA_STREAM, uri);
					    m.startActivity(Intent.createChooser(share, "Share Sound File"));
				}
	        });
	  
	    }else{ 
	        // we've just avoided calling findViewById() on resource everytime 
	        // just use the viewHolder 
	        viewHolder = (ViewHolderItem) convertView.getTag(R.string.id2);
	    } 
	    // assign values if the object is not null 
	    if(currSong!= null) {
	        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values 
	        
	        Typeface t= Typeface.createFromAsset(m.getAssets(), "fonts/constanz.ttf");
	        viewHolder.songView.setTypeface(t);
	        viewHolder.songView.setText(currSong.getTitle());
	        Typeface t1= Typeface.createFromAsset(m.getAssets(), "fonts/constanz.ttf");
	        viewHolder.artistView.setTypeface(t1);
	        viewHolder.artistView.setText(currSong.getArtist());
	       // if(currSong.getAlbumArt()!=null)
	        	//viewHolder.AlbumArt.setImageBitmap(currSong.getAlbumArt());
	        	new ImageLoader().execute(viewHolder.AlbumArt, currSong.getAlbum(),m);
	        	
			//else
				//viewHolder.AlbumArt.setImageResource(R.drawable.defalbart);
			
			 convertView.setTag(R.string.id2,viewHolder);
		     convertView.setTag(R.string.id1,position);
	    } 	  
	    return convertView;
	}
}

	

