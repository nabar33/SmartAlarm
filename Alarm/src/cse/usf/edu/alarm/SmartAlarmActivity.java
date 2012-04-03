package cse.usf.edu.alarm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;

public class SmartAlarmActivity extends TabActivity {
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Resources res = getResources();
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Reusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, StatusActivity.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("status")
				.setIndicator("Status", res.getDrawable(R.drawable.status_tab))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, AlarmsActivity.class);
		spec = tabHost.newTabSpec("alarms")
				.setIndicator("Alarms", res.getDrawable(R.drawable.alarms_tab))
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, PlacesActivity.class);
		spec = tabHost.newTabSpec("places")
				.setIndicator("Places", res.getDrawable(R.drawable.places_tab))
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		
		startService(new Intent(this, AlarmService.class));

		}
}