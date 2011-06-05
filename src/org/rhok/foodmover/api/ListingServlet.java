package org.rhok.foodmover.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.entities.FoodMoverUser;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.repackaged.org.json.JSONStringer;

@SuppressWarnings("serial")
public class ListingServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(req.getParameter("lat") == null)	throw new IllegalStateException("No lat parameter set");
		if(req.getParameter("lng") == null)	throw new IllegalStateException("No lng parameter set");
		if(req.getParameter("description") == null)	throw new IllegalStateException("No description parameter set");
		if(req.getParameter("quantity") == null) throw new IllegalStateException("No quantity parameter set");
		
		float lat = Float.parseFloat(req.getParameter("lat"));
		float longitude = Float.parseFloat(req.getParameter("lng"));
		String description = req.getParameter("description");
		int quantity = Integer.parseInt(req.getParameter("quantity"));
		
		FoodListing listing = new FoodListing();
		listing.setLat(lat);
		listing.setLongitude(longitude);
		listing.setDescription(description);
		listing.setQuantity(quantity);
		listing.setOwner(FoodMoverUser.getCurrentUser());
		
		listing.put();
		
		resp.getWriter().println(KeyFactory.keyToString(listing.getKey()));
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		
		if(req.getParameter("lat") == null)	throw new IllegalStateException("No lat parameter set");
		if(req.getParameter("lng") == null)	throw new IllegalStateException("No lng parameter set");	
		
		float latitude = Float.parseFloat(req.getParameter("lat"));
		float longitude = Float.parseFloat(req.getParameter("lng"));		
		float distance;
		if(req.getParameter("distance") == null){
			distance = Float.parseFloat(this.getInitParameter("default_distance"));
		} else {
			distance = Float.parseFloat(req.getParameter("distance"));
		}
		
		List<FoodListing> foodlistings = findFoodListings(longitude, latitude, distance);
		
		JSONStringer jsonStringer = new JSONStringer();
		try {
			jsonStringer.array();
			for (FoodListing foodListing : foodlistings) {
				jsonStringer.object()
				.key("lat").value(foodListing.getLat())
				.key("lng").value(foodListing.getLongitude())
				.key("description").value(foodListing.getDescription())
				.key("quantity").value(foodListing.getQuantity()).endObject();
			}
			jsonStringer.endArray();
			
			resp.setContentType("application/json");
			resp.getWriter().println(jsonStringer.toString());
		} catch (Exception e) {
			throw new ServletException(e);
		}

	}

	public static List<FoodListing> findFoodListings(Float longitude, Float latitude, Float distance)
	{
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
			
			if(resultItem.getLongitude() >= (longitude -distance) && resultItem.getLongitude() <= (longitude + distance)){
				result.add(resultItem);
			}
		}
		
		return result;
	}

}
