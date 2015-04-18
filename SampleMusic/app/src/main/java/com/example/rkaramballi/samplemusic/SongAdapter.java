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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * TODO: Write Javadoc for SongAdapter.
 *
 * @author rkaramballi
 */
class SongAdapter extends BaseAdapter {

	private ArrayList<Song> songs;
	private LayoutInflater songInf;

	public SongAdapter(Context c, ArrayList<Song> theSongs) {
		songs = theSongs;
		songInf = LayoutInflater.from(c);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return songs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int row) {
		// TODO Auto-generated method stub
		return row;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//map to song layout
		LinearLayout songLay = (LinearLayout) songInf.inflate
				(R.layout.song, parent, false);
		//get title and artist views
		TextView songView = (TextView) songLay.findViewById(R.id.song_title);
		TextView artistView = (TextView) songLay.findViewById(R.id.song_artist);
		//get song using position
		Song currSong = songs.get(position);
		//get title and artist strings
		songView.setText(currSong.getTitle());
		artistView.setText(currSong.getArtist());
		//set position as tag
		songLay.setTag(position);
		return songLay;
	}

}
