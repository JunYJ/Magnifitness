package com.madmonkey.magnifitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashScreen extends Activity
{

	/**
	 * The thread to process splash screen events
	 */
	private Thread mSplashThread;
	private boolean mIsBackButtonPressed;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		// Splash screen view
		setContentView(R.layout.splash);

		// The thread to wait for splash screen events
		mSplashThread = new Thread() 
		{
			@Override
			public void run()
			{
				try 
				{
					synchronized (this) 
					{
						// Wait given period of time or exit on touch
						wait(5000);
					}
				} 
				
				catch (InterruptedException ex) 
				{
					
				}

				// Run next activity
				if (!mIsBackButtonPressed) 
				{
					startActivity(new Intent(SplashScreen.this, FacebookLogin.class));
					
					
				}
				finish();
				overridePendingTransition(R.anim.appear, R.anim.disappear);
			}
		};

		mSplashThread.start();
	}
	
	@Override
	public void onBackPressed() 
	{
		// set the flag to true so the next activity won't start up
	    mIsBackButtonPressed = true;
	    super.onBackPressed();
	}

	/**
	 * Processes splash screen touch events
	 */
	@Override
	public boolean onTouchEvent(MotionEvent evt) 
	{
		if (evt.getAction() == MotionEvent.ACTION_DOWN)
		{
			synchronized (mSplashThread) 
			{
				mSplashThread.notifyAll();
			}
		}
		return true;
	}
}