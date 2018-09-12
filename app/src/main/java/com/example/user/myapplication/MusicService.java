package com.example.user.myapplication;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Random;
import java.util.TreeSet;
import java.util.TreeSet;

public class MusicService extends Service implements
MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
MediaPlayer.OnCompletionListener {
   Notification status;
	//media player
	private MediaPlayer player;
	//song list
	public TreeSet<Song> songs;
	//current position
	private int songPosn;
	//binder
	private final IBinder musicBind = new MusicBinder();
	//title of current song
    String songTitle="";
	//notification id
    //private static final int NOTIFY_ID=1;
	//shuffle flag and random
	private boolean shuffle=false;
	private boolean pauset=false;
	private boolean repeat=false;
	private Random rand;
	boolean isprepared=false;
	public SensorManager sensorManager;
	public Sensor proximitySensor;
	public SensorEventListener l;
	boolean isPause=false;
	boolean isLazy=false;
	RemoteViews views;
	ImageView img;
	Song playSong;
	 
public class ProximitySensorEventListener implements SensorEventListener {
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub			
		}
		
		boolean timerOn = false;
		CountDownTimer timer = new CountDownTimer(1000,200){
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				if(isLazy)
					playpause();				
			}
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub								
			}			
		};
		@Override
		public void onSensorChanged(SensorEvent se) {
			// TODO Auto-generated method stub
			System.out.println(se.values[0]);
			if (se.values[0] < proximitySensor.getMaximumRange()){											
						timer.start();					
				}
		    else{					
				if(isLazy)
				playNext();
				}
			}				
	}

