<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="org.rhok.foodmover.entities.FoodMoverUser"%>

<div id="menu">
	<ul id="menu">
		<li><a href="index.jsp">Home</a></li>
		<% 
		FoodMoverUser currentUser = FoodMoverUser.getCurrentUser();
		if (currentUser != null) {
			if (currentUser.isProducer()) { %>
				<li><a href="/createlisting">Create Listing</a></li>
			<% } else { %>
				<li><a href="/viewlistings">View Listing</a></li>
			<% } 
		}
		%>
	</ul>
</div>
