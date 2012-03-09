package cse.usf.edu.alarm;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlacesActivity extends MapActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       
        setContentView(R.layout.places_tab_view);
    }
    
    public void startAddPlaceActivity(View view)
    {
    	Intent intent = new Intent(this, AddPlaceActivity.class);
    	startActivity(intent);
    }
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}