package com.madmonkey.magnifitness.util;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.madmonkey.magnifitness.R;
import com.madmonkey.magnifitness.ServingSizeDialogFragment;
import com.madmonkey.magnifitnessClass.Food;

public class Search extends FragmentActivity
{
	private ListView		foodListView;
	EditText				inputSearch;

	Food					selectedFood;
	ArrayAdapter<String>	adapter;
	DatabaseHandler			db;
	Intent					returnIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		foodListView = (ListView) findViewById(R.id.list_view);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		db = new DatabaseHandler(this);

		final ArrayList<Food> dbFoodList = (ArrayList<Food>) db.getAllFood();

		ArrayList<String> foodNameList = new ArrayList<String>();
		for (int i = 0; i < dbFoodList.size(); i++)
		{
			foodNameList.add(dbFoodList.get(i).getTitle());
		}

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, foodNameList);

		foodListView.setAdapter(adapter);

		foodListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id)
			{
				// TODO Auto-generated method stub

				returnIntent = new Intent();
				selectedFood = dbFoodList.get(pos);
				returnIntent.putExtra("foodTitle", selectedFood.getTitle());
				returnIntent.putExtra("foodType", selectedFood.getType());
				returnIntent.putExtra("measure_unit",
						selectedFood.getMeasurementUnit());

				ServingSizeDialogFragment ssdf = new ServingSizeDialogFragment();
				Bundle args = new Bundle();
				args.putString("foodTitle", selectedFood.getTitle());
				args.putString("foodType", selectedFood.getType());
				args.putString("measure_unit",
						selectedFood.getMeasurementUnit());
				args.putDouble("calorie", selectedFood.getCalorie());

				ssdf.setArguments(args);
				ssdf.show(getSupportFragmentManager(), "Serving Size Dialog");

			}

		});

		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				adapter.getFilter().filter(s);
			}

		});
	}

	public void addSelectedFood(int servingSize)
	{
		returnIntent.putExtra("servingSize", servingSize);
		returnIntent.putExtra("calorie", selectedFood.getCalorie()
				* servingSize);
		setResult(RESULT_OK, returnIntent);
		// Log.i("Search.java (ServingSize): ", servingSize + "");
		finish();
	}

}
