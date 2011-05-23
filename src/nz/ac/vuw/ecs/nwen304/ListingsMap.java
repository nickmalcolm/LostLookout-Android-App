package nz.ac.vuw.ecs.nwen304;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ListingsMap extends MapActivity{
	
	Collection<Listing> listings = null;

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
        
        listings = bdba.getAllListings();
	    
	    MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.androidmarker);
	    for(Listing l : listings){
	    	ListingOverlay itemizedoverlay = new ListingOverlay(drawable);

		    OverlayItem overlayitem = l.asOverlayItem();
		    
		    itemizedoverlay.addOverlay(overlayitem);
		    mapOverlays.add(itemizedoverlay);
	    }
	    
	}

}
