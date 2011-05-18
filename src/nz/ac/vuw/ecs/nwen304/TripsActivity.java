package nz.ac.vuw.ecs.nwen304;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Project 2 - Nicholas Malcolm - 300170288
 * NWEN304 T12011
 * 
 * @author malcolnich
 *
 */
public class TripsActivity extends ListActivity {
    
	private BusDBAdapter bdba;
	
	private String route_selected = "";
	private int direction_selected = 0;
	private long trip_row_selected = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bdba = new BusDBAdapter(this);
        bdba.open();  
        registerForContextMenu(getListView());
    }
    
    protected void onResume(){
    	super.onResume();
    	setContentView(R.layout.list);
    	
    	String dir = direction_selected == 0 ? "Inbound" : "Outbound";
    	
    	setTitle("Select departure time on "+route_selected+" "+dir);
    	
    	Cursor mTripsCursor = bdba.fetchTripsForRoute(route_selected, direction_selected);
        startManagingCursor(mTripsCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{"MIN("+BusDBAdapter.KEY_STOP_TIME_DEPARTURE_TIME+")"};

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] to = new int[]{R.id.big};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter times = 
            new SimpleCursorAdapter(this, R.layout.stops_row, mTripsCursor, from, to);
        setListAdapter(times);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
		menu.add(0,R.id.route_selector,0,R.string.select_route);
		return true;
    }
    
    public boolean onOptionsItemSelected (MenuItem item){

    	switch (item.getItemId()){

    	case R.id.route_selector:
    		finish();
    		return true;
    	}

    	return false;

    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    	savePreferences();
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    	SharedPreferences sp = getApplicationContext().getSharedPreferences(RouteSelector.SHARED_PREFS_NAME, 0);
    	//Set these anytime this starts - defaults are OK
    	
    	route_selected = sp.getString("route_selected", "");
    	direction_selected = sp.getInt("direction_selected", 0);
    }
    
    private void savePreferences(){
    	SharedPreferences sp = getApplicationContext().getSharedPreferences(RouteSelector.SHARED_PREFS_NAME, 0);
    	Editor e = sp.edit();
    	e.putString("route_selected", route_selected);
    	e.putInt("direction_selected", direction_selected);
    	e.putLong("trip_row_id", trip_row_selected);
    	e.commit();
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(this, StopsActivity.class);
        trip_row_selected = id;
        savePreferences();
        startActivityForResult(i, 0);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	//If they didn't press the back button, they must want this to die
    	if (resultCode != RESULT_CANCELED){
        	finish();
    	}
    }

}
