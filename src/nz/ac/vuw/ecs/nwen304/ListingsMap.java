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
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.urbanairship.push.AirMail;

public class ListingsMap extends MapActivity{
	
	protected static boolean register = true;

	protected String apid;

	Collection<Listing> listings = null;

	private MapController mapController;
	private LocationManager locationManager;
	
	//Wellington
	private double lat = -41.2924945;
	private double lng = 174.7732353;
	public String recent_location = "Wellington";
	private int zoom = 14;
	
	private DBAdapter bdba;

	
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map);
	    
	    bdba = new DBAdapter(this);
        bdba.open();
        
        if(register){
            AirMail am = AirMail.getInstance();
            am.register(this);
        }

	}

	@Override
	public void onResume(){
		super.onResume();
		getPreferences();
		
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    mapController = mapView.getController();
		mapController.setZoom(1); // Zoom 1 is world view

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());
		
		
		
		String args = "lat="+lat+"&lng="+lng;
		String base_url = "http://10.0.2.2:3000/listings/near.json?";
		ArrayList<Listing> listings = JSONParser.getListings(base_url+args);
        
		bdba.updateAll(listings);
        listings = bdba.getAllListings();
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    //Lost items are red pins, found are green
	    Drawable lost = this.getResources().getDrawable(R.drawable.redblank);
	    Drawable found = this.getResources().getDrawable(R.drawable.greenblank);
	    for(Listing l : listings){
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
		
		registerWithLostLookout();
	    
	}
	
	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
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
	
	public void getPreferences(){
		SharedPreferences sp = getApplicationContext().getSharedPreferences("LostLookout", 0);
		this.apid = sp.getString("apid", "");
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
	
}
