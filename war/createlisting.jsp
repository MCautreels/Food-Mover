<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Create Listing</title>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js"></script>
		<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
		<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
		<script type="text/javascript" src="js/geo_autocomplete.js"></script>
		<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
		<script type="text/javascript" src="js/listing-map.js">
			
		</script>
	</head>
	<body>
		<div id="userDetails" style="width: 260px; margin-left: auto; margin-right: auto ;">
			<form id="create-listing-form" method="post">
				<label for="quantity">Quantity</label> 
				<input type="text" id="quantity" /><br />
				
				<label for="description">Description</label><br />
				<textarea rows="10" cols="25"></textarea><br />
		
				<label for="location">Location</label>
				<input type="text" width="150px" id="location" /><br />
				<div id="googlemap" style="width: 250px; height: 250px"></div><br />
				<input type="submit" value="Create" />
			</form>
		</div>
	</body>
</html>