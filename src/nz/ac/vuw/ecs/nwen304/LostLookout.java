package nz.ac.vuw.ecs.nwen304;

import android.app.Activity;
import android.os.Bundle;

public class LostLookout extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        JSONParser jp = new JSONParser("http://10.0.2.2:3000/listings.json");
    	jp.runJSONParser();
    	System.out.println("Hello");
    }
}