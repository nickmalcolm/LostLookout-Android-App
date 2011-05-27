package nz.ac.vuw.ecs.nwen304;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
/**
 * A normal map view doesn't listen for pan or zoom chages.
 * This one does.
 * 
 * Based on http://pa.rezendi.com/2010/03/responding-to-zooms-and-pans-in.html
 * 
 * @author nickmalcolm
 *
 */
public class SmartMapView extends MapView {
	
	Context ctx;

	public SmartMapView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		ctx = context;
	}

	public SmartMapView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		ctx = context;
	}

	public SmartMapView(android.content.Context context, java.lang.String apiKey) {
		super(context, apiKey);
		ctx = context;
	}
	
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction()==MotionEvent.ACTION_UP) {
			GeoPoint center = getMapCenter();
			map_lat = center.getLatitudeE6();
			map_lng = center.getLongitudeE6();
			writePreferences();
		}
		return super.onTouchEvent(ev);
	}
	
	private int oldZoomLevel=0;
	private int map_lat;
	private int map_lng;

	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (getZoomLevel() != oldZoomLevel) {
			//do your thing
			oldZoomLevel = getZoomLevel();
			writePreferences();
		}
	}
	
	private void writePreferences(){
		SharedPreferences sp = ctx.getSharedPreferences(LostLookout.SHARED_PREFS, 0);
		Editor e = sp.edit();
		e.putInt("zoom", oldZoomLevel);
		e.putInt("map_lat_e6", map_lat);
		e.putInt("map_lng_e6", map_lng);
		e.commit();
	}
}