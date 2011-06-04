package org.rhok.foodmover.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rhok.foodmover.entities.FoodMoverUser;

import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class IsConsumerServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ApiUtils.setCurrentUserType(FoodMoverUser.UserType.CONSUMER);
	}
	
}
