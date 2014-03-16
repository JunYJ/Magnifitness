package com.madmonkey.magnifitness.util;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.madmonkey.magnifitness.R;
import com.madmonkey.magnifitnessClass.Food;

public class Search extends Activity
{
	private ListView		foodListView;
	EditText				inputSearch;

	ArrayAdapter<String>	adapter;
	DatabaseHandler			db;

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
		for(int i = 0; i < dbFoodList.size(); i++)
		{
			foodNameList.add(dbFoodList.get(i).getTitle());
		}
		
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, foodNameList);

		foodListView.setAdapter(adapter);
		
		foodListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos,
					long id)
			{
				// TODO Auto-generated method stub
				
				Intent returnIntent = new Intent();
				Food selectedFood = dbFoodList.get(pos);
				returnIntent.putExtra("name", selectedFood.getTitle());
				setResult(RESULT_OK, returnIntent);
				finish();
			}

			
		});
		
		inputSearch.addTextChangedListener(new TextWatcher()
		{

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

}
