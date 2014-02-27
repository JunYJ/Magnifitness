package com.madmonkey.magnifitness.util;

import com.madmonkey.magnifitnessClass.Food;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper
{
	// Database Version
	private static final int	DATABASE_VERSION		= 1;

	// Database Name
	private static final String	DATABASE_NAME			= "MagnifitnessDB";

	// Table Names
	private static final String	TABLE_FOOD				= "food";
	private static final String	TABLE_USER				= "user";

	// Column Names
	// FOOD
	private static final String	KEY_TITLE				= "title";
	private static final String	KEY_MEASUREMENT_UNIT	= "measurementUnit";
	private static final String	KEY_CALORIE				= "calorie";
	private static final String	KEY_PHOTO				= "photo";
	private static final String	KEY_TYPE				= "type";

	// USER
	private static final String	KEY_NAME				= "name";
	private static final String	KEY_AGE					= "age";
	private static final String	KEY_PROFILE_PICTURE		= "profilePicture";
	private static final String	KEY_EMAIL				= "email";
	private static final String	KEY_GENDER				= "gender";
	private static final String	KEY_BMI					= "bmi";
	private static final String	KEY_BMR					= "bmr";
	private static final String	KEY_CALORIE_CAP			= "calorieCap";
	private static final String	KEY_CURRENT_WEIGHT		= "currentWeight";
	private static final String	KEY_HEIGHT				= "height";
	private static final String	KEY_IDEAL_WEIGHT		= "idealWeight";

	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		/* String CREATE_ROLE_TABLE = "CREATE TABLE " + TABLE_ROLE + "(" +
		 * KEY_ROLE_ID + " INTEGER PRIMARY KEY," + KEY_ROLE + " VARCHAR," +
		 * KEY_ROLE_LEVEL + " INT," + KEY_CONCURRENCY_ID + " TIMESTAMP," +
		 * KEY_CREATED_BY + " INTEGER," + KEY_CREATED_ON + " DATETIME," +
		 * KEY_MODIFIED_BY + " INT," + KEY_MODIFIED_ON + " DATETIME," +
		 * KEY_IS_ADMIN + " INTEGER NOT NULL" + ")"; */

		String CREATE_FOOD_TABLE = "CREATE TABLE " + TABLE_FOOD + "("
				+ KEY_TITLE + " TEXT PRIMARY KEY," + KEY_MEASUREMENT_UNIT
				+ " TEXT NOT NULL," + KEY_CALORIE + " REAL NOT NULL,"
				+ KEY_PHOTO + " BLOB," + KEY_TYPE + " STRING" + ")";
		
		String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "(";
		
		db.execSQL(CREATE_FOOD_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_FOOD);

	}
	
	public Food getFood(String title)
	{
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_FOOD,
				new String[] { KEY_TITLE, KEY_MEASUREMENT_UNIT, KEY_CALORIE,
						KEY_PHOTO, KEY_TYPE },
						KEY_TITLE + "=?", new String[] { String.valueOf(title) }, null,
				null, null, null);

		if (cursor != null)
		{
			cursor.moveToFirst();
		}

		Food food = null;
		/*Food user = new Food(cursor.getString(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4));*/

		return food;
	}
}
