package org.rhok.foodmover.api;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.Query;

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

	// having to pass both <T> and Class<T> is rough. Thanks Java and type erasure!
	public static <T> Query<T> findWithinDistance(float lat, float lng, float distanceKM, String latVarName, String lngVarName, Class<T> clazz) {
		Objectify objectify = ObjectifyUtil.get();

		// This query will probably fail for longitude wrap around
		Query<T> notifications = objectify.query(clazz)
				.filter(latVarName + " >", lat - kmToLatitude(distanceKM))
				.filter(latVarName + " <", lat + kmToLatitude(distanceKM))
				.filter(lngVarName + " >", lng - kmToLongitude(distanceKM))
				.filter(lngVarName + " <", lng + kmToLongitude(distanceKM));

		return notifications;
	}

}
