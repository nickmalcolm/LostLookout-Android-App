package nz.ac.vuw.ecs.nwen304;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
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
public class ListingsView extends ListActivity {
	
	public final static String SHARED_PREFS_NAME = "LostLookoutPrefs";
	
	private DBAdapter bdba;
	
	private String route_selected = "";
	private int direction_selected = 0;
	private int spinner_selected = 0;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bdba = new DBAdapter(this);
        bdba.open();
    }

	protected void onResume() { 
    	super.onResume();
    	
    	//populateDB();
    	
        setContentView(R.layout.list); 
        
        //Populate the database
    	//getData();	
        Cursor listingsCursor = bdba.fetchAllListings();
        startManagingCursor(listingsCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] from = new String[]{Listing.TITLE, Listing.DESCR};
		// and an array of the fields we want to bind those fields to (in this case just text1)
		int[] to = new int[]{R.id.big, R.id.small};
		
		// Now create a simple cursor adapter and set it to display
		SimpleCursorAdapter listings = 
			new SimpleCursorAdapter(this, R.layout.row, listingsCursor, from, to);
		setListAdapter(listings);
    }
    
    @Override
    protected void onStop(){
    	super.onStop();
    }
    
    @Override
    protected void onStart(){
    	super.onStart();
    }
    
    /**
     * Populates the database with various timetables if they need to be updated
     */
    private void populateDB() {        
        
    	
	}
    
    private void toastIt(String msg){
    	Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
