package nz.ac.vuw.ecs.nwen304;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Project 2 - Nicholas Malcolm - 300170288
 * NWEN304 T12011
 * 
 * @author malcolnich
 *
 *
 * 
 */
public class RouteSelector extends Activity {
	
	public final static String SHARED_PREFS_NAME = "DulutyRoutesPrefs";
	
	private BusDBAdapter bdba;
	
	private String route_selected = "";
	private int direction_selected = 0;
	private int spinner_selected = 0;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_selector);
        bdba = new BusDBAdapter(this);
        bdba.open();
    }

	protected void onResume() { 
    	super.onResume();
    	
    	populateDB();
    	
        setContentView(R.layout.route_selector); 
        
        //Populate the database
    	//getData();	
        Cursor mRoutesCursor = bdba.fetchAllRoutes();
        startManagingCursor(mRoutesCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{BusDBAdapter.KEY_ROUTE_ROUTE_LONG_NAME};

        // create simple cursor adapter
        SimpleCursorAdapter adapter =
          new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, 
        		  mRoutesCursor, 
        		  from, 
        		  new int[] {android.R.id.text1} );
        
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        // get reference to our spinner
        final Spinner s = (Spinner) findViewById( R.id.route_spinner );
        
        
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				spinner_selected = s.getSelectedItemPosition();
				
				Cursor cc = (Cursor)(s.getSelectedItem());
				startManagingCursor(cc);
				if (cc != null) {
				     route_selected = cc.getString(
				        cc.getColumnIndex(BusDBAdapter.KEY_ROUTE_ROUTE_LONG_NAME));
				}

		    	setPreferences();
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        s.setSelection(spinner_selected);
        
        
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Toast.makeText(getApplicationContext(),route_selected+" "+direction_selected,Toast.LENGTH_SHORT).show();

            	setPreferences();
            	
            	Intent i = new Intent(getApplicationContext(), TripsActivity.class);
                startActivity(i);
            }
        });
        
	    final CheckBox checkbox = (CheckBox) findViewById(R.id.outbound_checkbox);
	    checkbox.setChecked(direction_selected == 1);
	    checkbox.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	            // Perform action on clicks, depending on whether it's now checked
	            if (((CheckBox) v).isChecked()) {
	            	direction_selected = 1;
	            } else {
	            	direction_selected = 0;
	            }

	        	setPreferences();
	        }
	    });
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    	setPreferences();
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    	SharedPreferences sp = getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME, 0);
    	//Set these anytime this starts - defaults are OK
    	
    	route_selected = sp.getString("route_selected", "");
    	direction_selected = sp.getInt("direction_selected", 0);
    	spinner_selected = sp.getInt("spinner_selected", 0);
    }
    
    private void setPreferences(){
    	SharedPreferences sp = getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME, 0);
    	Editor e = sp.edit();
    	e.putString("route_selected", route_selected);
    	e.putInt("direction_selected", direction_selected);
    	e.putInt("spinner_selected", spinner_selected);
    	e.commit();
    }
    

    /**
     * Populates the database with various timetables if they need to be updated
     */
    private void populateDB() {        
        
    	SharedPreferences sp = getApplicationContext().getSharedPreferences(SHARED_PREFS_NAME, 0);
    	
    	//Get the current table versions from the preferences
    	//Default to -1, so they'll always update if we lose data
    	int cur_stops_version = sp.getInt(BusDBAdapter.STOPS_TABLE+"_version", -1);
    	int cur_stop_times_version = sp.getInt(BusDBAdapter.STOP_TIMES_TABLE+"_version", -1);
    	int cur_routes_version = sp.getInt(BusDBAdapter.ROUTES_TABLE+"_version", -1);
    	int cur_trips_version = sp.getInt(BusDBAdapter.TRIPS_TABLE+"_version", -1);
    	
    	//Get the latest table versions available
    	XmlPullFeedParser xml = new XmlPullFeedParser("http://homepages.ecs.vuw.ac.nz/~malcolnich/versions-mixed.xml");
    	HashMap<String, Integer> versions = xml.getDBVersions();
    	
    	//For saving new versions to preferences
		Editor e = sp.edit();
    	
    	//Compare and update if needed
		
		
    	if(cur_stops_version < versions.get(BusDBAdapter.STOPS_TABLE)){
    		Stop.fillDbFromXML("http://ecs.vuw.ac.nz/~ian/nwen304/stops.xml", bdba);
			e.putInt(BusDBAdapter.STOPS_TABLE+"_version", versions.get(BusDBAdapter.STOPS_TABLE));

    		toastIt("Updated Stops...");
    	}
    	
    	if(cur_stop_times_version < versions.get(BusDBAdapter.STOP_TIMES_TABLE)){
    		StopTime.fillDbFromXML("http://ecs.vuw.ac.nz/~ian/nwen304/stop_times.xml", bdba);
			e.putInt(BusDBAdapter.STOP_TIMES_TABLE+"_version", versions.get(BusDBAdapter.STOP_TIMES_TABLE));
			
    		toastIt("Updated Stop Times...");
    	}
    	
    	
    	if(cur_trips_version < versions.get(BusDBAdapter.TRIPS_TABLE)){
    		Trip.fillDbFromXML("http://ecs.vuw.ac.nz/~ian/nwen304/trips.xml", bdba);
			e.putInt(BusDBAdapter.TRIPS_TABLE+"_version", versions.get(BusDBAdapter.TRIPS_TABLE));

			toastIt("Updated Trips...");
    	}
    	
    	if(cur_routes_version < versions.get(BusDBAdapter.ROUTES_TABLE)){
    		Route.fillDbFromXML("http://ecs.vuw.ac.nz/~ian/nwen304/routes.xml", bdba);
			e.putInt(BusDBAdapter.ROUTES_TABLE+"_version", versions.get(BusDBAdapter.ROUTES_TABLE));

			toastIt("Updated Routes...");
    	}

    	//Save the new versions
    	e.commit();
    	
	}
    
    private void toastIt(String msg){
    	Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
