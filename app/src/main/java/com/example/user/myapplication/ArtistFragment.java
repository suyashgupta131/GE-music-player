package com.example.user.myapplication;

import android.app.Activity;
import android.app.ListFragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.TreeSet;

public class ArtistFragment extends ListFragment {
	
	 public ArtistFragment() {
		// TODO Auto-generated constructor stub
	}
	 ArrayList<String> albnamesfrarts=new ArrayList<String>();
	TreeSet<Artist> arts =new TreeSet<Artist>();
	ArrayList<Artist> arts1 ;
	ArrayList<Song> artsongs=new ArrayList<Song>();
	TreeSet<Song> artsongs1=new TreeSet<Song>();
	ArrayList<Song> albsonglist=new ArrayList<Song>();
	onclick3 p;
	ArrayList<String> albnames=new ArrayList<String>();
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_artists, container, false);
        Bundle b;
        b=this.getArguments();
        String artId = b.getString("artistId");
        Uri.Builder builder = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.buildUpon();
   	    builder.appendPath(artId);
   	    builder.appendPath("albums");
   	    Uri uri = builder.build();
      //arts= MainActivity.retartlist();
      // arts1=new ArrayList<Artist>(arts);
      // artsongs=arts1.get(a).getartsongs();
     //  artsongs1=new TreeSet<Song>(artsongs); 
   	 albnames=getAlbumnames(uri);
		for(String str:albnames)
		{
			artsongs.addAll(getSongs(str));
		}
        Collections.sort(artsongs, new Comparator<Song>(){
			public int compare(Song a, Song b){
				return a.getTitle().compareTo(b.getTitle());
			}
			
		});
        artsongs1=new TreeSet<Song>(artsongs);
        setListAdapter(new SongsAdapter(getActivity(),artsongs1));
     return rootView;
    }
	 public void getArtistlist()
     {    
      ContentResolver cr = getActivity().getContentResolver();
      final Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
      final String artist_id = MediaStore.Audio.Artists._ID;
      final String artist_name = MediaStore.Audio.Artists.ARTIST;
      final String[]columns={artist_id,artist_name};
      Cursor cursor = cr.query(uri,columns,null,null, null);
      if(cursor.moveToFirst())
      { 
             do{
            	 Uri.Builder builder = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.buildUpon();
            	 builder.appendPath(cursor.getString(cursor.getColumnIndex(artist_id)));
            	 builder.appendPath("albums");
                 arts.add(new Artist(cursor.getLong(cursor.getColumnIndex(artist_id)),
                 cursor.getString(cursor.getColumnIndex(artist_name)),getActivity(),builder.build()));                 
            }while(cursor.moveToNext());
             cursor.close();
            }        
         }
	
	 public interface onclick3
     {
    	 void click(int i, TreeSet<Song> ar);
     }
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		p=(onclick3)activity;
		super.onAttach(activity);
	}
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		p.click(position,artsongs1);
	}
	public ArrayList<String> getAlbumnames (Uri uri)
	{
		 ContentResolver cr = getActivity().getContentResolver();
	     final String album_name = MediaStore.Audio.Artists.Albums.ALBUM;
         final String[]columns={album_name};
	     Cursor cursor =cr.query(uri, columns, null,null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);
	     albnamesfrarts=new ArrayList<String>();
         if(cursor.moveToFirst())
	     {
	          do{
	              albnamesfrarts.add(cursor.getString(cursor.getColumnIndex(album_name)));
	            }while(cursor.moveToNext());
	            cursor.close();
	      }
	      return albnamesfrarts;
	}
	public ArrayList<Song> getSongs(String albTitle)
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
	                 String data= cursor.getString(cursor
	                		 .getColumnIndex(MediaStore.Audio.Media.DATA));
	                 albsonglist.add(new Song(id,songName,artist,albid,getActivity(),data));
	                 
	            } while (cursor.moveToNext());
	            
	        } 
	        return albsonglist;
	}
}
