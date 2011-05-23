package nz.ac.vuw.ecs.nwen304;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
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

public class ListingsMap extends MapActivity{
	
	Collection<Listing> listings = null;

	private MapController mapController;
	private LocationManager locationManager;

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
	    
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		ArrayList<Listing> listings = JSONParser.getListings("http://10.0.2.2:3000/listings.json");
        bdba.updateAll(listings);
        
        listings = bdba.getAllListings();
	    
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable lost = this.getResources().getDrawable(R.drawable.redblank);
	    Drawable found = this.getResources().getDrawable(R.drawable.greenblank);
	    for(Listing l : listings){
	    	ListingOverlay itemizedoverlay = new ListingOverlay(l.lost ? lost : found, this);

		    OverlayItem overlayitem = l.asOverlayItem();
		    
		    itemizedoverlay.addOverlay(overlayitem);
		    mapOverlays.add(itemizedoverlay);
	    }
	    
	    mapController = mapView.getController();
		mapController.setZoom(1); // Zoom 1 is world view
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());
	}
	
	/* Remove the locationlistener updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
	}

	
	public class GeoUpdateHandler implements LocationListener {
		//Defaults to Wellington
		int lat = (int) (-41.2924945*1E6);
		int lng = (int) (174.7732353*1E6);
		
		public GeoUpdateHandler(){
			GeoPoint gp = new GeoPoint(lat, lng);
			setLocation(gp, 12);
		}
		
		@Override
		public void onLocationChanged(Location location) {
			this.lat = (int) (location.getLatitude() * 1E6);
			this.lng = (int) (location.getLongitude() * 1E6);
			GeoPoint gp = new GeoPoint(lat, lng);
			setLocation(gp, 12);
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
	
	
}
