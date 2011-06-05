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
		<title>DriveConnect</title>
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
                <span style="float: right">
                    <a href="?lang=nl"><img src="images/flag_nl.png" alt="flag_nl" /></a>
                    <a href="?lang=en"><img src="images/flag_en.png" alt="flag_en" /></a>
                </span>
				<!--<nav>
					<ul>
						<li class="activeLink">Home</li>
						<li>Contact</li>
					</ul>
				</nav>-->
			</header>
			<div id="landingPageMiddle">
				<h1>Some kind of Slogan</h1>
				<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean posuere mi ut neque dictum euismod. Suspendisse posuere rhoncus ligula quis condimentum. Curabitur dignissim faucibus justo et faucibus. Suspendisse vel elit felis, eu rhoncus tellus. Vestibulum pulvinar tincidunt eros, non pharetra eros suscipit id. In nisi eros, semper id pretium et, tincidunt et tortor.</p>
				<img src="images/landingPageMiddleImage.png" alt="landingPageMiddleImage" />
			</div>
			<div id="landingPageBottom">
				<div class="bottomBox">
					<img src="images/finder.png" alt="finder" />
					<p>Find Food suppliers at light speed.</p>
				</div>
				<div class="bottomBox">
					<img src="images/notifications.png" alt="notifications" />
					<p>Easy notification system to stay up to date realtime.</p>
				</div>
				<div class="bottomBox">
					<img src="images/platforms.png" alt="platforms" />
					<p>Use different clients like Smartphones, Dumbphones, computer, netbook, ...</p>
				</div>
				<div class="bottomBox">
					<a class="largeButton" id="loginProducerButton" href="<%=userService.createLoginURL("/?producer=true")%>">Login as a Producer</a>
					<a class="largeButton" id="loginConsumerButton" href="<%=userService.createLoginURL("/?consumer=true")%>">Login as a Consumer</a>
				</div>
			</div>
		</div>
	</body>
</html>
