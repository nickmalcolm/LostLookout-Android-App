/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package nz.ac.vuw.ecs.nwen304;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Project 2 - Nicholas Malcolm - 300170288
 * NWEN304 T12011
 * 
 * @author malcolnich
 *
 */
public class BusDBAdapter {

    public static final String KEY_ROWID = "_id";

    private static final String TAG = "NotesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    

    public static final String STOPS_TABLE = "stops";
    public static final String STOP_TIMES_TABLE = "stop_times";
    public static final String TRIPS_TABLE = "trips";
    public static final String ROUTES_TABLE = "routes";
    
    public static final String KEY_STOP_STOP_ID = "stop_id";
    public static final String KEY_STOP_CHECK = "stop_check";
    public static final String KEY_STOP_STOP_NAME = "stop_name";
    public static final String KEY_STOP_STOP_LAT = "stop_lat";
    public static final String KEY_STOP_STOP_LON = "stop_lon";

    /**
     * Database creation sql statements
     */
    private static final String STOPS_TABLE_CREATE =
        "create table "+
        	STOPS_TABLE			+" (_id integer primary key autoincrement, "+
        	KEY_STOP_STOP_ID	+" integer UNIQUE, " +
        	KEY_STOP_CHECK		+" integer, " +
        	KEY_STOP_STOP_NAME	+" text not null," +
        	KEY_STOP_STOP_LAT	+" float," +
        	KEY_STOP_STOP_LON	+" float" +
        	");";
    
    public static final String KEY_ROUTE_ROUTE_ID = "route_id";
    public static final String KEY_ROUTE_AGENCY_ID = "agency_id";
    public static final String KEY_ROUTE_ROUTE_SHORT_NAME = "route_short_name";
    public static final String KEY_ROUTE_ROUTE_LONG_NAME = "route_long_name";
    public static final String KEY_ROUTE_ROUTE_TYPE = "route_type";
    
    private static final String ROUTES_TABLE_CREATE =
        "create table "+ROUTES_TABLE+" (_id integer primary key autoincrement, "
        + 	KEY_ROUTE_ROUTE_ID			+" integer UNIQUE, " +
        	KEY_ROUTE_AGENCY_ID			+" text not null," +
        	KEY_ROUTE_ROUTE_SHORT_NAME	+" text not null," +
        	KEY_ROUTE_ROUTE_LONG_NAME	+" text not null," +
        	KEY_ROUTE_ROUTE_TYPE		+" integer" +
        	");";

    public static final String KEY_TRIP_TRIP_ID = "trip_id";
    public static final String KEY_TRIP_SERVICE_ID = "service_id";
    public static final String KEY_TRIP_DIRECTION_ID = "direction_id";
    public static final String KEY_TRIP_BLOCK_ID = "block_id";
    
    private static final String TRIPS_TABLE_CREATE =
        "create table "+TRIPS_TABLE+" (_id integer primary key autoincrement, "
        + 	KEY_ROUTE_ROUTE_ID		+" integer, " +
        	KEY_TRIP_SERVICE_ID		+" integer," +
        	KEY_TRIP_TRIP_ID		+" integer UNIQUE," +
        	KEY_TRIP_DIRECTION_ID	+" integer," +
        	KEY_TRIP_BLOCK_ID		+" integer" +
        	");";
    
    public static final String KEY_STOP_TIME_ARRIVAL_TIME = "arrival_time";
    public static final String KEY_STOP_TIME_DEPARTURE_TIME = "departure_time";
    public static final String KEY_STOP_TIME_STOP_SEQUENCE = "stop_sequence";
    
    private static final String STOP_TIMES_TABLE_CREATE =
        "create table "+STOP_TIMES_TABLE+" (_id integer primary key autoincrement, "
        + 	KEY_TRIP_TRIP_ID			+" integer, " +
        	KEY_STOP_TIME_ARRIVAL_TIME 		+" text not null," +
        	KEY_STOP_TIME_DEPARTURE_TIME	+" text not null," +
        	KEY_STOP_STOP_ID				+" integer," +
        	KEY_STOP_TIME_STOP_SEQUENCE		+" integer" +
        	");";
    
