package com.madmonkey.magnifitness;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class SplashScreen extends Activity
{

	/**
	 * The thread to process splash screen events
	 */
	private Thread mSplashThread;
	private boolean mIsBackButtonPressed;
	private ImageView redRing, greenRing, yellowRing, mText;
	private static Boolean runAnimation = true;
	private ViewSwitcher switcher;
	private Animation animationFadeIn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		if(runAnimation)
		{
			overridePendingTransition(R.anim.appear, R.anim.disappear);
		}
		// Splash screen view
		setContentView(R.layout.splash);
		
		switcher = (ViewSwitcher) findViewById(R.id.splashSwitcher);
		switcher.showNext();
		
		animationFadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear);
		Animation animationRed = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_animation);
		Animation animationGreen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_animation);;
		Animation animationYellow = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_animation);
		Animation animationText = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_animation);

		redRing = (ImageView) findViewById(R.id.red_ring);
		greenRing = (ImageView) findViewById(R.id.green_ring);
		yellowRing = (ImageView) findViewById(R.id.yellow_ring);
		mText = (ImageView) findViewById(R.id.m_text);
		
		if(runAnimation)
		{
			redRing.startAnimation(animationRed);
			greenRing.startAnimation(animationGreen);
			yellowRing.startAnimation(animationYellow);
			mText.startAnimation(animationText);
			//runAnimation = false;
		}
		
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
	
	/*@Override
	public void onBackPressed() 
	{
		// set the flag to true so the next activity won't start up
	    mIsBackButtonPressed = true;
	    super.onBackPressed();
	}

	//Processes splash screen touch events

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
	}*/
}