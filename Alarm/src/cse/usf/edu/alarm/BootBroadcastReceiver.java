package cse.usf.edu.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent startAlarmService = new Intent(context, AlarmService.class);
		context.startService(startAlarmService);
	}

}
