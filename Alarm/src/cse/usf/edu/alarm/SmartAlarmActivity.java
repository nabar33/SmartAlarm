package cse.usf.edu.alarm;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class SmartAlarmActivity extends TabActivity 
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Resources res = getResources();
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, StatusActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("status").setIndicator("Status",
                          res.getDrawable(R.drawable.tab_alarms))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, AlarmsActivity.class);
        spec = tabHost.newTabSpec("alarms").setIndicator("Alarms",
                          res.getDrawable(R.drawable.tab_alarms))
                      .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, PlacesActivity.class);
        spec = tabHost.newTabSpec("places").setIndicator("Places",
                          res.getDrawable(R.drawable.tab_alarms))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(2);
    }
}