package cse.usf.edu.alarm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class AlarmsActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarms_tab_view);
        
        ListView lview = (ListView) findViewById(R.id.weekdays);
        String[] week_days = new String[] {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
        									R.layout.row_entry, R.id.day, week_days);
        lview.setAdapter(adapter);
    }
}