package org.rhok.foodmover;

import org.restlet.resource.Post;
import org.rhok.foodmover.api.ApiMethods;
import org.rhok.foodmover.api.ArgNames;

public class FoodMovrUserResource extends BaseResource {
	
	@Post
	public void setUserType() {
		requireUserLoggedIn();
		
		if (getArg(ArgNames.USER_KIND).equals("producer")) {
			ApiMethods.setCurrentUserIsProducer(true);
			return;
		}

		if (getArg(ArgNames.USER_KIND).equals("consumer")) {
			ApiMethods.setCurrentUserIsProducer(false);
			return;
		}
		
		throw new IllegalArgumentException("Unrecognized arguments for setting the user kind.");
	}

}
