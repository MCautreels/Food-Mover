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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class ApiMethods {

	public static Key makeNewFoodListing(float lat, float longitude, String description, int quantity,
			Date expirationDate) {
		FoodListing listing = new FoodListing();
		listing.setLat(lat);
		listing.setDateOfCreation(new Date());
		listing.setLongitude(longitude);
		listing.setDescription(description);
		listing.setQuantity(quantity);
		listing.setOwner(FoodMoverUser.getCurrentUser());

		if (expirationDate != null) {
			listing.setDateOfExpiration(expirationDate);
		}
		
		listing.put();

		notifyOfNewListing(listing);
		return listing.getKey();
	}

	private static void notifyOfNewListing(FoodListing listing) {
		Query q = new Query("Notification");

		// Latitude check with query
		q.addFilter(ArgNames.LAT_ARG_NAME, Query.FilterOperator.GREATER_THAN_OR_EQUAL, listing.getLat() - 10);
		q.addFilter(ArgNames.LAT_ARG_NAME, Query.FilterOperator.LESS_THAN_OR_EQUAL, listing.getLat() + 10);

		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery prepQ = data.prepare(q);

		for (Entity found : prepQ.asIterable()) {
			
			// notifyUser() will only do work if the listing is close enough,
			// so we don't need to check here
			new FoodListingNotification(found).notifyUser(listing);
		}
	}

	public static void updateListing(FoodListing listing, final String description, final float lat, final float lng,
			final int quantity) {
		listing.setDescription(description);
		listing.setLat(lat);
		listing.setLongitude(lng);
		listing.setQuantity(quantity);
	
		listing.put();
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
