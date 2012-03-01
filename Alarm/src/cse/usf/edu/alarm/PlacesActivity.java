package cse.usf.edu.alarm;

import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PlacesActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textview = new TextView(this);
        textview.setText("This is the Places tab!");
       
        setContentView(textview);
    }
}