package com.madmonkey.magnifitness;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitnessClass.User;

public class SetupUserDetails extends Activity implements OnCheckedChangeListener {
	TextView			heightTV, weightTV;
	EditText			name, email, age, weightET, heightET;
	RadioGroup			genderRG;
	NumberPicker		weightPicker, heightPicker;
	Spinner				lvOfActiveness;
	RadioButton			maleRB, femaleRB;
	Button				okBtn;
	User				user;
	SharedPreferences	userSP;
	String				gender;
	String				ageString;
	int					ageInt;
	int					levelOfActiveness;
	Intent				nextActivity;

	boolean				userCreated	= false;
	DatabaseHandler		db;

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_detail_setup);
		getView();
		user = new User();
		userSP = getSharedPreferences(FacebookLogin.filename, 0);

		loadFromPreferences();


		okBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
					{
					ageString = age.getText().toString();

					ageInt = Integer.parseInt(ageString);

					int weight = weightPicker.getValue();

					int height = heightPicker.getValue();

					if (weight != 0 && height != 0)
						{
						user.setUser(name.getText().toString(), ageInt, email.getText().toString(), gender, weightPicker.getValue(),
								heightPicker.getValue(), lvOfActiveness.getSelectedItemPosition());
						userCreated = true;
						saveInformation();

						nextActivity = new Intent(SetupUserDetails.this, Home.class);
						nextActivity.putExtra("userObject", user);
						startActivity(nextActivity);

						finish();
						}
					else
						{
						if (weight == 0)
							Toast.makeText(getBaseContext(), "Please choose a weight", Toast.LENGTH_SHORT).show();
						else if (height == 0)
							Toast.makeText(getBaseContext(), "Please choose a height", Toast.LENGTH_SHORT).show();
						}

					}

			});

		lvOfActiveness.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
			{

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
					{
					levelOfActiveness = pos;

					}

				@Override
				public void onNothingSelected(AdapterView<?> arg0)
					{

					}

			});
		}

	private void getView()
		{
		heightTV = (TextView) findViewById(R.id.heightTV);
		weightTV = (TextView) findViewById(R.id.weightTV);
		name = (EditText) findViewById(R.id.username);
		email = (EditText) findViewById(R.id.emailET);
		genderRG = (RadioGroup) findViewById(R.id.genderRG);
		maleRB = (RadioButton) findViewById(R.id.rbMale);
		femaleRB = (RadioButton) findViewById(R.id.rbFemale);
		age = (EditText) findViewById(R.id.ageET);
		weightPicker = (NumberPicker) findViewById(R.id.weightPicker);
		weightET = (EditText) findViewById(R.id.weightET);
		weightET.setInputType(InputType.TYPE_NULL);
		heightET = (EditText) findViewById(R.id.heightET);
		heightET.setInputType(InputType.TYPE_NULL);
		heightPicker = (NumberPicker) findViewById(R.id.heightPicker);

		lvOfActiveness = (Spinner) findViewById(R.id.lvOfActivenssSpinner);
		okBtn = (Button) findViewById(R.id.okBtn);

		LayoutInflater inflater = getLayoutInflater();
		View selectWeightLayout = inflater.inflate(R.layout.select_weight, null);
		weightPicker = (NumberPicker) selectWeightLayout.findViewById(R.id.weightPicker);

		View selectHeightLayout = inflater.inflate(R.layout.select_height, null);
		heightPicker = (NumberPicker) selectHeightLayout.findViewById(R.id.heightPicker);

		genderRG.setOnCheckedChangeListener(this);
		genderRG.check(R.id.rbMale);
		}

	/** This method handle the RadioGroup */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
		{

		switch (checkedId)
			{
			case R.id.rbMale:
				gender = "Male";
				break;
			case R.id.rbFemale:
				gender = "Female";
				break;
			default:
				gender = "";
				break;
			}
		}

	public void saveInformation()
		{
		SharedPreferences shared = getSharedPreferences(FacebookLogin.filename, MODE_PRIVATE);
		SharedPreferences.Editor editor = shared.edit();
		// Insert value below
		editor.putString("name", name.getText().toString());

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email.getText().toString());

		if (matcher.matches())
			{
			editor.putString("email", email.getText().toString());
			}
		else
			{
			Toast.makeText(getApplication(), "Invalid email", Toast.LENGTH_LONG).show();
			}

		editor.putString("gender", gender);
		editor.putInt("age", Integer.parseInt(age.getText().toString()));
		editor.putInt("lvOfActiveness", lvOfActiveness.getSelectedItemPosition());
		// editor.putInt("weight", weightPicker.getValue());
		// editor.putInt("height", heightPicker.getValue());
		// editor.putInt("idealWeight", idealWeightPicker.getValue());
		editor.putBoolean("userCreated", userCreated);

		DecimalFormat df = new DecimalFormat("#.##");
		editor.putString("bmi", df.format(user.getBmi()) + "");
		editor.putString("bmr", df.format(user.getBmr()) + "");

		// commit changes to the SharedPref
		editor.commit();
		}

	private void loadFromPreferences()
		{
		SharedPreferences shared = getSharedPreferences(FacebookLogin.filename, MODE_PRIVATE);
		name.setText(shared.getString("name", ""));
		email.setText(shared.getString("email", ""));
		weightET.setText(userSP.getInt("weight", 0) + "");
		heightET.setText(userSP.getInt("height", 0) + "");
		if (shared.getString("gender", "").equalsIgnoreCase("male"))
			maleRB.setChecked(true);
		else if (shared.getString("gender", "").equalsIgnoreCase("female"))
			femaleRB.setChecked(true);

		age.setText(shared.getInt("age", 0) + "");
		lvOfActiveness.setSelection(shared.getInt("lvOfActiveness", 0));

		// weightPicker.setValue(shared.getInt("weight", 50));
		// heightPicker.setValue(shared.getInt("height", 150));
		// idealWeightPicker.setValue(shared.getInt("idealWeight", 50));
		}

	public void selectWeight(View v)
		{
		
		Log.i("Y U NO CLICKED", "I'M CLICKED ONCE");
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

		LayoutInflater inflater = getLayoutInflater();
		View selectWeightLayout = inflater.inflate(R.layout.select_weight, null);
		helpBuilder.setView(selectWeightLayout);

		weightPicker = (NumberPicker) selectWeightLayout.findViewById(R.id.weightPicker);
		weightPicker.setMaxValue(250);
		weightPicker.setMinValue(30);
		weightPicker.setValue(userSP.getInt("weight", 50));

		helpBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
			{

				public void onClick(DialogInterface dialog, int which)
					{
					SharedPreferences shared = getSharedPreferences(FacebookLogin.filename, MODE_PRIVATE);
					shared.edit().putInt("weight", weightPicker.getValue()).commit();

					String weight = "Weight (kg): ";//

					weightET.setText(weightPicker.getValue() + "");
					}
			});

		helpBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
					{

					}
			});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();

		helpDialog.show();
		}

	public void selectHeight(View v)
		{
		AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);

		LayoutInflater inflater = getLayoutInflater();
		View selectHeightLayout = inflater.inflate(R.layout.select_height, null);
		helpBuilder.setView(selectHeightLayout);

		heightPicker = (NumberPicker) selectHeightLayout.findViewById(R.id.heightPicker);
		heightPicker.setMaxValue(250);
		heightPicker.setMinValue(130);
		heightPicker.setValue(userSP.getInt("height", 50));

		helpBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener()
			{

				public void onClick(DialogInterface dialog, int which)
					{
					SharedPreferences shared = getSharedPreferences(FacebookLogin.filename, MODE_PRIVATE);
					shared.edit().putInt("height", heightPicker.getValue()).commit();

					String height = "Height (cm): ";

					heightET.setText(heightPicker.getValue() + "");
					}
			});

		helpBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
					{

					}
			});

		// Remember, create doesn't show the dialog
		AlertDialog helpDialog = helpBuilder.create();

		helpDialog.show();
		}
}
