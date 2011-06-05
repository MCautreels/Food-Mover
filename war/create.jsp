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
		<script>
			var map;
			$().ready(
				function() {
		
				resetMap();
			
				$('.location').geo_autocomplete(
					new google.maps.Geocoder,
					{
						mapkey : 'ABQIAAAAbnvDoAoYOSW2iqoXiGTpYBTIx7cuHpcaq3fYV4NM0BaZl8OxDxS9pQpgJkMv0RxjVl6cDGhDNERjaQ', //TODO
						selectFirst : true,
						minChars : 3,
						delay : 100,
						cacheLength : 50,
						width : 300,
						scroll : true,
						scrollHeight : 330
					}).result(function(_event, _data) {
						var latLng = _data.geometry.location;
		
						var marker = new google.maps.Marker(
								{
									position : latLng,
									map : map,
									title : "Your location: "
											+ _data.formatted_address
								});
		
						map.setCenter(latLng);
						map.setZoom(15);
					});
				});
		
			function resetMap() {
				var latlng = new google.maps.LatLng(50.7, 4.5);
				var myOptions = {
					zoom : 1,
					center : latlng,
					mapTypeId : google.maps.MapTypeId.ROADMAP
				};
				map = new google.maps.Map(document.getElementById("googlemap"),
						myOptions);
			}
		</script>
	</head>
	<body>
		<div id="userDetails">
			<label for="quantity"></label> 
			<input type="text" id="quantity" /> 
			
			<label for="description">Description</label>
			<textarea rows="25" cols=""></textarea>
	
			<label for="location">Location</label>
			<input type="text" width="150px" id="location" />
			<div id="googlemap" style="width: 500px; height: 500px"></div>
		</div>
	</body>
</html>