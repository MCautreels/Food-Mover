<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>

<% 
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user == null) {
		RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
		rd.forward(request, response);
	} 
%>
<html lang="en" class="no-js"> <!--<![endif]-->
	<head>
		<meta charset="utf-8"/>
		<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<%
			if(request.getAttribute("title") == null || request.getAttribute("title").equals("")) { %>
				<title>Food Mover</title>
			<% } else { %>
				<title>${title}</title>
			<% } %>
		<title>Food Mover</title>
		<meta name="description" content=""/>
		<meta name="author" content=""/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js"></script>
		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
		<link rel="stylesheet" href="css/style.css"/>
		<link rel="stylesheet" type="text/css" href="ss/style.css"/>
        <link rel="stylesheet" href="css/pageStyle.css"/>
	</head>
	<body>
		<div id="container">
			<header>
				<img src="images/headerLogo.png" alt="headerLogo" />
                <span style="float: right">
                    <a href="#"><img src="images/flag_nl.png" alt="flag_nl" /></a>
                    <a href="#"><img src="images/flag_en.png" alt="flag_en" /></a>
                </span>
				<div>
					<jsp:include page="menu.jsp" />
				</div>
			</header>

			
			
			
			