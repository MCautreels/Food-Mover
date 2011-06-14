package org.rhok.foodmover;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;
import org.rhok.foodmover.api.ApiMethods;
import org.rhok.foodmover.api.ArgNames;
import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodMoverUser;

import com.google.appengine.api.datastore.KeyFactory;
import com.googlecode.objectify.Key;

/**
 * A resource exposing the FoodListing object to REST queries
 */
public class FoodListingResource extends ServerResource {

	private String getArg(String argName) {
		return getQuery().getFirstValue(argName);
	}

	@Get
	public List<FoodListing> retrieve() {
		if (getQuery().isEmpty()) { // if no args are given
			return FoodListing.getListingsFor(FoodMoverUser.getCurrentUser());
		}

		try {
			float lat = Float.parseFloat(getArg(ArgNames.LAT_ARG_NAME));
			float lng = Float.parseFloat(getArg(ArgNames.LONGITUDE_ARG_NAME));
			float distance = Float.parseFloat(getArg(ArgNames.DISTANCE_ARG_NAME));

			return FoodListing.findFoodListings(lat, lng, distance);

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(
					"Did you pass an argument that couldn't be parsed as a float?<br />" + e, e);
		}
	}

	@Post
	public String create() {
		try {
			float lat = Float.parseFloat(getArg(ArgNames.LAT_ARG_NAME));
			float lng = Float.parseFloat(getArg(ArgNames.LONGITUDE_ARG_NAME));
			String description = getArg(ArgNames.DESCRIPTION_ARG_NAME);
			int quantity = Integer.parseInt(getArg(ArgNames.QUANTITY_ARG_NAME));
			Date expiration = DateFormat.getDateInstance(DateFormat.LONG).parse(
					getArg(ArgNames.EXPIRATION_DATE_ARG_NAME));

			Key<FoodListing> key = ApiMethods.makeNewFoodListing(lat, lng, description, quantity, expiration);
			return KeyFactory.keyToString(key.getRaw());

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Did you pass an argument that couldn't be parsed as a number?<br />"
					+ e, e);
		} catch (ParseException e) {
			throw new IllegalArgumentException("Date was not properly formatted. It should be a unix timestamp.<br />"
					+ e, e);
		}
	}
}
