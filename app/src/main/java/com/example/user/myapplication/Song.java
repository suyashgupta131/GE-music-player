package com.example.user.myapplication;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.InputStream;


public class Song implements Comparable<Song> {
	
	private long id;
	private String title;
	private String artist;
	private String album;
	 String path;
	
	//private Bitmap albumArt;
	Context m;
	public Song(long songID, String songTitle, String songArtist, String songAlbum, Context m , String path){
		id=songID;
		title=songTitle;
		artist=songArtist;
		album = songAlbum;
		this.m=m;
		this.path=path;
	}
	
	public long getID(){return id;}
	public String getTitle(){return title;}
	public String getArtist(){return artist;}
	public String getAlbum(){return album;}
	public String getPath(){return path;}
	public Bitmap getAlbumArt()
	{
		String m=getAlbum();
		Bitmap bp=getArtistImage(m);
		return bp;
	}
	private Bitmap getArtistImage(String albumid) {
        Bitmap artwork = null;
        try { 
            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri,
                    Long.valueOf(albumid));
            ContentResolver res = m.getContentResolver();
            InputStream in = res.openInputStream(uri);
            artwork = BitmapFactory.decodeStream(in);
 
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } 
        return artwork;
    }

	@Override
	public int compareTo(Song another) {
		// TODO Auto-generated method stub
		
		return (this.getTitle().compareTo(another.getTitle()));
	} 
}
