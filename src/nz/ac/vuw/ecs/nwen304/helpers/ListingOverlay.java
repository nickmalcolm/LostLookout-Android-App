package nz.ac.vuw.ecs.nwen304.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import nz.ac.vuw.ecs.nwen304.ListingWebView;
import nz.ac.vuw.ecs.nwen304.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * Listing Overlays are customized to show Dialog boxes with
 * relevant information regarding a Listing.
 * 
 * @author Nicholas Malcolm - malcolnich - 300170288
 *
 */
public class ListingOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<ListingOverlayItem> mOverlays = new ArrayList<ListingOverlayItem>();
	private Context mContext;
	
	public ListingOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
	}

	@Override
	protected OverlayItem createItem(int i) {
		  return mOverlays.get(i);
	}

	@Override
	public int size() {
	  return mOverlays.size();
	}
	
	public void addOverlay(ListingOverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	protected boolean onTap(int index) {
	  final ListingOverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  //Fill the box with title and description
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.setCancelable(false);

	  Drawable image = imageOperations(mContext, "http://i.imgur.com/6LRWis.jpg");
	  if(image != null){
		  dialog.setIcon(image);
	  }
	  
	  //Add button to view on website, and to cancel dialog box.
	  
	  dialog.setPositiveButton("Full View", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
			  Log.i("onTap", item.getListing().url);
			  setURL(item.getListing().url);
			  Intent i = new Intent(mContext, ListingWebView.class);
			  mContext.startActivity(i);
			  dialog.cancel();
		  }
	  });
	  dialog.setNegativeButton("Done", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int id) {
			  dialog.cancel();
		  }
	  });
	  dialog.show();
	  return true;
	  
	}
	
	private Drawable imageOperations(Context mContext2, String url) {
		// TODO Auto-generated method stub
		try {
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	/**
	 * Saves an URL in the shared preferences. Used for ListingWebView
	 */
	private void setURL(String url){
		SharedPreferences sp = mContext.getSharedPreferences("LostLookout", 0);
		Editor e = sp.edit();
		e.putString("url", url);
		e.commit();
	}

}
