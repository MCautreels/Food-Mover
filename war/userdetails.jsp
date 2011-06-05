<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create listing</title>
</head>

<body>
<h1>Create listing</h1>
	<div id="createListing">
		<form method="post">
			<label for="quantity">Quantity</label> <input type="text" id="quantity" />
			<br />
			<label for="description">Description</label> <br />
			<textarea rows="10" cols="70" id="description"></textarea>
			<br />
			<button id="save" type="submit">Save</button>
		</form>

	</div>
</body>
</html>