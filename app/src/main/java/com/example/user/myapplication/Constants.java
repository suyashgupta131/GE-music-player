package com.example.user.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Constants {
    public interface ACTION {
    public static String MAIN_ACTION = "main";
    public static String INIT_ACTION = "init";
    public static String PREV_ACTION = "prev";
    public static String PLAY_ACTION = "play";
    public static String NEXT_ACTION = "next";
    public static String STARTFOREGROUND_ACTION = "startforeground";
    public static String STOPFOREGROUND_ACTION = "stopforeground";

    }

    public interface NOTIFICATION_ID {
    public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
    Bitmap bm = null;
    BitmapFactory.Options options = new BitmapFactory.Options();
    try {
    bm = BitmapFactory.decodeResource(context.getResources(),
    R.drawable.defalbart, options);
    } catch (Error ee) {
    } catch (Exception e) {
    }
    return bm;
    }
}
