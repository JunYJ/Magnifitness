package com.madmonkey.magnifitness.util;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.madmonkey.magnifitness.R;
import com.madmonkey.magnifitnessClass.Achievement;

public class AchievementAdapter extends ArrayAdapter<Achievement>
{

	private Context									context;
	private ArrayList<Achievement>	achievementList;

	TextView												achievementTV;
	ImageView												star_icon;

	public AchievementAdapter(Context context, ArrayList<Achievement> achievementList)
		{
		super(context, R.layout.custom_achivement_list, achievementList);
		this.context = context;
		this.achievementList = achievementList;
		}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
		{
		View rowView = convertView;

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rowView = inflater.inflate(R.layout.custom_achivement_list, null);

		TextView achievementTV = (TextView) rowView.findViewById(R.id.achievementText);
		ImageView star_icon = (ImageView) rowView.findViewById(R.id.achievementStar);

		if (achievementList.get(position).getStatus())
			{
			star_icon.setImageResource(R.drawable.achievenment_star_icon);

			}

		achievementTV.setText(achievementList.get(position).getTitle());

		return rowView;
		}

}
