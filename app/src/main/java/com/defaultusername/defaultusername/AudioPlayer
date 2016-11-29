/**************************************
* file: AudioPlayer.java
* @author Ervin Maulas, Joel Woods, Jose Garcia, Alan Chen
* Class: CS 245 - Graphical User Interface
*
* Assignment: Android Memory Game
* @version v1.0
* Date Last Modified: 28 November 2016
* Purpose: Creates a class used to stop and play the audio
*
***************************************/
package com.defaultusername.defaultusername;

public class AudioPlayer
{
    private MediaPlayer mPlayer;

    public void stop()
    {
        if(mPlayer != null)
        {
            mPlayer.release();
            mPlayer = null;
        }
    }
    public void play(Context c)
    {
        mPlayer = MediaPlayer.create(c, R.raw.one_small_step);
        mPlayer.start();
    }

}
