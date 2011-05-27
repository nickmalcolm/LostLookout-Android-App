package nz.ac.vuw.ecs.nwen304;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;


public class SettingsView extends Activity {

	private String spinner_selected = "";
	private int spinner_id = 0;
	private int distance;
	private boolean show_found = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	public void onResume(){
		super.onResume();
		
		setContentView(R.layout.settings);
		readPreferences();
		
		//Show Lost
		final CheckBox checkbox = (CheckBox) findViewById(R.id.show_find);
		checkbox.setChecked(show_found);
        checkbox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	show_found = ((CheckBox) v).isChecked();
				writePreferences();
            }
        });
		
		
		
		//Choose radius
		
		final Spinner spinner = (Spinner) findViewById(R.id.distance_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.distance_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent,
					View view, int pos, long id) {
				Object item = parent.getItemAtPosition(pos);
				spinner_selected = item.toString();
				distanceToInt(spinner_selected);
				writePreferences();
				Log.d("SPINNER", spinner_selected+"");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
		
		spinner.setSelection(spinner_id);
		
		//Done button
		final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
				writePreferences();
            	setResult(RESULT_OK);
        		finish();
            }
        });
		
	}

	private void distanceToInt(String str) {
		// "XXX km" to "The World"
		if(str.equals("10 km")){
			distance = 10;
		}else if(str.equals("50 km")){
			distance = 50;
		}else if(str.equals("1000 km")){
			distance = 1000;
		}else if(str.equals("The World")){
			distance = 20000;
		}
	}
	
	private void writePreferences(){
		SharedPreferences sp = this.getSharedPreferences(LostLookout.SHARED_PREFS, 0);
		Editor e = sp.edit();
		e.putInt("distance", distance);
		e.putBoolean("show_found", show_found);
		e.putInt("spinner_id", spinner_id);
		e.commit();
	}
	
	private void readPreferences(){
		SharedPreferences sp = this.getSharedPreferences(LostLookout.SHARED_PREFS, 0);
		distance = sp.getInt("distance", distance);
		show_found = sp.getBoolean("show_found", show_found);
		spinner_id = sp.getInt("spinner_id", 0);
	}

}
