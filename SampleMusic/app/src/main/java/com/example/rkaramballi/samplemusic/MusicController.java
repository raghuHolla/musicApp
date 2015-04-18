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
import android.widget.MediaController;

/**
 * TODO: Write Javadoc for MusicController.
 *
 * @author rkaramballi
 */
public class MusicController extends MediaController {

	public MusicController(Context context) {
		super(context);
	}

	public void hide() {}
	
	public void setPrevNextListeners(OnClickListener onClickListener) {
	}
}