PhoneStateListener phoneStateListener = new PhoneStateListener() {
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        if (state == TelephonyManager.CALL_STATE_RINGING) {
        	if(player.isPlaying())
        	{
        		pausePlayer();
        		pauset=true;
        	}
        	
        } else if(state == TelephonyManager.CALL_STATE_IDLE) {
            //Not in call: Play music
        	if(pauset)
        	{
        		go();
        		pauset=false;
        	}
        } else if(state == TelephonyManager.CALL_STATE_OFFHOOK) {
            //A call is dialing, active or on hold
        	 pausePlayer();
        }
        super.onCallStateChanged(state, incomingNumber);
    }
};


	public void setIsLazy(MenuItem item)
	{
		if(isLazy)
		{
			isLazy=false;
			Toast.makeText(getApplicationContext(), "Sensor Disabled", Toast.LENGTH_SHORT).show();
			item.setIcon(R.drawable.sensorss);
		}
		else
		{   isLazy=true;
			Toast.makeText(getApplicationContext(), "Sensor Enabled-swipe over the sensor located at the top most edge to play the next song ", Toast.LENGTH_LONG).show();
			item.setIcon(R.drawable.sensor1ss);
		}
	}
	public void onCreate(){
		//create the service
		super.onCreate();
		//initialize position
		songPosn=0;
		//random
		rand=new Random();
		//create player
		player = new MediaPlayer();
		//initialize
		initMusicPlayer();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		l = new ProximitySensorEventListener();
		//turn on sensor if needed
		sensorManager.registerListener(l, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
		TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if(mgr != null) {
		    mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(intent!=null&&intent.getAction()!=null)
		{
			
		
		if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
			
		//Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
		 
		} else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)) 
		{
		//Toast.makeText(this, "Clicked Previous", Toast.LENGTH_SHORT).show();
	    playPrev();
		
		} 
		else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION))
		{
	    Intent onPreparedIntent = new Intent("MEDIA_PLAYER_PREPARED");
	    LocalBroadcastManager.getInstance(this).sendBroadcast(onPreparedIntent);
		//Toast.makeText(this, "Clicked Play", Toast.LENGTH_SHORT).show();
		
		if(isPng())
			{
			views.setImageViewResource(R.id.playpause,android.R.drawable.ic_media_play);
			status.contentView=views;
			startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);			
		    pausePlayer();		   			
			}
		else 
		{
			views.setImageViewResource(R.id.playpause,android.R.drawable.ic_media_pause);
			status.contentView=views;
			startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);						
			go();
		}		    
		} 
		else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION))
		{
		//Toast.makeText(this, "Clicked Next", Toast.LENGTH_SHORT).show();
		playNext();
		} 
		else if (intent.getAction().equals(
		Constants.ACTION.STOPFOREGROUND_ACTION))
		{
		//Toast.makeText(this, "Service Stoped", Toast.LENGTH_SHORT).show();
		pausePlayer();
		Intent onPreparedIntent = new Intent("MEDIA_PLAYER_PREPARED");
	    LocalBroadcastManager.getInstance(this).sendBroadcast(onPreparedIntent);
		stopForeground(true);
		stopSelf();
		}		
		return START_STICKY;}
		return START_STICKY;
	}
	public void initMusicPlayer(){
		//set player properties
		player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		//set listeners
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	//pass song list
	public void setList(TreeSet<Song> theSongs){
		songs=theSongs;
	}

	//binder
	public class MusicBinder extends Binder {
		MusicService getService() { 
			return MusicService.this;
		}
	}

	//activity will bind to service
	@Override
	public IBinder onBind(Intent intent) {
		return musicBind;
	}

	//release resources when unbind
	@Override
	public boolean onUnbind(Intent intent){
		player.stop();
		player.release();
		return false;
	}

	//play a song
	public void playSong(){
		//play
		if(player!=null)
		{  
			//player.pause();
			//player.seekTo(0);
			player.reset();
		//get song 
			int i=0;
		if(songs!=null)
		{
			
			for(Song s: songs) {
			   if(i==songPosn)
			   {
				   playSong=s;
				   break;
			   }
			   else i++;
			} 
			//playSong = songs.get(songPosn);
		
		//get title
		songTitle=playSong.getTitle();
		//get id
		long currSong = playSong.getID();
		//set uri
		Uri trackUri = ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				currSong);
		//set the data source
		try{ player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setDataSource(getApplicationContext(), trackUri);
			player.prepareAsync(); 
		}
		catch(Exception e){
			Log.e("MUSIC SERVICE", "Error setting data source", e);
		}
		//player.prepareAsync(); 
		}}
	}

	//set the song
	public void setSong(int songIndex){
		songPosn=songIndex;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		//check if playback has reached the end of a track
		if(player.getCurrentPosition()>0){
			mp.reset();
			playNext();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Log.v("MUSIC PLAYER", "Playback Error");
		mp.reset();
		return false;
	}

	@SuppressLint("NewApi") @Override
	public void onPrepared(MediaPlayer mp) {
		//start playback
		  // Broadcast intent to activity to let it know the media player has been prepared
	    Intent onPreparedIntent = new Intent("MEDIA_PLAYER_PREPARED");
	    LocalBroadcastManager.getInstance(this).sendBroadcast(onPreparedIntent);
	    Log.d("mp","prepared");
	   // if(mp==player)
		mp.start();
		noti();
		isprepared=true;
	}
	private void noti() {
		// TODO Auto-generated method stub
		//Using RemoteViews to bind custom layouts into Notification
		 views = new RemoteViews(getPackageName(),R.layout.statusbar);
		views.setViewVisibility(R.id.title, View.VISIBLE);
		views.setViewVisibility(R.id.songart, View.VISIBLE);
		views.setViewVisibility(R.id.playnext, View.VISIBLE);
		views.setViewVisibility(R.id.playpause, View.VISIBLE);
		views.setViewVisibility(R.id.playprev, View.VISIBLE);
	   //if(playSong.getAlbumArt()==null)
		//{
		   views.setImageViewResource(R.id.songart,R.drawable.defart);
		//}
	  //else
	   //{
		//  views.setImageViewBitmap(R.id.songart,playSong.getAlbumArt());
	   //}
	    views.setTextViewText(R.id.title,playSong.getTitle());
	    views.setImageViewResource(R.id.playpause,android.R.drawable.ic_media_pause);	 
		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
		notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		Intent previousIntent = new Intent(this, MusicService.class);
		previousIntent.setAction(Constants.ACTION.PREV_ACTION);
		PendingIntent ppreviousIntent = PendingIntent.getService(this, 0,
		previousIntent, 0);
		 
		Intent playIntent = new Intent(this, MusicService.class);
		playIntent.setAction(Constants.ACTION.PLAY_ACTION);
		PendingIntent pplayIntent = PendingIntent.getService(this, 0,
		playIntent, 0);
		 
		Intent nextIntent = new Intent(this, MusicService.class);
		nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
		PendingIntent pnextIntent = PendingIntent.getService(this, 0,
		nextIntent, 0);
		 
		Intent closeIntent = new Intent(this, MusicService.class);
		closeIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
		PendingIntent pcloseIntent = PendingIntent.getService(this, 0,
		closeIntent, 0);
		 
		views.setOnClickPendingIntent(R.id.playpause, pplayIntent);
		//bigViews.setOnClickPendingIntent(R.id.status_bar_play, pplayIntent);
		 
		views.setOnClickPendingIntent(R.id.playnext, pnextIntent);
		//bigViews.setOnClickPendingIntent(R.id.status_bar_next, pnextIntent);
		 
		views.setOnClickPendingIntent(R.id.playprev, ppreviousIntent);
		//bigViews.setOnClickPendingIntent(R.id.status_bar_prev, ppreviousIntent);
		 
		views.setOnClickPendingIntent(R.id.close, pcloseIntent);
		//bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, pcloseIntent);
	 
		status = new Notification.Builder(this).build();
		status.contentView = views;
		//status.bigContentView = bigViews;
		status.flags = Notification.FLAG_ONGOING_EVENT;
		status.icon = R.drawable.defart;
		status.tickerText=playSong.getTitle();
		status.contentIntent = pendingIntent;
		startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
		
	}

	//playback methods
	public int getPosn(){
		if(player!=null)
		return player.getCurrentPosition();
		else return 0;
	}

	public int getDur(){
		if(player!=null&&isprepared)
		return player.getDuration();
		else return 0 ;
	}

	public boolean isPng(){
		if(player!=null)
		return player.isPlaying();
		else 
			return false;
	}

	public void pausePlayer(){
		player.pause();
		isPause=true;
		views.setImageViewResource(R.id.playpause,android.R.drawable.ic_media_play);
		status.contentView=views;
		startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
	}

	public void seek(int posn){
		player.seekTo(posn);
	}

	public void go(){
		if(isprepared)
		{
			player.start();
			views.setImageViewResource(R.id.playpause,android.R.drawable.ic_media_pause);
			status.contentView=views;
			startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, status);
		}
	}

	//skip to previous track
	public void playPrev(){
		if(songs==null)
			return;
		else
		{
			songPosn--;
		
		if(songPosn<0) songPosn=songs.size()-1;
		playSong();
		}
	}

	//skip to next
	public void playNext(){
		/*if(shuffle){
			int newSong = songPosn;
			while(songs!=null && newSong==songPosn){
				newSong=rand.nextInt(songs.size());
			}
			songPosn=newSong;
		}
		else if(repeat){
			
			}
			
		
		else{
			songPosn++;
			if(songs!=null)
			{
			if(songs!=null && songPosn>=songs.size()) songPosn=0;
			}
		}
		
		playSong();*/
		if(shuffle){
			int newSong = songPosn;
			while(newSong==songPosn){
				newSong=rand.nextInt(songs.size());
			}
			songPosn=newSong;
		}
		else{
			songPosn++;
			if(songPosn>=songs.size()) songPosn=0;
		}
		playSong();
	}



	@Override
	public void onDestroy() {
		stopForeground(true);
		player.release();
		player=null;
	}
	void playpause()
	{
		
			if (isPause&&player!=null){
				player.start();
				
			}
			else if(isPng())
				{
				player.pause();
				isPause = true;
			}
		
	}

	//toggle shuffle
	public void setShuffle(MenuItem shuffleitem, MenuItem repeatitem){
		if(shuffle) 
			{shuffle=false;
			shuffleitem.setIcon(R.drawable.shuffle);
			Toast.makeText(getApplicationContext(),"Shuffle Off", Toast.LENGTH_SHORT).show();
			}
	
		else 
			{
			shuffle=true;
			shuffleitem.setIcon(R.drawable.shuffle1);
			repeat=false;
			repeatitem.setIcon(R.drawable.repeat);
			//Toast.makeText(getApplicationContext(),"Repeat off", Toast.LENGTH_SHORT).show();
			Toast.makeText(getApplicationContext(),"Shuffle On", Toast.LENGTH_SHORT).show();
			}
	}
	public void setRepeat(MenuItem repeatitem, MenuItem shuffleitem){
		if(repeat) 
			{
			repeat=false;
			Toast.makeText(getApplicationContext(),"Repeat off", Toast.LENGTH_SHORT).show();
			repeatitem.setIcon(R.drawable.repeat);
			}
	
		else 
			{
			repeat=true;
			shuffle=false;
			shuffleitem.setIcon(R.drawable.shuffle);
			
			Toast.makeText(getApplicationContext(),"Repeat On", Toast.LENGTH_SHORT).show();
			repeatitem.setIcon(R.drawable.repeat1);
			//Toast.makeText(getApplicationContext(),"Shuffle Off",Toast.LENGTH_SHORT).show();
			}
	}

}
