$(document).ready(
		function() {
			
		if ($.getUrlVar("producer") === "true") {
			$.post("/api/v1/isProducer");
			window.location.replace("/");
		} else if ($.getUrlVar("consumer") === "true") {
			$.post("/api/v1/isConsumer");
			window.location.replace("/");
		}  
		
			
		});