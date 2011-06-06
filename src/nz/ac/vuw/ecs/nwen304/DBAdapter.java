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

import java.util.ArrayList;
import java.util.Collection;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Project 3 - Nicholas Malcolm - 300170288
 * NWEN304 T12011
 * 
 * @author malcolnich
 *
 */
public class DBAdapter {

    public static final String KEY_ROWID = "_id";

    private static final String TAG = "ListingsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_NAME = "data";
    
    private static final int DATABASE_VERSION = 3;
    
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
            db.execSQL(Listing.createSQLTable());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
        	db.execSQL("DROP TABLE IF EXISTS "+Listing.TABLE_NAME);
            onCreate(db);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public DBAdapter(Context ctx) {
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
    public DBAdapter open() throws SQLException {
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
    public long addListing(Listing listing) {
		Log.i("DB: ", "Adding "+listing.title+" to the database.");
        return mDb.insert(Listing.TABLE_NAME, null, listing.getContentValues());
    }
    
    /**
     * Takes a Collection of Listing and either adds or updates
     * @param listings
     */
    public void updateAll(Collection<Listing> listings){
    	int success = 0;
    	for(Listing l : listings){
    		Log.i("DB: ", "Updating "+l.title+" in the database.");
    		
    		success = mDb.update(Listing.TABLE_NAME, l.getContentValues(), "_id = ?", new String[] {l.id+""});
    		if(success < 1){
    			addListing(l);
    		}
    	}
    }
    
    /**
     * Will attempt to add any and all listings to DB
     * @param listings
     */
    public void addListings(Collection<Listing> listings){
    	for(Listing l : listings){
    		addListing(l);
    	}
    }
    
    public Cursor fetchAllListings() {
        return mDb.rawQuery("SELECT * FROM "+Listing.TABLE_NAME+";", null);
    }
    
    public ArrayList<Listing> getAllListings(){
    	Cursor cur = mDb.query(Listing.TABLE_NAME, null, null, null, null, null, null);
    	boolean importing = cur.moveToFirst();
    	
    	ArrayList<Listing> listings = new ArrayList<Listing>();
    	
    	int title = cur.getColumnIndex(Listing.TITLE);
    	int lat = cur.getColumnIndex(Listing.LAT);
    	int l_long = cur.getColumnIndex(Listing.LONG);
    	int descr = cur.getColumnIndex(Listing.DESCR);
    	int url = cur.getColumnIndex(Listing.URL);
    	int id = cur.getColumnIndex(Listing.ID);
    	int lost = cur.getColumnIndex(Listing.LOST);
    	int icon_url = cur.getColumnIndex(Listing.ICON_URL);
    	
    	
    	while(importing){
    		 listings.add(new Listing(
    			cur.getString(title),
    			cur.getDouble(lat),
    			cur.getDouble(l_long),
    			cur.getString(descr),
    			cur.getString(url),
    			cur.getInt(id),
    			cur.getInt(lost) == 1,
    			cur.getString(icon_url)
    		));
    		if(!cur.moveToNext()){
    			importing = false;
    		}
    	}
    	cur.close();
    	return listings;
    	
    }
    

}
