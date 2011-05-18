package nz.ac.vuw.ecs.nwen304;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;
/**
 * Project 2 - Nicholas Malcolm - 300170288
 * NWEN304 T12011
 * 
 * @author malcolnich
 *
 */
public class StopsActivity extends ListActivity {
	    
//	private BusDBAdapter bdba;
//	
//	private long trip_row_id = -1;
//
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        bdba = new BusDBAdapter(this);
//        bdba.open();  
//        registerForContextMenu(getListView());
//    }
//    
//    protected void onResume(){
//    	super.onResume();
//    	setContentView(R.layout.list);
//    	
//    	SharedPreferences sp = getApplicationContext().getSharedPreferences(RouteSelector.SHARED_PREFS_NAME, 0);
//    	String route_selected = sp.getString("route_selected", "");
//    	String dir = sp.getInt("direction_selected", 0) == 0 ? "Inbound" : "Outbound";
//    	setTitle("Stop times on "+route_selected+" "+dir);
//    	
//    	Cursor mStopsCursor = bdba.fetchStopsForTrip(trip_row_id);
//        startManagingCursor(mStopsCursor);
//
//        // Create an array to specify the fields we want to display in the list (only TITLE)
//        String[] from = new String[]{BusDBAdapter.KEY_STOP_STOP_NAME, BusDBAdapter.KEY_STOP_TIME_DEPARTURE_TIME};
//
//        // and an array of the fields we want to bind those fields to (in this case just text1)
//        int[] to = new int[]{R.id.big, R.id.small};
//
//        // Now create a simple cursor adapter and set it to display
//        SimpleCursorAdapter times = 
//            new SimpleCursorAdapter(this, R.layout.stops_row, mStopsCursor, from, to);
//        setListAdapter(times);
//    }
//    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//		menu.add(0,R.id.route_selector,0,R.string.select_route);
//		return true;
//    }
//    
//    public boolean onOptionsItemSelected (MenuItem item){
//
//    	switch (item.getItemId()){
//
//    	case R.id.route_selector:
//    		Intent i = new Intent();
//    		Bundle b = new Bundle();
//    		b.putBoolean("kill_self", true);
//    		i.putExtras(b);
//    		setResult(RESULT_OK, i);
//    		finish();
//    		return true;
//    	}
//
//    	return false;
//
//    }
//    
//    @Override
//    protected void onStop(){
//    	super.onStop();
//    	SharedPreferences sp = getApplicationContext().getSharedPreferences(RouteSelector.SHARED_PREFS_NAME, 0);
//    	Editor e = sp.edit();
//    	e.putLong("trip_row_id", trip_row_id);
//    	e.commit();
//    }
//    
//    @Override
//    protected void onStart(){
//    	super.onStart();
//    	SharedPreferences sp = getApplicationContext().getSharedPreferences(RouteSelector.SHARED_PREFS_NAME, 0);
//    	//Set these anytime this starts - defaults are OK
//    	trip_row_id = sp.getLong("trip_row_id", -1);
//    }
	
}
