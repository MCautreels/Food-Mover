package org.rhok.foodmover.api;

import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.GeoItem;

public class Util {

	public static float kmToLongitude(float km) {
		// approximation from
		// http://geography.about.com/library/faq/blqzdistancedegree.htm
		return km / 90;
	}

	public static float kmToLatitude(float km) {
		// approximation from
		// http://geography.about.com/library/faq/blqzdistancedegree.htm
		return km / 111;
	}
	
	public static float distanceBetween(float lat, float lng, GeoItem point) {
		// Spherical Law of Cosines
		// http://www.movable-type.co.uk/scripts/latlong.html
		final int EARTH_RADIUS_KM = 6371;

		double lat1 = Math.toRadians(point.getLat());
		double lng1 = Math.toRadians(point.getLng());

		double lat2 = Math.toRadians(lat);
		double lng2 = Math.toRadians(lng);

		// distance is in KM
		double distance = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2)
				* Math.cos(lng2 - lng1))
				* EARTH_RADIUS_KM;
		return (float) distance;
	}

}
