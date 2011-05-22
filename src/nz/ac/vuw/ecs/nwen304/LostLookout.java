package nz.ac.vuw.ecs.nwen304;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class LostLookout extends Activity {
    /** Called when the activity is first created. */
	
	private DBAdapter bdba;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        bdba = new DBAdapter(this);
        bdba.open();
        
        ArrayList<Listing> listings = JSONParser.getListings("http://10.0.2.2:3000/listings.json");
        bdba.addListings(listings);
        
    	System.out.println("Hello");
    }
}