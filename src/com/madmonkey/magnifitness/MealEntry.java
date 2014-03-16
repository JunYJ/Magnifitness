package com.madmonkey.magnifitness;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitness.util.Search;
import com.madmonkey.magnifitnessClass.Food;

public class MealEntry extends Activity implements OnClickListener
{

	Button				add, confirm;
	// String[] foodList;
	DatabaseHandler		dbHandler;
	ListView			foodListView;
	ArrayList<String>	selectedFood;

	final static int	BREAKFAST	= 1;
	final static int	LUNCH		= 2;
	final static int	SNACK		= 3;
	final static int	DINNER		= 4;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meal_entry);
		initialization();

		Intent i = getIntent();
		int meal_type = i.getIntExtra("meal_type", 0);

		switch (meal_type)
		{
			case BREAKFAST:
				Toast.makeText(getApplication(), "Breakfast",
						Toast.LENGTH_SHORT).show();
				break;
			case LUNCH:
				Toast.makeText(getApplication(), "Lunch", Toast.LENGTH_SHORT)
						.show();
				break;
			case SNACK:
				Toast.makeText(getApplication(), "Snack", Toast.LENGTH_SHORT)
						.show();
				break;
			case DINNER:
				Toast.makeText(getApplication(), "Dinner", Toast.LENGTH_SHORT)
						.show();
				break;
		}

	}

	private void initialization()
	{
		add = (Button) findViewById(R.id.add_btn);
		confirm = (Button) findViewById(R.id.confirm_btn);

		add.setOnClickListener(this);
		confirm.setOnClickListener(this);

		foodListView = (ListView) findViewById(R.id.food_records);
		selectedFood = new ArrayList<String>();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onClick(View v)
	{
		dbHandler = new DatabaseHandler(this);
		ArrayList<Food> list = (ArrayList<Food>) dbHandler.getAllFood();
		for (int i = 0; i < list.size(); i++)
		{
			Log.i("DATABASE SIZE TEST: ", list.get(i).toString());
		}

		switch (v.getId())
		{

			case R.id.add_btn:
				Intent i = new Intent(this, Search.class);
				startActivityForResult(i,1);
				/*AlertDialog.Builder searchDialog = new AlertDialog.Builder(this);

				searchDialog.setTitle("Search Food");

				final ArrayList<Food> foodItems = (ArrayList<Food>) dbHandler
						.getAllFood();

				ArrayList<String> foodList = new ArrayList<String>();
				for (int i = 0; i < foodItems.size(); i++)
				{
					foodList.add(foodItems.get(i).getTitle());
				}

				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, foodList);

				searchDialog.setAdapter(adapter,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								// TODO Auto-generated method stub
								Toast.makeText(getApplicationContext(),
										foodItems.get(which).getTitle(),
										Toast.LENGTH_SHORT).show();

								selectedFood.add(foodItems.get(which)
										.getTitle());

								ArrayAdapter<String> fa = new ArrayAdapter<String>(
										getBaseContext(),
										android.R.layout.simple_list_item_1,
										selectedFood);
								foodListView.setAdapter(fa);
							}
						});

				searchDialog.show();*/

				break;

			case R.id.confirm_btn:
				Log.i("confirm_button", "Confirm button clicked");

				finish();
				break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 1)
		{
			if(resultCode == RESULT_OK)
			{
				selectedFood.add(dbHandler.getFood(data.getStringExtra("name")).getTitle());

				ArrayAdapter<String> fa = new ArrayAdapter<String>(
						getBaseContext(),
						android.R.layout.simple_list_item_1,
						selectedFood);
				foodListView.setAdapter(fa);
			}
		}
	}
	
	

}
