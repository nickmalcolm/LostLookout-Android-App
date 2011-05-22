package nz.ac.vuw.ecs.nwen304;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONParser {
	
	public static ArrayList<Listing> getListings(String url){
		//Make a container for our Listings
		Type collectionType = new TypeToken<ArrayList<Listing>>(){}.getType();

		ArrayList<Listing> listings = null;

		Gson gson = new Gson();

		String response = curl(url);

		listings = gson.fromJson(response, collectionType);

		return listings;
    }
	
	private static String curl(String url){
    	String r = "";
        try {
        	URL url_u = new URL(url);
        	InputStream response = url_u.openStream();
        	BufferedReader reader = new BufferedReader(new InputStreamReader(response));
        	for (String line; (line = reader.readLine()) != null;) {
        		r += line;
        	}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return r;
	}

}
