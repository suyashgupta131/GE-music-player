package com.example.user.myapplication;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;

public class Artist  implements Comparable<Artist> {
	Long artid;
	String artname;
	Context c;
	Bitmap bp;
	String albid;
	Uri uri;

	ArrayList<String> albnamesfrarts;
	ArrayList<String> albnames=new ArrayList<String>();
	ArrayList<Song> artsongs=new ArrayList<Song>();
	ArrayList<Song> albsonglist=new ArrayList<Song>();
	public Artist(Long artid, String artname, Context m, Uri uri1)
	{   c=m;
	    uri=uri1;
		this.artid=artid;
		this.artname = artname;
		albnames=getAlbumnames(uri1);
		for(String str:albnames)
		{
			artsongs.addAll(getSongs(str));
		}
		
		bp = getArtistImage(getid(albnamesfrarts.get(0)));
	}
	ArrayList<String> getalbnames(){ return albnamesfrarts;}
	public Long getID(){return artid;}
	public ArrayList<Song> getartsongs(){return artsongs;}
	public String getTitle(){return artname;}
	public Bitmap getImage(){return bp;}
	private Bitmap getArtistImage(String artistid) {
	        Bitmap artwork = null;
	        try { 
	            Uri sArtworkUri = Uri
	                    .parse("content://media/external/audio/albumart");
	            
	           
	            Uri uri = ContentUris.withAppendedId(sArtworkUri,
	                    Long.valueOf(artistid));
	            ContentResolver res = c.getContentResolver();
	            InputStream in = res.openInputStream(uri);
	            BitmapFactory.Options options = new BitmapFactory.Options();
	            options.inDither = false;
	            options.inJustDecodeBounds = false;
	            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	            options.inSampleSize = 5;
	            options.inPurgeable = true;
	            artwork = BitmapFactory.decodeStream(in,null, options);
	 
	        } catch (Exception e) {
	            Log.e("Exception", e.toString());
	        } 
	        return artwork;
	    }
	@Override
	public int compareTo(Artist another) {
		
		return this.artname.compareTo(another.artname);
		
	}
	public ArrayList<String> getAlbumnames (Uri uri)
	{
		 ContentResolver cr = c.getContentResolver();
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
	String getid(String albTitle)
	{
		  String[] column = {
	                MediaStore.Audio.Media._ID,
	                MediaStore.Audio.Media.ALBUM_ID,
	                MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.ARTIST };
	 
	        String where = android.provider.MediaStore.Audio.Media.ALBUM + "=?";

	        String whereVal[] = { albTitle };

	        String orderBy = android.provider.MediaStore.Audio.Media.TITLE;

	        Cursor cursor = c.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	                column, where, whereVal, orderBy);

	        if (cursor.moveToFirst()) {
	            do {
	            	albid=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
	            } while (cursor.moveToNext());
	        }
	        return albid;
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
	 
	        Cursor cursor = c.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
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
	                 albsonglist.add(new Song(id,songName,artist,albid,c,data));
	                 
	            } while (cursor.moveToNext());
	            
	        } 
	        return albsonglist;
	}
}

