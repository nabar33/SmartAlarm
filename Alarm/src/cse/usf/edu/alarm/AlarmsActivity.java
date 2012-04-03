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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.TimePicker;
import android.widget.Toast;



public class AlarmsActivity extends Activity implements AdapterView.OnItemSelectedListener {
    
	public AlarmDBManager myAlarmDB;
	public static final String[] WEEK_DAYS = 
		{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarms_tab_view);
        
        // Open Alarm Database
        myAlarmDB = new AlarmDBManager(this);
        myAlarmDB.open();
        
        // Populate Alarm spinner
        Spinner spinner = (Spinner) findViewById(R.id.day_choice);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_entry, R.id.day, WEEK_DAYS);
        spinner.setAdapter(adapter);
        
        // Fill remaining fields
        populateAlarmFields(WEEK_DAYS[0]);
    }
    
    public void populateAlarmFields(String selectedDay)
    {
    	int atime;
    	try
    	{
    		Cursor alarmData = myAlarmDB.getAlarm(selectedDay);
    		
    		if (alarmData.getColumnCount() > 4)
    		{
    			// Set Time Picker
    			TimePicker timeSetter = (TimePicker) findViewById( R.id.destination_time_picker );
    			atime = alarmData.getInt(4);
    			timeSetter.setCurrentHour(atime / 100);
    			timeSetter.setCurrentMinute(atime % 100);

    			// Set prep time fields
    			// hour
    			EditText ted = (EditText) findViewById(R.id.Hour);
    			atime = alarmData.getInt(3);
    			ted.setText(Integer.toString(atime / 60));
    			// minute
    			ted = (EditText) findViewById(R.id.Minute);
    			ted.setText(Integer.toString(atime % 60));
    		}
    		else
    		{
    			clearAlarmFields();
    		}
    	}
    	catch (SQLException squeak)
    	{
    		clearAlarmFields();
    	}
    	catch (CursorIndexOutOfBoundsException cioobe)
    	{
    		clearAlarmFields();
    	}
    }
    
    public void clearAlarmFields()
    {
    	TimePicker timeSetter = (TimePicker) findViewById(R.id.destination_time_picker);
    	timeSetter.setCurrentHour(12);
    	timeSetter.setCurrentMinute(0);
    	
    	EditText ted = (EditText) findViewById(R.id.Hour);
		ted.setText("0");
		ted = (EditText) findViewById(R.id.Minute);
		ted.setText("0");
		
		CheckBox checky = (CheckBox) findViewById(R.id.alarm_everyday_checkbox);
		checky.setChecked(false);
    }
    
    public void addAlarm(View v)
    {
    	Spinner spinny = (Spinner) findViewById(R.id.day_choice);
    	int curr_day = spinny.getLastVisiblePosition();
    	
    	// Retrieve info from TimePicker
    	TimePicker timeSetter = (TimePicker) findViewById(R.id.destination_time_picker);
    	int ahour = timeSetter.getCurrentHour();
    	int aminute = timeSetter.getCurrentMinute();
    	
    	// Retrieve info from prep time fields
    	EditText ted1 = (EditText) findViewById(R.id.Hour);
    	EditText ted2 = (EditText) findViewById(R.id.Minute);
    	int phour;
    	int pminute;
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
    			myAlarmDB.createAlarm(day, "0", raw_prep, raw_alarm);
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

    		if (id >= 0) // then update alarm
    		{
    			myAlarmDB.editAlarm(id, WEEK_DAYS[curr_day], "0", raw_prep, raw_alarm);
    		}
    		else // create new alarm in database
    		{
    			myAlarmDB.createAlarm(WEEK_DAYS[curr_day], "0", raw_prep, raw_alarm);
    		}

    		Context context = getApplicationContext();
    		Toast.makeText(context, "Alarm Set for " + WEEK_DAYS[curr_day], Toast.LENGTH_SHORT).show();
    	}
    }
    
    public void deleteAlarm(View v)
    {
    	Spinner spinny = (Spinner) findViewById(R.id.day_choice);
    	int curr_day = spinny.getLastVisiblePosition();
    	
    	this.myAlarmDB.deleteAlarm(WEEK_DAYS[curr_day]);
    	clearAlarmFields();
    }
    
    public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id)
    {
    	
    	populateAlarmFields(WEEK_DAYS[position]);
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
	public void onStop()
	{
		super.onStop();
		myAlarmDB.close();
	}
}