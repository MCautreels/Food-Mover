<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.Key"%>
<%@ page import="com.google.appengine.api.datastore.KeyFactory"%>		
<%@ page import="org.rhok.foodmover.entities.FoodListing"%>

<jsp:include page="templates/header.jsp" />	
	<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
	<script type="text/javascript" src="js/geo_autocomplete.js"></script>
	<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
	<script type="text/javascript" src="js/listing-map.js"></script>
	<script type="text/javascript" src="js/geo-location.js"></script>
	<script type="text/javascript" src="js/jquery.rsv.js"></script>
	
	<script type="text/javascript">
		$().ready(function() {			
			$("#create-listing-form").RSV({
		          onCompleteHandler: submitForm,
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
		
		function submitForm() {
			var lat = latLng.lat();
			var lng = latLng.lng();
			var description = $("#description").attr("value");
			var quantity = $("#quantity").attr("value");

			var data = {
				lat : lat,
				lng : lng,
				description : description,
				quantity : quantity
			};
			
			if ($.getUrlVar("listingKey") !== undefined) {
				data["key"] = $.getUrlVar("listingKey");
				
				$.post('/api/v1/listings?action=put', data, function(result) {
						alert("Listing updated");
				});
				
			} else {
				
				$.post("/api/v1/listings", data, function(data) {
					$( "#dialog-message" ).dialog({
						modal: true,
						buttons: {
							Ok: function() {
								$( this ).dialog( "close" );
							}
					
						}
					});
				});
			}
			
			return false;
		}
		
		function afterHtml5Geocoding(coordinates) {
			latLng = coordinates;
			updateMap(coordinates, "Your location");
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
	<% 
	String quantity = "";
	String description = "";
	String location = "";
	String heading = "Create new listing";
	String submitLabel = "Create";
	
	String strKey = request.getParameter("listingKey");
	if (strKey != null) {
		Key key = KeyFactory.stringToKey(strKey);
		FoodListing listing = new FoodListing(DatastoreServiceFactory.getDatastoreService().get(key));
		
		quantity = listing.getQuantity() + "";
		description = listing.getDescription();
		location = String.format("%f, %f", listing.getLat(), listing.getLongitude());
		
		heading = "Edit listing";
		submitLabel = "Update";
		
	} %>
	
	<div id="dialog-message" style="display: none;" title="Saved succesfully">
		<p>
			<span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
			Your listing has successfully been created!
		</p>
	</div>
	
	<div id="userDetails" style="width: 310px; float: left; margin-right: 5px;">
		<h1><%=heading%></h1>
		
		<article>
			<p>Finding your location: <span id="status">checking...</span></p>
		</article>
		
		<form id="create-listing-form">
			<label for="quantity">Quantity</label> 
			<input class="required number" type="text" id="quantity" value="<%=quantity%>" /><br />
			
			<label for="description">Description</label><br />
			<textarea class="required" id="description" style="margin-left: 8px;" rows="10" cols="25"><%=description%></textarea><br />
	
			<label for="location">Location</label>

			<input value="<%=location%>" class="required" type="text" id="location" /><br />
			<input type="submit" value="<%=submitLabel%>" />
			<div id="rsvErrors"></div> 
		</form>
	</div>
	<div id="googlemap" style="width: 250px; height: 250px; float: left;" ></div><br />
<jsp:include page="templates/footer.jsp" />	
