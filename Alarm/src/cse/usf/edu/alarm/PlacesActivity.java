package cse.usf.edu.alarm;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView.LayoutParams;  
import com.google.android.maps.MapView;
import com.google.android.maps.MapController;
import com.google.android.maps.GeoPoint;
import android.view.View;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import android.content.Intent;
import android.os.Bundle;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Context;
import android.widget.Toast;



public class PlacesActivity extends MapActivity {
	MapView mapView; 
	LocationManager lm;
	LocationListener locationListener;
	MapController mc;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.places_tab_view);
        
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

     // Define a listener that responds to location updates
     LocationListener locationListener = new LocationListener() {
         public void onLocationChanged(Location location) {
           // Called when a new location is found by the network location provider.
         }

         public void onStatusChanged(String provider, int status, Bundle extras) {}

         public void onProviderEnabled(String provider) {}

         public void onProviderDisabled(String provider) {}
       };

     // Register the listener with the Location Manager to receive location updates
     lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        
        mapView = (MapView) findViewById(R.id.mapView);
        mc = mapView.getController();
        mc.setZoom(14); // Zoom 1 is world view
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