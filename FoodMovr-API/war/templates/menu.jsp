<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="org.rhok.foodmover.entities.FoodMoverUser"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>

<ul>
	<!-- <li><a href="home.jsp"><img src="images/home.png"/><span>Home</span></a></li> -->
<% 
UserService userService = UserServiceFactory.getUserService();
FoodMoverUser currentUser = FoodMoverUser.getCurrentUser();
if (currentUser != null) {
	if (currentUser.isProducer()) { %>
		<li><a href="/mylistings.jsp"><img src="images/myRoutes.png"/>My Listings</a></li>
		<li><a href="/createlisting.jsp"><img src="images/register.png"/>Create Listing</a></li>
	<% } else { %>
		<li><a href="/findlisting.jsp"><img src="images/findRoutes.png"/>Find Listing</a></li>
		<li><a href="notifications.jsp"><img src="images/personalSettings.png"/>Notification Settings</a></li>
	<% }     
}
%>
	<!-- <li><a href="#"><img src="images/inbox.png"/>Inbox</a></li> -->
	<li><a href="<%=userService.createLogoutURL(request.getRequestURI())%>"><img src="images/logout.png"/>Logout</a></li>
</ul>
