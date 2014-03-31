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
	private static final String	KEY_TITLE				= "title";
	private static final String	KEY_MEASUREMENT_UNIT	= "measurementUnit";
	private static final String	KEY_CALORIE				= "calorie";
	private static final String	KEY_TYPE				= "type";
	
	// MEAL ENTRY
	private static final String KEY_MEAL_TYPE 			= "mealType";
	private static final String KEY_DATE 				= "date";
	private static final String KEY_TOTAL_CALORIE 		= "totalCalorie";
	private static final String KEY_SELECTED_FOOD		= "selectedFood";
	private static final String KEY_NUM_EACH_FOOD		= "numOfEachFood";
	
	String CREATE_FOOD_TABLE = "CREATE TABLE " + TABLE_FOOD + "("
			+ KEY_TITLE + " TEXT PRIMARY KEY," + KEY_MEASUREMENT_UNIT
			+ " TEXT NOT NULL," + KEY_CALORIE + " REAL NOT NULL,"
		    + KEY_TYPE + " TEXT" + ")";
	
	String CREATE_USER_MEAL_ENTRY_TABLE = "CREATE TABLE " + TABLE_USER_MEAL_ENTRY + "(" + KEY_MEAL_TYPE + " TEXT NOT NULL,"
			+ KEY_DATE + " TEXT NOT NULL," + KEY_TOTAL_CALORIE + " REAL NOT NULL," + KEY_SELECTED_FOOD + " TEXT," 
			+ KEY_NUM_EACH_FOOD + " TEXT" + ")";
	
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
		
		//Store food using string separated by ","
		String foodTitleStr = "";
		//Store number of each food in meal entry
		String numOfEachFoodStr = "";
		
		ArrayList<Food> foodList = mealEntry.getFoodList();
		for(int i = 0; i < foodList.size(); i++)
		{
			if(i == foodList.size() - 1)
			{
				foodTitleStr = foodTitleStr + foodList.get(i).getTitle();
				numOfEachFoodStr = numOfEachFoodStr + foodList.get(i).getNumOfEntry();
			}
			else
			{
				foodTitleStr = foodTitleStr + foodList.get(i).getTitle() + ",";
				numOfEachFoodStr = numOfEachFoodStr + foodList.get(i).getNumOfEntry() + ",";
			}
						
			Log.i("(DB) Food in current Meal Entry (FoodList): ", foodList.get(i).getTitle());
			Log.i("(DB) Number of each food in Meal Entry: ", foodList.get(i).getNumOfEntry() + "");
		}
		values.put(KEY_SELECTED_FOOD, foodTitleStr);
		values.put(KEY_NUM_EACH_FOOD, numOfEachFoodStr);
		
		Log.i("(DB) Food consumed for " + mealEntry.getMealType() + " at " + mealEntry.getDateStr(), foodTitleStr);
		Log.i("New Meal Entry added: ", mealEntry.getMealType() + " at " + mealEntry.getDateStr());
		
		db.insert(TABLE_USER_MEAL_ENTRY, null, values);
		db.close();
	}

	//Get specific Meal Entry
	public MealEntry getMealEntry(String mealType, String dateStr)
	{
		SQLiteDatabase db = getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_USER_MEAL_ENTRY, new String [] {KEY_MEAL_TYPE, KEY_DATE, KEY_TOTAL_CALORIE, KEY_SELECTED_FOOD ,KEY_NUM_EACH_FOOD }, 
				KEY_MEAL_TYPE + "=? AND " + KEY_DATE + "=?", new String[] { String.valueOf(mealType), String.valueOf(dateStr) }
		, null, null, null, null);
		
		MealEntry mealEntry = null;
		
		if(cursor != null)
		{
			cursor.moveToFirst();
		}
		
		//if mealEntry is found
		if(cursor.getCount() > 0)
		{
			/*Log.i("CURSOR MOVE TO FIRST: ", cursor.moveToFirst() + "");
			Log.i("CURSOR GET COUNT: ", cursor.getCount() + "");
			Log.i("CURSOR GET COLUMN COUNT: ", cursor.getColumnCount() + "");*/
			/*Log.i("CURSOR: ", cursor.toString());
			Log.i("CURSOR C(1): ", cursor.getString(0));
			Log.i("CURSOR C(2): ", cursor.getString(1));
			Log.i("CURSOR C(3): ", cursor.getString(2));
			Log.i("CURSOR C(4): ", cursor.getString(3));*/
			mealEntry = new MealEntry();
			mealEntry.setMealType(cursor.getString(0));
			mealEntry.setDateStr(cursor.getString(1));
			mealEntry.setTotalCalorie(Double.parseDouble(cursor.getString(2)));
			
			//Split the foodStr into individual title
			//Get Food that matched the title
			//Store into food list
			String foodTitleStr = cursor.getString(3);
			List<String> foodTitleSplitStr = Arrays.asList(foodTitleStr.split("\\s*,\\s*"));
			
			String numOfEachFoodStr = cursor.getString(4);
			List<String> numOfEachFoodSplitStr = Arrays.asList(numOfEachFoodStr.split("\\s*,\\s*"));
			
			//All food in database
			ArrayList<Food> dbFoodList = (ArrayList<Food>) getAllFood();
			
			//Search matched food & store
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
			
			for(int i = 0; i < foodList.size(); i++)
			{
				foodList.get(i).setNumOfEntry(Integer.parseInt(numOfEachFoodSplitStr.get(i)));
			}
			
			mealEntry.setFoodList(foodList);
			
			for(int i = 0; i < mealEntry.getFoodList().size(); i++)
			{
				Log.i("Foods of matched Meal Entry: ", mealEntry.getFoodList().get(i).getTitle());
			}
		}
		else
		{
			Log.i("ERROR: ", "NO MEAL ENTRY IS FOUND");
		}
		
		return mealEntry;
	}
	
	//Get all Meal Entry
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
				
				String foodTitleStr = cursor.getString(3); 
				List<String> foodTitleSplitStr = Arrays.asList(foodTitleStr.split("\\s*,\\s*"));
				
				String numOfEachFoodStr = cursor.getString(4);
				List<String> numOfEachFoodSplitStr = Arrays.asList(numOfEachFoodStr.split("\\s*,\\s*"));
				
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
				
				for(int i = 0; i < foodList.size(); i++)
				{
					foodList.get(i).setNumOfEntry(Integer.parseInt(numOfEachFoodSplitStr.get(i)));
				}
				
				mealEntry.setFoodList(foodList);
				
				mealEntryList.add(mealEntry);
			}
			while(cursor.moveToNext());
		}
		
		return mealEntryList;
	}
	
	//Get all Meal Entries of the day
	public List<MealEntry> getTodayMealEntry(String dateStr)
	{
		List<MealEntry> mealEntryList = new ArrayList<MealEntry>();
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_USER_MEAL_ENTRY, new String [] {KEY_MEAL_TYPE, KEY_DATE, KEY_TOTAL_CALORIE, KEY_SELECTED_FOOD ,KEY_NUM_EACH_FOOD }, 
				KEY_DATE + "=?", new String[] { String.valueOf(dateStr) }
		, null, null, null, null);
		
		if(cursor.moveToFirst())
		{
			do
			{
				MealEntry mealEntry = new MealEntry();
				mealEntry.setMealType(cursor.getString(0));
				mealEntry.setDateStr(cursor.getString(1));
				mealEntry.setTotalCalorie(Double.parseDouble(cursor.getString(2)));
				
				//Split the foodStr into individual title
				//Get Food that matched the title
				//Store into food list
				String foodTitleStr = cursor.getString(3);
				List<String> foodTitleSplitStr = Arrays.asList(foodTitleStr.split("\\s*,\\s*"));
				
				String numOfEachFoodStr = cursor.getString(4);
				List<String> numOfEachFoodSplitStr = Arrays.asList(numOfEachFoodStr.split("\\s*,\\s*"));
				
				//All Food in database
				ArrayList<Food> dbFoodList = (ArrayList<Food>) getAllFood();
				
				ArrayList<Food> foodList = new ArrayList<Food>();
				
				//Search matched Food & store
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
				Log.i("(DB)MEAL TYPE: ", mealEntry.getMealType());
				Log.i("(DB)FOOD LIST SIZE: ", foodList.size() + "");
				Log.i("(DB)NUM_OF_EACH_FOOD_SPLIT_STR: ", numOfEachFoodSplitStr + "");
				for(int i = 0; i < foodList.size(); i++)
				{
					foodList.get(i).setNumOfEntry(Integer.parseInt(numOfEachFoodSplitStr.get(i)));
				}
				
				mealEntry.setFoodList(foodList);
				
				mealEntryList.add(mealEntry);
			}
			while(cursor.moveToNext());
		}
		return mealEntryList;
		
	}

	public void updateMealEntry(MealEntry mealEntry)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_MEAL_TYPE, mealEntry.getMealType());
		values.put(KEY_DATE, mealEntry.getDateStr());
		values.put(KEY_TOTAL_CALORIE, mealEntry.getTotalCalorie());
		
		String foodTitleStr = "";
		String numOfEachFoodStr = "";
		
		ArrayList<Food> foodList = mealEntry.getFoodList();
		for(int i = 0; i < foodList.size(); i++)
		{
			if(i == foodList.size() - 1)
			{
				foodTitleStr = foodTitleStr + foodList.get(i).getTitle();
				numOfEachFoodStr = numOfEachFoodStr + foodList.get(i).getNumOfEntry();
			}
			else
			{
				foodTitleStr = foodTitleStr + foodList.get(i).getTitle() + ",";
				numOfEachFoodStr = numOfEachFoodStr + foodList.get(i).getNumOfEntry() + ",";
			}
						
			Log.i("DB (Meal Entry Food): ", foodList.get(i).getTitle());
		}
		
		values.put(KEY_SELECTED_FOOD, foodTitleStr);
		values.put(KEY_NUM_EACH_FOOD, numOfEachFoodStr);
		
		db.update(TABLE_USER_MEAL_ENTRY, values, KEY_MEAL_TYPE + "=? AND " 
		+ KEY_DATE + "=?", new String[] { String.valueOf(mealEntry.getMealType()), String.valueOf(mealEntry.getDateStr()) });
		
		db.close();
		Log.i("UPDATE: ", "UPDATE SUCCESS");
	}
	
	public void removeFoodFromMealEntry(MealEntry mealEntry, ArrayList<Food> tempFoodList)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_MEAL_TYPE, mealEntry.getMealType());
		values.put(KEY_DATE, mealEntry.getDateStr());
		
		
		ArrayList<Food> mealEntryFoodList = mealEntry.getFoodList();
		
		for(int i = 0; i < tempFoodList.size(); i++)
		{
			for(int count = 0; count < mealEntryFoodList.size(); count++)
			{
				if(mealEntryFoodList.get(count).equals(tempFoodList.get(i)))
				{
					mealEntry.getFoodList().remove(count);
					break;
				}
			}
		}
		double totalCalorie = 0.0;
		
		for(int i = 0; i < mealEntryFoodList.size(); i++)
		{
			totalCalorie += mealEntryFoodList.get(i).getCalorie();
		}
		values.put(KEY_TOTAL_CALORIE, totalCalorie);
		
		String foodTitleStr = "";
		String numOfEachFoodStr = "";
		
		for(int i = 0; i < mealEntryFoodList.size(); i++)
		{
			if(i == mealEntryFoodList.size() - 1)
			{
				foodTitleStr = foodTitleStr + mealEntryFoodList.get(i).getTitle();
				numOfEachFoodStr = numOfEachFoodStr + mealEntryFoodList.get(i).getNumOfEntry();
			}
			else
			{
				foodTitleStr = foodTitleStr + mealEntryFoodList.get(i).getTitle() + ",";
				numOfEachFoodStr = numOfEachFoodStr + mealEntryFoodList.get(i).getNumOfEntry() + ",";
			}
						
			Log.i("DB (Meal Entry Food): ", mealEntryFoodList.get(i).getTitle());
		}
		
		values.put(KEY_SELECTED_FOOD, foodTitleStr);
		values.put(KEY_NUM_EACH_FOOD, numOfEachFoodStr);
		
		db.update(TABLE_USER_MEAL_ENTRY, values, KEY_MEAL_TYPE + "=? AND " 
		+ KEY_DATE + "=?", new String[] { String.valueOf(mealEntry.getMealType()), String.valueOf(mealEntry.getDateStr()) });
		
		db.close();
		Log.i("REMOVE: ", "REMOVE SUCCESS");
	}
}
