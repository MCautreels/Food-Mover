package org.rhok.foodmover;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class Utils {
	public static double mLongitude;
	public static double mLatitude;
	public static LocationListener mLocListener;
	
	
	public static void setCurLocation(final Context ctx) {
		// Acquire a reference to the system Location Manager
		final LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		mLocListener = new LocationListener() {
		    @Override
			public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		    	//this code will listen for one update and then stop burning CPU when the location is returned
		    	mLongitude = location.getLongitude();
		    	mLatitude = location.getLatitude();
		    	locationManager.removeUpdates(mLocListener);
		    }

		    @Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}

		    @Override
			public void onProviderEnabled(String provider) {}

		    @Override
			public void onProviderDisabled(String provider) {}
		  };

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocListener);

	}

}
