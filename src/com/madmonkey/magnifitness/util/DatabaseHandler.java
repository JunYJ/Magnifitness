package com.madmonkey.magnifitness.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.madmonkey.magnifitnessClass.Food;
import com.madmonkey.magnifitnessClass.MealEntry;

public class DatabaseHandler extends SQLiteOpenHelper
{
	// Database Version
	private static final int	DATABASE_VERSION		= 1;

	// Database Name
	private static final String	DATABASE_NAME			= "MagnifitnessDB";

	// Table Names
	private static final String	TABLE_FOOD				= "food";
	private static final String	TABLE_USER_MEAL_ENTRY	= "userMealEntry";

	// Column Names
	// FOOD
	//private static final String KEY_FOOD_ID				= "id";
	private static final String	KEY_TITLE				= "title";
	private static final String	KEY_MEASUREMENT_UNIT	= "measurementUnit";
	private static final String	KEY_CALORIE				= "calorie";
	private static final String	KEY_TYPE				= "type";
	
	// MEAL ENTRY
	private static final String KEY_MEAL_TYPE 			= "mealType";
	private static final String KEY_DATE 				= "date";
	private static final String KEY_TOTAL_CALORIE 		= "totalCalorie";
	private static final String KEY_SELECTED_FOOD		= "selectedFood";

	// USER
	/*private static final String	KEY_NAME				= "name";
	private static final String	KEY_AGE					= "age";
	private static final String	KEY_PROFILE_PICTURE		= "profilePicture";
	private static final String	KEY_EMAIL				= "email";
	private static final String	KEY_GENDER				= "gender";
	private static final String	KEY_BMI					= "bmi";
	private static final String	KEY_BMR					= "bmr";
	private static final String	KEY_CALORIE_CAP			= "calorieCap";
	private static final String	KEY_CURRENT_WEIGHT		= "currentWeight";
	private static final String	KEY_HEIGHT				= "height";
	private static final String	KEY_IDEAL_WEIGHT		= "idealWeight";*/
	
	String CREATE_FOOD_TABLE = "CREATE TABLE " + TABLE_FOOD + "("
			+ KEY_TITLE + " TEXT PRIMARY KEY," + KEY_MEASUREMENT_UNIT
			+ " TEXT NOT NULL," + KEY_CALORIE + " REAL NOT NULL,"
		    + KEY_TYPE + " TEXT" + ")";
	
	String CREATE_USER_MEAL_ENTRY_TABLE = "CREATE TABLE " + TABLE_USER_MEAL_ENTRY + "(" + KEY_MEAL_TYPE + " TEXT NOT NULL,"
			+ KEY_DATE + " TEXT NOT NULL," + KEY_TOTAL_CALORIE + " REAL NOT NULL," + KEY_SELECTED_FOOD + " TEXT" + ")";
	
	/*String CREATE_USER_MEAL_ENTRY_TABLE = "CREATE TABLE " + TABLE_USER_MEAL_ENTRY + "(" + KEY_MEAL_TYPE + " TEXT NOT NULL,"
			+ KEY_DATE + " TEXT NOT NULL," + KEY_TOTAL_CALORIE + " REAL NOT NULL," + KEY_SELECTED_FOOD + " TEXT," + " FOREIGN KEY ("
			+ KEY_SELECTED_FOOD + ") REFERENCES " + TABLE_FOOD + " (" + KEY_TITLE + "))";*/

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

