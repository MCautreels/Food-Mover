var map;
var latLng;

$().ready(function() {
	resetMap();

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
		}).result(function(_event, _data) {
			latLng = _data.geometry.location;
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
	
	var lat = 50.7;
	var lng = 4.5;
	
	var locRegex = /(-?\d+(\.\d+)?), (-?\d+(\.\d+)?)/;
	var locVal = $("#location").val();
	if (locRegex.test(locVal)) {
		lat = parseFloat(locVal.substring(0, locVal.indexOf(',')));
		lng = parseFloat(locVal.substring(locVal.indexOf(',') + 1, locVal.length));
	}
	
	   var myLatlng = new google.maps.LatLng(lat, lng);
	    var myOptions = {
	      zoom: 15,
	      center: myLatlng,
	      mapTypeId: google.maps.MapTypeId.ROADMAP
	    }
	    map = new google.maps.Map(document.getElementById("googlemap"), myOptions);
	    
	    var marker = new google.maps.Marker({
	        position: myLatlng, 
	        map: map,
	    });   
}