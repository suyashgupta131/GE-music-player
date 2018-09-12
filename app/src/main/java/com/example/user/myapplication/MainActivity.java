package com.example.user.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.AlbumFragment.onclick1;
import com.example.user.myapplication.ArtistFragment.onclick3;
import com.example.user.myapplication.MusicService.MusicBinder;
import com.example.user.myapplication.PlaylistFragment.onclick2;
import com.example.user.myapplication.SongFragment.onclick;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.TreeSet;

import static com.example.user.myapplication.R.drawable.ic_drawers;

public class MainActivity extends AppCompatActivity implements MediaPlayerControl,onclick,onclick1,onclick2,onclick3,LoaderCallbacks<Cursor> {
	Toolbar tool;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	 MenuItem shuffle;
	 MenuItem repeat;
	 
	private ActionBarDrawerToggle mDrawerToggle;
	private MusicService musicSrv;
	//private static final int SWIPE_MIN_DISTANCE=120;
	//private static final int SWIPE_THRESHOLD_VELOCITY=200;
	private Intent playIntent;

	static TreeSet<Artist> arts =new TreeSet<Artist>();
	AlbumsAdapter albAdapter;
	ArtistsAdapter artAdapter;
	//binding
	private boolean musicBound=false;	
	//controller
	private MusicController controller;
	//View m;
	BroadcastReceiver onPrepareReceiver ;
	//activity and playback pause flags
	private boolean paused=false, playbackPaused=false;
	// nav drawer title
	private CharSequence mDrawerTitle;
	// used to store app title
	private CharSequence mTitle;
	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;


