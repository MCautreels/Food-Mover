<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find listing</title>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.13/jquery-ui.min.js"></script>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
    <script type="text/javascript" src="js/libs/jquery.autocomplete_geomod.js"></script>
    <script type="text/javascript" src="js/geo_autocomplete.js"></script>
    <link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
    <script type="text/javascript" src="js/find-listings.js"></script>
  </head>

  <body>
    <h1>Find listing</h1>
	<div id="findListing" style="width: 260px; margin-left: auto; margin-right: auto;">
	  <form method="get">
		<label for="quantity">Find food near:</label>
        <input type="text" id="location" style="display:inline;"/><input type="submit" value="Search" style="display: inline;"/>
		<div id="googlemap" style="width: 250px; height: 250px"></div><br />
	  </form>
      <table id="listings" style="border: 1px solid black;">
        <tr>
          <th>Description</th>
          <th>Distance</th>
          <th>Time</th>
        </tr>
      </table>
	</div>
  </body>
</html>
