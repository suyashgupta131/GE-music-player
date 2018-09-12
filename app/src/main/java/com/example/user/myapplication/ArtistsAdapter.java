package com.example.user.myapplication;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

public class ArtistsAdapter extends CursorAdapter {
	 private LayoutInflater mInflater;
	 Context context;
	 ArrayList<String> albnames=new ArrayList<String>();
	 ArrayList<String> albnamesfrarts=new ArrayList<String>();
	 String albid;
	 
     public ArtistsAdapter(Context context, Cursor c) {
        super(context, c);
        this.context=context;
        mInflater= LayoutInflater.from(context);
    } 
 
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
 
        TextView artistTitle =(TextView)view.findViewById(R.id.albname1);
        Typeface t= Typeface.createFromAsset(context.getAssets(),"fonts/constanz.ttf");
        artistTitle.setTypeface(t);
        artistTitle.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
 
        ImageView artistArt=(ImageView)view.findViewById(R.id.albart1);
        Uri.Builder builder = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI.buildUpon();
   	    builder.appendPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)));
   	    builder.appendPath("albums");
   	    Uri uri = builder.build();
   	    albnames=getAlbumnames(uri);
   	    if(albnames!=null)
   	    {
   	    	//if(getArtistImage(getid(albnames.get(0)))!=null)
   	    
     	//artistArt.setImageBitmap(getArtistImage(getid(albnames.get(0))));
    	//else
   		artistArt.setImageResource(R.drawable.defalbart);
   	    }
   	    view.setTag(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)));
   	    
   	
   	 
        
 
    } 
 
 
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        @SuppressLint("ResourceType") View view=mInflater.inflate(R.layout.album,parent,false);
        return view;
    }
    public ArrayList<String> getAlbumnames (Uri uri)
	{
		 ContentResolver cr = context.getContentResolver();
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
	 
	        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
	                column, where, whereVal, orderBy);
	 
	        if (cursor.moveToFirst()) {
	            do { 
	            	albid=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
	            } while (cursor.moveToNext());	           
	        } 
	        return albid;
	}
    private Bitmap getArtistImage(String artistid) {
        Bitmap artwork = null;
        try { 
            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            
           
            Uri uri = ContentUris.withAppendedId(sArtworkUri,
                    Long.valueOf(artistid));
            ContentResolver res = context.getContentResolver();
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


	

}
