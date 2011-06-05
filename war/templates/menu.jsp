<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="org.rhok.foodmover.entities.FoodMoverUser"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>

<ul>
<% 
UserService userService = UserServiceFactory.getUserService();
FoodMoverUser currentUser = FoodMoverUser.getCurrentUser();
if (currentUser != null) {
	if (currentUser.isProducer()) { %>
		<li><a href="/createlisting.jsp"><img src="images/register.png"/>Create Listing</a></li>
		<li><a href="#"><img src="images/myRoutes.png"/><span>My Listings</span></a></li>
	<% } else { %>
		<li><a href="/findlisting.jsp"><img src="images/findRoutes.png"/>View Listing</a></li>
	<% }     
}
%>
	<li><a href="#"><img src="images/personalSettings.png"/><span>Personal Settings</span></a></li>
	<li><a href="#"><img src="images/inbox.png"/><span>Inbox</span></a></li>
	<li><a href="<%=userService.createLogoutURL(request.getRequestURI())%>"><img src="images/logout.png"/><span>Logout</span></a></li>
</ul>
