package org.rhok.foodmover.api;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public abstract class BaseHttpServlet extends HttpServlet {

	protected static final String LAT_ARG_NAME = "lat";
	protected static final String LONGITUDE_ARG_NAME = "lng";

	/**
	 * Checks that the request's data contains an entry for argKey
	 * 
	 * If the url is /listings?bar=foo, checkParam(req, "omg") would throw an
	 * exception.
	 * 
	 * @param req
	 *            the current request
	 * @param argKey
	 *            the argument key to check
	 */
	protected static void checkParam(HttpServletRequest req, String argKey) {
		if (req.getParameter(argKey) == null) {
			throw new IllegalArgumentException("Argument not specified: " + argKey);
		}
	}
	
	/**
	 * Call checkParams for every element in argKeys.
	 * 
	 * @param req
	 * @param argKeys
	 */
	protected static void checkParams(HttpServletRequest req, String[] argKeys) {
		for (String argKey : argKeys) {
			checkParam(req, argKey);
		}
	}

	/**
	 * Write a key to the HTTP response
	 * 
	 * @param resp
	 *            the current response
	 * @param key
	 *            the key to write
	 * @throws IOException
	 */
	protected void writeKey(HttpServletResponse resp, Key key) throws IOException {
		resp.getWriter().println(KeyFactory.keyToString(key));
	}

}
