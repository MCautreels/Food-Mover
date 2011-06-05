package org.rhok.foodmover.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rhok.foodmover.entities.FoodMoverUser;
import org.rhok.foodmover.entities.Notification;

import com.google.appengine.api.datastore.KeyFactory;

public class NotificationServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2170298708623332165L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if(req.getParameter("lat") == null)	throw new IllegalStateException("No lat parameter set");
		if(req.getParameter("lng") == null)	throw new IllegalStateException("No lng parameter set");
		if(req.getParameter("radius") == null)	throw new IllegalStateException("No radius parameter set");
		if(req.getParameter("type") == null) throw new IllegalStateException("No type parameter set");
		
		float lat = Float.parseFloat(req.getParameter("lat"));
		float longitude = Float.parseFloat(req.getParameter("lng"));
		int radius = Integer.parseInt(req.getParameter("radius").toString());
		String type = req.getParameter("type").toString();
		
		Notification notification = new Notification();
		notification.setLat(lat);
		notification.setLongitude(longitude);
		notification.setNotificationType(type);
		notification.setRadius(radius);
		notification.setOwner(FoodMoverUser.getCurrentUser());
		
		notification.put();
		
		resp.getWriter().println(KeyFactory.keyToString(notification.getKey()));
	}

}
