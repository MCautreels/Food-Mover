package org.rhok.foodmover.api;

import java.util.Collection;
import java.util.Date;

import javax.activity.InvalidActivityException;

import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;
import org.rhok.foodmover.entities.FoodMoverUser;

import com.google.appengine.repackaged.com.google.common.base.Predicate;
import com.google.appengine.repackaged.com.google.common.collect.Collections2;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

// TODO refactor this into different test classes, bound together in a TestSuite
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
				return ! listing.expired();
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

}
