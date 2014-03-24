package com.madmonkey.magnifitness;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class ViewHistoryEntryFragment extends Fragment {

	GraphViewSeries	exampleSeries;
	LineGraphView	graphView;

	@Override
	public void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);

		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
		View rootView = inflater.inflate(R.layout.view_history_meal_entry, container, false);

		// init example series data
		exampleSeries = new GraphViewSeries(new GraphViewData[] { 
				new GraphViewData(1, 2.0d), 
				new GraphViewData(1.5, 1.5d), 
				new GraphViewData(2, 2.5d),
				new GraphViewData(2.5, 1.0d) }); // Replace with calorie value variables

		graphView = new LineGraphView(getActivity() // context
				, "Weekly Calorie Intake" // heading
		);
		graphView.addSeries(exampleSeries); // data
		graphView.setDrawDataPoints(true);
		graphView.setDataPointsRadius(15f);
		graphView.setHorizontalLabels(new String[] { "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun" });
		graphView.setVerticalLabels(new String[] { "high", "middle", "low" }); //Set to calorie value

		graphView.setCustomLabelFormatter(new CustomLabelFormatter()
			{
				@Override
				public String formatLabel(double value, boolean isValueX)
					{
					if (isValueX)
						{
						if (value < 5)
							{
							return "small";
							}
						else if (value < 15)
							{
							return "middle";
							}
						else
							{
							return "big";
							}
						}
					return null; // let graphview generate Y-axis label for us
					}
			});

		// set styles
		graphView.getGraphViewStyle().setNumHorizontalLabels(5);
		graphView.getGraphViewStyle().setNumVerticalLabels(4);
//		graphView.getGraphViewStyle().setVerticalLabelsWidth(300);

		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
		layout.addView(graphView);

		return rootView;
		}

}
