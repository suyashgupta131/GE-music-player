package com.example.user.myapplication;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageLoader extends AsyncTask<Object, String, Bitmap> {
	 
    private View view;
    private Bitmap bitmap = null;
    Context c;
 
    @Override
    protected Bitmap doInBackground(Object... parameters) {
 
        // Get the passed arguments here 
        view = (View) parameters[0];
        String albumid = (String)parameters[1];
         c = (Context)parameters[2];
 
        // Create bitmap from passed in Uri here 
         
        // ...
       bitmap=  getArtistImage(albumid);
        return bitmap;
    } 
 
    @Override
    protected void onPostExecute(Bitmap bitmap) {
       // if (bitmap != null && view != null) {
           
           // ((ImageView) view).setImageBitmap(bitmap);
      //  } 
       // else if(bitmap==null)
       // {
        	 ((ImageView) view).setImageResource(R.drawable.defalbart);
       // }
    } 
    private Bitmap getArtistImage(String albumid) {
        Bitmap artwork = null;
        try { 
            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri uri = ContentUris.withAppendedId(sArtworkUri,
                    Long.valueOf(albumid));
            ContentResolver res = c.getContentResolver();
            InputStream in = res.openInputStream(uri);
            artwork = BitmapFactory.decodeStream(in);
 
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        } 
        return artwork;
    }
} 