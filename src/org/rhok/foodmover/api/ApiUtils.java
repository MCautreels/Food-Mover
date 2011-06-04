package org.rhok.foodmover.api;

import javax.activity.InvalidActivityException;

import org.rhok.foodmover.entities.FoodMoverUser;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class ApiUtils {

	public static void setCurrentUserType(FoodMoverUser.UserType type) throws InvalidActivityException {
		User currRawUser = UserServiceFactory.getUserService().getCurrentUser();
		if (currRawUser == null) {
			throw new InvalidActivityException("User is not logged in.");
		}
		
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("FoodMoverUser");
		q.addFilter("User", Query.FilterOperator.EQUAL, currRawUser);
		PreparedQuery prepQ = data.prepare(q);
	}
	
}
