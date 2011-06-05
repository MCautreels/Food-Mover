<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<jsp:include page="templates/header.jsp" />

<script type="text/javascript"
	src="js/libs/jquery.autocomplete_geomod.js"></script>
<script type="text/javascript" src="js/geo_autocomplete.js"></script>
<link rel="stylesheet" type="text/css"
	href="css/jquery.autocomplete.css" />
<script type="text/javascript" src="js/listing-map.js"></script>
<script type="text/javascript" src="js/jquery.rsv.js"></script>

<script type="text/javascript">
	$().ready(function() {
		function myOnComplete() {
			$("#add-notification-form").submit(function() {
				var lat = latLng.lat();
				var lng = latLng.lng();
				
				return false;
			});
		}

	});
</script>


<form id="add-notification-form">
	<h1>Add location to receive notifications for</h1>
	<label for="location">Location</label> <input class="required"
		type="text" id="location" /><br />

	<div id="googlemap" style="width: 250px; height: 250px; float: left;"></div>
	<br />
</form>
<jsp:include page="templates/footer.jsp" />