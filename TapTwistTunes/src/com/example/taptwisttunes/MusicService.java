package com.example.taptwisttunes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.TextView;

public class MusicService extends Service implements
		MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
		MediaPlayer.OnCompletionListener {
	private MediaPlayer player; // Here is the media player
	private ArrayList<Song> songs; // Here is the song list
	private int songPosn; // This is a instance variable counter for the current
							// song position
	TextView current;

	private final IBinder musicBind = new MusicBinder();

	@Override
	public void onCreate() { // setup to creating the service when started
		super.onCreate(); // Create the service
		songPosn = 0; // Setting the song position counter
		player = new MediaPlayer(); // creating the media player

		initMusicPlayer();
	}

	public void initMusicPlayer() { // method that initializes the MediaPlayer
									// class and sets preferences for the player
		player.setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// the top 2 lines will allow music to continue to screen even after the
		// screen has gone idle

		player.setOnPreparedListener(this); // listener for when mediaplayer is
											// prepared
		player.setOnCompletionListener(this); // listener for when mediaplayer
												// has completed a song
		player.setOnErrorListener(this); // listener for when an error is
											// returned

	}

	public void setList(ArrayList<Song> theSongs) { // This method passes the
													// song list from main
													// activity
		songs = theSongs;
	}

	public class MusicBinder extends Binder { // works with setList to help
												// interaction between
												// mainactivity and service
												// classes
		MusicService getService() {
			return MusicService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) { // Controls binding
		return musicBind;
	}

	@Override
	public boolean onUnbind(Intent intent) { // This unbinds and releases
												// resources when the app is
												// closed
		player.stop();
		player.release();
		return false;
	}

	public String playSong() { // this method plays the song by retrieving ID
								// and modeling it as uri

		player.reset(); // resets media player
		System.out.println("Reset worked");
		Song playSong = songs.get(songPosn); // retrieves song to be played
		System.out.println("fuck this app");
		System.out.println(playSong);
		long currSong = playSong.getId();
		System.out.println("about to load song");
		Uri trackUri = ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				currSong);
		// System.out.println(trackUri);
		// System.out.println(trackUri.toString());
		System.out.println(playSong.getTitle());

		// try block not sure if uri will work as a data source

		try {
			player.setDataSource(getApplicationContext(), trackUri);
		} catch (Exception e) {
			Log.e("Music Service",
					"There has been an error setting the data source", e);
		}

		player.prepareAsync();
		return playSong.getTitle();
	}

	public void setSong(int songIndex) { // this method allows selection of the
											// current song
		songPosn = songIndex;
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) { // This will start the playback
		mp.start();

	}

}
