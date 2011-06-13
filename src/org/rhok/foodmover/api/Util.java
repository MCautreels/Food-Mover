package org.rhok.foodmover.api;

import java.util.Collection;

import org.rhok.foodmover.entities.GeoItem;

import com.google.appengine.repackaged.com.google.common.base.Predicate;
import com.google.appengine.repackaged.com.google.common.collect.Collections2;
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

	// having to pass both <T> and Class<T> is rough. Thanks Java and type
	// erasure!
	public static <T extends GeoItem> Collection<T> findWithinDistance(float lat, final float lng,
			final float distanceKM, String latVarName, Class<T> clazz) {
		Objectify objectify = ObjectifyUtil.get();

		// This query will probably fail for lat wrap around
		Collection<T> items = objectify.query(clazz).filter(latVarName + " >", lat - kmToLatitude(distanceKM))
				.filter(latVarName + " <", lat + kmToLatitude(distanceKM)).list();

		// GAE only supports queries on a single field, so we must filter by
		// longitude in memory
		// If this becomes a bottleneck, consider using FusionTables, which
		// support spatial queries

		return Collections2.filter(items, new Predicate<T>() {

			public boolean apply(T item) {
				return item.getLng() > lng - kmToLongitude(distanceKM)
						&& item.getLng() < lng + kmToLongitude(distanceKM);
			}
		});
	}

}
