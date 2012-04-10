package cse.usf.edu.alarm;


import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.place_spinner_row_entry, R.id.place );
        adapter.add("{New Place}");
        
        ArrayList<String> places = getAllPlaces();
        if (places != null)
        	for (String p : places)
        		adapter.add(p);
        
        spinner.setAdapter(adapter); 
    }
    
    public void addPlace(View v)
    {	
    	String name = ((EditText) findViewById(R.id.name_edittext)).getText().toString();
    	String street = ((EditText) findViewById(R.id.street_edittext)).getText().toString();
    	String city = ((EditText) findViewById(R.id.city_edittext)).getText().toString();
    	String state = ((EditText) findViewById(R.id.state_edittext)).getText().toString();
    	String zip = ((EditText) findViewById(R.id.zip_edittext)).getText().toString();
    	
    	while (true)
    	{
    		try {
    			myPlaceDB.createPlace(name, street, city, state, zip);
    			break;
    		}
    		catch (SQLiteException squeak)
    		{
    			Log.d("SmartAlarm", "Failed to add place to database");
    			return;
    		}
    		catch (IllegalStateException ise)
    		{
    			myPlaceDB.open();
    		}
    	}
    	
    	// update spinner
    	Spinner sp = (Spinner) findViewById(R.id.place_edit_spinner);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.place_spinner_row_entry, R.id.place );
        adapter.add("{New Place}");
        ArrayList<String> places = getAllPlaces();
    	for (String p : places)
    		adapter.add(p);
    	
    	sp.setAdapter(adapter);
    }
    
    public void deletePlace(View v)
    {
    	Spinner sp = (Spinner) findViewById(R.id.place_edit_spinner);
    	String place = (String) sp.getSelectedItem();
    	
    	if (place == null) return;
    	
    	// remove from database
    	while (true)
    	{
    		try {
    			myPlaceDB.deletePlace(place);
    			break;
    		}
    		catch (SQLiteException squeak)
    		{
    			Log.d("SmartAlarm", "Failed to remove place from database");
    			return;
    		}
    		catch (IllegalStateException ise)
    		{
    			myPlaceDB.open();
    		}
    	}
    	
    	// clear it from the spinner
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.place_spinner_row_entry, R.id.place );
        adapter.add("{New Place}");
        ArrayList<String> places = getAllPlaces();
    	for (String p : places)
    		adapter.add(p);
    	
    	sp.setAdapter(adapter);
    	
    	populateFields("{New Place}");
    }

    public ArrayList<String> getAllPlaces()
    {
    	Cursor places = null;
    	while (true)
    	{
    		try {
    			places = myPlaceDB.getAllPlaceNames();
    			break;
    		}
    		catch (SQLiteException squeak)
    		{
    			Log.d("SmartAlarm", "Failed to remove place from database");
    			return null;
    		}
    		catch (IllegalStateException ise)
    		{
    			myPlaceDB.open();
    		}
    	}
    	ArrayList<String> plist = new ArrayList<String>();
    	
        if (places != null && places.getCount() > 0)
        {
        	for (int i = 0; i < places.getCount(); i++)
        	{
        		plist.add(places.getString(0));
        		places.moveToNext();
        	}
        	places.close();
        	return plist;
        }
        
        return null;
    }
    
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		
		Spinner sp = (Spinner) findViewById(R.id.place_edit_spinner);
		String place = (String) sp.getItemAtPosition(arg2);
		populateFields(place);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void populateFields(String place)
	{
		if (place.equals("{New Place}"))
		{
			((EditText) findViewById(R.id.name_edittext)).setText("");
	    	((EditText) findViewById(R.id.street_edittext)).setText("");
	    	((EditText) findViewById(R.id.city_edittext)).setText("");
	    	((EditText) findViewById(R.id.state_edittext)).setText("");
	    	((EditText) findViewById(R.id.zip_edittext)).setText("");
		}
		else
		{
			Cursor mCursor = null;
			while (true)
	    	{
	    		try {
	    			mCursor = myPlaceDB.getPlace(place);
	    			break;
	    		}
	    		catch (SQLiteException squeak)
	    		{
	    			Log.d("SmartAlarm", "Failed to remove place from database");
	    			return;
	    		}
	    		catch (IllegalStateException ise)
	    		{
	    			myPlaceDB.open();
	    		}
	    	}
			
			if (mCursor != null && mCursor.getColumnCount() == 6)
			{
				((EditText) findViewById(R.id.name_edittext))
					.setText(mCursor.getString(1));
		    	((EditText) findViewById(R.id.street_edittext))
		    		.setText(mCursor.getString(2));
		    	((EditText) findViewById(R.id.city_edittext))
		    		.setText(mCursor.getString(3));
		    	((EditText) findViewById(R.id.state_edittext))
		    		.setText(mCursor.getString(4));
		    	((EditText) findViewById(R.id.zip_edittext))
		    		.setText(mCursor.getString(5));
		    	mCursor.close();
			}
		}
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		myPlaceDB.close();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		myPlaceDB.open();
	}
}