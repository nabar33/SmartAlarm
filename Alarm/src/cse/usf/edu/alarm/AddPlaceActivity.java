package cse.usf.edu.alarm;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

public class AddPlaceActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_route_view);
	}

	public void addLocation(View view)
	{
		
		//Move destination to database
		finish();
	}
	
	
}
