package org.rhok.foodmover.api;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.services.FoodListingService;

import com.google.appengine.repackaged.org.json.JSONStringer;

@SuppressWarnings("serial")
public class ListingServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		float lat = Float.parseFloat(req.getParameter("lat"));
		float longitude = Float.parseFloat(req.getParameter("lng"));
		String description = req.getParameter("description");
		int quantity = Integer.parseInt(req.getParameter("quantity"));

		FoodListing listing = new FoodListing();
		listing.setLatitude(lat);
		listing.setLongitude(longitude);
		listing.setDescription(description);
		listing.setQuantity(quantity);

		FoodListingService.addFoodListing(listing);
		resp.getWriter().println(listing.getKey());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/plain");
		float latitude = Float.parseFloat(req.getParameter("lat"));
		float longitude = Float.parseFloat(req.getParameter("lng"));

		// TODO: Make distance optional
		float distance = Float.parseFloat(req.getParameter("distance"));

		List<FoodListing> foodlistings = FoodListingService.findFoodListings(longitude, latitude, distance);

		
		JSONStringer jsonStringer = new JSONStringer();
		try {
			jsonStringer.array();
			for (FoodListing foodListing : foodlistings) {
				jsonStringer.object()
				.key("lat").value(foodListing.getLatitude())
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

}
