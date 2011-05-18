package nz.ac.vuw.ecs.nwen304;

import java.util.List;

public class Trip {
	
	public int route_id;
	public int service_id;
	public int trip_id;
	public int direction_id;
	public int block_id;
	
	public Trip(){}
	
	public static void fillDbFromXML(String url, BusDBAdapter bdba){
		//Clear the database table
		bdba.recreateTable(BusDBAdapter.TRIPS_TABLE);
		
		XmlPullFeedParser xml = new XmlPullFeedParser(url);
		List<Trip> trips = xml.parseTrips();
    	for(Trip t : trips){
    		bdba.addTrip(t);
    	}
	}

}
