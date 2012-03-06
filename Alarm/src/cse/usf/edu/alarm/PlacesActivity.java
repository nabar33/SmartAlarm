package cse.usf.edu.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlacesActivity extends Activity {
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
}