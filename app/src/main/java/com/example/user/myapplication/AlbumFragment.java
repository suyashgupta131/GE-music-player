package com.example.user.myapplication;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.TreeSet;

public class AlbumFragment extends ListFragment {
	
	public AlbumFragment(){}
      TreeSet<String> ts=new TreeSet<String>();
      TreeSet<String> albname=new TreeSet<String>();
      onclick1 p;
	  ArrayList<Bitmap> bp= new ArrayList<Bitmap>();
	  TreeSet<Song> albsonglist=new TreeSet<Song>();
	  ArrayList<Song> albsonglist1=new ArrayList<Song>();
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		 
		View rootView = inflater.inflate(R.layout.fragment_album, container, false);
        Bundle b;
        b=this.getArguments();
        String albname = b.getString("albname");
        getSongs(albname);
        albsonglist1=new ArrayList<Song>(albsonglist);
        setListAdapter(new SongsAdapter(getActivity(),albsonglist));
        return rootView;
    }
		void getSongs(String albTitle)
	{
		  String[] column = { MediaStore.Audio.Media.DATA,
	                MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
	                MediaStore.Audio.Media.DISPLAY_NAME,
	                MediaStore.Audio.Media.ALBUM_ID,
	                MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST };
	 
	        String where = android.provider.MediaStore.Audio.Media.ALBUM + "=?";

	        String whereVal[] = { albTitle };

	        String orderBy = android.provider.MediaStore.Audio.Media.TITLE;

	        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	                column, where, whereVal, orderBy);

	        if (cursor.moveToFirst()) {
	            do {

	                 String songName=  cursor.getString(cursor
	                                .getColumnIndex(MediaStore.Audio.Media.TITLE));
	                 Long id = cursor.getLong(cursor
                             .getColumnIndex(MediaStore.Audio.Media._ID));
	                 String artist=cursor.getString(cursor
                             .getColumnIndex(MediaStore.Audio.Media.ARTIST));
	                 String albid=cursor.getString(cursor
                             .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
	                 String data=cursor.getString(cursor
                             .getColumnIndex(MediaStore.Audio.Media.DATA));
	                 albsonglist.add(new Song(id,songName,artist,albid,getActivity().getApplicationContext(),data));

	            } while (cursor.moveToNext());
	        }
	}

	 public interface onclick1
     {
    	 void click(int i, TreeSet<Song> ar);
     }
	@Override
	public void onAttach(Activity activity) {
		
		p=(onclick1)activity;		
		super.onAttach(activity);
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		p.click(Integer.parseInt(v.getTag(R.string.id1).toString()),albsonglist);
	}

}
