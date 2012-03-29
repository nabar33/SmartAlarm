package cse.usf.edu.alarm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.TimePicker;



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
        
        // Temporary
        myAlarmDB.createAlarm("Wednesday", "0", 90, 1530);
        
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
    		
    		if (alarmData.getCount() > 3)
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
    			Log.d("WTF", "HUH?");
    	}
    	catch (SQLException squeak)
    	{
    		Log.d("TODO", "Clear fields when switching to empty day");
    	}
    }
    
    public void addAlarm(View v)
    {
    	
    }
    
    public void deleteAlarm(View v)
    {
    	
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
		myAlarmDB.close();
	}
}