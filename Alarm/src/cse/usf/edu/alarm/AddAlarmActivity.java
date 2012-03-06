package cse.usf.edu.alarm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddAlarmActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        setContentView(R.layout.add_alarm_view);
        
        Bundle data = this.getIntent().getExtras();
        String day_text = (String)data.get("week_day");
        
        if (day_text != null)
        {
        	TextView day_label = (TextView)this.findViewById(R.id.day_text);
        	CharSequence day_string = "Day: " + day_text;
        
        	day_label.setText(day_string);
        }
    }
	
	public void addAlarm(View view)
	{
		finish();
	}
}
