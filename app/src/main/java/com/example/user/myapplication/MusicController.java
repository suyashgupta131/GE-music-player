package com.example.user.myapplication;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.MediaController;

public class MusicController extends MediaController {

	public MusicController(Context c){
		super(c);
	}

	public void hide(){
		//super.hide();	
	}
	 @Override
	 public boolean dispatchKeyEvent(KeyEvent event)
     { 
         super.hide();
         return super.dispatchKeyEvent(event);
     }
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

}