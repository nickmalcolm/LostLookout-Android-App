package nz.ac.vuw.ecs.nwen304;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;

public class JSONParser {
	
	private List<Listing> listings = new ArrayList<Listing>();
	
	private String url;
	
	public JSONParser(String url){
		this.url = url;
	}
	
	private InputStream jsonReader(String url){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        URI uri;
        InputStream data = null;
        try {
            uri = new URI(url);
            HttpGet method = new HttpGet(uri);
            HttpResponse response = httpClient.execute(method);
            data = response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }
	
	public void runJSONParser(){
        try{
        Log.i("MY INFO", "Json Parser started..");
        Gson gson = new Gson();
        Reader r = new InputStreamReader(jsonReader(url));
        Log.i("MY INFO", r.toString());
        Listings listings = gson.fromJson(r, Listings.class);
        Log.i("MY INFO", ""+listings.getListings().size());
        for(Listing l : listings.getListings()){
            Log.i("LISTING ", l.title);
        }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
