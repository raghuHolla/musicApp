package com.example.rkaramballi.samplemusic;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class MusicActivity extends ActionBarActivity implements MediaController.MediaPlayerControl{

	private ArrayList<Song> mSongList;
	private ListView mSongView;
	private MusicService mMusicService;
	private Intent mPlayIntent;
	private boolean musicBound = false;
	private MusicController mMusicController;
	private boolean paused = false;
	private boolean playbackPaused = false;
	TextView txtTimeTotal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		mSongView = (ListView) findViewById(R.id.song_list);
		txtTimeTotal = (TextView) findViewById(R.id.time_total);
		mSongList = new ArrayList<>();
		getSongList();
		Collections.sort(mSongList, new Comparator<Song>() {
			public int compare(Song a, Song b) {
				return a.getTitle().compareTo(b.getTitle());
			}
		});
		SongAdapter songAdt = new SongAdapter(this, mSongList);
		mSongView.setAdapter(songAdt);
//		setController();
	}

	public void playSong(View view) {
		mMusicService.setSong(Integer.parseInt(view.getTag().toString()));
		mMusicService.playSong();
		getDurationView();
		if(playbackPaused){
//			setController();
			playbackPaused=false;
		}
//		mMusicController.show(0);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (mPlayIntent == null) {
			mPlayIntent = new Intent(this, MusicService.class);
			bindService(mPlayIntent, musicConnection, Context.BIND_AUTO_CREATE);
			startService(mPlayIntent);
		}
	}
//
//	private void setController() {
//		//set the controller up
//		mMusicController = new MusicController(this);
//		mMusicController.setMediaPlayer(this);
//		mMusicController.setAnchorView(findViewById(R.id.song_list));
//		mMusicController.setEnabled(true);
//		mMusicController.setPrevNextListeners(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				playNext();
//
//			}
//		}, new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				playPrev();
//			}
//		});
//	}


	//play next
	public void playNext(View view) {
		mMusicService.playNext();
		if(playbackPaused){
//		 	setController();
			playbackPaused=false;
		}
//		mMusicController.show(0);
	}

	//play previous
	public void playPrev(View view) {
		mMusicService.playPrev();
		if(playbackPaused){
//			setController();
			playbackPaused=false;
		}
//		mMusicController.show(0);
	}

	//connect to the service
	private ServiceConnection musicConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
			//get service
			mMusicService = binder.getService();
			//pass list
			mMusicService.setList(mSongList);
			musicBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			musicBound = false;
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_music, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_shuffle:
				//shuffle
				mMusicService.setShuffle();
				break;
			case R.id.action_end:
				stopService(mPlayIntent);
				mMusicService = null;
				System.exit(0);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		stopService(mPlayIntent);
		mMusicService = null;
		super.onDestroy();
	}

	@Override
	protected void onStop() {
//		mMusicController.hide();
		super.onStop();
	}

	private void getSongList() {


//		retrieve song info
		ContentResolver musicResolver = getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		if (musicCursor != null && musicCursor.moveToFirst()) {
			//get columns
			int titleColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media._ID);
			int artistColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.ARTIST);
			//add songs to list
			do {
				long thisId = musicCursor.getLong(idColumn);
				String thisTitle = musicCursor.getString(titleColumn);
				String thisArtist = musicCursor.getString(artistColumn);
				mSongList.add(new Song(thisId, thisTitle, thisArtist));
			}
			while (musicCursor.moveToNext());
		}
	}

	@Override
	public void start() {
		mMusicService.go();
	}

	@Override
	public void pause() {
		mMusicService.pausePlayer();
		paused = true;
		playbackPaused=true;
	}

	public void pauseorPlay(View view) {
		if (isPlaying()) {
			mMusicService.pausePlayer();
			paused = true;
			playbackPaused = true;
		} else {
			mMusicService.go();
		}
	}

	@Override
	protected void onResume(){
		super.onResume();
		if(paused){
//			setController();
			paused=false;
		}
	}

	public void getDurationView() {
		if (mMusicService != null && mMusicService.isPng() && musicBound) {
			txtTimeTotal.setText(mMusicService.getDur());
		} else {
			txtTimeTotal.setText("0");
		}
	}

	@Override
	public int getDuration() {
		if (mMusicService != null && mMusicService.isPng() && musicBound) {
			txtTimeTotal.setText(mMusicService.getDur());
			return mMusicService.getDur();
		} else {
			txtTimeTotal.setText(0);
			return 0;
		}
	}

	@Override
	public int getCurrentPosition() {
		if (mMusicService != null && mMusicService.isPng() && musicBound) {
			return mMusicService.getPosn();
		} else {
			return 0;
		}
	}

	@Override
	public void seekTo(int i) {
		mMusicService.seek(i);
	}

	@Override
	public boolean isPlaying() {
		if (mMusicService != null && musicBound) {
			return mMusicService.isPng();
		} else {
			return false;
		}
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}

//
//	@Override
//	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//		mMusicService.setSong(Integer.parseInt(view.getTag().toString()));
//		mMusicService.playSong();
//
//		if(playbackPaused){
//			setController();
//			playbackPaused=false;
//		}
//		mMusicController.show(0);
//	}
}
