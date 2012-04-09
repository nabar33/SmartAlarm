package cse.usf.edu.alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PlaceDBManager {
	private Context context;
	private SQLiteDatabase database;
	private AlarmDBHelper dbHelper;
	
	public PlaceDBManager(Context context)
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

	public ContentValues buildPlace(String name, String destination)
	{
		ContentValues cv = new ContentValues();
		cv.put("place_name", name);
		cv.put("end_address", destination);
		
		return cv;
	}
	
	public Cursor getPlace(String name) throws SQLException
	{
		Cursor mCursor = database.query(true, "places", null, 
				"place_name=\"" + name + "\"", null, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
	
	public Cursor getPlace(long placeId) throws SQLException
	{
		Cursor mCursor = database.query(true, "places", null, 
				"_id=" + placeId, null, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
	
	public long createPlace(String name, String destination)
	{
		ContentValues cv = buildPlace(name, destination);
		return database.insert("places", null, cv);
	}
	
	public long editPlace(long placeId, String name, String destination)
	{
		ContentValues cv = buildPlace(name, destination);
		return database.update("places", cv, "_id=" + placeId, null);
	}
	
	public boolean deletePlace(String name)
	{
		return database.delete("places", "place_name=\"" + name + "\"", null) > 0;
	}
	
	public boolean deletePlace(long placeId)
	{
		return database.delete("places", "_id=" + placeId, null) > 0;
	}
}
