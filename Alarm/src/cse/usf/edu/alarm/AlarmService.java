package cse.usf.edu.alarm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class AlarmService extends Service {
	private final static int GPS_UPDATE_INTERVAL = 1000 * 60 * 10;
	private final static int GPS_UPDATE_DISTANCE = 100;
	LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

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
	
	public void onStartCommand(){
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, GPS_UPDATE_INTERVAL, GPS_UPDATE_DISTANCE, locationListener);
	}
	
	public void updateAlarmTimes(Location location) throws Exception
	{
		Double latitude = location.getLatitude();
		Double longitude = location.getLongitude();

		StringBuilder urlString = new StringBuilder();
		
		String street = "blagh";
		String city = "blah";
		String state = "bah";
		
		urlString.append("https://maps.googleapis.com/maps/api/distancematrix/json?");
		urlString.append("origins=");// from
		urlString.append(Double.toString(latitude));
		urlString.append(",");
		urlString.append(Double.toString(longitude));
		urlString.append("&destinations=");// to
		street.replace(' ', '+');
		city.replace(' ', '+');
		state.replace(' ', '+');
		urlString.append(street);
		urlString.append("+");
		urlString.append(city);
		urlString.append("+");
		urlString.append(state);
		
		urlString.append("&sensor=true");
		
		HttpURLConnection urlConnection = null;
		URL url = new URL(urlString.toString());
		urlConnection=(HttpURLConnection)url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoOutput(true);
		urlConnection.setDoInput(true);
		urlConnection.connect();
		
		InputStream inStream = urlConnection.getInputStream();
		BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
		
		String temp, response = "";
	    while((temp = bReader.readLine()) != null){
	        //Parse data
	        response += temp;
	    }
	    //Close the reader, stream & connection
	    bReader.close();
	    inStream.close();
	    urlConnection.disconnect();
		
	    //Sortout JSONresponse 
	    JSONObject object = (JSONObject) new JSONTokener(response).nextValue();
	    JSONArray rows = object.getJSONArray("rows");

	    JSONArray elements = rows.getJSONArray(0);

	    JSONObject duration = elements.getJSONObject(1);
	    
	    int travelTime = duration.getInt("value");	
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
