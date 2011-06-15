package org.rhok.foodmover;

import org.restlet.resource.ServerResource;

import com.google.appengine.api.users.UserServiceFactory;

public class BaseResource extends ServerResource {

	protected String getArg(String argName) {
		String arg = getQuery().getFirstValue(argName);
		if (arg == null) {
			throw new IllegalArgumentException("Missing argument " + argName);
		}
		return arg;
	}

	protected void requireUserLoggedIn() {
		if (UserServiceFactory.getUserService().getCurrentUser() == null) {
			throw new IllegalStateException("This REST call requires a logged in user, but none was found. "
					+ "Have you obtained an authenticated session with the Google server?");
		}
	}

}
