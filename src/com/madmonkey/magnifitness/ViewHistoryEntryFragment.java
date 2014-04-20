package com.madmonkey.magnifitness;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitnessClass.MealEntry;
import com.madmonkey.magnifitnessClass.Pedometer;

public class ViewHistoryEntryFragment extends Fragment
{

	public interface OnRefreshListener
	{
		public void onRefresh();
	}

	GraphViewSeries				calorieValueSeries, pedoValueSeries;
	LineGraphView				graphView;
	List<MealEntry>				mealListData;
	ToggleButton				calorieBtn, pedoBtn;
	ArrayList<GraphViewData>	calorieData;
	DatabaseHandler				db;
	SharedPreferences			userSP;
	Pedometer					pedo;

	final static double			MON				= 1;
	final static double			TUE				= 1.5;
	final static double			WED				= 2;
	final static double			THU				= 2.5;
	final static double			FRI				= 3;
	final static double			SAT				= 3.5;
	final static double			SUN				= 4;
	final static double			DEFAULT_ZERO	= 0d;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		userSP = getActivity().getSharedPreferences(FacebookLogin.filename, 0);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)

	{
		View rootView = inflater.inflate(R.layout.view_history_meal_entry,
				container, false);

		db = new DatabaseHandler(getActivity());

		calorieBtn = (ToggleButton) rootView.findViewById(R.id.calorie_toggle_btn);
		pedoBtn = (ToggleButton) rootView.findViewById(R.id.pedometer_toggle_btn);
		
		Calendar c = Calendar.getInstance();
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		String date = "";
		if (c.get(Calendar.DAY_OF_MONTH) == 1
				|| c.get(Calendar.DAY_OF_MONTH) == 11
				|| c.get(Calendar.DAY_OF_MONTH) == 21
				|| c.get(Calendar.DAY_OF_MONTH) == 31)
		{
			date = "" + c.get(Calendar.DAY_OF_MONTH) + "st "
					+ months[(c.get(Calendar.MONTH))] + " "
					+ c.get(Calendar.YEAR);
		}
		else if (c.get(Calendar.DAY_OF_MONTH) == 2
				|| c.get(Calendar.DAY_OF_MONTH) == 12
				|| c.get(Calendar.DAY_OF_MONTH) == 22)
		{
			date = "" + c.get(Calendar.DAY_OF_MONTH) + "nd "
					+ months[(c.get(Calendar.MONTH))] + " "
					+ c.get(Calendar.YEAR);
		}
		else if (c.get(Calendar.DAY_OF_MONTH) == 3
				|| c.get(Calendar.DAY_OF_MONTH) == 13
				|| c.get(Calendar.DAY_OF_MONTH) == 23)
		{
			date = "" + c.get(Calendar.DAY_OF_MONTH) + "rd "
					+ months[(c.get(Calendar.MONTH))] + " "
					+ c.get(Calendar.YEAR);
		}
		else
		{
			date = "" + c.get(Calendar.DAY_OF_MONTH) + "th "
					+ months[(c.get(Calendar.MONTH))] + " "
					+ c.get(Calendar.YEAR);
		}

		mealListData = db.getTodayMealEntry(date);
		pedo = db.getPedometerState(date);
		
		GraphViewData gvd;
		if (mealListData.isEmpty() || mealListData == null)
		{

			// Dummy Data
			calorieValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 1700d),
					new GraphViewData(TUE, getTodayCalorie()),
					new GraphViewData(WED, DEFAULT_ZERO),
					new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO),
					new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}
		else
		{

			calorieValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 1700d),
					new GraphViewData(TUE, getTodayCalorie()),
					new GraphViewData(WED, DEFAULT_ZERO),
					new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO),
					new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}

		if(pedo != null)
		{
			double step = pedo.getStep();
			
			pedoValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 2700d), new GraphViewData(TUE, step),
					new GraphViewData(WED, DEFAULT_ZERO), new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO), new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}
		else 
		{
			//double step = pedo.getStep();
			pedoValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 2700d), new GraphViewData(TUE, DEFAULT_ZERO),
					new GraphViewData(WED, DEFAULT_ZERO), new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO), new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}
		

		graphView = new LineGraphView(getActivity(), "Weekly Calorie Intake & Step count");

		graphView.setDrawDataPoints(true);
		graphView.setDataPointsRadius(15f);

		// set styles
		// graphView.getGraphViewStyle().setTextSize(20f);
		// graphView.getGraphViewStyle().setNumVerticalLabels(8);
		// graphView.setDrawValuesOnTop(true);
		graphView.setBackgroundColor(Color.LTGRAY);
		// graphView.getGraphViewStyle().setVerticalLabelsWidth(100);
		// graphView.getGraphViewStyle().setVerticalLabelsAlign(Align.CENTER);

		graphView.setHorizontalLabels(new String[] { "Mon", "Tue", "Wed",
				"Thu", "Fri", "Sat", "Sun" });

		calorieValueSeries.getStyle().color = Color.RED;
		calorieValueSeries.getStyle().thickness = 5;
		pedoValueSeries.getStyle().color = Color.BLUE;
		pedoValueSeries.getStyle().thickness = 5;
		
		if(calorieBtn.isChecked() == true)
			graphView.addSeries(calorieValueSeries);
		
		if(pedoBtn.isChecked() == true)
			graphView.addSeries(pedoValueSeries);

		//graphView.setViewPort(2, 10);
		graphView.setScalable(true);

		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.graph1);
		layout.addView(graphView);
		
		// layout.invalidate();

		return rootView;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		
		Calendar c = Calendar.getInstance();
		DateFormatSymbols dfs = new DateFormatSymbols();
		String[] months = dfs.getMonths();
		String date = "";
		if (c.get(Calendar.DAY_OF_MONTH) == 1
				|| c.get(Calendar.DAY_OF_MONTH) == 11
				|| c.get(Calendar.DAY_OF_MONTH) == 21
				|| c.get(Calendar.DAY_OF_MONTH) == 31)
		{
			date = "" + c.get(Calendar.DAY_OF_MONTH) + "st "
					+ months[(c.get(Calendar.MONTH))] + " "
					+ c.get(Calendar.YEAR);
		}
		else if (c.get(Calendar.DAY_OF_MONTH) == 2
				|| c.get(Calendar.DAY_OF_MONTH) == 12
				|| c.get(Calendar.DAY_OF_MONTH) == 22)
		{
			date = "" + c.get(Calendar.DAY_OF_MONTH) + "nd "
					+ months[(c.get(Calendar.MONTH))] + " "
					+ c.get(Calendar.YEAR);
		}
		else if (c.get(Calendar.DAY_OF_MONTH) == 3
				|| c.get(Calendar.DAY_OF_MONTH) == 13
				|| c.get(Calendar.DAY_OF_MONTH) == 23)
		{
			date = "" + c.get(Calendar.DAY_OF_MONTH) + "rd "
					+ months[(c.get(Calendar.MONTH))] + " "
					+ c.get(Calendar.YEAR);
		}
		else
		{
			date = "" + c.get(Calendar.DAY_OF_MONTH) + "th "
					+ months[(c.get(Calendar.MONTH))] + " "
					+ c.get(Calendar.YEAR);
		}

		mealListData = db.getTodayMealEntry(date);
		
		if (mealListData.isEmpty() || mealListData == null)
		{

			// Dummy Data
			calorieValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 1700d),
					new GraphViewData(TUE, getTodayCalorie()),
					new GraphViewData(WED, DEFAULT_ZERO),
					new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO),
					new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}
		else
		{

			calorieValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 1700d),
					new GraphViewData(TUE, getTodayCalorie()),
					new GraphViewData(WED, DEFAULT_ZERO),
					new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO),
					new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}
		
		if(pedo != null)
		{
			double step = pedo.getStep();
			
			pedoValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 2700d), new GraphViewData(TUE, step),
					new GraphViewData(WED, DEFAULT_ZERO), new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO), new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}
		else
		{
			//double step = pedo.getStep();
			pedoValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 2700d), new GraphViewData(TUE, DEFAULT_ZERO),
					new GraphViewData(WED, DEFAULT_ZERO), new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO), new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}
		
		graphView.removeAllSeries();
		calorieValueSeries.getStyle().color = Color.RED;
		calorieValueSeries.getStyle().thickness = 5;
		pedoValueSeries.getStyle().color = Color.BLUE;
		pedoValueSeries.getStyle().thickness = 5;
		
		if(calorieBtn.isChecked() == true)
			graphView.addSeries(calorieValueSeries);
		else
			graphView.removeSeries(calorieValueSeries);
		
		if(pedoBtn.isChecked() == true)
			graphView.addSeries(pedoValueSeries);
		else
			graphView.removeSeries(pedoValueSeries);
	}

	private double getTodayCalorie()
	{
		return Double.longBitsToDouble(userSP.getLong("todayConsumedCalorie", 0));
	}
	
}
