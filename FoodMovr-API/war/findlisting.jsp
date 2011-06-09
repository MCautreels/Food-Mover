<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<jsp:include page="templates/header.jsp" />
<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
<script type="text/javascript" src="js/geo_autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
<link rel="stylesheet" type="text/css" href="css/find-listings.css" />
<script type="text/javascript" src="js/find-listings.js"></script>
<script type="text/javascript" src="js/geo-location.js"></script>
<h1>Find listing</h1>

<article>
  <p>Finding your location: <span id="status">checking...</span></p>
</article>

<div id="locationSearch">
	<label for="quantity">Find food near:</label> 
	<input type="text" id="location" />
</div>
<div id="wrap">
	<div id="googlemap" style="height: 400px;"></div>
	<table id="listingsTable">
		<tr class="header">
			<th>Description</th>
			<th>Distance</th>
			<th>Quantity</th>
		</tr>
	</table>
</div>

<jsp:include page="templates/footer.jsp" />
