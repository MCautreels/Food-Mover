package org.rhok.foodmover.api;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.activity.InvalidActivityException;

import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;
import org.rhok.foodmover.entities.FoodMoverUser;
import org.rhok.foodmover.entities.GeoItem;

import com.google.appengine.repackaged.com.google.common.base.Predicate;
import com.google.appengine.repackaged.com.google.common.collect.Collections2;
import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Ordering;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ApiMethods {

	public static Key<FoodListing> makeNewFoodListing(float lat, float longitude, String description, int quantity,
			Date expirationDate) {
		FoodListing listing = new FoodListing();
		listing.setLat(lat);
		listing.setLng(longitude);
		listing.setDescription(description);
		listing.setQuantity(quantity);
		listing.setOwner(FoodMoverUser.getCurrentUser());

		if (expirationDate != null) {
			listing.setExpirationDate(expirationDate);
		}

		Objectify objectify = ObjectifyUtil.get();
		Key<FoodListing> key = objectify.put(listing);

		FoodListingNotification.notifyOfNewListing(listing);

		return key;
	}

	public static void updateListing(FoodListing listing, final String description, final float lat, final float lng,
			final int quantity) {
		listing.setDescription(description);
		listing.setLat(lat);
		listing.setLng(lng);
		listing.setQuantity(quantity);

		Objectify objectify = ObjectifyService.begin();
		objectify.put(listing);
	}

	public static Collection<FoodListing> getCurrentUserFoodListings() {
		Collection<FoodListing> listings = FoodListing.getListingsFor(FoodMoverUser.getCurrentUser());

		return Collections2.filter(listings, new Predicate<FoodListing>() {

			public boolean apply(FoodListing listing) {
				return !listing.expired();
			}
		});
	}

	public static void setCurrentUserType(boolean isProducer) throws InvalidActivityException {
		FoodMoverUser user = FoodMoverUser.getCurrentUser();
		if (user == null) {
			user = FoodMoverUser.createCurrentUser();
		}

		user.setIsProducer(isProducer);
		ObjectifyUtil.get().put(user);
	}

	public static Key<FoodListingNotification> makeNotification(float lat, float longitude, float radiusKM) {
		FoodListingNotification notification = new FoodListingNotification();

		notification.setLat(lat);
		notification.setLng(longitude);
		notification.setRadiusKm(radiusKM);
		notification.setOwner(FoodMoverUser.getCurrentUser().getKey());

		return ObjectifyUtil.get().put(notification);
	}

	// having to pass both <T> and Class<T> is rough. Thanks Java and type
	// erasure!
	public static <T extends GeoItem> List<T> findWithinDistance(final float lat, final float lng, final float distanceKM,
			String latVarName, Class<T> clazz) {
		Objectify objectify = ObjectifyUtil.get();

		// This query will probably fail for lat wrap around
		Collection<T> items = objectify.query(clazz).filter(latVarName + " >", lat - Util.kmToLatitude(distanceKM))
				.filter(latVarName + " <", lat + Util.kmToLatitude(distanceKM)).list();

		// sort by distance from center point
		return Ordering.from(new Comparator<GeoItem>() {

			/**
			 * Get the distance from `item` to the point described by (lat, lng). 
			 */
			private Float distanceToQueryPoint(GeoItem item) {
				return Util.distanceBetween(lat, lng, item);
			}
			
			public int compare(GeoItem item1, GeoItem item2) {
				return distanceToQueryPoint(item1).compareTo(distanceToQueryPoint(item2));
			}
			
		}).sortedCopy((Collections2.filter(items, new Predicate<T>() {

			// GAE only supports queries on a single field, so we must filter by
			// longitude in memory
			// If this becomes a bottleneck, consider using FusionTables, which
			// support spatial queries
			public boolean apply(T item) {
				return item.getLng() > lng - Util.kmToLongitude(distanceKM)
						&& item.getLng() < lng + Util.kmToLongitude(distanceKM);
			}
		})));
	}

}
