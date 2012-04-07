package cse.usf.edu.alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AlarmDBHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "smart_db";
	public static final int DB_VERSION = 1;
	
	private static final String CREATE_ALARMS_TABLE = 
    		"CREATE TABLE alarms(" +
    		"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
    		"week_day TEXT NOT NULL, " +
    	    "route_id INTEGER NOT NULL, " +
    		"prep_time INTEGER NOT NULL, " +
    	    "alarm_time INTEGER NOT NULL)";
	
	private static final String CREATE_ALARMS_DUMB_TABLE = 
    		"CREATE TABLE dumbs(" +
    		"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
    		"week_day TEXT NOT NULL, " +
    	    "alarm_time INTEGER NOT NULL)";
	
	private static final String CREATE_ROUTES_TABLE = 
			"CREATE TABLE routes(" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"place_name TEXT NOT NULL, " +
			"end_address TEXT NOT NULL, " +
			"from_address TEXT NOT NULL)";
	
	public AlarmDBHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ALARMS_TABLE);
		db.execSQL(CREATE_ALARMS_DUMB_TABLE);
		db.execSQL(CREATE_ROUTES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS alarms");
		db.execSQL("DROP TABLE IF EXISTS routes");
		db.execSQL("DROP TABLE IF EXISTS dumbs");
		onCreate(db);
	}

}
