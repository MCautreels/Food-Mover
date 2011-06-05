package org.rhok.foodmover.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodListingNotification;
import org.rhok.foodmover.entities.FoodMoverUser;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONStringer;
import com.google.appengine.repackaged.org.json.JSONWriter;

@SuppressWarnings("serial")
public class ListingServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		checkParams(req);

		float lat = Float.parseFloat(req.getParameter("lat"));
		float longitude = Float.parseFloat(req.getParameter("lng"));
		String description = req.getParameter("description");
		int quantity = Integer.parseInt(req.getParameter("quantity"));

		FoodListing listing = new FoodListing();
		listing.setLat(lat);
		listing.setDateOfCreation(new Date());
		listing.setLongitude(longitude);
		listing.setDescription(description);
		listing.setQuantity(quantity);
		listing.setOwner(FoodMoverUser.getCurrentUser());
		
		if(req.getParameter("expirationdate") != null){
			SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
			try {
				listing.setDateOfExpiration(formatter.parse((req.getParameter("expirationdate"))));
			} catch (ParseException e) {
				throw new ServletException("expirationdate must be of format mm/dd/yyyy");
			}
		}
		
		listing.put();
		
		notifyOfNewListing(listing);

		writeKey(resp, listing);
	}

	private void notifyOfNewListing(FoodListing listing) {
		/*Query q = new Query(FoodListingNotification.NOTIFICATION_KEY);
		q.addFilter(FoodListingNotification.OWNER_KEY, Query.FilterOperator.EQUAL, FoodMoverUser.getCurrentUser().getRawUserObject());
		
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		
		for (Entity entity : pq.asIterable()) {
			new FoodListingNotification(entity).notifyUser(listing);
		}*/
		
		Query q = new Query("Notification");

		// Latitude check with query
		q.addFilter(FoodListingNotification.LAT_KEY, Query.FilterOperator.GREATER_THAN_OR_EQUAL, listing.getLat() - 10);
		q.addFilter(FoodListingNotification.LAT_KEY, Query.FilterOperator.LESS_THAN_OR_EQUAL, listing.getLat() + 10);

		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery prepQ = data.prepare(q);

		for (Entity found : prepQ.asIterable()) {
			// Longitude check in memory
			FoodListingNotification notification = new FoodListingNotification(found);
			if (notification.getLongitude() >= (listing.getLongitude() - 10)
					&& notification.getLongitude() <= (listing.getLongitude() + 10)) {
				notification.notifyUser(listing);
			}
		}		
	}

	private void writeKey(HttpServletResponse resp, FoodListing listing) throws IOException {
		resp.getWriter().println(KeyFactory.keyToString(listing.getKey()));
	}

	private void checkParams(HttpServletRequest req) {
		if (req.getParameter("lat") == null)
			throw new IllegalStateException("No lat parameter set");
		if (req.getParameter("lng") == null)
			throw new IllegalStateException("No lng parameter set");
		if (req.getParameter("description") == null)
			throw new IllegalStateException("No description parameter set");
		if (req.getParameter("quantity") == null)
			throw new IllegalStateException("No quantity parameter set");
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		checkParams(req);
		final String strKey = req.getParameter("key");
		if (strKey == null)
			throw new IllegalStateException("No key parameter set");

		Key key = KeyFactory.stringToKey(strKey);
		try {
			FoodListing listing = new FoodListing(DatastoreServiceFactory.getDatastoreService().get(key));
			listing.setDescription(req.getParameter("description"));
			listing.setLat(Float.parseFloat(req.getParameter("lat")));
			listing.setLongitude(Float.parseFloat(req.getParameter("long")));
			listing.setQuantity(Integer.parseInt(req.getParameter("quantity")));
			listing.setOwner(FoodMoverUser.getCurrentUser());
			
			listing.put();
			
			writeKey(resp, listing);
			
		} catch (EntityNotFoundException e) {
			throw new IllegalStateException("key does not match any current food listing. Did you use KeyFactory.keyToString() correctly?");
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		if (req.getParameter("key") == null)
			throw new IllegalStateException("No key parameter set");

		Key key = KeyFactory.stringToKey(req.getParameter("key"));
		FoodListing fl = new FoodListing(key);

		if (fl.getOwner().getRawUserObject().equals(FoodMoverUser.getCurrentUser().getRawUserObject())) {
			fl.delete();
		} else {
			throw new IllegalAccessError("You have to be the owner of the listing");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");

		List<FoodListing> foodlistings = null;

		if (req.getParameter("lat") != null && req.getParameter("lng") != null) {
			float latitude = Float.parseFloat(req.getParameter("lat"));
			float longitude = Float.parseFloat(req.getParameter("lng"));
			float distance;
			if (req.getParameter("distance") == null) {
				distance = Float.parseFloat(this.getInitParameter("default_distance"));
			} else {
				distance = Float.parseFloat(req.getParameter("distance"));
			}
			foodlistings = findFoodListings(longitude, latitude, distance);
		} else if (FoodMoverUser.getCurrentUser() != null) {
			foodlistings = getMyFoodListings();
		} else {
			JSONStringer jsonStringer = new JSONStringer();
			try {
				resp.getWriter().println(
						jsonStringer.object().key("ERROR").value("Not logged in / No lng or lat set").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		JSONStringer jsonStringer = new JSONStringer();
		try {
			jsonStringer.array();
			for (FoodListing foodListing : foodlistings) {
				jsonStringer.object()
				.key("id").value(KeyFactory.keyToString(foodListing.getKey()))
				.key("dateofcreation").value(foodListing.getDateOfCreation().getTime())
				.key("lat").value(foodListing.getLat())
				.key("lng").value(foodListing.getLongitude())
				.key("description").value(foodListing.getDescription())
				.key("quantity").value(foodListing.getQuantity())
				.key("email").value(foodListing.getOwner().getRawUserObject().getEmail());
				if(foodListing.getDateOfExpiration() != null) {
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

	public static List<FoodListing> getMyFoodListings() {
		List<FoodListing> result = new ArrayList<FoodListing>();
		FoodMoverUser me = FoodMoverUser.getCurrentUser();
		Query q = new Query("FoodListing");
		q.addFilter("owner", Query.FilterOperator.EQUAL, me.getRawUserObject());

		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery prepQ = data.prepare(q);

		for (Entity found : prepQ.asIterable()) {
			FoodListing resultItem = new FoodListing(found);
			
			if (resultItem.expired()) {
				continue;
			}
			
			result.add(resultItem);
		}

		return result;
	}

	public static List<FoodListing> findFoodListings(Float longitude, Float latitude, Float distance) {
		List<FoodListing> result = new ArrayList<FoodListing>();

		Query q = new Query("FoodListing");

		// Latitude check with query
		q.addFilter(FoodListing.LAT_KEY, Query.FilterOperator.GREATER_THAN_OR_EQUAL, latitude - distance);
		q.addFilter(FoodListing.LAT_KEY, Query.FilterOperator.LESS_THAN_OR_EQUAL, latitude + distance);

		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		PreparedQuery prepQ = data.prepare(q);

		for (Entity found : prepQ.asIterable()) {
			// Longitude check in memory
			FoodListing resultItem = new FoodListing(found);

			if (! resultItem.expired() && resultItem.getLongitude() >= (longitude - distance)
					&& resultItem.getLongitude() <= (longitude + distance)) {
				result.add(resultItem);
			}
		}

		return result;
	}

}
