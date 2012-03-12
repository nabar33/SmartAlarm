package cse.usf.edu.alarm;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView.LayoutParams;  
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.maps.MapView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class PlacesActivity extends MapActivity {
	MapView mapView; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.places_tab_view);
        mapView = (MapView) findViewById(R.id.mapView);
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  
        View zoomView = mapView.getZoomControls(); 
 
        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
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