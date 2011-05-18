package nz.ac.vuw.ecs.nwen304;

import java.util.List;

public class Route {

	public int route_id;
	public String agency_id;
	public String short_name;
	public String long_name;
	public int route_type;
	
	public Route(){}
	
	public static void fillDbFromXML(String url, BusDBAdapter bdba){
		//Clear the database table
		bdba.recreateTable(BusDBAdapter.ROUTES_TABLE);
		
		XmlPullFeedParser xml = new XmlPullFeedParser(url);
    	List<Route> routes = xml.parseRoutes();
    	for(Route r : routes){
    		bdba.addRoute(r);
    	}
	}
	
}
