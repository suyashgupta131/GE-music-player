package com.example.user.myapplication;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeveloperFragment extends Fragment {
	
	public DeveloperFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_developer, container, false);
		TextView tvt= (TextView)rootView.findViewById(R.id.dev);
		// Loading Font Face
        Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/constanz.ttf");
        // Applying font
        tvt.setTypeface(tf1);
        //tvt.setMovementMethod(new ScrollingMovementMethod());
        tvt.setMovementMethod(new ScrollingMovementMethod());
        tvt.setText("Musique is a simple, clean and free music player for android. It features a classical interface" +
	    		" and the style is simply elegant. \n"+"\n"+"  � Quick share all music files and support" +
	    				" all the most popular music file formats \n"+"  � Browse and play your music by albums, artists, " +
	    						"songs and playlists \n"+"  � Support notification status: show album artwork, " +
	    								"play/pause and other media controls \n"+"  � Swipe over the sensor " +
	    										"to change playing music: next song \n"+"  � Easy playlist creation through Multi-Selection \n" 
	    												+ "  � Headset support \n \n"+
	    	"Musique is an effort put in by one budding engineer to show himself as a valuable asset in the field of innovation " +
	    	"and technology, giving the mobile product user an attractive and interacting music play experience. \n"					
	    										);
        
        return rootView;
		
	}
}
