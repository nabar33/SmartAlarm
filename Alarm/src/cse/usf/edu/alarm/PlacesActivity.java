package cse.usf.edu.alarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class PlacesActivity extends Activity {
    public RouteDBManager myRouteDB;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.places_tab_view);
        
        this.myRouteDB = new RouteDBManager(this);
        this.myRouteDB.open();
        
        populateAddressFields();
    }
    
    public void populateAddressFields()
    {
    	
    }
    
    public void deleteLocation(View v)
    {
    	
    }
    
    public void saveLocation(View v)
    {
    	
    }
}