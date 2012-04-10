package cse.usf.edu.alarm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class AlarmService extends Service {
	private final static int GPS_UPDATE_INTERVAL = 1000 * 60 * 10;
	private final static int GPS_UPDATE_DISTANCE = 100;
	private AlarmDBManager alarmData;
	private DumbDBManager dumbData;
	private PlaceDBManager placeData;
	LocationManager locationManager;

	public void onStartCommand(){
		alarmData.open();
		dumbData.open();
		placeData.open();
		
		LocationListener locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					// Called when a new location is found by the network location
					// provider.
					try {
						updateAlarmTimes(location);
					} catch (Exception e) {
						Toast.makeText(null, "Oh Lawd..", Toast.LENGTH_LONG);
					}
				}
				public void onStatusChanged(String provider, int status, Bundle extras) {}
				public void onProviderEnabled(String provider) {}
				public void onProviderDisabled(String provider) {}
			};
			
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, GPS_UPDATE_INTERVAL, GPS_UPDATE_DISTANCE, locationListener);
	}
	
	public void updateAlarmTimes(Location location) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("E");
		Date d = new Date();
		String dayOfTheWeek = sdf.format(d);
		

		Cursor alarm = alarmData.getAlarm(dayOfTheWeek);
		Cursor dumbAlarm = dumbData.getAlarm(dayOfTheWeek);
		
		int travelTime = getETA(location, alarm);
		int prepTime = alarm.getInt(3);
	    int destinationTime = alarm.getInt(4);
	    
	    int wakeTime = destinationTime - travelTime - prepTime;
	    int dumbWakeTime = dumbAlarm.getInt(2);
	    
	    int currentTime = 60 * d.getHours() + d.getMinutes();
	    
	    int alarmTime = 1;
	    
	    if(wakeTime < dumbWakeTime)
	    	alarmTime = currentTime - wakeTime;
	    else
	    	alarmTime = currentTime - dumbWakeTime;
	    
	    //TODO send the alarmTime int for displaying time til next alarm
	    // make this service check periodically for alarmTime <= 0 -> play sound
	    
	    
	}
	
	public int getETA(Location location, Cursor alarm)
	{
		int travelTime = 0;
		Double latitude = location.getLatitude();
		Double longitude = location.getLongitude();

		StringBuilder urlString = new StringBuilder();
		
		String street = alarm.getString(1);
		String city = alarm.getString(2);
		String state = alarm.getString(3);
		String zip = alarm.getString(4);	
		
		street.replace(' ', '+');
		city.replace(' ', '+');
		state.replace(' ', '+');
		zip.replace(' ', '+');
		
		urlString.append("https://maps.googleapis.com/maps/api/distancematrix/json?");
		urlString.append("origins=");// from
		urlString.append(Double.toString(latitude));
		urlString.append(",");
		urlString.append(Double.toString(longitude));
		urlString.append("&destinations=");
		urlString.append(street);
		urlString.append("+");
		urlString.append(city);
		urlString.append("+");
		urlString.append(state);
		urlString.append("+");
		urlString.append(zip);
		urlString.append("&sensor=true");
		
		try {
			HttpURLConnection urlConnection = null;
			URL url = new URL(urlString.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.connect();

			InputStream inStream = urlConnection.getInputStream();
			BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));

			String temp, response = "";
			while ((temp = bReader.readLine()) != null) {
				// Parse data
				response += temp;
			}
			// Close the reader, stream & connection
			bReader.close();
			inStream.close();
			urlConnection.disconnect();

			// Sort out JSONresponse
			JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
			JSONArray rows = object.getJSONArray("rows");

			JSONArray elements = rows.getJSONArray(0);

			JSONObject duration = elements.getJSONObject(1);

			travelTime = duration.getInt("value");
			Toast.makeText(null, travelTime, Toast.LENGTH_LONG);
		} catch (Exception e) {
			
		}
		
	    return travelTime;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
