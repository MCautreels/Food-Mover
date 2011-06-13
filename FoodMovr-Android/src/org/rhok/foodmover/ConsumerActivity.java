package org.rhok.foodmover;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ConsumerActivity extends Activity {
	public static final String TAG = "ConsumerActivity";
	public static final String ACCOUNT_KEY = "acctkey";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumer);
		Toast.makeText(getApplicationContext(),
				"consumer activity ",
				Toast.LENGTH_SHORT).show();				
		
        Button mtrackButton;
		
		mtrackButton = (Button) findViewById(R.id.c_browse);
		mtrackButton.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {
				
				startActivity(new Intent(getApplicationContext(), ListShow.class));
	          
				// initialValues.put("url", "http://foodmovr.appspot.com/api/v1/listings?lat=37.3861111&lng=-122.0827778");				
				
			}
		}); // End OnClickListener mtrackButton
			
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
		((LinearLayout)findViewById(R.id.c_loginSetup)).setVisibility(View.GONE);
		((LinearLayout)findViewById(R.id.c_mainButtons)).setVisibility(View.VISIBLE);
    }
    
    public void fakeAccount(View target) {
    	//this method is defined as a button listener using the onClick tag in the xml layout
		final SharedPreferences.Editor ed = (PreferenceManager.getDefaultSharedPreferences(this)).edit();
		ed.putString(ACCOUNT_KEY, ((EditText)findViewById(R.id.c_username)).getText().toString());
		ed.commit();
		transition2Main();
    }
}
