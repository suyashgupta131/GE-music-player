package com.example.user.myapplication;

import android.net.Uri;

public class Playlist {

	String name;
	long id;
	Uri uri;
	Playlist(String name, long id)
	{
		this.name=name;
		this.id=id;
		
	}
	Playlist(String name, Uri uri)
	{
		this.name = name;
		this.uri = uri;
	}
	String getName()
	{
		return(name);
	}
	long getId()
	{
		return(id);
	}
	Uri getUri()
	{
		return uri;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
		
	}
	
}
