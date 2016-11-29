public class MainFragment extends Fragment
{
	private AudioPlayer mPlayer = new AudioPlayer();
	private Button muteButton;

	muteButton = (Button)v.findViewById(R.id.hellomoon_muteButton);
	muteButton.setOnClickListener(
	{
		new View.OnClickListener()
		{
			public void on click(View v);
			{
				mPlayer.stop();
			}
		});
	}

	public void onStart()
	{
    mPlayer.onStart();
	}

	public void onResume()
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
    }
}
