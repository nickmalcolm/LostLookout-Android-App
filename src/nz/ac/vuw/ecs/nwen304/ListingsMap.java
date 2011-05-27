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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.urbanairship.push.AirMail;

public class ListingsMap extends MapActivity{
	
	protected static boolean register = true;

	protected String apid;

	private MapView mapView;
	
	Collection<Listing> listings = null;

	private MapController mapController;
	private LocationManager locationManager;
	
	//Wellington
	private double lat = -41.2924945;
	private double lng = 174.7732353;
	public String recent_location = "Wellington";
	private int zoom = 14;
	private int distance = 10;
	private boolean show_found = true;
	
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
            AirMail am = AirMail.getInstance();
            am.register(this);
        }
        
	}

	@Override
	public void onResume(){
		super.onResume();
		
	    this.mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    mapController = mapView.getController();
		mapController.setZoom(1); // Zoom 1 is world view

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());
		
		pullAndShowListings();
		
		registerWithLostLookout();
	    
	}
	
	private void pullAndShowListings() {
		readPreferences();
		String args = "lat="+lat+"&lng="+lng+"&within="+distance;
		String base_url = LostLookout.BASE_URL+"listings/near.json?";
		ArrayList<Listing> listings = JSONParser.getListings(base_url+args);
        
		dba.updateAll(listings);
        listings = dba.getAllListings();
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
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
	    	if(DistanceCalculator.distance(lat, lng, l.latitude, l.longitude, 'K') > distance){
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
	        return true;
	    case R.id.settings:
	    	Intent i = new Intent(this, SettingsView.class);
	        startActivityForResult(i, 0);
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public int mapLat(){
		return (int) (lat*1E6);
	}
	
	public int mapLng(){
		return (int) (lng*1E6);
	}

	
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
	

	public void setRecentLocation(){
		List<Address> possibleAddresses = new ArrayList<Address>();
		possibleAddresses = ReverseGeocode.getFromLocation(lat, lng, 3);

		if(!possibleAddresses.isEmpty()){
			recent_location = possibleAddresses.get(0).getAdminArea();
		}
	}
	
    public void registerWithLostLookout() {
        
        //Build parameter string
        String data = "area="+recent_location+"&apid="+apid;
        try {
            
            // Send the request
            URL url = new URL("http://10.0.2.2:3000/devices/register");
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
    
	private void readPreferences(){
		SharedPreferences sp = this.getSharedPreferences(LostLookout.SHARED_PREFS, 0);
		this.apid = sp.getString("apid", "");
		this.distance = sp.getInt("distance", 10);
		this.show_found = sp.getBoolean("show_found", true);
	}
	
}
