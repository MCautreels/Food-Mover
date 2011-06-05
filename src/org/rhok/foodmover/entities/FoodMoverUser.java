package org.rhok.foodmover.entities;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class FoodMoverUser extends BaseEntity {
	
	private static final String FOOD_MOVER_USER_KIND = "FoodMoverUser";
	
	private static final String USER_KEY = "user";
	private static final String USER_TYPE_KEY = "user type";
	
	private FoodMoverUser(Entity entity) {
		this.entity = entity;
	}
	
	public FoodMoverUser() {
		entity = new Entity(FOOD_MOVER_USER_KIND);
	}

	public void setUser(User user) {
		entity.setProperty(USER_KEY, user);
	}
	
	public User getRawUserObject()
	{
		return (User) entity.getProperty(USER_KEY);
	}
	
	public void setIsProducer(boolean isProducer) {
		entity.setProperty(USER_TYPE_KEY, isProducer);
	}
	
	public static FoodMoverUser getCurrentUser() {
		User currRawUser = UserServiceFactory.getUserService().getCurrentUser();
		if (currRawUser == null) {
			return null;
		}
		
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(FOOD_MOVER_USER_KIND);
		q.addFilter(USER_KEY, Query.FilterOperator.EQUAL, currRawUser);
		PreparedQuery prepQ = data.prepare(q);
		
		for (Entity found : prepQ.asIterable()) {
			FoodMoverUser result = new FoodMoverUser(found);
			result.setUser(currRawUser);
			return result;
		}
		
		return null;
	}
	
	public static FoodMoverUser registerCurrentUser() {
		FoodMoverUser result = new FoodMoverUser();
		result.setUser(UserServiceFactory.getUserService().getCurrentUser());
		return result;
	}

	public boolean isProducer() {
		return (Boolean) entity.getProperty(USER_TYPE_KEY);
	}
}
