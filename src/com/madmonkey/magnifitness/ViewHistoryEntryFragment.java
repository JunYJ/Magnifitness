package com.madmonkey.magnifitness;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class ViewHistoryEntryFragment extends Fragment {

	GraphViewSeries		exampleSeries;
	BarGraphView		graphView;
	final static double	MON	= 1;
	final static double	TUE	= 1.5;
	final static double	WED	= 2;
	final static double	THU	= 2.5;
	final static double	FRI	= 3;
	final static double	SAT	= 3.5;
	final static double	SUN	= 4;

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
				new GraphViewData(MON, 500d), new GraphViewData(TUE, 600d), new GraphViewData(WED, 400d), new GraphViewData(THU, 450d),
				new GraphViewData(FRI, 700d), new GraphViewData(SAT, 1050d), new GraphViewData(SUN, 800d) });// with
																												// calorie
																												// value
																												// variables

		graphView = new BarGraphView(getActivity() // context
				, "Weekly Calorie Intake" // heading
		);
		graphView.addSeries(exampleSeries); // data
		// graphView.setDrawDataPoints(true);
		// graphView.setDataPointsRadius(15f);


		graphView.setHorizontalLabels(new String[] {
				"Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", "Sun" });

		// set styles
		graphView.getGraphViewStyle().setTextSize(20f);
		graphView.getGraphViewStyle().setNumVerticalLabels(8);
//		graphView.setDrawValuesOnTop(true);
		graphView.getGraphViewStyle().setVerticalLabelsWidth(100);
		graphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);
		graphView.setBackgroundColor(Color.LTGRAY);

		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
		layout.addView(graphView);

		return rootView;
		}

}
