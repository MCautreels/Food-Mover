$().ready(
	function() {
		if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(success, error);
		} else {
			error('not supported');
		}
	}
);
function success(position) {
	var s = document.querySelector('#status');

	if (s.className == 'success') {
		// not sure why we're hitting this twice in FF, I think it's
		// to do with a cached result coming back
		return;
	}

	s.innerHTML = "found you!";
	s.className = 'success';

	latlng = new google.maps.LatLng(position.coords.latitude,
			position.coords.longitude);
	var myOptions = {
		zoom : 15,
		center : latlng,
		navigationControlOptions : {
			style : google.maps.NavigationControlStyle.SMALL
		},
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("googlemap"),
			myOptions);
	
	afterHtml5Geocoding(latlng);

	var marker = new google.maps.Marker({
		position : latlng,
		map : map,
		title : "You are here!"
	});
}

function error(msg) {
	var s = document.querySelector('#status');
	s.innerHTML = typeof msg == 'string' ? msg : "failed";
	s.className = 'fail';

	// console.log(arguments);
}
