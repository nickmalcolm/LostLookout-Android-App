package nz.ac.vuw.ecs.nwen304;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ListingWebView extends Activity {
	
	WebView mWebView;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.getSettings().setJavaScriptEnabled(true);
	    mWebView.loadUrl("http://www.google.com");
	}

}
