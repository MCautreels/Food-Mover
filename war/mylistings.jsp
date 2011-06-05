<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<jsp:include page="templates/header.jsp" />
<script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
<script type="text/javascript">
$().ready(function() {
	getListing();
});

function getListing(){
    $.ajax({
        type: "GET",
        url: "/api/v1/listings",
        dataType: "json",
        success: function(json){
        	if(json.length <= 0) {
        		$( "#dialog-message" ).dialog({
    				modal: true,
    				buttons: {
    					Ok: function() {
    						$( this ).dialog( "close" );
    					}
    				}
    			});
        	}
        	
        	var latLngs = new Array();
        	var latlng = new google.maps.LatLng(50.7, 4.5);
    		var myOptions = {
    				zoom : 1,
    				center : latlng,
    				mapTypeId : google.maps.MapTypeId.ROADMAP
    			};
    		var map = new google.maps.Map(document.getElementById("googlemap"), myOptions);
    		var i = 0;
    		
            // create table from json
        	$(json).each(function(index, value) {
        		var destinationPoint = new google.maps.LatLng(value.lat, value.lng);
        		
        		var marker = new google.maps.Marker(
				{
					position : destinationPoint,
					map : map,
					title : "A possible destination",
					icon: new google.maps.MarkerImage("images/marker_blue.png"),
				});
        		
        		latLngs[i] = destinationPoint; 
        		i++;
        		
        		var newRow = '<tr class="entry">';
        		newRow += '<td>' + value.description + '</td>';
        		newRow += '<td>' + value.quantity + '</td>';
        		
        		$('#listings').append(newRow);
        	});
        	
        	var latlngbounds = new google.maps.LatLngBounds( );
        	for ( var i = 0; i < latLngs.length; i++ ) {
        	  latlngbounds.extend( latLngs[ i ] );
        	}
        	
        	map.fitBounds(latlngbounds);
        }
    });
};
</script>


<h1>All my listings</h1>
<div id="myListing" style="width: 308px; float: left; margin-right: 5px;">
	<table id="listings" style="border: 1px solid black; width: 306px;">
		<tr class="header">
			<th>Description</th>
			<th>Quantity</th>
		</tr>
	</table>
</div>
<div id="googlemap" style="width: 250px; height: 250px; float: left;"></div>
<jsp:include page="templates/footer.jsp" />
