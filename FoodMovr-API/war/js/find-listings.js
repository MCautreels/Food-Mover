var t; // Repetition variable
var oldResponse = new Object(); 

$().ready(function() {
	resetMap();

	$('#location').geo_autocomplete(
		new google.maps.Geocoder,
		{
			mapkey : 'ABQIAAAAbnvDoAoYOSW2iqoXiGTpYBTIx7cuHpcaq3fYV4NM0BaZl8OxDxS9pQpgJkMv0RxjVl6cDGhDNERjaQ', // TODO
			selectFirst : true,
			minChars : 3,
			delay : 100,
			cacheLength : 50,
			width : 300,
			scroll : true,
			scrollHeight : 330
		}).result(afterGeocoding);
});

function afterGeocoding(_event, _data){
    updateMap(_data.geometry.location, _data.formatted_address);
    getListing(_data.geometry.location.lat(), _data.geometry.location.lng());
};

function afterHtml5Geocoding(latlng) {
	updateMap(latlng, "Your location");
	getListing(latlng.lat(), latlng.lng());
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

function getListing(lat, lng){
    $.ajax({
        type: "GET",
        url: "/api/v1/listings?lat=" + lat + "&lng=" + lng,
        dataType: "json",
        success: function(json){

        	$('.entry').remove();
        	var latLngs = new Array();
        	var startingPoint = new google.maps.LatLng(lat, lng);
        	latLngs[0] = startingPoint;
        	var i = 1;
        	var doRefresh = true;
        	
        	if(json.length <= 0) {
        		$( "#dialog-message" ).dialog({
    				modal: true,
    				buttons: {
    					Ok: function() {
    						$( this ).dialog( "close" );
    					}
    				}
    			});
        		
        		clearTimeout(t);
        		
        		doRefresh = false;
        	}

        	if(oldResponse != null && oldResponse != json) {
        		oldResponse = json;
        	
	            // create table from json
	        	$(json).each(function(index, value) {
	        		var destinationPoint = new google.maps.LatLng(value.lat, value.lng);
	        		var distance = google.maps.geometry.spherical.computeDistanceBetween(startingPoint, destinationPoint);
	        		distance = Math.round(distance * 0.000621371192);
	        		var marker = new google.maps.Marker(
					{
						position : destinationPoint,
						map : map,
						title : "OMG Free Food!",
						icon: new google.maps.MarkerImage("images/marker_blue.png"),
					});
	        		
	        		var infoWindow = new google.maps.InfoWindow({
	        			content: "<p><strong>Description from producer:</strong> " + value['description'] 
	        						+ "</p><p><strong>Quantity:</strong>" + value['quantity'] + "</p>"
	        		
	        		});
	        		
	        		google.maps.event.addListener(marker, 'click', function() {
	        			infoWindow.open(map, marker);
	        		});
	        		
	        		latLngs[i] = destinationPoint;
	        		i++;
	        		
	        		var newRow = '<tr class="entry">';
	        		newRow += '<td>' + value.description + '</td>';
	        		newRow += '<td>' + distance + ' miles</td>';
	        		newRow += '<td>' + value.quantity + '</td></tr>';
	        		
	        		$('#listingsTable').append(newRow);
	        	});
        	}
	        
        	if(oldResponse != null && oldResponse != json) {
	        	var latlngbounds = new google.maps.LatLngBounds( );
	        	for ( var i = 0; i < latLngs.length; i++ ) {
	        		latlngbounds.extend( latLngs[ i ] );
	        	}
	        	
	        	map.fitBounds(latlngbounds);
        	}
        	
        	if(doRefresh) {
        		t = setTimeout("getListing(" + lat + "," + lng + ")", 2000);
        	}
        }
    });
};

function resetMap() {
	var latlng = new google.maps.LatLng(50.7, 4.5);
	var myOptions = {
		zoom : 1,
		center : latlng,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("googlemap"), myOptions);
};