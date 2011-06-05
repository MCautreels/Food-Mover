<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<jsp:include page="templates/header.jsp" />
<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
<script type="text/javascript" src="js/geo_autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
<script type="text/javascript" src="js/find-listings.js"></script>
<h1>Find listing</h1>
<div id="findListing" style="width: 308px; float: left; margin-right: 5px;">
	<label for="quantity">Find food near:</label> 
	<input type="text" id="location" />
	<table id="listings" style="border: 1px solid black; width: 306px;">
		<tr class="header">
			<th>Description</th>
			<th>Distance</th>
			<th>Quantity</th>
		</tr>
	</table>
</div>
<div id="googlemap" style="width: 250px; height: 250px; float: left;"></div>
<jsp:include page="templates/footer.jsp" />
