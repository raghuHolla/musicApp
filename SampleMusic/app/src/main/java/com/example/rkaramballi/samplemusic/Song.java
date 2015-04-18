package com.example.rkaramballi.samplemusic;/*
 * Copyright (c) 2015 PayPal, Inc.
 *
 * All rights reserved.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

/**
 * TODO: Write Javadoc for com.example.rkaramballi.samplemusic.Song.
 *
 * @author rkaramballi
 */
class Song {
	private long id;
	private String title;
	private String artist;

	public Song(long id, String title, String artist) {
		this.id = id;
		this.title = title;
		this.artist = artist;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
}
