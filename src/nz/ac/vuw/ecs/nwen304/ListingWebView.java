package nz.ac.vuw.ecs.nwen304;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * A simple portal to the website, for displaying a Listing
 * 
 * @author Nicholas Malcolm - malcolnich - 300170288
 *
 */
public class ListingWebView extends Activity {
	
	WebView mWebView;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);


		SharedPreferences sp = getApplicationContext().getSharedPreferences("LostLookout", 0);
		String relative_url = sp.getString("url", "");
		//Remove double slash
		String url = LostLookout.BASE_URL.substring(0, LostLookout.BASE_URL.length()-1)+relative_url;

		// initialize the browser object
		WebView browser = (WebView) findViewById(R.id.webview);
		browser.getSettings().setJavaScriptEnabled(true);
		browser.getSettings().setLoadWithOverviewMode(true);
		browser.getSettings().setUseWideViewPort(true);

		try {
			// load the url
			browser.loadUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
