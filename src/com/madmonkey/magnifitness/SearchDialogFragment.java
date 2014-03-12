package com.madmonkey.magnifitness;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

public class SearchDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
		{
		AlertDialog.Builder searchDialog = new AlertDialog.Builder(getActivity());
		
		searchDialog.setTitle("Search Food");
		searchDialog.setItems(R.array.food_list, new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
					{
					
					Toast.makeText(getActivity(), "Item " + which + " is selected", Toast.LENGTH_SHORT).show();
					
					
					}
			});
		// Create the AlertDialog object and return it
		return searchDialog.create();
		}
	
	
	

}
