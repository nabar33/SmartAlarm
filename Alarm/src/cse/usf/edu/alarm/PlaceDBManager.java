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

	public ContentValues buildPlace(String name, String street, String city, String state, String zip)
	{
		ContentValues cv = new ContentValues();
		cv.put("place_name", name);
		cv.put("street", street);
		cv.put("city", city);
		cv.put("state", state);
		cv.put("zip", zip);
		
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
	
	public Cursor getAllPlaceNames()
	{
		Cursor mCursor = database.query(true, "places", new String[] {"place_name"}, 
										null, null, null, null, null, null);
		if (mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
		
	}
	
	public long createPlace(String name, String street, String city, String state, String zip)
	{
		ContentValues cv = buildPlace(name, street, city, state, zip);
		return database.insert("places", null, cv);
	}
	
	public long editPlace(long placeId, String name, String street, String city, String state, String zip)
	{
		ContentValues cv = buildPlace(name, street, city, state, zip);
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
