package nz.ac.vuw.ecs.nwen304.models;

import android.content.ContentValues;

/**
 * A POJO for LostLookout.com Listings. It also knows how to define itself as SQL (SQLite).
 * 
 * @author nickmalcolm
 *
 */
public class Listing {

	public String title;
	public double latitude;
	public double longitude;
	public String description;
	public String url;
	public int id;
	public boolean lost;
	
	//Key values for a database
	public static String TABLE_NAME = "listings";
	public static String TITLE = "title";
	public static String LAT = "latitude";
	public static String LONG = "longitude";
	public static String DESCR = "description";
	public static String URL = "url";
	public static String ID = "_id";
	public static String LOST = "lost";
	
	public Listing(String title, double latitude, double longitude,
			String description, String url, int id, boolean lost) {
		this.title = title;
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
		this.url = url;
		this.id = id;
		this.lost = lost;
	}
	
	public ContentValues getContentValues(){
		ContentValues initialValues = new ContentValues();
        initialValues.put(ID, this.id);
        initialValues.put(TITLE, this.title);
        initialValues.put(LAT, this.latitude);
        initialValues.put(LONG, this.longitude);
        initialValues.put(DESCR, this.description);
        initialValues.put(URL, this.url);
        initialValues.put(LOST, this.lost);
        return initialValues;
	}
	
	public static String createSQLTable(){
		return "CREATE TABLE "+TABLE_NAME+
		" (_id integer primary key autoincrement, title text, latitude float, " +
		"longitude float, description text, url text, lost boolean)";
		
	}
	
	public static String tableColumns(){
		return "(_id, title, latitude, longitude, description, url, lost)";
	}

}
