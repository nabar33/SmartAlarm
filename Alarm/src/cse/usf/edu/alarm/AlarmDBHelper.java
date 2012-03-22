package cse.usf.edu.alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDBHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "smart_db";
	public static final int DB_VERSION = 1;
	
	private static final String CREATE_ALARMS_TABLE = 
    		"create table alarms(" +
    		"_id integer primary key autoincrement, " +
    		"week_day text not null, " +
    	    "route_id text not null, " +
    		"prep_time integer not null, " +
    	    "alarm_time integer not null)";
	
	private static final String CREATE_ROUTES_TABLE = 
			"create table routes(" +
			"_id integer primary key autoincrement, " +
			"place_name text not null, " +
			"end_address text not null, " +
			"from_address text not null)";
	
	public AlarmDBHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ALARMS_TABLE);
		db.execSQL(CREATE_ROUTES_TABLE);
		
		//db.execSQL("");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS alarms");
		db.execSQL("DROP TABLE IF EXISTS routes");
		onCreate(db);
	}

}
