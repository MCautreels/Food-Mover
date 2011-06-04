package org.rhok.foodmover.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.services.FoodListingService;

@SuppressWarnings("serial")
public class AddListingServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
	
}
