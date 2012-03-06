package cse.usf.edu.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class AlarmsActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarms_tab_view);
        
        ListView lview = (ListView) findViewById(R.id.weekdays);
        String[] week_days = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.row_entry, R.id.day, week_days);
        lview.setAdapter(adapter);
    }
    
    public void startAddAlarmActivity(View view)
    {
    	TextView day = (TextView)((View) view.getParent()).findViewById(R.id.day);
    		
    	String day_text = (String)day.getText();
    	
    	Intent alarm_intent = new Intent(this, AddAlarmActivity.class);
    	alarm_intent.putExtra("week_day", day_text);
    	
    	startActivity(alarm_intent);
    }
}