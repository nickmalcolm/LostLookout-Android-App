package nz.ac.vuw.ecs.nwen304.views;

import java.util.ArrayList;

import nz.ac.vuw.ecs.nwen304.DBAdapter;
import nz.ac.vuw.ecs.nwen304.JSONParser;
import nz.ac.vuw.ecs.nwen304.R;
import nz.ac.vuw.ecs.nwen304.models.Listing;
import android.app.Activity;
import android.os.Bundle;

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