<jsp:include page="templates/header.jsp" />	
	<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
	<script type="text/javascript" src="js/geo_autocomplete.js"></script>
	<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
	<script type="text/javascript" src="js/listing-map.js"></script>
	<script type="text/javascript" src="js/jquery.rsv.js"></script>
	
	<script type="text/javascript">
		$().ready(function() {
			function myOnComplete()
			  {
				$("#create-listing-form").submit(function() {	
					var lat = latLng.lat();
					var lng = latLng.lng();
					var description = $("#description").attr("value");
					var quantity = $("#quantity").attr("value");

					$.post("/api/v1/listings", {
						lat : lat,
						lng : lng,
						description : description,
						quantity : quantity
					}, function(data) {
						window.location = "/index.jsp";
					});
					return false;
				});
			  }
			
			$("#create-listing-form").RSV({
		          onCompleteHandler: myOnComplete,
		          errorFieldClass: "errorField",
		          displayType: "display-html",
		          errorHTMLItemBullet: "&#8212; ",
		                rules: [
							"required,quantity,Quantity is a required field.",
							"required,description,Description is a required field.",
							"required,location,Location is a required field.",
							"digits_only,quantity,Quantity should be a number."
		    ]
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
			<div id="rsvErrors"></div> 
		</form>
	</div>
	<div id="googlemap" style="width: 250px; height: 250px; float: left;" ></div><br />
<jsp:include page="templates/footer.jsp" />	