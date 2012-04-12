package cse.usf.edu.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.TimePicker;
import android.widget.Toast;



public class AlarmsActivity extends Activity implements AdapterView.OnItemSelectedListener {
    
	private AlarmDBManager myAlarmDB;
	private PlaceDBManager myPlaceDB;
	public static final String[] WEEK_DAYS = 
		{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	public static boolean dirty = false;
	
	// Handles to GUI elements
	private Spinner daySpinner, placeSpinner;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarms_tab_view);
        
        // Open Alarm Database
        myAlarmDB = new AlarmDBManager(this);
        myAlarmDB.open();
        
        // Open Place Database
        myPlaceDB = new PlaceDBManager(this);
        myPlaceDB.open();

        this.daySpinner = (Spinner) findViewById(R.id.day_spinner);
        this.placeSpinner = (Spinner) findViewById(R.id.place_spinner);
        
        // Populate Alarm spinner
        this.daySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> day_adapter = new ArrayAdapter<String>(this, R.layout.day_spinner_row_entry, R.id.day, WEEK_DAYS);
        this.daySpinner.setAdapter(day_adapter);
        
        //Populate Place spinner
        this.placeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> place_adapter = new ArrayAdapter<String>(this, R.layout.place_spinner_row_entry, R.id.place);
        this.placeSpinner.setAdapter(place_adapter);
        
        populatePlaceSpinner(place_adapter);
        
