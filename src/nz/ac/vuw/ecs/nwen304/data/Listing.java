package nz.ac.vuw.ecs.nwen304.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import nz.ac.vuw.ecs.nwen304.helpers.ListingOverlayItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 
 * A POJO for LostLookout.com Listings. It also knows how to define itself using SQL (SQLite).
 * 
 * @author Nicholas Malcolm - malcolnich - 300170288
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
	public String icon_url;
	
	//Key values for a database
	public static String TABLE_NAME = "listings";
	public static String TITLE = "title";
	public static String LAT = "latitude";
	public static String LONG = "longitude";
	public static String DESCR = "description";
	public static String URL = "url";
	public static String ID = "_id";
	public static String LOST = "lost";
	public static String ICON_URL = "icon_url";
	
	/**
	 * Make a Listing POJO
	 * 
	 * @param title
	 * @param latitude
	 * @param longitude
	 * @param description
	 * @param url
	 * @param id
	 * @param lost
	 */
	public Listing(String title, double latitude, double longitude,
			String description, String url, int id, boolean lost, String icon_url) {
		this.title = title;
		this.latitude = latitude;
		this.longitude = longitude;
		this.description = description;
		this.url = url;
		this.id = id;
		this.lost = lost;
		this.icon_url = icon_url;
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
        initialValues.put(ICON_URL, this.icon_url);
        return initialValues;
	}
	
	public static String createSQLTable(){
		return "CREATE TABLE "+TABLE_NAME+
		" (_id integer primary key autoincrement, title text, latitude float, " +
		"longitude float, description text, url text, lost boolean, icon_url text)";
		
	}
	
	public static String tableColumns(){
		return "(_id, title, latitude, longitude, description, url, lost, icon_url)";
	}
	
	/**
	 * @return
	 */
	public ListingOverlayItem asOverlayItem(){
		return new ListingOverlayItem(getGeoPoint(), longTitle(), this.description, this);
	}
	
	public GeoPoint getGeoPoint(){
		return new GeoPoint((int) (this.latitude*1e6), (int) (this.longitude*1e6));
	}
	
	public String longTitle(){
		return (this.lost ? "Lost " : "Found ")+title;
	}
}
