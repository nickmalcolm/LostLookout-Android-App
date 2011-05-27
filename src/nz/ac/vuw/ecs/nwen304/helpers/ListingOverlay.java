package nz.ac.vuw.ecs.nwen304.helpers;

import java.util.ArrayList;

import nz.ac.vuw.ecs.nwen304.ListingWebView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

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
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.setCancelable(false);
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
	
	private void setURL(String url){
		SharedPreferences sp = mContext.getSharedPreferences("LostLookout", 0);
		Editor e = sp.edit();
		e.putString("url", url);
		e.commit();
	}

}
