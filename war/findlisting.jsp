<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<jsp:include page="templates/header.jsp" />
<script type="text/javascript" src="js/find-listings.js"></script>
<h1>Find listing</h1>
<div id="findListing" style="width: 310px; float: left; margin-right: 5px;">
	<form method="get">
		<label for="quantity">Find food near:</label> <input type="text"
			id="location" style="display: inline;" /><input type="submit"
			value="Search" style="display: inline;" />
		<div id="googlemap" style="width: 250px; height: 250px"></div>
		<br />
	</form>
	<table id="listings" style="border: 1px solid black;">
		<tr>
			<th>Description</th>
			<th>Distance</th>
			<th>Time</th>
		</tr>
	</table>
</div>
<jsp:include page="templates/footer.jsp" />
