package nz.ac.vuw.ecs.nwen304;

import android.graphics.Canvas;
import android.view.MotionEvent;

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

	public SmartMapView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public SmartMapView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SmartMapView(android.content.Context context, java.lang.String apiKey) {
		super(context, apiKey);
	}
	
	public boolean onTouchEvent(MotionEvent ev) {
		if (ev.getAction()==MotionEvent.ACTION_UP) {
			//do your thing
		}
		return super.onTouchEvent(ev);
	}
	
	int oldZoomLevel=-1;

	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (getZoomLevel() != oldZoomLevel) {
			//do your thing
			oldZoomLevel = getZoomLevel();
		}
	}
}