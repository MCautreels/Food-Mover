package org.rhok.foodmover.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activity.InvalidActivityException;

import org.rhok.foodmover.ArgNames;
import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;
import org.rhok.foodmover.entities.FoodMoverUser;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ApiMethods {

	public static Key<FoodListing> makeNewFoodListing(float lat, float longitude, String description, int quantity,
			Date expirationDate) {
		FoodListing listing = new FoodListing();
		listing.setLat(lat);
		listing.setCreationDate(new Date());
		listing.setLng(longitude);
		listing.setDescription(description);
		listing.setQuantity(quantity);
		listing.setOwner(FoodMoverUser.getCurrentUser());

		if (expirationDate != null) {
			listing.setExpirationDate(expirationDate);
		}
		
		Objectify objectify = ObjectifyService.begin();
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

	public static List<FoodListing> findFoodListings(Float longitude, Float latitude, Float distance) {
		List<FoodListing> result = new ArrayList<FoodListing>();
	
		Query q = new Query("FoodListing");
	
		// Latitude check with query
		q.addFilter(FoodListing.LAT_KEY, Query.FilterOperator.GREATER_THAN_OR_EQUAL, latitude - distance);
		q.addFilter(FoodListing.LAT_KEY, Query.FilterOperator.LESS_THAN_OR_EQUAL, latitude + distance);
	
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery prepQ = data.prepare(q);
	
		for (Entity found : prepQ.asIterable()) {
			// Longitude check in memory
			FoodListing resultItem = new FoodListing(found);
	
			if (!resultItem.expired() && resultItem.getLongitude() >= (longitude - distance)
					&& resultItem.getLongitude() <= (longitude + distance)) {
				result.add(resultItem);
			}
		}
	
		return result;
	}

	public static List<FoodListing> getCurrentUserFoodListings() {
		List<FoodListing> result = new ArrayList<FoodListing>();
		FoodMoverUser me = FoodMoverUser.getCurrentUser();
		Query q = new Query("FoodListing");
		q.addFilter("owner", Query.FilterOperator.EQUAL, me.getRawUserObject());
	
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery prepQ = data.prepare(q);
	
		for (Entity found : prepQ.asIterable()) {
			FoodListing resultItem = new FoodListing(found);
	
			if (resultItem.expired()) {
				continue;
			}
	
			result.add(resultItem);
		}
	
		return result;
	}

	public static void setCurrentUserType(boolean isProducer) throws InvalidActivityException {
		FoodMoverUser user = FoodMoverUser.getCurrentUser(); 
		if (user == null) {
			user = FoodMoverUser.registerCurrentUser();
		}
		
		user.setIsProducer(isProducer);
		user.put();
	}

	public static Key makeNotification(float lat, float longitude, float radius, String type) {
		FoodListingNotification notification = new FoodListingNotification();
		
		notification.setLat(lat);
		notification.setLongitude(longitude);
		notification.setNotificationType(type);
		notification.setRadiusKm(radius);
		notification.setOwner(FoodMoverUser.getCurrentUser());
	
		notification.put();
		
		return notification.getKey();
	}

}
