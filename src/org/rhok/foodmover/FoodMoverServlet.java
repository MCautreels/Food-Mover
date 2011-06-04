package org.rhok.foodmover;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import org.rhok.foodmover.entities.FoodListing;
import org.rhok.foodmover.services.FoodListingService;

@SuppressWarnings("serial")
public class FoodMoverServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