    private static final String DATABASE_NAME = "data";
    
    private static final int DATABASE_VERSION = 7;
    
    public static int getDatabaseVersion(){
    	return DATABASE_VERSION;
    }

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(STOPS_TABLE_CREATE);
            db.execSQL(STOP_TIMES_TABLE_CREATE);
            db.execSQL(TRIPS_TABLE_CREATE);
            db.execSQL(ROUTES_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
        	db.execSQL("DROP TABLE IF EXISTS "+STOPS_TABLE);
        	db.execSQL("DROP TABLE IF EXISTS "+STOP_TIMES_TABLE);
        	db.execSQL("DROP TABLE IF EXISTS "+TRIPS_TABLE);
        	db.execSQL("DROP TABLE IF EXISTS "+ROUTES_TABLE);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public BusDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the notes database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public BusDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }
    /**
     * Add a new stop to the stops table. If the stop is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @return rowId or -1 if failed
     */
    public long addStop(Stop stop) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_STOP_STOP_ID, stop.stop_id);
        initialValues.put(KEY_STOP_CHECK, stop.stop_check);
        initialValues.put(KEY_STOP_STOP_NAME, stop.stop_name);
        initialValues.put(KEY_STOP_STOP_LAT, stop.stop_lat);
        initialValues.put(KEY_STOP_STOP_LON, stop.stop_lon);
        return mDb.insert(STOPS_TABLE, null, initialValues);
    }
    
    /**
     * Create a new stop time. If the stop is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @return rowId or -1 if failed
     */
    public long addStopTime(StopTime s) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TRIP_TRIP_ID, s.trip_id);
        initialValues.put(KEY_STOP_TIME_ARRIVAL_TIME, s.arrival_time);
        initialValues.put(KEY_STOP_TIME_DEPARTURE_TIME, s.departure_time);
        initialValues.put(KEY_STOP_STOP_ID, s.stop_id);
        initialValues.put(KEY_STOP_TIME_STOP_SEQUENCE,s.stop_sequence);
        
        return mDb.insert(STOP_TIMES_TABLE, null, initialValues);
    }
    
    
    /**
     * Create a new trip. If the stop is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @return rowId or -1 if failed
     */
    public long addTrip(Trip t) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROUTE_ROUTE_ID, t.route_id);
        initialValues.put(KEY_TRIP_SERVICE_ID, t.service_id);
        initialValues.put(KEY_TRIP_TRIP_ID, t.trip_id);
        initialValues.put(KEY_TRIP_DIRECTION_ID, t.direction_id);
        initialValues.put(KEY_TRIP_BLOCK_ID, t.block_id);
        
        return mDb.insert(TRIPS_TABLE, null, initialValues);
    }
    /**
     * Create a new trip. If the stop is
     * successfully created return the new rowId for that note, otherwise return
     * a -1 to indicate failure.
     * 
     * @return rowId or -1 if failed
     */
    public long addRoute(Route route) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ROUTE_ROUTE_ID, route.route_id);
        initialValues.put(KEY_ROUTE_AGENCY_ID, route.agency_id);
        initialValues.put(KEY_ROUTE_ROUTE_SHORT_NAME, route.short_name);
        initialValues.put(KEY_ROUTE_ROUTE_LONG_NAME, route.long_name);
        initialValues.put(KEY_ROUTE_ROUTE_TYPE, route.route_type);
        		
        return mDb.insert(ROUTES_TABLE, null, initialValues);
    }
    
    public Cursor fetchAllRoutes() {
        return mDb.query(ROUTES_TABLE, new String[] {KEY_ROWID, KEY_ROUTE_ROUTE_LONG_NAME},
        		null, null, null, null, null);
    }
    
    public Cursor fetchTripsForRoute(String route_long_name, int direction_id){
    	//Get route ID    	
    	Cursor c = mDb.query(ROUTES_TABLE, new String[] {KEY_ROUTE_ROUTE_ID},
    			KEY_ROUTE_ROUTE_LONG_NAME+" LIKE ?", new String[] {route_long_name},
    			null, null, null);
    	int route_id = -1;
    	if (c.moveToFirst()){
    		int route_col = c.getColumnIndex(KEY_ROUTE_ROUTE_ID);
    		 route_id = c.getInt(route_col);
    	}
    	c.close();
    	
    	String sql = "SELECT " +
    			TRIPS_TABLE+"."+KEY_TRIP_TRIP_ID+" AS _id, " +
    			"MIN("+KEY_STOP_TIME_DEPARTURE_TIME+") FROM " +
    			TRIPS_TABLE+" INNER JOIN " +
    			STOP_TIMES_TABLE+" " +
    			"WHERE " +
    			TRIPS_TABLE+"."+KEY_ROUTE_ROUTE_ID+" = ? AND " +
    			TRIPS_TABLE+"."+KEY_TRIP_DIRECTION_ID+" = ? AND " +
    			STOP_TIMES_TABLE+"."+KEY_TRIP_TRIP_ID+" = "+
    			TRIPS_TABLE+"."+KEY_TRIP_TRIP_ID+" "+
    			"GROUP BY "+STOP_TIMES_TABLE+"."+KEY_TRIP_TRIP_ID+" "+
    			"ORDER BY "+STOP_TIMES_TABLE+"."+KEY_STOP_TIME_DEPARTURE_TIME+" DESC;";
    	System.out.println(sql);
    	return mDb.rawQuery(sql, new String[] {route_id+"", direction_id+""});
    }
    
	public Cursor fetchStopsForTrip(long trip_id) {
		String sql = "SELECT stop_times._id AS _id, stop_name, departure_time FROM " +
		STOP_TIMES_TABLE+" INNER JOIN " +
		STOPS_TABLE+" WHERE " +
		KEY_TRIP_TRIP_ID+" = "+trip_id+
		" AND "+
		STOPS_TABLE+"."+KEY_STOP_STOP_ID+" = "+
		STOP_TIMES_TABLE+"."+KEY_STOP_STOP_ID+" "+
		"ORDER BY "+STOP_TIMES_TABLE+"."+KEY_STOP_TIME_DEPARTURE_TIME+";";
		
		System.out.println(sql);
		
		return mDb.rawQuery(sql, null);
	}
    
    /**
     * Return a Cursor positioned at the note that matches the given rowId
     * 
     * @param rowId id of note to retrieve
     * @return Cursor positioned to matching note, if found
     * @throws SQLException if note could not be found/retrieved
     */
    public Cursor fetch(String table, String[] columns, long rowId) throws SQLException {
        Cursor mCursor =

            mDb.query(true, table, columns, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    
    public void emptyDB(){
    	mDb.delete(STOPS_TABLE, null, null);
    	mDb.delete(STOP_TIMES_TABLE, null, null);
    	mDb.delete(ROUTES_TABLE, null, null);
    	mDb.delete(TRIPS_TABLE, null, null);
    }

	public void recreateTable(String table_name) {
		mDb.execSQL("DROP TABLE "+table_name+";");
		
		//Recreate the table
		if(table_name.equals(BusDBAdapter.STOPS_TABLE)){
			mDb.execSQL(BusDBAdapter.STOPS_TABLE_CREATE);
		}
		else if(table_name.equals(BusDBAdapter.STOP_TIMES_TABLE)){
			mDb.execSQL(BusDBAdapter.STOP_TIMES_TABLE_CREATE);
		}
		else if(table_name.equals(BusDBAdapter.ROUTES_TABLE)){
			mDb.execSQL(BusDBAdapter.ROUTES_TABLE_CREATE);
		}
		else if(table_name.equals(BusDBAdapter.TRIPS_TABLE)){
			mDb.execSQL(BusDBAdapter.TRIPS_TABLE_CREATE);
		}
	}


}
