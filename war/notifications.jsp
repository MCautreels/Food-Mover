<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<jsp:include page="templates/header.jsp" />

<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
<script type="text/javascript" src="js/geo_autocomplete.js"></script>
<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
<script type="text/javascript" src="js/jquery.rsv.js"></script>

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
		}).result(updateMap);
	
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

function updateMap(_event, _data) {
	latlng = _data.geometry.location;

	var marker = new google.maps.Marker(
		{
			position : latlng,
			map : map,
			title : "Your location: "
				+ _data.formatted_address
		});

	map.setCenter(latlng);
	map.setZoom(15);
};

function success(position) {
  var s = document.querySelector('#status');
  
  if (s.className == 'success') {
    // not sure why we're hitting this twice in FF, I think it's to do with a cached result coming back    
    return;
  }
  
  s.innerHTML = "found you!";
  s.className = 'success';
  
  latlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
  var myOptions = {
    zoom: 15,
    center: latlng,
    navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  map = new google.maps.Map(document.getElementById("googlemap"), myOptions);
  
  var marker = new google.maps.Marker({
      position: latlng, 
      map: map, 
      title:"You are here!"
  });
}

function error(msg) {
  var s = document.querySelector('#status');
  s.innerHTML = typeof msg == 'string' ? msg : "failed";
  s.className = 'fail';
  
  // console.log(arguments);
}

if (navigator.geolocation) {
  navigator.geolocation.getCurrentPosition(success, error);
} else {
  error('not supported');
}

</script>

<div id="dialog-message" title="Saved succesfully">
	<p>
		<span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		Your notification settings have successfully been saved
	</p>
</div>

<form id="add-notification-form" style="width: 308px; float: left;">
	<h1>Add location to receive notifications for</h1>
	<label for="location" style="height: 35px;">Location</label> 
	<input class="required" type="text" id="location" /><br />
	
	<input type="submit" value="Save" />
	
	<div id="googlemap" style="width: 250px; height: 250px; float: left;"></div>
	<br />
</form>
<jsp:include page="templates/footer.jsp" />