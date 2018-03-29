function handlePayinfoResult(resultDataString) {
	resultDataJson = JSON.parse(resultDataString);
	
	console.log("handle payinfo response");
	console.log(resultDataJson);
	console.log(resultDataJson["status"]);

	// if login success, redirect to index.html page
	if (resultDataJson["status"] == "success") {
		window.location.replace("/cs122b-Project2-FabFlix/confirm.html");
	} else {
		console.log("show error message");
		console.log(resultDataJson["message"]);
		jQuery("#payinfo_error_message").text(resultDataJson["message"]);
	}
}

function submitPayinfoForm(formSubmitEvent) {
	console.log("submit payinfo form");
	
	// important: disable the default action of submitting the form
	//   which will cause the page to refresh
	//   see jQuery reference for details: https://api.jquery.com/submit/
	formSubmitEvent.preventDefault();

	jQuery.post(
		"/cs122b-Project2-FabFlix/TestPay", 
		// serialize the login form to the data sent by POST request
		jQuery("#payinfo_form").serialize(),
		(resultDataString) => handlePayinfoResult(resultDataString));
}

// bind the submit action of the form to a handler function
jQuery("#payinfo_form").submit((event) => submitPayinfoForm(event));