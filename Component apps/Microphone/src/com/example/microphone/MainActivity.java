package com.example.microphone;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


//import android.R;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.appcompat.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	Button play, record, stopRec, stopPlay;
	TextView display;
	MediaRecorder mRecorder;
	MediaPlayer mPlayer;
	String mediaFile = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//initialize all the buttons
		initialize();
		
		//create TapTwistTunes folder in SD card and store recordings in it
		String newFolder = "/TapTwistTunes";
	    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	    File myNewFolder = new File(extStorageDirectory + "/" + newFolder);
	    myNewFolder.mkdir();
	    mediaFile = Environment.getExternalStorageDirectory().toString() + "/" + newFolder + "/myRecordings.mp3";
		 
			
		//disable buttons that can't use until we record
		stopRec.setEnabled(false);
		play.setEnabled(false);
		stopPlay.setEnabled(false);
		//start recording when I click record button
		record.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("Recording Audio");
				record(v);
			}
		});
		
		//stop recording when I click the first stop button
		stopRec.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("Audio Recorded");
				stopRec(v);
			}
		});
		
		//play recorded audio when I click play button
		play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("Playing the recording");
				play(v);
			}
		});
		
		//stop playing recorded audio when I click the second stop button 
		stopPlay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				display.setText("Stop playing the recording");
				stopPlayFunction(v);
			}
		});
	}
	
	private void initialize() {
		// TODO Auto-generated method stub
		record = (Button) findViewById(R.id.bRecord);
		stopRec = (Button) findViewById(R.id.bStopRec);
		play = (Button) findViewById(R.id.bPlay)
		stopPlay = (Button) findViewById(R.id.bStopPlay);
		display = (TextView) findViewById(R.id.actionText);
	}

	//call to record audio
	public void record(View v) {
		// TODO Auto-generated method stub
		//create new instance of MediaRecorder class
		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		mRecorder.setOutputFile(mediaFile);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		//exception handler 
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mRecorder.start();
		//enable the stopRec button and disable the rest
		stopRec.setEnabled(true);
		record.setEnabled(false);
		play.setEnabled(false);	
	}
	
	//call to stop recording audio
	public void stopRec(View v) {
		// TODO Auto-generated method stub
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		//enable play and record button and disable the rest
		stopRec.setEnabled(false);
		record.setEnabled(true);
		play.setEnabled(true);
		stopPlay.setEnabled(false);
	}
	
	//call to play recorded audio
	public void play(View v) {
		// TODO Auto-generated method stub
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(mediaFile);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//enable the stopPlay button and disable the rest
		if (mPlayer == null) {
			play.setEnabled(true);
			record.setEnabled(true);
		}
		play.setEnabled(false);
		stopPlay.setEnabled(true);
		stopRec.setEnabled(false);
	}
	
	//call to stop playing audio
	public void stopPlayFunction(View v) {
		// TODO Auto-generated method stub
		if (mPlayer!=null) {
			mPlayer.release();
			mPlayer = null;
			//enable the playButton and record and disable the rest
			stopPlay.setEnabled(false);
			play.setEnabled(true);
			record.setEnabled(true);
			stopRec.setEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

