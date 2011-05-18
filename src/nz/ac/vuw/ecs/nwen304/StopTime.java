package nz.ac.vuw.ecs.nwen304;

import java.util.List;

public class StopTime {

	public int trip_id;
	public String arrival_time;
	public String departure_time;
	public int stop_id;
	public int stop_sequence;
	
	public StopTime(){}
	
	public static void fillDbFromXML(String url, BusDBAdapter bdba){
		//Clear the database table
		bdba.recreateTable(BusDBAdapter.STOP_TIMES_TABLE);
		
		XmlPullFeedParser xml = new XmlPullFeedParser(url);
		List<StopTime> stop_times = xml.parseStopTimes();
    	for(StopTime s : stop_times){
    		bdba.addStopTime(s);
    	}
	}
	
}
