package nz.ac.vuw.ecs.nwen304;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;
/**
 * Project 2 - Nicholas Malcolm - 300170288
 * NWEN304 T12011
 * 
 * @author malcolnich
 *
 */
public class XmlPullFeedParser extends BaseFeedParser{
	
	public final String RECORD = "RECORD";
	public final String DOCUMENT = "DOCUMENT";
	
    public XmlPullFeedParser(String feedUrl) {
		super(feedUrl);
		// TODO Auto-generated constructor stub
	}

    
	public List<Stop> parseStops(){
		List<Stop> stops = null;
		
		XmlPullParser parser = Xml.newPullParser();
	    try {
	        // auto-detect the encoding from the stream
	        parser.setInput(this.getInputStream(), null);
	        int eventType = parser.getEventType();
	        Stop currentStop = null;
	        boolean done = false;
	        while (eventType != XmlPullParser.END_DOCUMENT && !done){
	        	String name = null;
	        	  switch (eventType){
	        	    case XmlPullParser.START_DOCUMENT:
	        	      stops = new ArrayList<Stop>();
	        	      break;
	        	    case XmlPullParser.START_TAG:
	        	        name = parser.getName();
	        	        if(name.equalsIgnoreCase(DOCUMENT)){
	        	        	//Do nothing
	        	        }
	        	        if(name.equalsIgnoreCase(RECORD)){
	        	        	currentStop = new Stop();
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_STOP_STOP_ID)){
	        	        	currentStop.stop_id = Integer.parseInt(parser.nextText());
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_STOP_CHECK)){
	        	        	currentStop.stop_check = Integer.parseInt(parser.nextText());
	        	        }else if(name.equalsIgnoreCase(BusDBAdapter.KEY_STOP_STOP_NAME)){
	        	        	currentStop.stop_name = parser.nextText();
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_STOP_STOP_LAT)){
	        	        	currentStop.stop_lat = Double.parseDouble(parser.nextText());
	        	        }else if(name.equalsIgnoreCase(BusDBAdapter.KEY_STOP_STOP_LON)){
	        	        	currentStop.stop_lon = Double.parseDouble(parser.nextText());
	        	        }
	        	        break;
	        	    case XmlPullParser.END_TAG:
	        	      name = parser.getName();
	        	      if (name.equalsIgnoreCase(RECORD) && currentStop != null){
	        	        stops.add(currentStop);
	        	      }
	        	      break;

	        	  }
	        	  eventType = parser.next();
	        }
	    }
	    catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	    return stops;
	 }
	
	public List<Route> parseRoutes(){
		List<Route> routes = null;
		
		XmlPullParser parser = Xml.newPullParser();
	    try {
	        // auto-detect the encoding from the stream
	        parser.setInput(this.getInputStream(), null);
	        int eventType = parser.getEventType();
	        Route currentRoute = null;
	        boolean done = false;
	        while (eventType != XmlPullParser.END_DOCUMENT && !done){
	        	String name = null;
	        	  switch (eventType){
	        	    case XmlPullParser.START_DOCUMENT:
	        	      routes = new ArrayList<Route>();
	        	      break;
	        	    case XmlPullParser.START_TAG:
	        	        name = parser.getName();
	        	        if(name.equalsIgnoreCase(DOCUMENT)){
	        	        	//Do nothing
	        	        }
	        	        if(name.equalsIgnoreCase(RECORD)){
	        	        	currentRoute = new Route();
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_ROUTE_ROUTE_ID)){
	        	        	currentRoute.route_id = Integer.parseInt(parser.nextText());
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_ROUTE_AGENCY_ID)){
	        	        	currentRoute.agency_id = parser.nextText();
	        	        }else if(name.equalsIgnoreCase(BusDBAdapter.KEY_ROUTE_ROUTE_SHORT_NAME)){
	        	        	currentRoute.short_name = parser.nextText();
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_ROUTE_ROUTE_LONG_NAME)){
	        	        	currentRoute.long_name = parser.nextText();
	        	        }else if(name.equalsIgnoreCase(BusDBAdapter.KEY_ROUTE_ROUTE_TYPE)){
	        	        	currentRoute.route_type = Integer.parseInt(parser.nextText());
	        	        }
	        	        break;
	        	    case XmlPullParser.END_TAG:
	        	      name = parser.getName();
	        	      if (name.equalsIgnoreCase(RECORD) && currentRoute != null){
	        	        routes.add(currentRoute);
	        	      }
	        	      break;

	        	  }
	        	  eventType = parser.next();
	        }
	    }
	    catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	    return routes;
	 }
	
	public List<Trip> parseTrips(){
		List<Trip> trips = null;
		
		XmlPullParser parser = Xml.newPullParser();
	    try {
	        // auto-detect the encoding from the stream
	        parser.setInput(this.getInputStream(), null);
	        int eventType = parser.getEventType();
	        Trip currentTrip = null;
	        boolean done = false;
	        while (eventType != XmlPullParser.END_DOCUMENT && !done){
	        	String name = null;
	        	  switch (eventType){
	        	    case XmlPullParser.START_DOCUMENT:
	        	      trips = new ArrayList<Trip>();
	        	      break;
	        	    case XmlPullParser.START_TAG:
	        	        name = parser.getName();
	        	        if(name.equalsIgnoreCase(DOCUMENT)){
	        	        	//Do nothing
	        	        }
	        	        if(name.equalsIgnoreCase(RECORD)){
	        	        	currentTrip = new Trip();
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_ROUTE_ROUTE_ID)){
	        	        	currentTrip.route_id = Integer.parseInt(parser.nextText());
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_TRIP_SERVICE_ID)){
	        	        	currentTrip.service_id = Integer.parseInt(parser.nextText());
	        	        }else if(name.equalsIgnoreCase(BusDBAdapter.KEY_TRIP_TRIP_ID)){
	        	        	currentTrip.trip_id = Integer.parseInt(parser.nextText());
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_TRIP_DIRECTION_ID)){
	        	        	currentTrip.direction_id = Integer.parseInt(parser.nextText());
	        	        }else if(name.equalsIgnoreCase(BusDBAdapter.KEY_TRIP_BLOCK_ID)){
	        	        	currentTrip.block_id = Integer.parseInt(parser.nextText());
	        	        }
	        	        break;
	        	    case XmlPullParser.END_TAG:
	        	      name = parser.getName();
	        	      if (name.equalsIgnoreCase(RECORD) && currentTrip != null){
	        	        trips.add(currentTrip);
	        	      }
	        	      break;

	        	  }
	        	  eventType = parser.next();
	        }
	    }
	    catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	    return trips;
	 }
	
	public List<StopTime> parseStopTimes(){
		List<StopTime> stop_times = null;
		
		XmlPullParser parser = Xml.newPullParser();
	    try {
	        // auto-detect the encoding from the stream
	        parser.setInput(this.getInputStream(), null);
	        int eventType = parser.getEventType();
	        StopTime currentStopTime = null;
	        boolean done = false;
	        while (eventType != XmlPullParser.END_DOCUMENT && !done){
	        	String name = null;
	        	  switch (eventType){
	        	    case XmlPullParser.START_DOCUMENT:
	        	      stop_times = new ArrayList<StopTime>();
	        	      break;
	        	    case XmlPullParser.START_TAG:
	        	        name = parser.getName();
	        	        if(name.equalsIgnoreCase(DOCUMENT)){
	        	        	//Do nothing
	        	        }
	        	        if(name.equalsIgnoreCase(RECORD)){
	        	        	currentStopTime = new StopTime();
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_TRIP_TRIP_ID)){
	        	        	currentStopTime.trip_id = Integer.parseInt(parser.nextText());
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_STOP_TIME_ARRIVAL_TIME)){
	        	        	currentStopTime.arrival_time = parser.nextText();
	        	        }else if(name.equalsIgnoreCase(BusDBAdapter.KEY_STOP_TIME_DEPARTURE_TIME)){
	        	        	currentStopTime.departure_time = parser.nextText();
	        	        } else if(name.equalsIgnoreCase(BusDBAdapter.KEY_STOP_STOP_ID)){
	        	        	currentStopTime.stop_id = Integer.parseInt(parser.nextText());
	        	        }else if(name.equalsIgnoreCase(BusDBAdapter.KEY_STOP_TIME_STOP_SEQUENCE)){
	        	        	currentStopTime.stop_sequence = Integer.parseInt(parser.nextText());
	        	        }
	        	        break;
	        	    case XmlPullParser.END_TAG:
	        	      name = parser.getName();
	        	      if (name.equalsIgnoreCase(RECORD) && currentStopTime != null){
	        	        stop_times.add(currentStopTime);
	        	      }
	        	      break;

	        	  }
	        	  eventType = parser.next();
	        }
	    }
	    catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	    return stop_times;
	 }
	
	public HashMap<String, Integer> getDBVersions(){
		HashMap<String, Integer> versions = new HashMap<String, Integer>();
		String file_name = "";
		int version = -1;
		XmlPullParser parser = Xml.newPullParser();
	    try {
	        // auto-detect the encoding from the stream
	        parser.setInput(this.getInputStream(), null);
	        int eventType = parser.getEventType();
	        boolean done = false;
	        while (eventType != XmlPullParser.END_DOCUMENT && !done){
	        	String name = null;
	        	  switch (eventType){
	        	    case XmlPullParser.START_DOCUMENT:
	        	      break;
	        	    case XmlPullParser.START_TAG:
	        	        name = parser.getName();
	        	        if(name.equalsIgnoreCase(DOCUMENT)){
	        	        	//Do nothing
	        	        }
	        	        if(name.equalsIgnoreCase(RECORD)){
	        	        	file_name = "";
	        	        	version = -1;
	        	        } else if(name.equalsIgnoreCase("data")){
	        	        	file_name = parser.nextText();
	        	        } else if(name.equalsIgnoreCase("version")){
	        	        	version = Integer.parseInt(parser.nextText());
	        	        }
	        	        break;
	        	    case XmlPullParser.END_TAG:
	        	      name = parser.getName();
	        	      if (name.equalsIgnoreCase(RECORD) && !file_name.equals("")){
	        	    	//Get the right table name
	        	    	String table  = null;
	        	    	if(file_name.equalsIgnoreCase("routes.xml")){
	        	    		table = BusDBAdapter.ROUTES_TABLE;
	        	    	}else if(file_name.equals("stop_times.xml")){
	        	    		table = BusDBAdapter.STOP_TIMES_TABLE;
	        	    	}else if(file_name.equals("stops.xml")){
	        	    		table = BusDBAdapter.STOPS_TABLE;
	        	    	}else if(file_name.equals("trips.xml")){
	        	    		table = BusDBAdapter.TRIPS_TABLE;
	        	    	}
	        	        versions.put(table, version);
	        	      }
	        	      break;

	        	  }
	        	  eventType = parser.next();
	        }
	    }
	    catch (Exception e) {
	    	throw new RuntimeException(e);
	    }
	    return versions;
	 }
}