		db.execSQL(CREATE_FOOD_TABLE);
		Log.i("EXECSQL", CREATE_FOOD_TABLE);
		db.execSQL(CREATE_USER_MEAL_ENTRY_TABLE);
		Log.i("EXECSQL", CREATE_USER_MEAL_ENTRY_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_FOOD);
	}
	
	public void addFood(Food food)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// values.put(KEY_USER_ID, user.getUserID());
		values.put(KEY_TITLE, food.getTitle());
		values.put(KEY_MEASUREMENT_UNIT, food.getMeasurementUnit());
		values.put(KEY_CALORIE, food.getCalorie());
		values.put(KEY_TYPE, food.getType());

		db.insert(TABLE_FOOD, null, values);
		db.close();
	}
	
	public Food getFood(String title)
	{
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_FOOD,
				new String[] { KEY_TITLE, KEY_MEASUREMENT_UNIT, KEY_CALORIE,
						 KEY_TYPE },
						KEY_TITLE + "=?", new String[] { String.valueOf(title) }, null,
				null, null, null);

		if (cursor != null)
		{
			cursor.moveToFirst();
		}

		//Food food = null;
		Food food = new Food(cursor.getString(0), cursor.getString(1),
				Double.parseDouble(cursor.getString(2)), cursor.getString(3));

		return food;
	}
	
	public List<Food> getAllFood()
	{
		List<Food> foodList = new ArrayList<Food>();
		String selectQuery = "SELECT * FROM " + TABLE_FOOD;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				Food food = new Food(cursor.getString(0), cursor.getString(1),
						Double.parseDouble(cursor.getString(2)), cursor.getString(3));
				
				foodList.add(food);
			}
			while(cursor.moveToNext());
		}
		
		return foodList;
	}
	
	public void addMealEntry(MealEntry mealEntry)
	{
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// values.put(KEY_USER_ID, user.getUserID());
		values.put(KEY_MEAL_TYPE, mealEntry.getMealType());
		values.put(KEY_DATE, mealEntry.getDateStr());
		values.put(KEY_TOTAL_CALORIE, mealEntry.getTotalCalorie());
		
		String foodTitleStr = "";
		ArrayList<Food> foodList = mealEntry.getFoodList();
		for(int i = 0; i < foodList.size(); i++)
		{
			if(i == foodList.size() - 1)
			{
				foodTitleStr = foodTitleStr + foodList.get(i).getTitle();
			}
			else
			{
				foodTitleStr = foodTitleStr + foodList.get(i).getTitle() + ",";
			}
						
			Log.i("DB (Meal Entry Food): ", foodList.get(i).getTitle());
		}
		values.put(KEY_SELECTED_FOOD, foodTitleStr);
		Log.i("DB FOOD STR: ", foodTitleStr);
		
		/*Log.i("MEAL_TYPE", mealEntry.getMealType());
		Log.i("DATE", mealEntry.getDateStr());
		Log.i("TOTAL CALORIE", mealEntry.getTotalCalorie() + "");*/
		
		db.insert(TABLE_USER_MEAL_ENTRY, null, values);
		db.close();
	}
	
	/*public MealEntry getMealEntry(String mealType, String dateStr)
	{
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_USER_MEAL_ENTRY, new String [] {KEY_MEAL_TYPE, KEY_DATE, KEY_TOTAL_CALORIE, KEY_SELECTED_FOOD }, 
				KEY_MEAL_TYPE + "=? AND " + KEY_DATE + "=?", new String[] { String.valueOf(mealType), String.valueOf(dateStr) }
		, null, null, null, null);
		
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		
		MealEntry mealEntry = new MealEntry();
		mealEntry.setMealType(cursor.getString(0));
		mealEntry.setDateStr(cursor.getString(1));
		mealEntry.setTotalCalorie(Double.parseDouble(cursor.getString(2)));
		
		Log.i("DB (Meal Entry food list): ", cursor.getString(3));
		ArrayList<Food> dbFoodList = (ArrayList<Food>) getAllFood();
		
		//for(int i = 0; i < dbFoodList.)
		ArrayList<Food> foodList = new ArrayList<Food>();
		foodList.add(getFood(cursor.getString(3)));
		mealEntry.setFoodList(foodList);
		
		return mealEntry;
	}*/
	
	public MealEntry getMealEntry(String mealType)
	{
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_USER_MEAL_ENTRY, new String [] {KEY_MEAL_TYPE, KEY_DATE, KEY_TOTAL_CALORIE, KEY_SELECTED_FOOD }, 
				KEY_MEAL_TYPE + "=?", new String[] { String.valueOf(mealType) }
		, null, null, null, null);
		
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		
		MealEntry mealEntry = new MealEntry();
		mealEntry.setMealType(cursor.getString(0));
		mealEntry.setDateStr(cursor.getString(1));
		mealEntry.setTotalCalorie(Double.parseDouble(cursor.getString(2)));
		
		/*Log.i("DB (Meal Entry food list): ", cursor.getString(3));
		ArrayList<Food> dbFoodList = (ArrayList<Food>) getAllFood();
		
		//for(int i = 0; i < dbFoodList.)
		ArrayList<Food> foodList = new ArrayList<Food>();
		foodList.add(getFood(cursor.getString(3)));
		mealEntry.setFoodList(foodList);*/
		
		String foodTitleStr = cursor.getString(3);
		List<String> foodTitleSplitStr = Arrays.asList(foodTitleStr.split("\\s*,\\s*"));
		ArrayList<Food> dbFoodList = (ArrayList<Food>) getAllFood();
		
		ArrayList<Food> foodList = new ArrayList<Food>();
		
		for(int i = 0; i < foodTitleSplitStr.size(); i++)
		{
			for(int count = 0; count < dbFoodList.size(); count++)
			{
				if(dbFoodList.get(count).getTitle().equals(foodTitleSplitStr.get(i)))
				{
					foodList.add(dbFoodList.get(count));
					break;
				}
			}
		}
		
		mealEntry.setFoodList(foodList);
		
		for(int i = 0; i < mealEntry.getFoodList().size(); i++)
		{
			Log.i("TESTING: ", mealEntry.getFoodList().get(i).getTitle());
		}
		
		return mealEntry;
	}
	
	public List<MealEntry> getAllMealEntry()
	{
		List<MealEntry> mealEntryList = new ArrayList<MealEntry>();
		String selectQuery = "SELECT * FROM " + TABLE_USER_MEAL_ENTRY;
		
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				MealEntry mealEntry = new MealEntry();
				mealEntry.setMealType(cursor.getString(0));
				mealEntry.setDateStr(cursor.getString(1));
				mealEntry.setTotalCalorie(Double.parseDouble(cursor.getString(2)));
				
				ArrayList<Food> dbFoodList = (ArrayList<Food>) getAllFood();
				
				ArrayList<Food> foodList = new ArrayList<Food>();
				//foodList.add(getFood(cursor.getString(3)));
				mealEntry.setFoodList(foodList);
				mealEntryList.add(mealEntry);
			}
			while(cursor.moveToNext());
		}
		
		return mealEntryList;
	}
}
