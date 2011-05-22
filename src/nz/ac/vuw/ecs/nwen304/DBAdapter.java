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

import java.util.Collection;

import nz.ac.vuw.ecs.nwen304.models.Listing;

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
public class DBAdapter {

    public static final String KEY_ROWID = "_id";

    private static final String TAG = "ListingsDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_NAME = "data";
    
    private static final int DATABASE_VERSION = 1;
    
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
        return mDb.insert(Listing.TABLE_NAME, null, listing.getContentValues());
    }
    
    /**
     * Will attempt to add any and all listings to DB
     * @param listings
     */
    public void addListings(Collection<Listing> listings){
    	for(Listing l : listings){
    		Log.i("DB: ", "Adding "+l.title+" to the database.");
    		addListing(l);
    	}
    }
    
    public Cursor fetchAllListings() {
        return mDb.rawQuery("SELECT * FROM "+Listing.TABLE_NAME+";", null);
    }


}
