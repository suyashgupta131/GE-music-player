package com.example.user.myapplication;

import android.app.Activity;
import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class PlaylistFragment extends ListFragment {
	
	public PlaylistFragment(){}
	 Long id ;
	 onclick2 p;
	 TreeSet<Song> playsong;
	 ArrayList<Song> playsong1=new ArrayList<Song>();
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);
        Bundle b;
        b=this.getArguments();
        String playname = b.getString("playlistname");
        getSongs(playname);
        playsong1=new ArrayList<Song>(playsong);
        setListAdapter(new SongsAdapter(getActivity(),playsong));
        return rootView;
    }
	void getSongs(String playlisttitle)
	{
		  String[] column = { MediaStore.Audio.Playlists.NAME,
	                MediaStore.Audio.Playlists._ID};
	 
	        String where = android.provider.MediaStore.Audio.Playlists.NAME+ "=?";

	        String whereVal[] = { playlisttitle };

	        String orderBy = android.provider.MediaStore.Audio.Playlists.NAME;

	        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
	                column, where, whereVal, orderBy);

	        if (cursor.moveToFirst()) {
	            do {
	                  id = cursor.getLong(cursor
                             .getColumnIndex(MediaStore.Audio.Playlists._ID));
	            } while (cursor.moveToNext());
	        playsong= new TreeSet<Song>(Arrays.asList(PlaylistDatabase.getTracks(getActivity(),id)));
	        }
	}
	public interface onclick2
    {
   	 void click(int i, TreeSet<Song> ar);
    }
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		p=(onclick2)activity;		
		super.onAttach(activity);
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		p.click(Integer.parseInt(v.getTag(R.string.id1).toString()),playsong);
	}

}
