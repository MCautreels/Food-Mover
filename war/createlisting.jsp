<jsp:include page="templates/header.jsp" />	
	<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
	<script type="text/javascript" src="js/geo_autocomplete.js"></script>
	<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
	<script type="text/javascript" src="js/listing-map.js"></script>
	<script type="text/javascript">
		$().ready(function() {
			$("#create-listing-form").submit(function() {
				
				var lat = latLng.lat();
				var lng = latLng.lng();
				var description = $("#description").attr("value");
				var quantity = $("#quantity").attr("value");
				
				$.post("/api/v1/listings", 
					{ 
						lat: lat, 
						lng: lng, 
						description: description, 
						quantity: quantity
					}, function(data) {
						//TODO: show message or redirect
					}
				);
				return false;
			});
		});
	</script>
	<div id="userDetails" style="width: 260px; margin-left: auto; margin-right: auto ;">
		<form id="create-listing-form">
			<label for="quantity">Quantity</label> 
			<input type="text" id="quantity" /><br />
			
			<label for="description">Description</label><br />
			<textarea id="description" rows="10" cols="25"></textarea><br />
	
			<label for="location">Location</label>
			<input type="text" width="150px" id="location" /><br />
			<div id="googlemap" style="width: 250px; height: 250px"></div><br />
			<input type="submit" value="Create" />
		</form>
	</div>
<jsp:include page="templates/footer.jsp" />	