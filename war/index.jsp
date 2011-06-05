<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>


<% request.setAttribute("title", "Food Mover"); %>

<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user != null) {
		RequestDispatcher rd = request.getRequestDispatcher("/home.jsp");
		rd.forward(request, response);
	}
%>
<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"> <!--<![endif]-->
	<head>
		<meta charset="utf-8"/>
		<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<title>Food Mover</title>
		<meta name="description" content=""/>
		<meta name="author" content=""/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<link rel="stylesheet" href="css/style.css"/>
		<link rel="stylesheet" href="css/landingPageStyle.css"/>
	</head>
	<body>
		<div id="container">
			<header>
				<img src="images/headerLogo.png" alt="headerLogo" />
			</header>
			<div id="landingPageMiddle">
				<h1>Stop wasting food</h1>
				<p>Food waste is inescapable, whether you're a caterer, a cafeteria, or a restaurant.
				With FoodMovr, you can find people to make use of the food, instead of throwing it away.</p>
				<p>If you're a food bank, soup kitchen, or some random person on the street, this app 
					connects you with those who have extra food.</p>
				<img src="images/landingPageMiddleImage.png" alt="landingPageMiddleImage" />
			</div>
			<div id="landingPageBottom">
				<div class="bottomBox">
					<img src="images/finder.png" alt="finder" />
					<p>Find Food suppliers at light speed.</p>
				</div>
				<div class="bottomBox">
					<img src="images/notifications.png" alt="notifications" />
					<p>Get notified when food is available nearby.</p>
				</div>
				<div class="bottomBox">
					<img src="images/platforms.png" alt="platforms" />
					<p>Connect from your desktop or smartphone</p>
				</div>
				<div class="bottomBox">
					<a class="largeButton" id="loginProducerButton" href="<%=userService.createLoginURL("/?producer=true")%>">I want to give food away</a>
					<a class="largeButton" id="loginConsumerButton" href="<%=userService.createLoginURL("/?consumer=true")%>">I want to get extra food</a>
				</div>
			</div>
		</div>
	</body>
</html>
