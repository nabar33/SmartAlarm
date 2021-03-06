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
    	    "place_id INTEGER NOT NULL, " +
    		"prep_time INTEGER NOT NULL, " +
    	    "alarm_time INTEGER NOT NULL, " +
    		"simple INTEGER NOT NULL)";
	
	private static final String CREATE_PLACES_TABLE = 
			"CREATE TABLE places(" +
			"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
			"place_name TEXT NOT NULL, " +
			"street TEXT NOT NULL, " +
			"city TEXT NOT NULL, " +
			"state TEXT NOT NULL, " +
			"zip TEXT NOT NULL)";
	
	public AlarmDBHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_ALARMS_TABLE);
		db.execSQL(CREATE_PLACES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS alarms");
		db.execSQL("DROP TABLE IF EXISTS places");
		onCreate(db);
		
	}

}
