function handleStarResult(resultData) {
	console.log("handleStarResult: populating search result from resultData");

	// populate the search result
	var starTableBodyElement = jQuery("#single_star_movie");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr class = row id = "+ resultData[i]["movieId"] +" onClick = jumpToMovie(this.id)>";
		rowHTML += "<th class = cell>" +resultData[i]["title"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["year"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["director"] + "</th>";
		//rowHTML += "<th class = cell>" +resultData[i]["star"] + "</th>";
		rowHTML += "</tr>"
		starTableBodyElement.append(rowHTML);
	}
}

function addChart(val){
	//alert(val);
	jQuery.ajax({
		type: 'GET',
		url:"/cs122b-Project2-FabFlix/HandleSession",
		data:{movieId:val,modifyType:3},
		success: function(result){
			alert("add movie" + val);
		},
		error : function(error) {  
			alert("fail to add chart");
        } 
	});
}

function jumpToMovie(val){
	window.location.href = "singlemovie.html?movieId=" + val;
}

$(document).ready(function(){
    var URL = document.location.toString(); // 获取带参URL
    var para = URL.split("?");
    $.ajax({
    	dataType: "json",
  	  	method: "GET",
  	  	url: "/cs122b-Project2-FabFlix/SingleStar?" + para[1],
  	  	success: (resultData) => handleStarResult(resultData),
  	  	error : function(xhr, status, errMsg)
        {
             alert("fail!");
        }
	});
});