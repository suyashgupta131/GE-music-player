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
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

public class AlbumsAdapter extends CursorAdapter {

    private LayoutInflater mInflater;
    Context context;
     public AlbumsAdapter(Context context, Cursor c) {
        super(context, c);
        this.context=context;
        
        mInflater= LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor alcursor) {

        TextView albumTitle =(TextView)view.findViewById(R.id.albname1);
        Typeface t= Typeface.createFromAsset(context.getAssets(),"fonts/constanz.ttf");
        albumTitle.setTypeface(t);
 
        ImageView albumart=(ImageView)view.findViewById(R.id.albart1);
        try{
        
        
        if(alcursor!=null)
        {
          albumTitle.setText(alcursor.getString(alcursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
        // if(getArtistImage(alcursor.getString(alcursor.getColumnIndex(MediaStore.Audio.Albums._ID)))==null)
        	albumart.setImageResource(R.drawable.defalbart);
        // else
        //  albumart.setImageBitmap(getArtistImage(alcursor.getString(alcursor.getColumnIndex(MediaStore.Audio.Albums._ID))));
        }}
        catch(Exception e)
        {
        	
        }
      

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        @SuppressLint("ResourceType") View view=mInflater.inflate(R.layout.album,parent,false);
        return view;
    }
    private Bitmap getArtistImage(String albumid) {
        Bitmap artwork = null;
        try { 
            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri,
                    Long.valueOf(albumid));
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

	
