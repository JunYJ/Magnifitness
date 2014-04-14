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

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import com.madmonkey.magnifitness.util.DatabaseHandler;
import com.madmonkey.magnifitnessClass.MealEntry;

public class ViewHistoryEntryFragment extends Fragment
{

	public interface OnRefreshListener
	{
		public void onRefresh();
	}

	GraphViewSeries				calorieValueSeries, pedoValueSeries;
	LineGraphView				graphView;
	List<MealEntry>				mealListData;
	ArrayList<GraphViewData>	calorieData;
	DatabaseHandler				db;
	SharedPreferences			userSP;

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

		Log.i("DATE_STR", "" + c.get(Calendar.DAY_OF_WEEK));
		mealListData = db.getTodayMealEntry(date);

		GraphViewData gvd;
		if (mealListData.isEmpty() || mealListData == null)
		{

			// Dummy Data
			calorieValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 1700d),
					new GraphViewData(TUE, getTodayCalorie()),
					new GraphViewData(WED, 1550d),
					new GraphViewData(THU, 1690d),
					new GraphViewData(FRI, 2321d),
					new GraphViewData(SAT, 1590d),
					new GraphViewData(SUN, 1790d) });
		}
		else
		{

			calorieValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, DEFAULT_ZERO),
					new GraphViewData(TUE, getTodayCalorie()),
					new GraphViewData(WED, DEFAULT_ZERO),
					new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO),
					new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}

		pedoValueSeries = new GraphViewSeries(new GraphViewData[] {
				new GraphViewData(MON, 2700d), new GraphViewData(TUE, 4050d),
				new GraphViewData(WED, 4000d), new GraphViewData(THU, 6000d),
				new GraphViewData(FRI, 3000d), new GraphViewData(SAT, 1050d),
				new GraphViewData(SUN, 5500d) });

		graphView = new LineGraphView(getActivity(), "Weekly Calorie Intake");

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
		// pedoValueSeries.getStyle().color = Color.BLUE;
		// pedoValueSeries.getStyle().thickness = 5;
		graphView.addSeries(calorieValueSeries);
		// graphView.addSeries(pedoValueSeries);

		// graphView.setViewPort(2, 10);
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

		Log.i("DATE_STR", "" + c.get(Calendar.DAY_OF_WEEK));
		mealListData = db.getTodayMealEntry(date);
		
		if (mealListData.isEmpty() || mealListData == null)
		{

			// Dummy Data
			calorieValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, 1700d),
					new GraphViewData(TUE, getTodayCalorie()),
					new GraphViewData(WED, 1550d),
					new GraphViewData(THU, 1690d),
					new GraphViewData(FRI, 2321d),
					new GraphViewData(SAT, 1590d),
					new GraphViewData(SUN, 1790d) });
		}
		else
		{

			calorieValueSeries = new GraphViewSeries(new GraphViewData[] {
					new GraphViewData(MON, DEFAULT_ZERO),
					new GraphViewData(TUE, getTodayCalorie()),
					new GraphViewData(WED, DEFAULT_ZERO),
					new GraphViewData(THU, DEFAULT_ZERO),
					new GraphViewData(FRI, DEFAULT_ZERO),
					new GraphViewData(SAT, DEFAULT_ZERO),
					new GraphViewData(SUN, DEFAULT_ZERO) });
		}
		
		
	}

	private double getTodayCalorie()
	{

		/*double counter = 0.0;

		for (MealEntry me : mealListData)
		{
			counter += me.getTotalCalorie();
		}

		return counter;*/
		return Double.longBitsToDouble(userSP.getLong("todayConsumedCalorie", 0));
	}
	
}
