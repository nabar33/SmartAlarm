package cse.usf.edu.alarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PlacesActivity extends Activity implements AdapterView.OnItemSelectedListener {
    public RouteDBManager myRouteDB;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.places_tab_view);
        
        this.myRouteDB = new RouteDBManager(this);
        this.myRouteDB.open();
        
        //Populate place spinner
        Spinner spinner = (Spinner) findViewById(R.id.place_edit_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.place_spinner_row_entry, R.id.day );
        adapter.add("{New Place}");
        spinner.setAdapter(adapter);
        
        
    }

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
    
}