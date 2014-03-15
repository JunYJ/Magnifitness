package com.madmonkey.magnifitness;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitnessClass.Food;

public class SplashScreen extends Activity
{

	/** The thread to process splash screen events */
	private Thread			mSplashThread;
	private boolean			mIsBackButtonPressed;
	private ImageView		redRing, greenRing, yellowRing, mText;
	private static Boolean	runAnimation		= true;
	private ViewSwitcher	switcher;
	// private Animation animationFadeIn;
	DatabaseHandler			db					= null;

	boolean					databasePopulated	= false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		initDatabase();
		
		startAnimations();

	}

	private void startAnimations()
	{
		if (runAnimation)
		{
			overridePendingTransition(R.anim.appear, R.anim.disappear);
		}

		// Splash screen view
		setContentView(R.layout.splash);

		switcher = (ViewSwitcher) findViewById(R.id.splashSwitcher);
		switcher.showNext();

		// animationFadeIn =
		// AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear);
		Animation animationRed = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.red_ring_animation);
		Animation animationGreen = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.green_ring_animation);
		;
		Animation animationYellow = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.yellow_ring_animation);
		Animation animationText = AnimationUtils.loadAnimation(
				getApplicationContext(), R.anim.text_animation);

		redRing = (ImageView) findViewById(R.id.red_ring);
		greenRing = (ImageView) findViewById(R.id.green_ring);
		yellowRing = (ImageView) findViewById(R.id.yellow_ring);
		mText = (ImageView) findViewById(R.id.m_text);

		if (runAnimation)
		{
			redRing.startAnimation(animationRed);
			greenRing.startAnimation(animationGreen);
			yellowRing.startAnimation(animationYellow);
			mText.startAnimation(animationText);
			// runAnimation = false;
		}

		// The thread to wait for splash screen events
		mSplashThread = new Thread() {
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
					startActivity(new Intent(SplashScreen.this,
							FacebookLogin.class));
				}

				finish();
				overridePendingTransition(R.anim.appear, R.anim.disappear);
			}
		};

		mSplashThread.start();

	}

	/* @Override public void onBackPressed() { // set the flag to true so the
	 * next activity won't start up mIsBackButtonPressed = true;
	 * super.onBackPressed(); } //Processes splash screen touch events
	 * 
	 * @Override public boolean onTouchEvent(MotionEvent evt) { if
	 * (evt.getAction() == MotionEvent.ACTION_DOWN) { synchronized
	 * (mSplashThread) { mSplashThread.notifyAll(); } } return true; } */

	private void populateFoodDatabase() throws XmlPullParserException,
			IOException
	{
		List<Food> foodList = db.getAllFood();

		if (foodList.size() <= 0)
		{
			XmlResourceParser parser = getResources().getXml(R.xml.foodlist);

			while (parser.next() != XmlPullParser.END_TAG)
			{
				if (parser.getEventType() != XmlPullParser.START_TAG)
				{
					continue;
				}

				String tag = parser.getName();
				if (tag.equals("Food"))
				{
					String title = "";
					String measurementUnit = "";
					double calorie = 0.0;
					String type = "";
					while (parser.next() != XmlPullParser.END_TAG)
					{
						if (parser.getEventType() != XmlPullParser.START_TAG)
						{
							continue;
						}
						tag = parser.getName();
						if (tag.equals("Title"))
						{
							title = readText(parser);
						}
						else if (tag.equals("MeasurementUnit"))
						{
							measurementUnit = readText(parser);
						}
						else if (tag.equals("Calorie"))
						{
							calorie = Double.parseDouble(readText(parser));
						}
						else if (tag.equals("Type"))
						{
							type = readText(parser);
						}
					}

					Food food = new Food(title, measurementUnit, calorie, type);

					db.addFood(food);
				}
			}
		}
	}

	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException
	{
		String result = "";
		if (parser.next() == XmlPullParser.TEXT)
		{
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void initDatabase()
	{
		if (db == null)
		{
			db = new DatabaseHandler(this);
			db.getReadableDatabase();
			db.close();
			Log.i("DATABASE CREATED: ", "TRUE");
		}

		File database = getBaseContext().getDatabasePath(db.getDatabaseName());

		if (databasePopulated == false && database.exists() == true)
		{
			Log.i("DATABASE EXIST: ", "TRUE");

			try
			{
				populateFoodDatabase();
			}
			catch (XmlPullParserException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			databasePopulated = true;
		}
		else
			Log.i("EXIST: ", "FALSE");

	}
}