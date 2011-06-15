package org.rhok.foodmover;

import org.restlet.resource.Post;
import org.rhok.foodmover.api.ApiMethods;
import org.rhok.foodmover.api.ArgNames;

public class FoodListingNotificationResource extends BaseResource {

	@Post
	public void create() {
		requireUserLoggedIn();
		
		try {
			float lat = Float.parseFloat(getArg(ArgNames.LAT_ARG_NAME));
			float lng = Float.parseFloat(getArg(ArgNames.LONGITUDE_ARG_NAME));
			float radiusKM = Float.parseFloat(getArg(ArgNames.RADIUS_ARG_NAME));
			
			ApiMethods.makeNotification(lat, lng, radiusKM);

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Did you pass an argument that couldn't be parsed as a float?<br />" + e, e);
		}
	}

}
