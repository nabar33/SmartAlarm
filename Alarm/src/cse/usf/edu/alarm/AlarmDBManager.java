package cse.usf.edu.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class AlarmDBManager 
{

	private Context context;
	private SQLiteDatabase database;
	private AlarmDBHelper dbHelper;
	
	public AlarmDBManager(Context context)
	{
		this.context = context;
	}
	
	public void open() throws SQLException
	{
		dbHelper = new AlarmDBHelper(this.context);
		database = dbHelper.getWritableDatabase();
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	private ContentValues buildAlarm(String day, String dest, int prep_time, int alarm_time)
	{
		ContentValues values = new ContentValues();
		values.put("week_day", day);
		values.put("route_id", dest);
		values.put("prep_time", prep_time);
		values.put("alarm_time", alarm_time);
		
		return values;
	}
	
	public long createAlarm(String day, String dest, int prep_time, int alarm_time)
	{
		ContentValues values = buildAlarm(day, dest, prep_time, alarm_time);
		return database.insert("alarms", null, values);
	}
	
	public long editAlarm(long alarmId, String day, String dest, int prep_time, int alarm_time)
	{
		ContentValues values = buildAlarm(day, dest, prep_time, alarm_time);
		return database.update("alarms", values, "_id = " + alarmId, null);
	}
	
	public boolean deleteAlarm(String day)
	{
		return database.delete("alarms", "week_day=\"" + day + "\"", null) > 0;
	}
	
	public boolean deleteAllAlarms()
	{
		return database.delete("alarms", null, null) > 0;
	}

	public Cursor getAlarm(long alarmId) throws SQLException
	{
		Cursor mCursor = database.query(true, "alarms", null, 
						"_id=" + alarmId, null, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
	
	public Cursor getAlarm(String day) throws SQLException
	{
		Cursor mCursor = database.query(true, "alarms", null, 
						"week_day=\"" + day + "\"", null, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
}
