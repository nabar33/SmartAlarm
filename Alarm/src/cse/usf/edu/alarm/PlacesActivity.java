package cse.usf.edu.alarm;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class PlacesActivity extends Activity implements AdapterView.OnItemSelectedListener {
    public PlaceDBManager myPlaceDB;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.places_tab_view);
        
        myPlaceDB = new PlaceDBManager(this);
        myPlaceDB.open();
        

        //Populate place spinner
        Spinner spinner = (Spinner) findViewById(R.id.place_edit_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.place_spinner_row_entry, R.id.day );
        adapter.add("{New Place}");
        
        Cursor places = myPlaceDB.getAllPlaceNames();
        if (places != null && places.getCount() > 0)
        {
        	for (int i = 0; i < places.getCount(); i++)
        	{
        		adapter.add(places.getString(0));
        		places.moveToNext();
        	}
        }
        
        spinner.setAdapter(adapter);
        
    }
    
    public void addPlace(View v)
    {
    	EditText name = (EditText) findViewById(R.id.name_edittext);
    	EditText street = (EditText) findViewById(R.id.street_edittext);
    	EditText city = (EditText) findViewById(R.id.city_edittext);
    	EditText state = (EditText) findViewById(R.id.state_edittext);
    	EditText zip = (EditText) findViewById(R.id.zip_edittext);
    	
    	String destination = street.getText() + " " +
    					 city.getText() + " " +
    					 state.getText() + " " +
    					 zip.getText();
    					
    	myPlaceDB.createPlace(name.getText().toString(), destination);
    }
    
    public void deletePlace(View v)
    {
    	EditText name = (EditText) findViewById(R.id.name_edittext);
    	myPlaceDB.deletePlace(name.getText().toString());
    	
    	
    }

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
    
}