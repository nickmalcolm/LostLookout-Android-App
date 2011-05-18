package nz.ac.vuw.ecs.nwen304;

import java.util.List;


public class Stop{

	public int stop_id;
	public int stop_check;
	public String stop_name;
	public double stop_lat;
	public double stop_lon;
	
	public Stop(){}
	
	public static void fillDbFromXML(String url, BusDBAdapter bdba){
		//Clear the database table
		bdba.recreateTable(BusDBAdapter.STOPS_TABLE);
		
		//Fill it up again
		XmlPullFeedParser xml = new XmlPullFeedParser(url);
    	List<Stop> stops = xml.parseStops();
    	for(Stop s : stops){
    		bdba.addStop(s);
    	}
	}
	
}
