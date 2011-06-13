package org.rhok.foodmover;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class Start extends Activity {
	
	public static final String TAG = "Start";
	public static final String CONSUMER_KEY = "conkey";
	public static final String PRODUCER_KEY = "prodkey";	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Utils.setCurLocation(this);
    }
    
    @Override
	protected void onResume() {
		// This method will switch to the correct activity
		super.onResume();
        /*if((PreferenceManager.getDefaultSharedPreferences(this)).contains(CONSUMER_KEY)) {
    		startActivity(new Intent(this, ConsumerActivity.class));
    		finish();
    		return;
        }
        if((PreferenceManager.getDefaultSharedPreferences(this)).contains(PRODUCER_KEY)) {
    		startActivity(new Intent(this, ProducerActivity.class));
    		finish();
    		return;
        }*/
        
	}
    /*
     * After a commit, use the command line to push the changes to the remote server
     * first try git push origin master
     * if that fails, use the force flag
     * git push origin master --force
     */

	public void pickSet(View target) {
		//this method will set if the app will run consumer or producer forevermore...
    	//this method is defined as a button listener using the onClick tag in the xml layout
		final SharedPreferences.Editor ed = (PreferenceManager.getDefaultSharedPreferences(this)).edit();

    	switch(target.getId()) {
    	case R.id.mn_consumer:
    		ed.putBoolean(CONSUMER_KEY, true);
    		ed.commit();
    		startActivity(new Intent(this, ConsumerActivity.class));
    		break;
    	case R.id.mn_producer:
    		ed.putBoolean(PRODUCER_KEY, true);
    		ed.commit();
    		startActivity(new Intent(this, ProducerActivity.class));
    		break;
    	}
    	finish();
	}
}