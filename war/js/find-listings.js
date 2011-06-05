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
		}).result(afterGeocoding);
});

function afterGeocoding(_event, _data){
    updateMap(_event, _data);
    getListing(_data.geometry.location.lat(), _data.geometry.location.lng());
};

function updateMap(_event, _data) {
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
};

function getListing(lat, lng){
    $.ajax({
        type: "GET",
        url: "/api/v1/listings?lat=" + lat + "&lng=" + lng + "&distance=1",
        dataType: "JSON",
        success: function(json){
            // create table from json
            $.each(json, function(i, listing){
                $("#listings").children().append("<tr><td>" + listing["description"] + "</td><td>0.2 mi</td><td>" + listing["quantity"] + "</td></tr>");
            });
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