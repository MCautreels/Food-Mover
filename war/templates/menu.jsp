<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="org.rhok.foodmover.entities.FoodMoverUser"%>

<div id="menu">
	<ul id="menu">
		<% 
		FoodMoverUser currentUser = FoodMoverUser.getCurrentUser();
		if (currentUser != null) {
			if (currentUser.isProducer()) { %>
				<li><a href="/createListing"><img src="images/register.png"/>Create Listing</a></li>
			<% } else { %>
				<li><a href="/viewlistings"><img src="images/findRoutes.png"/>View Listing</a></li>
			<% } 
		}
		%>
	</ul>
</div>
