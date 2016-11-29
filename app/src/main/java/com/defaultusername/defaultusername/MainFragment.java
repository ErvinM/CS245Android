/**************************************
* file: MainFragment.java
* @author Ervin Maulas, Joel Woods, Jose Garcia, Alan Chen
* Class: CS 245 - Graphical User Interface
*
* Assignment: Android Memory Game
* @version v1.0
* Date Last Modified: 28 November 2016
* Purpose: contains methods needed to utilize
* the music for the game
***************************************/

package com.defaultusername.defaultusername;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainFragment extends Fragment
{
	private AudioPlayer mPlayer = new AudioPlayer();
	private Button muteButton;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);

	}
	public  void start(Context c)
	{
		mPlayer.play(c);
	}
	public void stop()
	{
		mPlayer.stop();
	}

	/*public void onResume()
  {
     mPlayer.resume();
     if(mPlayer == null)
     {
	    initializeAudioPlayer();
     }
   }
   public void onPause()
   {
    	mPlayer.pause();
	    if(mPlayer != null)
      {
		    mPlayer.release();
		    mPlayer = null;
	    }
    }

   public void onStop()
	 {
      mPlayer.stop();
	 }

  public void onDestroy()
    {
        mPlayer.stop();
        mPlayer.release();
    }
    public void onCreate()
    {
    	mPlayer.onCreate();
	    mPlayer = MediaPlayer.create(this, R.raw.one_small_step);
    }*/
}
