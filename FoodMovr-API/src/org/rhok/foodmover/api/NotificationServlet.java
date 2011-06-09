package org.rhok.foodmover.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;

/**
 * Allows the user to register for notifications.
 * 
 * API documentation: https://docs.google.com/document/pub?id= 1EENWGEVDM3xLEEF1zLXG_SZZphxhE8MnYspsrLNdkvE
 */
@SuppressWarnings("serial")
public class NotificationServlet extends BaseHttpServlet {

	private final static String RADIUS_ARG_NAME = "radius";
	private final static String TYPE_ARG_NAME = "type";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		checkParams(req, new String[] {LAT_ARG_NAME, LONGITUDE_ARG_NAME, RADIUS_ARG_NAME, TYPE_ARG_NAME});

		float lat = Float.parseFloat(req.getParameter(LAT_ARG_NAME));
		float longitude = Float.parseFloat(req.getParameter(LONGITUDE_ARG_NAME));
		float radius = Float.parseFloat(req.getParameter(RADIUS_ARG_NAME));
		String type = req.getParameter(TYPE_ARG_NAME);

		Key key = ApiMethods.makeNotification(lat, longitude, radius, type);

		writeKey(resp, key);
	}

}
