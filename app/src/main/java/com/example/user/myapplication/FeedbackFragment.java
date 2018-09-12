package com.example.user.myapplication;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FeedbackFragment extends Fragment {
	public FeedbackFragment(){}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
		TextView tvt1= (TextView)rootView.findViewById(R.id.feed);
	   // Loading Font Face
       Typeface tf2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/constanz.ttf");
       // Applying font
       tvt1.setTypeface(tf2);
       //tvt.setMovementMethod(new ScrollingMovementMethod());
       tvt1.setMovementMethod(new ScrollingMovementMethod());
       tvt1.setText("Problems ? Feedback ?\n \n" +"The more you tell us, the better Musique will be."+" Since this audio player" +
   			" is still in Beta Version and is under developement so if you find a bug or if the player crashes, make sure that" +
   			" you let us know (and give us a chance to fix it) before you give us a bad review. \n");
       TextView tvt2=(TextView)rootView.findViewById(R.id.link);
      Typeface tf3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/constanz.ttf");
       // Applying font
       tvt2.setTypeface(tf3);      
       tvt2.setMovementMethod(new ScrollingMovementMethod());
       String linkText = "Write to us :  <a href='https://docs.google.com/forms/d/e/1FAIpQLSfFin5ZX8kFh6xhZadNT7eXWyqYP2WP2yvnOnpVZocgUP-7jQ/viewform'>Feedback@Musique</a> ";
       tvt2.setText(Html.fromHtml(linkText));
       tvt2.setMovementMethod(LinkMovementMethod.getInstance());
       TextView tvt3= (TextView)rootView.findViewById(R.id.fin);
	   // Loading Font Face
       Typeface tf4= Typeface.createFromAsset(getActivity().getAssets(), "fonts/constanz.ttf");
       // Applying font
       tvt3.setTypeface(tf4);
       //tvt.setMovementMethod(new ScrollingMovementMethod());
       tvt3.setMovementMethod(new ScrollingMovementMethod());
       tvt3.setText("\n Thank you for creating connections with the builders. It makes the problems more tangible and human.");      
       return rootView;
		
	}
}
