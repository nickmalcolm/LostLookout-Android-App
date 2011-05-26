package nz.ac.vuw.ecs.nwen304;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class ListingOverlayItem extends OverlayItem {

	private Listing listing = null;
	
	public ListingOverlayItem(GeoPoint point, String title, String snippet, Listing listing) {
		super(point, title, snippet);
		this.listing = listing;
	}
	
	public Listing getListing(){
		return listing;
	}

}
