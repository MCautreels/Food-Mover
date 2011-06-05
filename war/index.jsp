<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>

<jsp:include page="templates/header.jsp" />
<script type="text/javascript" src="/js/setUserType.js" ></script>
<%
	UserService userService = UserServiceFactory.getUserService();
	User user = userService.getCurrentUser();
	if (user != null) {
%>
<p>
	Welcome,
	<%=user.getNickname()%>! (You can <a href="<%=userService.createLogoutURL(request.getRequestURI())%>">sign out</a>.)
</p>
<%
	} else {
%>
<p>
	Log in as a: <a href="<%=userService.createLoginURL("/?producer=true")%>">Producer</a> | <a href="<%=userService.createLoginURL("/?consumer=true")%>">Consumer</a>.
</p>
<%
	}
%>

<jsp:include page="templates/footer.jsp" />