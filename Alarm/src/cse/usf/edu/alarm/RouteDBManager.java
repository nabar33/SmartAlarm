package cse.usf.edu.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class RouteDBManager {
	private Context context;
	private SQLiteDatabase database;
	private AlarmDBHelper dbHelper;
	
	public RouteDBManager(Context context)
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

	public ContentValues buildRoute(String name, String dest, String origin)
	{
		ContentValues cv = new ContentValues();
		cv.put("place_name", name);
		cv.put("end_address", dest);
		
		return cv;
	}
	
	public Cursor getRoute(String name) throws SQLException
	{
		Cursor mCursor = database.query(true, "routes", null, 
				"place_name=\"" + name + "\"", null, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
	
	public Cursor getRoute(long routeId) throws SQLException
	{
		Cursor mCursor = database.query(true, "routes", null, 
				"_id=" + routeId, null, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
	
	public long createRoute(String name, String dest, String origin)
	{
		ContentValues cv = buildRoute(name, dest, origin);
		return database.insert("routes", null, cv);
	}
	
	public long editRoute(long routeId, String name, String dest, String origin)
	{
		ContentValues cv = buildRoute(name, dest, origin);
		return database.update("routes", cv, "_id=" + routeId, null);
	}
	
	public boolean deleteRoute(String name)
	{
		return database.delete("routes", "place_name=\"" + name + "\"", null) > 0;
	}
	
	public boolean deleteRoute(long routeId)
	{
		return database.delete("routes", "_id=" + routeId, null) > 0;
	}
}
