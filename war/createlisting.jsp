<jsp:include page="templates/header.jsp" />	
	<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
	<script type="text/javascript" src="js/geo_autocomplete.js"></script>
	<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
	<script type="text/javascript" src="js/listing-map.js"></script>
	<script type="text/javascript" src="js/jquery-validation-1.8.1/jquery.validate.js"></script>
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
	<div id="userDetails" style="width: 310px; float: left; margin-right: 5px;">
		<form id="create-listing-form">
			<label for="quantity">Quantity</label> 
			<input class="required number" type="text" id="quantity" /><br />
			
			<label for="description">Description</label><br />
			<textarea class="required" id="description" style="margin-left: 8px;" rows="10" cols="25"></textarea><br />
	
			<label for="location">Location</label>
			<input class="required" type="text" id="location" /><br />
			<input type="submit" value="Create" />
		</form>
	</div>
	<div id="googlemap" style="width: 250px; height: 250px; float: left;" ></div><br />
<jsp:include page="templates/footer.jsp" />	