        populateAlarmFields(WEEK_DAYS[0]);

        
        //Set up radio buttons
        addRadioButtonListeners();
    }
    
    
    private void addRadioButtonListeners() {
		RadioGroup alarm_type_radiogroup = (RadioGroup) findViewById(R.id.alarm_type_radiogroup);
		alarm_type_radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton smart = (RadioButton) findViewById(R.id.alarm_type_smart);
				
				if(smart.getId()==checkedId){
					//smart selected
					findViewById(R.id.place_choice_textview).setVisibility(View.VISIBLE);
					findViewById(R.id.place_spinner).setVisibility(View.VISIBLE);
					findViewById(R.id.prep_time_textview).setVisibility(View.VISIBLE);
					findViewById(R.id.hour_edittext).setVisibility(View.VISIBLE);
					findViewById(R.id.minute_edittext).setVisibility(View.VISIBLE);
					((TextView)findViewById(R.id.place_time_textview)).setText("When should you be there?");

				} else {
					//dumb selected
					findViewById(R.id.place_choice_textview).setVisibility(View.GONE);
					findViewById(R.id.place_spinner).setVisibility(View.GONE);
					findViewById(R.id.prep_time_textview).setVisibility(View.GONE);
					findViewById(R.id.hour_edittext).setVisibility(View.GONE);
					findViewById(R.id.minute_edittext).setVisibility(View.GONE);
					((TextView)findViewById(R.id.place_time_textview)).setText("What time do you need to wake up?");
				}
			}
			
		});
	}
    
    private boolean isSmartView()
    {
    	RadioGroup alarm_type = (RadioGroup) findViewById(R.id.alarm_type_radiogroup);
    	return R.id.alarm_type_smart == alarm_type.getCheckedRadioButtonId();
    }

    
	public void populatePlaceSpinner(ArrayAdapter<String> adap)
	{
		if (adap != null)
		{
			try
			{
				Cursor places = myPlaceDB.getAllPlaceNames();
				if (places != null && places.getCount() > 0)
				{
					for (int i = 0; i < places.getCount(); i++)
					{
						if (adap.getPosition(places.getString(0)) < 0)
							adap.add(places.getString(0));
						places.moveToNext();
					}
				}
				places.close();
			}
			catch (SQLException squeak)
			{
				Log.d("SmartAlarm", "Failed to populate place spinner");
			}
			catch (IllegalStateException ise)
			{
				Log.d("SmartAlarm", "Failed to populate place spinner");
			}
		}
	}

	public void populateAlarmFields(String selectedDay)
    {
    	int atime;
    	try
    	{
    		boolean isSmart = isSmartView();
    		Cursor alarmData = myAlarmDB.getAlarm(selectedDay);

    		if (alarmData != null && alarmData.getColumnCount() > 4)
    		{
    			if (isSmart) 
    			{
    				// Set PlaceSpinner
    				String place = alarmData.getString(2);
    				ArrayAdapter<String> adap = (ArrayAdapter<String>) this.placeSpinner.getAdapter();
    				this.placeSpinner.setSelection(adap.getPosition(place));
    				
    				// Set prep time fields
    				// hour
    				EditText ted = (EditText) findViewById(R.id.hour_edittext);
    				atime = alarmData.getInt(3);
    				ted.setText(Integer.toString(atime / 60));
    				// minute
    				ted = (EditText) findViewById(R.id.minute_edittext);
    				ted.setText(Integer.toString(atime % 60));
    			}

    			// Set Time Picker
    			// This is universal regardless of alarm type
    			TimePicker timeSetter = (TimePicker) findViewById( R.id.place_time_timepicker );
    			atime = alarmData.getInt(4);
    			timeSetter.setCurrentHour(atime / 100);
    			timeSetter.setCurrentMinute(atime % 100);
    		}
    		else
    		{
    			clearAlarmFields();
    		}
    		alarmData.close();
    	}
    	catch (SQLException squeak)
    	{
    		clearAlarmFields();
    	}
    	catch (CursorIndexOutOfBoundsException cioobe)
    	{
    		clearAlarmFields();
    	}
    	catch (IllegalStateException ise)
    	{ // database is most likely not open
    		myAlarmDB.open();
    		populateAlarmFields(selectedDay); // this is dangerous
    	}
    }
    
    public void clearAlarmFields()
    {
    	TimePicker timeSetter = (TimePicker) findViewById(R.id.place_time_timepicker);
    	timeSetter.setCurrentHour(12);
    	timeSetter.setCurrentMinute(0);
    	
    	EditText ted = (EditText) findViewById(R.id.hour_edittext);
		ted.setText("");
		ted = (EditText) findViewById(R.id.minute_edittext);
		ted.setText("");
		
		CheckBox checky = (CheckBox) findViewById(R.id.alarm_everyday_checkbox);
		checky.setChecked(false);
    }
    
    public void addAlarm(View v)
    {
    	Spinner spinny = (Spinner) findViewById(R.id.day_spinner);
    	int curr_day = spinny.getLastVisiblePosition();
    	boolean isSmart = isSmartView();
    	int ahour = 0, aminute = 0, phour = 0, pminute = 0;
    	String place = "null";
    	
    	// Retrieve info from Place Spinner
    	if (isSmart)
    	{
    		place = (String) this.placeSpinner.getSelectedItem();
    		if (place == null)
    		{
    			Toast.makeText(this, "Please add a destination.", Toast.LENGTH_SHORT).show();
    			return;
    		}
    	}
    	
    	// Retrieve info from TimePicker
    	TimePicker timeSetter = (TimePicker) findViewById(R.id.place_time_timepicker);
    	ahour = timeSetter.getCurrentHour();
    	aminute = timeSetter.getCurrentMinute();
    	
    	if (isSmart)
    	{   // Retrieve info from prep time fields
    		EditText ted1 = (EditText) findViewById(R.id.hour_edittext);
    		EditText ted2 = (EditText) findViewById(R.id.minute_edittext);
    		try
    		{
    			phour = Integer.parseInt(ted1.getText().toString());
    			pminute = Integer.parseInt(ted2.getText().toString());
    		}
    		catch (NumberFormatException nfe)
    		{
    			Context context = getApplicationContext();
    			Toast.makeText(context, "Invalid Preparation Time", Toast.LENGTH_SHORT).show();
    			return;
    		}
    	}
    	
    	// Raw data
    	int raw_prep = phour * 60 + pminute;
    	int raw_alarm = ahour * 100 + aminute;

    	/* Determine if Apply to All box is checked */
    	CheckBox checky = (CheckBox) findViewById(R.id.alarm_everyday_checkbox);
    	if (checky.isChecked()) // set alarm for all days
    	{
    		myAlarmDB.deleteAllAlarms();
    		for (String day : WEEK_DAYS)
    		{
    			myAlarmDB.createAlarm(day, place, raw_prep, raw_alarm, isSmart ? 0:1);
    		}
    		
    		Context context = getApplicationContext();
    		Toast.makeText(context, "Alarm Set for All Days", Toast.LENGTH_SHORT).show();
    	}
    	else  // set alarm for the current day only
    	{
    		/* Determine if an alarm already exists on that day */
    		long id;
    		try 
    		{
    			Cursor alarmData = this.myAlarmDB.getAlarm(WEEK_DAYS[curr_day]);
    			if (alarmData.getColumnCount() > 0)
    				id = alarmData.getLong(0);
    			else
    				id = -1;
    			alarmData.close();
    		}
    		catch (SQLException squeak)
    		{
    			// alarm doesn't exist yet for that day
    			id = -1;
    		}
    		catch (CursorIndexOutOfBoundsException cioobe)
    		{
    			id = -1;
    		}
    		catch (IllegalStateException ise)
    		{
    			myAlarmDB.open();
    			addAlarm(v);
    			return;
    		}

    		if (id >= 0) // then update alarm
    		{
    			myAlarmDB.editAlarm(id, WEEK_DAYS[curr_day], place, raw_prep, raw_alarm, isSmart ? 0:1);
    		}
    		else // create new alarm in database
    		{
    			myAlarmDB.createAlarm(WEEK_DAYS[curr_day], place, raw_prep, raw_alarm, isSmart ? 0:1);
    		}

    		Context context = getApplicationContext();
    		Toast.makeText(context, "Alarm Set for " + WEEK_DAYS[curr_day], Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void deleteAlarm(View v)
    {
    	Spinner spinny = (Spinner) findViewById(R.id.day_spinner);
    	int curr_day = spinny.getLastVisiblePosition();
    	
    	this.myAlarmDB.deleteAlarm(WEEK_DAYS[curr_day]);
    	clearAlarmFields();
    	
    	Context context = getApplicationContext();
		Toast.makeText(context, "Removed Alarm for " + WEEK_DAYS[curr_day], Toast.LENGTH_SHORT).show();
    }

    
    public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id)
    {
    	if (parent.getId() == R.id.day_spinner)
    	{
    		populateAlarmFields(WEEK_DAYS[position]);
    	}
    }
    
    public void onNothingSelected(AdapterView<?> parent, View v, int position,
			long id)
    {
    	Log.d("MESSAGE", "Hurrah!");
    }

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		myAlarmDB.close();
		myPlaceDB.close();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		myAlarmDB.open();
		myPlaceDB.open();	
		if (dirty)
		{
			ArrayAdapter<String> place_adapter = new ArrayAdapter<String>(this, R.layout.place_spinner_row_entry, R.id.place);
	        this.placeSpinner.setAdapter(place_adapter);
	        
	        populatePlaceSpinner(place_adapter);
			dirty = false;
		}
	}
}