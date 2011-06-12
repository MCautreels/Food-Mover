package org.rhok.foodmover.entities;

import javax.persistence.Id;

import org.rhok.foodmover.api.ObjectifyUtil;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

public class FoodMoverUser {

	@Id
	private Long id = null;

	// this has to be the exact name of the user field, for querying
	private static final String USER_VAR_NAME = "user";

	private boolean isProducer;
	private transient Key<User> userKey;

	/**
	 * Get the currently logged in user.
	 * 
	 * @return null if not user is logged in <br />
	 *         the FoodMoverUser wrapping the logged in user. If no
	 *         FoodMoverUser already wrapping the current user already exists,
	 *         it will be created and saved.
	 */
	public static FoodMoverUser getCurrentUser() {
		User currRawUser = UserServiceFactory.getUserService().getCurrentUser();
		if (currRawUser == null) {
			return null;
		}

		Objectify objectify = ObjectifyUtil.get();
		FoodMoverUser foodMoverUser = objectify.query(FoodMoverUser.class).filter(USER_VAR_NAME, currRawUser).get();

		if (foodMoverUser != null) {
			return foodMoverUser;
		}

		foodMoverUser = new FoodMoverUser();
		foodMoverUser.setUser(currRawUser);
		objectify.put(foodMoverUser);
		return foodMoverUser;
	}

	private void setUser(User user) {
		userKey = new Key<User>(User.class, user.getEmail());
	}

	public static FoodMoverUser createCurrentUser() {
		FoodMoverUser result = new FoodMoverUser();
		result.setUser(UserServiceFactory.getUserService().getCurrentUser());
		return result;
	}

	public void setIsProducer(boolean isProducer) {
		this.isProducer = isProducer;
	}

	public boolean isProducer() {
		return isProducer;
	}

	public Key<FoodMoverUser> getKey() {
		return new Key<FoodMoverUser>(FoodMoverUser.class, id);
	}

	public User getRawUserObject() {
		return ObjectifyUtil.get().get(userKey);
	}
}
