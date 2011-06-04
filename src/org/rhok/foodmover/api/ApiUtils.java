package org.rhok.foodmover.api;

import javax.activity.InvalidActivityException;

import org.rhok.foodmover.entities.FoodMoverUser;

public class ApiUtils {

	public static void setCurrentUserType(boolean isProducer) throws InvalidActivityException {
		FoodMoverUser user = FoodMoverUser.getCurrentUser(); 
		if (user == null) {
			user = FoodMoverUser.registerCurrentUser();
		}
		
		user.setIsProducer(isProducer);
		user.put();
	}
	
}