	@SuppressWarnings("ResourceType")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//getAlbumlist();
		//getArtistlist();	
		mTitle = mDrawerTitle = getTitle();		
		// load slide menu items		
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		// nav drawer icons from resources
		navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.listslide);
		navDrawerItems = new ArrayList<>();
		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// What's hot, We  will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		View v= getLayoutInflater().inflate(R.layout.header,null);
		mDrawerList.addFooterView(v);

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
//tool=findViewById(R.id.tool);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,tool,R.string.app_name,R.string.app_name) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.addDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
		setController();
        onPrepareReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context c, Intent i) {
			    // When music player has been prepared, show controller
				if(isPlaying())
					setController();
			    controller.show(0);
			    }
			};
	}
		/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.toggle, menu);
		 shuffle = menu.findItem(R.id.shuffle);
		 repeat = menu.findItem(R.id.repeat);
		 
		return true;
	}	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if(item.getItemId()==R.id.sensor)
		{
			musicSrv.setIsLazy(item);
		}
		else if(item.getItemId()== R.id.shuffle)
		{
		musicSrv.setShuffle(item,repeat);	
		}
		else if(item.getItemId()== R.id.repeat)
		{
		musicSrv.setRepeat(item,shuffle);	
		}
			return super.onOptionsItemSelected(item);
		
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		//boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		final int pos=position;
		
		switch (position) {
		case 0:
			fragment = new SongFragment();
			
			break;
		case 1:
			
		LayoutInflater li1 = LayoutInflater.from(this);
           View view1= li1.inflate(R.layout.alertalbum, null,false);
           GridView gv21 = (GridView) view1.findViewById(R.id.gridView1);
           PlaylistAdapter pladp =new PlaylistAdapter(getApplicationContext(),PlaylistDatabase.getPlaylists(getApplicationContext()));
           gv21.setAdapter(pladp);           
           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_TRADITIONAL);
           alertDialogBuilder.setView(view1).setTitle(" Playlists");
          // create alert dialog 
          final AlertDialog alertDialog = alertDialogBuilder.create();
          // show it         
         alertDialog.show();         
         gv21.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	         Fragment fragment2;
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
				// TODO Auto-generated method stub
				setTitle(navMenuTitles[pos]);
				Bundle b = new Bundle();
     			String playlistname = ((TextView)arg1.findViewById(R.id.albname1)).getText().toString();
     			fragment2 = new PlaylistFragment();
	     		b.putString("playlistname",playlistname);	     				     			
	     		fragment2.setArguments(b);	     			
	     		FragmentManager fragmentManager = getFragmentManager();
	     		fragmentManager.beginTransaction().replace(R.id.frame_container, fragment2,"unique").commit();
	     	    alertDialog.dismiss();
	        	
			} 
	 
	           }); 
         mDrawerList.setItemChecked(position, true);
		 mDrawerList.setSelection(position);
		//setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);      
           break;
		case 2:      
			      LayoutInflater li = LayoutInflater.from(this);
		          View view= li.inflate(R.layout.alertalbum, null,false);
		          GridView gv2 = (GridView) view.findViewById(R.id.gridView1);
		          albAdapter=new AlbumsAdapter(this,null); 
		          
		         // mDrawerList.setItemChecked(position, false);
		        gv2.setAdapter(albAdapter);
		        getLoaderManager().initLoader(0, null,this);
		          
		         AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this, AlertDialog.THEME_TRADITIONAL);
	                alertDialogBuilder1.setView(view).setTitle("Albums");
		         //create alert dialog 
	               final AlertDialog alertDialog1 = alertDialogBuilder1.create();
	             // show it 	              
	              alertDialog1.show(); 
		          gv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		         Fragment fragment1;
		 
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
					setTitle(navMenuTitles[pos]);
					//mDrawerList.setSelection(pos);
					// TODO Auto-generated method stub
					Bundle b = new Bundle();
					//mDrawerList.setItemChecked(pos, true);
	     			String albname = ((TextView)arg1.findViewById(R.id.albname1)).getText().toString();
					 fragment1 = new AlbumFragment();				     	
		     		b.putString("albname",albname);
		     		fragment1.setArguments(b);		     			
		     		FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment1,"unique").commit();
		     		alertDialog1.dismiss();
		        	 //Toast.makeText(getApplicationContext(),arg2+" ", 1000).show();					
				} 		 
		           }); 
		         
	           mDrawerList.setItemChecked(position, true);
	  			mDrawerList.setSelection(position);
	         	//setTitle(navMenuTitles[position]);	  			
	  			mDrawerLayout.closeDrawer(mDrawerList);		 
			                      break;
		case 3:
			  LayoutInflater li3 = LayoutInflater.from(this);
              View view3= li3.inflate(R.layout.alertalbum, null,false);
              GridView gv3 = (GridView) view3.findViewById(R.id.gridView1);
              //getArtistlist();
         artAdapter=new ArtistsAdapter(getApplicationContext(),null); 
         gv3.setAdapter(artAdapter);
        // Customloader cr = new Customloader();
         getLoaderManager().initLoader(1, null,new LoaderCallbacks<Cursor>() {

			@Override
			public Loader<Cursor> onCreateLoader(int id, Bundle args) {
				// TODO Auto-generated method stub
				String select = null;
				 return new CursorLoader(getApplicationContext(), MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,ARTIST_SUMMARY_PROJECTION, select, null, null);
				
			}

			@Override
			public void onLoadFinished(Loader<Cursor> arg0, Cursor artCursor) {
				// TODO Auto-generated method stub
				artAdapter.swapCursor(artCursor); 
				
			}

			@Override
			public void onLoaderReset(Loader<Cursor> artCursor) {
				// TODO Auto-generated method stub
				artAdapter.swapCursor(null); 
				
			}
        	 
		});
         
         AlertDialog.Builder alertDialogBuilder3 = new AlertDialog.Builder(this, AlertDialog.THEME_TRADITIONAL);
            alertDialogBuilder3.setView(view3).setTitle("Artists");
          // create alert dialog 
           final AlertDialog alertDialog3 = alertDialogBuilder3.create();
          // show it 
          alertDialog3.show(); 
        
          gv3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         Fragment fragment1;
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
                                int arg2, long arg3) {
			// TODO Auto-generated method stub
			setTitle(navMenuTitles[pos]);
			Bundle b = new Bundle();
			 fragment1 = new ArtistFragment();		
			 b.putString("artistId",(String)arg1.getTag());
  			fragment1.setArguments(b);
     	    FragmentManager fragmentManager = getFragmentManager();
     	    fragmentManager.beginTransaction().replace(R.id.frame_container, fragment1,"unique").commit();
     	    alertDialog3.dismiss();
        	 //Toast.makeText(getApplicationContext(),arg2+" ", 1000).show();        	 			
		} 
           }); 
         
          mDrawerList.setItemChecked(position, true);
		  mDrawerList.setSelection(position);
			//setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
 
	                      break;
		case 4:
			fragment = new FeedbackFragment();
			break;
		case 5:
			fragment = new DeveloperFragment();
			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
						.replace(R.id.frame_container, fragment,"unique").commit();
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
		if(position==6)
		{
			Toast.makeText(getApplicationContext(), "Cavatina Music Player", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(isPlaying())
		{					
		if(controller.hasWindowFocus())
			{setController();
		controller.show(0);}
		}
		LocalBroadcastManager.getInstance(this).registerReceiver(onPrepareReceiver,
        new IntentFilter("MEDIA_PLAYER_PREPARED"));
	

      if(paused){	
	     if(isPlaying()||playbackPaused)
	     {	setController();
	        controller.show(0);
	        paused=false;}
         }
	}
	private  void setController(){
	
	if (controller == null) controller = new MusicController(this);
	//set previous and next button listeners
	controller.setPrevNextListeners(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			playNext();
		}
	}, new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			playPrev();
		}
	});
	//set and show
	controller.setMediaPlayer(this);
	controller.setAnchorView(findViewById(R.id.frame_container));
	controller.setEnabled(true);
	//controller.show(0);
}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		paused=true;
	}

	private void playNext(){
		musicSrv.playNext();
		if(playbackPaused){ 
			setController();
			playbackPaused=false;
		}
		controller.show(0);
	}

	private void playPrev(){
		musicSrv.playPrev();
		if(playbackPaused){
			setController();
			playbackPaused=false;
		}
		controller.show(0);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		moveTaskToBack(true);
	}

	@Override
	public boolean canPause() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canSeekForward() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}

	@Override
	public int getBufferPercentage() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getCurrentPosition() {
		// TODO Auto-generated method stub
		if(musicSrv!=null && musicBound )
			return musicSrv.getPosn();
		else return 0;
		
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		if(musicSrv!=null && musicBound )
			return musicSrv.getDur();
		else return 0;
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		if(musicSrv!=null && musicBound)
			return musicSrv.isPng();
		return false;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		playbackPaused=true;
		musicSrv.pausePlayer();
	}

	@Override
	public void seekTo(int pos) {
		// TODO Auto-generated method stub
		musicSrv.seek(pos);
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		musicSrv.go();
		
	}
	private ServiceConnection musicConnection = new ServiceConnection(){

  		@Override
  		public void onServiceConnected(ComponentName name, IBinder service) {
  		MusicBinder binder = (MusicBinder)service;
  			//get service
  			musicSrv = binder.getService();
  			//pass list
  			//musicSrv.setList(songList);
  			musicBound = true;
  		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	//start and bind the service when the activity starts
 	@Override
	public void onStart() {
  		super.onStart();
  		if(playIntent==null){
  			playIntent = new Intent(this, MusicService.class);
  			playIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
  			bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
  		startService(playIntent);
  		}
  	}


	@Override
	public void click(int i,TreeSet<Song> sg) {
		// TODO Auto-generated method stub
		musicSrv.setList(sg);
		musicSrv.setSong(i);
		
		
		
		
		musicSrv.playSong();
		setController();
		
		if(playbackPaused){
			//setController();
			playbackPaused=false;
		}
		if(controller.hasWindowFocus())
		controller.show(0);
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	stopService(playIntent);
	unbindService(musicConnection);
		//musicSrv=null;
	
	}
	
	static final String[] ALBUM_SUMMARY_PROJECTION = { MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST};
	String[] ARTIST_SUMMARY_PROJECTION = { MediaStore.Audio.Artists._ID, MediaStore.Audio.Artists.ARTIST};
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		  String select = null;
		return new CursorLoader(this, MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,ALBUM_SUMMARY_PROJECTION, select, null, null);
		
		
	}
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		albAdapter.swapCursor(arg1); 
		
	}
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		albAdapter.swapCursor(null); 
		
		
	}
	
}

