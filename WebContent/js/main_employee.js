function add_new_star(){
		var star_name = document.getElementById("star_name").value;
		var birthyear = document.getElementById("birth_year").value;
		//alert(star_name);
		jQuery.ajax({
			dataType: "json",
			type: "GET",
			url:"/cs122b-Project2-FabFlix/Main_Employee",
			data:{star_name:star_name,birthyear:birthyear,methodNumber:"1"},
			success: (resultData) => handleStarDataResult(resultData),
			error : function(xhr, status, errMsg)
	        {
	             alert("fail!");
	        }
		});
}

function show_metadata(){
	//alert(star_name);
	jQuery.ajax({
	    	dataType: "json",
	  	  	method: "GET",
	  	  	url: "/cs122b-Project2-FabFlix/Main_Employee",
	  	    data:{methodNumber:"2"},
	  	  	success: (resultData) => handleMetaDataResult(resultData),
	  	  	error : function(xhr, status, errMsg)
	        {
	             alert("fail!");
	        }
		});
}

function add_new_movie(){
	var movie_title = document.getElementById("movie_title").value;
	var movie_year = document.getElementById("movie_year").value;
	var movie_director = document.getElementById("movie_director").value;
	var movie_star = document.getElementById("movie_star").value;
	var movie_genre = document.getElementById("movie_genre").value;
	jQuery.ajax({
    	dataType: "json",
  	  	method: "GET",
  	  	url: "/cs122b-Project2-FabFlix/Main_Employee",
  	    data:{methodNumber:"3", 
  	    	  movie_title:movie_title,
  	    	  movie_year:movie_year,
  	    	  movie_director:movie_director,
  	    	  movie_star:movie_star,
  	    	  movie_genre:movie_genre},
  	  	success: (resultData) => handleStarDataResult(resultData),
  	  	error : function(xhr, status, errMsg)
        {
             alert("fail!");
        }
	});
}

function handleMetaDataResult(resultData){
	console.log("handleStarResult: populating search result from resultData");
	// populate the search result
	$("#show_result").empty();
	var starTableBodyElement = jQuery("#show_result");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr class = row >";
		rowHTML += "<th class = cell>" +resultData[i]["TABLE_NAME"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["COLUMN_NAME"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["IS_NULLABLE"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["DATA_TYPE"] + "</th>";
		rowHTML += "</tr>"
		starTableBodyElement.append(rowHTML);
	}
}

function handleStarDataResult(resultData){
	console.log("handleStarResult: populating search result from resultData");
	// populate the search result
	$("#show_result").empty();
	var starTableBodyElement = jQuery("#show_result");
	var rowHTML = "";
	rowHTML += "<tr class = row >";
	rowHTML = "<th class = cell>" +resultData[0]["OUTPUT"]+ "</th>";
	rowHTML += "</tr>"
	starTableBodyElement.append(rowHTML);
	
}

function handleParseDataResult(resultData){
	console.log("handleStarResult: populating search result from resultData");
	// populate the search result
	$("#show_result").empty();
	var starTableBodyElement = jQuery("#show_result");
	var rowHTML = "";
	rowHTML += "<tr class = row >";
	rowHTML = "<th class = cell>" +resultData[0]["OUTPUT"]+ "</th>";
	rowHTML += "</tr>"
	starTableBodyElement.append(rowHTML);
	for (var i = 0; i < resultData.length-1; i++) {
		rowHTML += "<tr class = row >";
		rowHTML += "<th class = cell>" +resultData[i]["message"] + "</th>";
		rowHTML += "</tr>"
		starTableBodyElement.append(rowHTML);
	}	
}

function parseHandler(id){
	jQuery.ajax({
    	dataType: "json",
  	  	method: "GET",
  	  	url: "/cs122b-Project2-FabFlix/HandlerParser",
  	    data:{methodNumber:id},
  	    success: (resultData) => handleStarDataResult(resultData),
	  	error : function(xhr, status, errMsg)
      {
           alert("fail!");
      }
	});
}