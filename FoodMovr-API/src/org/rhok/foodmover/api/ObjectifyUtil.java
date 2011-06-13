package org.rhok.foodmover.api;

import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;
import org.rhok.foodmover.entities.FoodMoverUser;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ObjectifyUtil {

	static {
		// every type in the datastore must be registered here
		ObjectifyService.register(FoodListing.class);
		ObjectifyService.register(FoodListingNotification.class);
		ObjectifyService.register(FoodMoverUser.class);
	}
	
	/**
	 * Get an instance of Objectify to use for datastore operations.
	 * 
	 * <b>Always</b> use this instead of ObjectifyService.begin() - that way, all datastore types are ensured to be registered. 
	 * 
	 * @return
	 */
	public static Objectify get() {
		return ObjectifyService.begin();
	}
	
}
