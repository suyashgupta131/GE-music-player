package com.example.user.myapplication;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;


public class PlaylistDatabase {
	static String playListName[];
	static Playlist lists[];
	static ArrayList<Long> playlistIds=new ArrayList<Long>();
	static ArrayList<Playlist> play1;
	static Uri uri;
    
    public static void createPlaylist(Context context, String name, long[] trackIds) {
       
        ContentResolver resolver = context.getContentResolver();
        for(int i = 0;i<play1.size();i++)
        {
        if(name.equals(play1.get(i).toString()))
        {
        	 uri= ContentUris.withAppendedId(MediaStore.Audio.Playlists.getContentUri("external"),play1.get(i).getId());
        	break;
        }
        }       
        int size = trackIds.length;
        ContentValues[] values = new ContentValues[size];
        for (int i = 0; i < size; i++) {
            values[i] = new ContentValues();
            values[i].put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, i);
            values[i].put(MediaStore.Audio.Playlists.Members.AUDIO_ID, trackIds[i]);
        }
        
        resolver.bulkInsert(uri, values);
        Toast.makeText(context,"Added to Playlist : "+name, Toast.LENGTH_SHORT).show();
    }
    public static void makePlaylist(Context context, String name, long[] trackIds) {
        if (TextUtils.isEmpty(name)) return;
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Audio.Playlists.NAME, name);
        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Audio.Playlists.getContentUri("external"), cv);
        int size = trackIds.length;
        ContentValues[] values = new ContentValues[size];
        for (int i = 0; i < size; i++) {
            values[i] = new ContentValues();
            values[i].put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, i);
            values[i].put(MediaStore.Audio.Playlists.Members.AUDIO_ID, trackIds[i]);
        }
        
       try
       {
    	   resolver.bulkInsert(uri, values);
    	   Toast.makeText(context,"Playlist Created", Toast.LENGTH_SHORT).show();
       }
       catch(Exception e)
       {
    	   Toast.makeText(context,"Playlist of this name already exists", Toast.LENGTH_SHORT).show();
       }
     }
    

    public static ArrayList<Playlist> getPlaylists(Context context) {
    	  
     final ContentResolver resolver = context.getContentResolver();
     final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
     final String idKey = MediaStore.Audio.Playlists._ID;
     final String nameKey = MediaStore.Audio.Playlists.NAME;
     final String[] columns = { idKey, nameKey };
     final Cursor playLists = resolver.query(uri, columns, null, null, null);
     if (playLists == null) {
        
    	 Toast.makeText(context, "Found no playlist", Toast.LENGTH_SHORT).show();
         return null; 
     } 
     playListName=new String[playLists.getCount()];
     lists = new Playlist[playLists.getCount()];
     play1= new ArrayList<Playlist>();
     
     for (boolean hasItem = playLists.moveToFirst(); hasItem; hasItem = playLists.moveToNext()) {
        play1.add(new Playlist( new String(playLists.getString(playLists.getColumnIndex(nameKey))),new Long(playLists.getLong(playLists.getColumnIndex(idKey)))));
     }        
        return play1;
    }    
    /**
     * Get tracks of playlist. If playlistId equals -1 - return all tracks in system.
     */
    public static Song[] getTracks(Context context, long playlistId) {
        String[] projection = {
                (playlistId == -1) ?
                        MediaStore.Audio.Media._ID :
                        MediaStore.Audio.Playlists.Members.AUDIO_ID,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID
        };
        
        Cursor tracksCursor;
        if (playlistId == -1) {
            // Get all tracks.
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0 ";
            tracksCursor = query(context,
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection, selection, null, null, 0);
        } else {
            // Get tracks of playlist.
            tracksCursor = query(context,
                  MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
                  projection, null);
        }
        
        int size = tracksCursor.getCount();
        Song[] song = new Song[size];
        
        tracksCursor.moveToFirst();
        for (int i = 0; i < size; i++) {
            long audioId = tracksCursor.getInt(0);
            String artist = tracksCursor.getString(1);
            String title = tracksCursor.getString(2);
            String album = tracksCursor.getString(3);
            String data = tracksCursor.getString(tracksCursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            song[i] = new Song(audioId, title, artist,album,context,data);
            
            tracksCursor.moveToNext();
        }
        
        return song;
    }
    
    private static Cursor query(Context context,
                                Uri uri, String[] projection, String sortOrder) {
        return query(context, uri, projection, null, null, sortOrder, 0);
    }
    
    private static Cursor query(Context context, Uri uri, String[] projection,
                                String selection, String[] selectionArgs, String sortOrder, int limit) {
        try {
            ContentResolver resolver = context.getContentResolver();
            if (resolver == null) {
                return null;
            }
            if (limit > 0) {
                uri = uri.buildUpon().appendQueryParameter("limit", String.valueOf(limit)).build();
            }
            return resolver.query(uri, projection, selection, selectionArgs, sortOrder);
        } catch (UnsupportedOperationException ex) {
           
            return null;
        }
    }
}

