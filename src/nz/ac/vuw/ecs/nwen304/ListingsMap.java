package nz.ac.vuw.ecs.nwen304;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nz.ac.vuw.ecs.nwen304.helpers.DistanceCalculator;
import nz.ac.vuw.ecs.nwen304.helpers.ListingOverlay;
import nz.ac.vuw.ecs.nwen304.helpers.ListingOverlayItem;
import nz.ac.vuw.ecs.nwen304.helpers.ReverseGeocode;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.urbanairship.push.AirMail;

/**
 * The main view for this app. It displays Listings on a map. 
 * 
 * It fetches Listings from LostLookout.com
 * 
 * It also keeps LostLookout.com notified of it's most recent location. 
 * This allows geographically targeted notifications when new listings
 * are created on the website
 * 
 * @author Nicholas Malcolm - malcolnich - 300170288
 *
 */
public class ListingsMap extends MapActivity{
	
	protected static boolean register = true;

	//A unique identifier for UrbanAirship push notifications
	protected String apid;

	private MapView mapView; //SmartMapView mapView;
	
	Collection<Listing> listings = null;

	private MapController mapController;
	private LocationManager locationManager;
	
	//Wellington
	private double lat = -41.2924945;
	private double lng = 174.7732353;
	public String recent_location = "Wellington";
	private int zoom = 14;
	private int distance_in_m = 10;
	private boolean show_found = true;
	private GeoPoint map_center = new GeoPoint(mapLat(), mapLng());
	
	private DBAdapter dba;

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
        
	    dba = new DBAdapter(this);
        dba.open();
	    
        if(register){
        	//Make sure this installation is able to recieve pushes
            AirMail am = AirMail.getInstance();
            am.register(this);
        }
        
	}

	@Override
	public void onResume(){
		super.onResume();
		
		//Create the map
		
	    this.mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    mapController = mapView.getController();
	    mapController.setCenter(map_center);
		mapController.setZoom(zoom); // Zoom 1 is world view

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());
		
		pullAndShowListings();
		
		registerWithLostLookout();
	    
	}
	
	/**
	 * Fetches the Listings from LostLookout and displays them on the map
	 */
	private void pullAndShowListings() {
		readPreferences();
		String args = "lat="+lat+"&lng="+lng+"&within="+(dist_to_km());
		String base_url = LostLookout.BASE_URL+"listings/near.json?";
		ArrayList<Listing> listings = JSONParser.getListings(base_url+args);
        
		dba.updateAll(listings);
        listings = dba.getAllListings();
	    
        
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    //We always want to redraw this, incase some have been removed.
	    mapOverlays.clear();
	    //Lost items are red pins, found are green
	    Drawable lost = this.getResources().getDrawable(R.drawable.redblank);
	    Drawable found = this.getResources().getDrawable(R.drawable.greenblank);
	    for(Listing l : listings){
	    	
	    	//If we don't want to show found items
	    	if(!show_found && !l.lost){
	    		continue;
	    	}
	    	
	    	//We might have lessened the distance radius. Those listings will still be in the database,
	    	// but we don't want to show them!
	    	if(DistanceCalculator.distance(lat, lng, l.latitude, l.longitude, 'K') > (dist_to_km())){
	    		continue;
	    	}
	    	
	    	ListingOverlay itemizedoverlay = new ListingOverlay(l.lost ? lost : found, this);

		    ListingOverlayItem overlayitem = l.asOverlayItem();
		    
		    itemizedoverlay.addOverlay(overlayitem);
		    mapOverlays.add(itemizedoverlay);
	    }
	    
		try {
			URL url_u = new URL(base_url+args);
	    	InputStream response = url_u.openStream();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private double dist_to_km(){
		return (double) distance_in_m/1000;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getMenuInflater().inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.refresh:
	    	pullAndShowListings();
	        return true;
	    case R.id.settings:
	    	Intent i = new Intent(this, SettingsView.class);
	        startActivityForResult(i, 0);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	/**
	 * Returns the latitude*1E6
	 * @return
	 */
	public int mapLat(){
		return (int) (lat*1E6);
	}
	
	/**
	 * Returns the longitude*1E6
	 * @return
	 */
	public int mapLng(){
		return (int) (lng*1E6);
	}

	/**
	 * Use to track GPS data
	 * 
	 * @author Nicholas Malcolm - malcolnich - 300170288
	 *
	 */
	public class GeoUpdateHandler implements LocationListener {
		//Defaults to Wellington
		
		public GeoUpdateHandler(){
			GeoPoint gp = new GeoPoint(mapLat(), mapLng());
			setLocation(gp, zoom );
			setRecentLocation();
			registerWithLostLookout();
		}
		
		@Override
		public void onLocationChanged(Location location) {
			lat = location.getLatitude();
			lng = location.getLongitude();
			GeoPoint gp = new GeoPoint(mapLat(), mapLng());
			setRecentLocation();
			setLocation(gp, zoom);
			registerWithLostLookout();
		}
		
		public void setLocation(GeoPoint gp, int zoom){
			mapController.animateTo(gp); //	mapController.setCenter(point);
			mapController.setZoom(zoom);
		}
		

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
	
	/**
	 * Figures out our general area, e.g. "Wellington".
	 * This allows targeted push notifications
	 */
	public void setRecentLocation(){
		List<Address> possibleAddresses = new ArrayList<Address>();
		possibleAddresses = ReverseGeocode.getFromLocation(lat, lng, 3);

		if(!possibleAddresses.isEmpty()){
			recent_location = possibleAddresses.get(0).getAdminArea();
		}
	}
	
	/**
	 * Registers the current state of this application
	 * with LostLookout.com
	 */
    public void registerWithLostLookout() {
        
        //Build parameter string
        String data = "area="+recent_location+"&apid="+apid;
        try {
            
            // Send the request
            URL url = new URL(LostLookout.BASE_URL+"devices/register");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            
            //write parameters
            writer.write(data);
            writer.flush();
            
            // Get the response
            StringBuffer answer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();
            
            //Output the response
            System.out.println(answer.toString());
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
      
        }
   }
    
    /**
     * Read various shared preferences
     */
	private void readPreferences(){
		SharedPreferences sp = this.getSharedPreferences(LostLookout.SHARED_PREFS, 0);
		this.apid = sp.getString("apid", "");
		this.distance_in_m = sp.getInt("distance", 10000);
		this.show_found = sp.getBoolean("show_found", true);
		this.zoom = sp.getInt("zoom", 14);
		int lat = sp.getInt("map_lat_e6", -1);
		int lng = sp.getInt("map_lng_e6", -1);
		
		if(lat != -1 && lng != -1){
			this.map_center = new GeoPoint(lat, lng);
		}
	}
	
}
