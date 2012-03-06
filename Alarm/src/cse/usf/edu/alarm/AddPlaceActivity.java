package cse.usf.edu.alarm;

import android.app.Activity;
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
		finish();
	}
}
