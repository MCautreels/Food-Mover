<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<jsp:include page="templates/header.jsp" />

<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
<script type="text/javascript" src="js/geo_autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
<script type="text/javascript" src="js/jquery.rsv.js"></script>
<script type="text/javascript" src="js/geo-location.js"></script>

<h1>Notification Settings</h1>

<article>
  <p>Finding your location: <span id="status">checking...</span></p>
</article>
<script>
var latlng;
var map;

$().ready(function() {
	$('#location').geo_autocomplete(
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
		}).result(afterGeocoding);
	
	$("#add-notification-form").submit(function() {
		var lat = latlng.lat();
		var lng = latlng.lng();

		var data = {
			lat : lat,
			lng : lng,
			radius : 10, //TODO: temp default value
			type : "email"
		};
		
		if ($.getUrlVar("listingKey") !== undefined) {
			data["key"] = $.getUrlVar("listingKey");
		}

		$.post("/api/v1/notification", data, function(data) {
			// if the user is editing a listing, keep them on that page. 
			/*if (window.location.href.indexOf("listingKey") == -1) {
				window.location = "/index.jsp";
			}*/
			$( "#dialog-message" ).dialog({
				modal: true,
				buttons: {
					Ok: function() {
						$( this ).dialog( "close" );
					}
				}
			});
		});
		
		return false;
	});
});

function afterGeocoding(_event, _data){
    updateMap(_data.geometry.location, _data.formatted_address);
};

function afterHtml5Geocoding(latlng) {
	updateMap(latlng, "Your location");
}

function updateMap(latLng, address) {

	var marker = new google.maps.Marker(
		{
			position : latLng,
			map : map,
			title : "Your location: "
				+ address
		});

	map.setCenter(latLng);
	map.setZoom(15);
};

</script>

<div id="dialog-message" style="display: none;" title="Saved succesfully">
	<p>
		<span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		Your notification settings have successfully been saved
	</p>
</div>

<form id="add-notification-form" style="width: 308px; float: left;">
	<h1>Add location to receive notifications for</h1>
	<label for="location" style="height: 35px;">Location</label> 
	<input class="required" type="text" id="location" /><br />
	
	<input type="submit" value="Save" style="margin-left: 6px;" />
	
	<div id="googlemap" style="width: 306px; height: 306px; float: left;"></div>
	<br />
</form>
<jsp:include page="templates/footer.jsp" />