package org.rhok.foodmover.entities;

import org.rhok.foodmover.ArgNames;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;

public class FoodMoverUser extends BaseEntity {
	
	private static final String FOOD_MOVER_USER_KIND = "FoodMoverUser";
	
	private FoodMoverUser(Entity entity) {
		this.entity = entity;
	}
	
	public FoodMoverUser(Key key)
	{
		try {
			this.entity = DatastoreServiceFactory.getDatastoreService().get(key);
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public FoodMoverUser() {
		entity = new Entity(FOOD_MOVER_USER_KIND);
	}

	public void setUser(User user) {
		entity.setProperty(ArgNames.USER_KEY, user);
	}
	
	public User getRawUserObject()
	{
		return (User) entity.getProperty(ArgNames.USER_KEY);
	}
	
	public void setIsProducer(boolean isProducer) {
		entity.setProperty(ArgNames.IS_PRODUCER_ARG_NAME, isProducer);
	}
	
	/**
	 * Get the currently logged in user.
	 * 
	 * @return	null if not user is logged in
	 * 			the FoodMoverUser wrapping the logged in user, if it is already stored in the database
	 * 			a new FoodMoverUser object, if no record of the user exists in the database. It will not be saved already!
	 */
	public static FoodMoverUser getCurrentUser() {
		User currRawUser = UserServiceFactory.getUserService().getCurrentUser();
		if (currRawUser == null) {
			return null;
		}
		
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query(FOOD_MOVER_USER_KIND);
		q.addFilter(ArgNames.USER_KEY, Query.FilterOperator.EQUAL, currRawUser);
		PreparedQuery prepQ = data.prepare(q);
		
		for (Entity found : prepQ.asIterable()) {
			FoodMoverUser result = new FoodMoverUser(found);
			result.setUser(currRawUser);
			return result;
		}
		
		final FoodMoverUser result = new FoodMoverUser();
		result.setUser(currRawUser);
		return result;
	}
	
	public static FoodMoverUser registerCurrentUser() {
		FoodMoverUser result = new FoodMoverUser();
		result.setUser(UserServiceFactory.getUserService().getCurrentUser());
		return result;
	}

	public boolean isProducer() {
		return (Boolean) entity.getProperty(ArgNames.IS_PRODUCER_ARG_NAME);
	}

	@Override
	protected String getEntityName() {
		return FOOD_MOVER_USER_KIND;
	}
}
