<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>

<% 
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
%>

<jsp:include page="templates/header.jsp" />
Welcome,
<%=user.getNickname()%>! (You can <a href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign out</a>.)
<jsp:include page="templates/footer.jsp" />	

	
	