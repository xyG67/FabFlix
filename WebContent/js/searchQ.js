/*
 * CS 122B Project 4. Autocomplete Example.
 * 
 * This Javascript code uses this library: https://github.com/devbridge/jQuery-Autocomplete
 * 
 * This example implements the basic features of the autocomplete search, features that are 
 *   not implemented are mostly marked as "TODO" in the codebase as a suggestion of how to implement them.
 * 
 * To read this code, start from the line "$('#autocomplete').autocomplete" and follow the callback functions.
 * 
 */


/*
 * This function is called by the library when it needs to lookup a query.
 * 
 * The parameter query is the query string.
 * The doneCallback is a callback function provided by the library, after you get the
 *   suggestion list from AJAX, you need to call this function to let the library know.
 */
function handleLookup(query, doneCallback) {
	console.log("autocomplete initiated")
	console.log("handleLookup: You are searching for: " + query);
	
	// TODO: if you want to check past query results first, you can do it here
	var curr_data = getCookie(query);
	if(curr_data != ""){
		console.log("Get data from cache");
		handleLookupAjaxSuccess(curr_data, query, doneCallback);
	} else {	
		// sending the HTTP GET request to the Java Servlet endpoint hero-suggestion
		// with the query data
		console.log("Get data from servlet");
		jQuery.ajax({
			method: "GET",
			url: "SearchQ?query=" + escape(query),
			"success": function(data) {
				//console.log(data);
				setCookie(query, data);
				handleLookupAjaxSuccess(data, query, doneCallback);
			},
			"error": function(errorData) {
				console.log("lookup ajax error");
				console.log(errorData);
			}
		})
	}
}

/*
 * This function is used to handle the ajax success callback function.
 * It is called by our own code upon the success of the AJAX request
 * 
 * data is the JSON data string you get from your Java Servlet
 * 
 */
function handleLookupAjaxSuccess(data, query, doneCallback) {
	console.log("lookup ajax successful");
	console.log(data);
	
	var jsonData = JSON.parse(data);
	console.log(jsonData)
	doneCallback( { suggestions: jsonData } );
}

/*
 * This function is the select suggestion handler function. 
 * When a suggestion is selected, this function is called by the library.
 * 
 * You can redirect to the page you want using the suggestion data.
 */
function handleSelectSuggestion(suggestion) {
	console.log("you select " + suggestion["value"])
	var method = suggestion["data"]["category"];
	
	console.log(method);
	
	if(method == "Movies"){
		var val = suggestion["data"]["heroID"];
		console.log("move to single movie" + val);
		window.location.href = "singlemovie.html?movieId=" + val;
	}else if(method == "Stars"){
		var val = suggestion["value"];
		console.log("move to single star" + val);
		window.location.href = "singlestar.html?starName=" + val;
	}
}

/*
 * This statement binds the autocomplete library with the input box element and 
 *   sets necessary parameters of the library.
 * 
 * The library documentation can be find here: 
 *   https://github.com/devbridge/jQuery-Autocomplete
 *   https://www.devbridge.com/sourcery/components/jquery-autocomplete/
 * 
 */
$('#autocomplete').autocomplete({
    lookup: function (query, doneCallback) {
    		handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
    		handleSelectSuggestion(suggestion)
    },
    minChars:3,
    delimiter: /(,|;)\s*/,
    showNoSuggestionNotice: true,
    noSuggestionNotice: 'No results found',
    groupBy: "category",
    deferRequestBy: 300,
});


/*
 * do normal full text search if no suggestion is selected 
 */
function handleNormalSearch(query) {
	console.log("doing normal search with query: " + query);
	//set cookie here
	
	var movieName = query;
	window.location.href = "browsingpage.html?movieTitle=" + movieName+"&year=" + "&director=" + "&star="+"&page=20"+"&range=1"+"&sort=title";
}

// bind pressing enter key to a handler function
$('#autocomplete').keypress(function(event) {
	if (event.keyCode == 13) {
		handleNormalSearch($('#autocomplete').val())
	}
})

// TODO: if you have a "search" button, you may want to bind the onClick event as well of that button
function searchButton(){
	var query = document.getElementById("autocomplete").value;
	console.log("click search Button: "+query);
	//checkCookie("PassedSearchData");
	handleNormalSearch(query);
}

function checkCookie(query){
	if(getCookie(query) == null){
		setCookie("PassedSearchData");
	}
	
}

// Set key 
function setCookie(cname, cvalue)
{
  var d = new Date();
  d.setTime(d.getTime()+(1*60*60*1000));
  var expires = "expires="+d.toGMTString();
  document.cookie = cname + "=" + cvalue + "; " + expires;
}

function delCookie(name)  {  
        var exp = new Date();  
        exp.setTime(exp.getTime() - 1);  
        var cval=getCookie(name);  
        if(cval!=null)  
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();  
} 

function getCookie(cname) {  
	var name = cname + "=";
	var ca = document.cookie.split(';');
	for(var i=0; i< ca.length; i++) 
	{
	    var c = ca[i].trim();
	    if (c.indexOf(name)==0) 
	    	return c.substring(name.length,c.length);
	}
	return "";
}  

