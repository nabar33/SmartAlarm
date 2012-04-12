package cse.usf.edu.alarm;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
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
    private PlaceDBManager myPlaceDB;
    
    // Handles to GUI elements
    private Spinner placeSpinner;
    private EditText nameBox, streetBox, cityBox, stateBox, zipBox;
    private final static String NULL_PLACE = "{New Place}";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.places_tab_view);
        
        this.placeSpinner = (Spinner) findViewById(R.id.place_edit_spinner);
        this.nameBox = (EditText) findViewById(R.id.name_edittext);
        this.streetBox = (EditText) findViewById(R.id.street_edittext);
        this.cityBox = (EditText) findViewById(R.id.city_edittext);
        this.stateBox = (EditText) findViewById(R.id.state_edittext);
        this.zipBox = (EditText) findViewById(R.id.zip_edittext);
        
        myPlaceDB = new PlaceDBManager(this);
        myPlaceDB.open();
        

        //Populate place spinner
        this.placeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.place_spinner_row_entry, R.id.place );
        adapter.add(NULL_PLACE);
        
        ArrayList<String> places = getAllPlaces();
        if (places != null)
        	for (String p : places)
        		adapter.add(p);
        
        this.placeSpinner.setAdapter(adapter); 
    }
    
    public void addPlace(View v)
    {	
    	long place_id = -1;
    	String selected = (String) this.placeSpinner.getSelectedItem();
    	String name = this.nameBox.getText().toString();
    	String street = this.streetBox.getText().toString();
    	String city = this.cityBox.getText().toString();
    	String state = this.stateBox.getText().toString();
    	String zip = this.zipBox.getText().toString();
    	
    	// check for empty fields
    	if (name.length() == 0 || street.length() == 0 || city.length() == 0
    		|| state.length() == 0 || zip.length() == 0)
    		Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
    		
    	while (true)
    	{
    		try {
    			Cursor c = myPlaceDB.checkPlace(selected);
    			if (c != null && c.getCount() > 0)
    				place_id = c.getLong(0);
    			
    			if (place_id >= 0)
    				myPlaceDB.editPlace(place_id, name, street, city, state, zip);
    			else
    				myPlaceDB.createPlace(name, street, city, state, zip);
    			
    			if (c != null)
    				c.close();
    			break;
    		}
    		catch (SQLException squeak)
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
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.place_spinner_row_entry, R.id.place );
        adapter.add(NULL_PLACE);
        ArrayList<String> places = getAllPlaces();
    	if (places != null)
    		for (String p : places)
    			adapter.add(p);
    	
    	this.placeSpinner.setAdapter(adapter);
    }
    
    public void deletePlace(View v)
    {
    	String place = (String) this.placeSpinner.getSelectedItem();
    	
    	if (place == null) return;
    	
    	// remove from database
    	while (true)
    	{
    		try {
    			if (myPlaceDB.deletePlace(place))
    				break;
    			else
    				return;
    		}
    		catch (SQLException squeak)
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
        if (places != null)
        	for (String p : places)
        		adapter.add(p);
    	
    	this.placeSpinner.setAdapter(adapter);
    	
    	populateFields(NULL_PLACE);
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
    		catch (SQLException squeak)
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
		
		String place = (String) this.placeSpinner.getItemAtPosition(arg2);
		populateFields(place);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void populateFields(String place)
	{
		if (place.equals(NULL_PLACE))
		{
			this.nameBox.setText("");
	    	this.streetBox.setText("");
	    	this.cityBox.setText("");
	    	this.stateBox.setText("");
	    	this.zipBox.setText("");
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
	    		catch (SQLException squeak)
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
				this.nameBox.setText(mCursor.getString(1));
		    	this.streetBox.setText(mCursor.getString(2));
		    	this.cityBox.setText(mCursor.getString(3));
		    	this.stateBox.setText(mCursor.getString(4));
		    	this.zipBox.setText(mCursor.getString(5));
		    	mCursor.close();
			}
		}
	}
	
	@Override
	public void onStop()
	{
		myPlaceDB.close();
		super.onStop();
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		myPlaceDB.open();
	}
}