package org.rhok.foodmover.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rhok.foodmover.ArgNames;
import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodMoverUser;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.org.json.JSONStringer;

/**
 * Handles calls to /api/v1/listings. Supports creating, updating, deleting, and
 * querying listings.
 * 
 * This class is mostly concerned with validating requests and getting data from requests.
 * The real logic for working with data exists in ApiMethods.
 * 
 * API documentation: https://docs.google.com/document/pub?id=
 * 1EENWGEVDM3xLEEF1zLXG_SZZphxhE8MnYspsrLNdkvE
 */
@SuppressWarnings("serial")
public class ListingServlet extends BaseHttpServlet {

	private static final String ACTION_ARG_NAME = "action";
	private static final String DELETE_ACTION = "delete";
	private static final String PUT_ACTION = "put";

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// stupid hack: in the jQuery, somehow only POST requests would work
		// would
		// so we use POST to get at other HTTP verbs by using the "action"
		// parameter.
		if (req.getParameter(ACTION_ARG_NAME) != null) {
			if (req.getParameter(ACTION_ARG_NAME).equals(DELETE_ACTION)) {
				doDelete(req, resp);
				return;
			}
			if (req.getParameter(ACTION_ARG_NAME).equals(PUT_ACTION)) {
				doPut(req, resp);
				return;
			}
		}

		checkParams(req);

		// get args from request
		float lat = Float.parseFloat(req.getParameter(LAT_ARG_NAME));
		float longitude = Float.parseFloat(req.getParameter(LONGITUDE_ARG_NAME));
		String description = req.getParameter(ArgNames.DESCRIPTION_ARG_NAME);
		int quantity = Integer.parseInt(req.getParameter(ArgNames.QUANTITY_ARG_NAME));
		Date expirationDate = null;

		if (req.getParameter(ArgNames.EXPIRATION_DATE_ARG_NAME) != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
			try {
				expirationDate = formatter.parse((req.getParameter(ArgNames.EXPIRATION_DATE_ARG_NAME)));
			} catch (ParseException e) {
				throw new IllegalArgumentException("expiration date must be of format mm/dd/yyyy");
			}
		}

		// use args to build a food listing
		Key key = ApiMethods.makeNewFoodListing(lat, longitude, description, quantity, expirationDate);

		// send the key for the newly created listing back to the caller
		writeKey(resp, key);
	}

	/**
	 * Throws an IllegalArgumentException is `req` does not include parameters
	 * for lattitude, longitude, description, and quantity.
	 * 
	 * @param req
	 *            the current request
	 */
	private static void checkParams(HttpServletRequest req) {
		for (String argKey : new String[] { LAT_ARG_NAME, LONGITUDE_ARG_NAME, ArgNames.DESCRIPTION_ARG_NAME, ArgNames.QUANTITY_ARG_NAME }) {
			checkParam(req, argKey);
		}
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		checkParams(req);
		BaseHttpServlet.checkParam(req, ArgNames.KEY_ARG_NAME);

		final String strKey = req.getParameter(ArgNames.KEY_ARG_NAME);

		Key key = KeyFactory.stringToKey(strKey);
		try {
			FoodListing listing = new FoodListing(DatastoreServiceFactory.getDatastoreService().get(key));

			assert listing.getKey().equals(key) : "Keys don't match";

			checkOwner(listing);

			final String description = req.getParameter(ArgNames.DESCRIPTION_ARG_NAME);
			final float lat = Float.parseFloat(req.getParameter(LAT_ARG_NAME));
			final float lng = Float.parseFloat(req.getParameter(LONGITUDE_ARG_NAME));
			final int quantity = Integer.parseInt(req.getParameter(ArgNames.QUANTITY_ARG_NAME));

			ApiMethods.updateListing(listing, description, lat, lng, quantity);

			writeKey(resp, listing.getKey());

		} catch (EntityNotFoundException e) {
			throw new IllegalArgumentException(
					"key does not match any current food listing. Did you use KeyFactory.keyToString() correctly?");
		}
	}

	/**
	 * Throw an IllegalArgumentException if the current user is not the owner of
	 * the listing. Used to validate that the request is authenticated to
	 * perform updates and deletes.
	 * 
	 * @param listing
	 *            the food listing to check
	 */
	private void checkOwner(FoodListing listing) {
		if (!FoodMoverUser.getCurrentUser().equals(listing.getOwner())) {
			throw new IllegalArgumentException(
					"Only listings of the currently authenticated user can be modified. Did you authenticate with GAE?");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		BaseHttpServlet.checkParam(req, ArgNames.KEY_ARG_NAME);

		Key key = KeyFactory.stringToKey(req.getParameter(ArgNames.KEY_ARG_NAME));
		FoodListing foodListing = new FoodListing(key);
		checkOwner(foodListing);

		foodListing.delete();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");

		List<FoodListing> foodlistings = getFoodListingsForRequest(req);

		JSONStringer jsonStringer = new JSONStringer();
		try {
			jsonStringer.array();
			for (FoodListing foodListing : foodlistings) {
				jsonStringer.object().key("id").value(KeyFactory.keyToString(foodListing.getKey()))
						.key("dateofcreation").value(foodListing.getDateOfCreation().getTime()).key("lat")
						.value(foodListing.getLat()).key("lng").value(foodListing.getLongitude()).key("description")
						.value(foodListing.getDescription()).key("quantity").value(foodListing.getQuantity())
						.key("email").value(foodListing.getOwner().getRawUserObject().getEmail());
				if (foodListing.getDateOfExpiration() != null) {
					jsonStringer.key("dateofexpiration").value(foodListing.getDateOfExpiration().getTime());
				}
				jsonStringer.endObject();
			}
			jsonStringer.endArray();

			resp.setContentType("application/json");
			resp.getWriter().println(jsonStringer.toString());
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private List<FoodListing> getFoodListingsForRequest(HttpServletRequest req) {
		if (req.getParameter(LAT_ARG_NAME) != null && req.getParameter(LONGITUDE_ARG_NAME) != null) {
			float latitude = Float.parseFloat(req.getParameter(LAT_ARG_NAME));
			float longitude = Float.parseFloat(req.getParameter(LONGITUDE_ARG_NAME));
			float distance = Float.parseFloat(this.getInitParameter("default_distance"));
			if (req.getParameter(ArgNames.DISTANCE_ARG_NAME) != null) {
				distance = Float.parseFloat(req.getParameter(ArgNames.DISTANCE_ARG_NAME));
			}
			return ApiMethods.findFoodListings(longitude, latitude, distance);
		}

		if (FoodMoverUser.getCurrentUser() != null) {
			return ApiMethods.getCurrentUserFoodListings();
		}

		throw new IllegalArgumentException(
				"You must either be logged in, or specify a lat/longitude. (Distance is optional)");

	}
}
