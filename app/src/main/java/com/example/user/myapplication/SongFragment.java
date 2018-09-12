package com.example.user.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeSet;

public class SongFragment extends ListFragment implements MultiChoiceModeListener {
	static TreeSet<Song> songList=new TreeSet<Song>() ;
	ListView lv;
	View m;

	onclick p;
	int itr;
	ArrayList<Integer> trackpos=new ArrayList<Integer>();
	long trackid[];
	String[]palert;
	ArrayList<Song> songList1=new ArrayList<Song>() ;

	public SongFragment(){

	}
	      @Override
          public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_song, container, false);
                m=rootView;
                getSongList();

                songList1=new ArrayList<Song>(songList);
                setListAdapter(new SongsAdapter(getActivity(),songList));
                return rootView;
          }

	    @Override
		public void onDestroy() {
			// TODO Auto-generated method stub
	    	//getActivity().stopService(playIntent);
			//musicSrv=null;
			super.onDestroy();
		}


		 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			//paused=true;
		}


		  @Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();

		}

		 @Override
		public void onStop() {
			// TODO Auto-generated method stub
			super.onStop();
		}


		//method to retrieve song info from device
	     public  void  getSongList(){

			//query external audio
			ContentResolver musicResolver = getActivity().getContentResolver();
			Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
			//iterate over results if valid
			if(musicCursor!=null && musicCursor.moveToFirst()){
				//get columns
				int titleColumn = musicCursor.getColumnIndex
						(android.provider.MediaStore.Audio.Media.TITLE);
				int idColumn = musicCursor.getColumnIndex
						(android.provider.MediaStore.Audio.Media._ID);
				int artistColumn = musicCursor.getColumnIndex
						(android.provider.MediaStore.Audio.Media.ARTIST);
				int albumColumn = musicCursor.getColumnIndex
						(android.provider.MediaStore.Audio.Media.ALBUM_ID);
				int data = musicCursor.getColumnIndex
						(android.provider.MediaStore.Audio.Media.DATA);
				//add songs to list
				do {
					long thisId = musicCursor.getLong(idColumn);
					String thisTitle = musicCursor.getString(titleColumn);
					String thisArtist = musicCursor.getString(artistColumn);
					String thisAlbum = musicCursor.getString(albumColumn);
					String thisData = musicCursor.getString(data);
					songList.add(new Song(thisId, thisTitle, thisArtist,thisAlbum,getActivity().getApplicationContext(),thisData));
				}
				while (musicCursor.moveToNext());
				musicCursor.close();

			}

		}


		@Override
	    public void onActivityCreated(Bundle savedInstanceState) {

	        super.onActivityCreated(savedInstanceState);
	        ListView listView = getListView();
	        listView.setScrollingCacheEnabled(false);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(this);
		}

		 public interface onclick
	     {
	    	 void click(int i, TreeSet<Song> ar);
	     }
		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			p=(onclick)activity;			
			super.onAttach(activity);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO Auto-generated method stub
			super.onListItemClick(l, v, position, id);
			p.click(Integer.parseInt(v.getTag(R.string.id1).toString()),songList);
		}
		@RequiresApi(api = Build.VERSION_CODES.O)
		@SuppressLint("ResourceType")


		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			if(item.getItemId()==R.id.create)
			{
				
			AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
			b.setView(getActivity().getLayoutInflater().inflate(R.layout.createplaylist,null));
			b.setPositiveButton("Create", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				//Toast.makeText(getActivity(), ""+which, 1000).show();
				Dialog d = (Dialog)dialog;
				EditText tv = (EditText)d.findViewById(R.id.editText1);
				String str = tv.getText().toString();
				gettrackids(trackpos);
				PlaylistDatabase.makePlaylist(getActivity(), str,trackid);
			  }
			});
		       b.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//Toast.makeText(getActivity(), ""+which, 1000).show();
					dialog.cancel();
					
				}
		    	   
		       });
		       AlertDialog alert3 = b.create();
				alert3.setTitle("New Playlist");
				alert3.show();
			}
			else if(item.getItemId()==R.id.add)
			{
				AlertDialog.Builder b = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_DARK);
				b.setItems(palertget(), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					gettrackids(trackpos);
					PlaylistDatabase.createPlaylist(getActivity(), PlaylistDatabase.getPlaylists(getActivity()).get(which).getName(), trackid);
					dialog.cancel();
					}
				});
				
				  AlertDialog alert3 = b.create();
					alert3.setTitle("Add to Playlist");
					alert3.show();
			}			
				return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			MenuInflater inflater = mode.getMenuInflater();
	        inflater.inflate(R.menu.main, menu);
	        trackpos.clear();
	        return true; 

			
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
                                              long id, boolean checked) {
			// TODO Auto-generated method stub
			
//		   Toast.makeText(getActivity(), position+"", Toast.LENGTH_SHORT).show();
		  
		   if(checked)
			{
				
					trackpos.add(new Integer(position));
					
			}
			else
			{
				
					trackpos.remove(Integer.valueOf(position));
			}
			
		}
	
		
		
		void gettrackids(ArrayList<Integer> trackpos)
		{
			trackid=new long[trackpos.size()];
			for(int i=0;i<trackpos.size();i++)
			{
				trackid[i]=songList1.get(trackpos.get(i)).getID();
			}
		}		
		public String[] palertget()
		{ 
			 int i=0;
			 palert=new String[PlaylistDatabase.getPlaylists(getActivity()).size()];
			for(Playlist obj :PlaylistDatabase.getPlaylists(getActivity()))
			{
				palert[i]=obj.toString();
				i++;
			}
			
			return palert;
			
		}
		
}

