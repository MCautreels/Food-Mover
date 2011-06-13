package org.rhok.foodmover;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ProducerActivity extends Activity {

	public static final String TAG = "ProducerActivity";
	public static final String ACCOUNT_KEY = "acctkey";

	String[] latarr = new String[100];
	String[] lngarr = new String[100];
	String[] descarr = new String[100];
	String[] quantityarr = new String[100];
	String[] emailarr = new String[100];
	String[] doexparr = new String[100];
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.producer);
									
    }
    
    @Override
	protected void onResume() {
		// This method will display the correct components on the screen
		super.onResume();
        /*if((PreferenceManager.getDefaultSharedPreferences(this)).contains(ACCOUNT_KEY)) {
        	transition2Main();
    		return;
        } */
	}

    void transition2Main() {
		((LinearLayout)findViewById(R.id.pr_loginSetup)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.pr_mainButtons)).setVisibility(View.VISIBLE);
    }

	// Function to Convert Cursor to Array
	private final int[] arrayFromCursor(Cursor c, int colNum) {
		final int sz = c.getCount();

		if (sz == 0) {
			return null;
		}
		final int[] values = new int[sz];

		if (c.moveToFirst()) {
			for (int k = 0; k < sz; k++) {
				values[k] = c.getInt(colNum);
				// Log.v(TAG, "value set is " +values[k]);
				c.moveToNext();

			} // end for loop
			return values;

		}
		return null;

	} // end arrayFromCursor
	
    public void fakePAccount(View target) {
    	//this method is defined as a button listener using the onClick tag in the xml layout
		final SharedPreferences.Editor ed = (PreferenceManager.getDefaultSharedPreferences(this)).edit();
		ed.putString(ACCOUNT_KEY, ((EditText)findViewById(R.id.pr_username)).getText().toString());
		ed.commit();
		transition2Main();
    }

}
