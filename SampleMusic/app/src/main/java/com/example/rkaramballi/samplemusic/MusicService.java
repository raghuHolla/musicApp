/*
 * Copyright (c) 2015 PayPal, Inc.
 *
 * All rights reserved.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.example.rkaramballi.samplemusic;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

/**
 * TODO: Write Javadoc for MusicService.
 *
 * @author rkaramballi
 */
public class MusicService extends Service implements
		MediaPlayer.OnPreparedListener,
		MediaPlayer.OnErrorListener,
		MediaPlayer.OnCompletionListener {

	//media player
	private MediaPlayer player;
	//song list
	private ArrayList<Song> songs;
	//current position
	private int songPosn;
	private final IBinder musicBind = new MusicBinder();
	private String songTitle = "";
	private static final int NOTIFY_ID = 1;
	private boolean shuffle = false;
	private Random rand;

	@Override
	public void onCreate() {
		super.onCreate();
		//initialize position
		songPosn = 0;
		//create player
		player = new MediaPlayer();
		initMusicPlayer();
		rand = new Random();
	}

	private void initMusicPlayer() {
		//set player properties
		player.setWakeMode(getApplicationContext(),
				PowerManager.PARTIAL_WAKE_LOCK);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnPreparedListener(this);
		player.setOnCompletionListener(this);
		player.setOnErrorListener(this);
	}

	public void setList(ArrayList<Song> theSongs) {
		songs = theSongs;
	}

	public class MusicBinder extends Binder {
		MusicService getService() {
			return MusicService.this;
		}
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		if(player.getCurrentPosition()>0) {
			mediaPlayer.reset();
			playNext();
		}
	}

	@Override
	public boolean onError(MediaPlayer mediaPlayer, int position, int extra) {
		mediaPlayer.reset();
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		mediaPlayer.start();
		Intent notIntent = new Intent(this, MusicActivity.class);
		notIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendInt = PendingIntent.getActivity(this, 0,
				notIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(this);

		builder.setContentIntent(pendInt)
				.setSmallIcon(R.drawable.android_music_player_play)
				.setTicker(songTitle)
				.setOngoing(true)
				.setContentTitle("Playing")
				.setContentText(songTitle);
		Notification not = builder.build();

		startForeground(NOTIFY_ID, not);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return musicBind;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		player.stop();
		player.release();
		return false;
	}

	public void setSong(int songIndex) {
		songPosn = songIndex;
	}

	public void playSong() {
		//play a song
		player.reset();
		//get song
		Song playSong = songs.get(songPosn);

		songTitle = playSong.getTitle();
//get id
		long currSong = playSong.getId();
//set uri
		Uri trackUri = ContentUris.withAppendedId(
				android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				currSong);
		try {
			player.setDataSource(getApplicationContext(), trackUri);
		} catch (Exception e) {
			Log.e("MUSIC SERVICE", "Error setting data source", e);
		}
		player.prepareAsync();

	}

	@Override
	public void onDestroy() {
		stopForeground(true);
	}

	public int getPosn() {
		return player.getCurrentPosition();
	}

	public int getDur() {
		return player.getDuration();
	}

	public boolean isPng() {
		return player.isPlaying();
	}

	public void pausePlayer() {
		player.pause();
	}

	public void seek(int posn) {
		player.seekTo(posn);
	}

	public void go() {
		player.start();
	}

	public void playPrev() {
		songPosn--;
		if (songPosn < 0) {
			songPosn = songs.size() - 1;
		}
		playSong();
	}

	//skip to next
	public void playNext() {
		if (shuffle) {
			int newSong = songPosn;
			while (newSong == songPosn) {
				newSong = rand.nextInt(songs.size());
			}
			songPosn = newSong;
		} else {
			songPosn++;
			if (songPosn >= songs.size()) {
				songPosn = 0;
			}
		}
		playSong();
	}

	public void setShuffle() {
//		if (shuffle) {
//			shuffle = false;
//		} else {
//			shuffle = true;
//		}
		shuffle = !shuffle;
	}
}
