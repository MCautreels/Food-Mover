$(document).ready(
		function() {
			
		if ($.getUrlVar("producer") === "true") {
			$.post("/api/v1/isProducer");
			window.location = "/mylistings.jsp";
		} else if ($.getUrlVar("consumer") === "true") {
			$.post("/api/v1/isConsumer");
			window.location = "/findlisting.jsp";
		}  
		
			
		});