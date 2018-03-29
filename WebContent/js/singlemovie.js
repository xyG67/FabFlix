function handleStarResult(resultData) {
	console.log("handleStarResult: populating search result from resultData");

	// populate the search result
	var starTableBodyElement = jQuery("#single_movie");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr class = row id = "+ resultData[i]["id"] +" >";
		rowHTML += "<th class = cell >" +resultData[i]["title"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["year"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["director"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["genre"] + "</th>";
		rowHTML += "<th class = cell id = "+ resultData[i]["id"] +" onClick = addChart(this.id)>" +"add to chart" + "</th>";
		//rowHTML += "<th class = cell>" +resultData[i]["star"] + "</th>";
		rowHTML += "</tr>"
		starTableBodyElement.append(rowHTML);
		
		
		var starTableBodyElement = jQuery("#single_movie_stars");
		for (var i = 0; i < resultData.length; i++) {
			var rowHTML = "";
			//rowHTML += "<th class = cell>" +resultData[i]["star"] + "</th>";
			rowHTML += "</tr>"
			rowHTML += "<tr class = row >";
			rowHTML += "<th class = cell colspan = 5>" +"stars"+ "</th>";
			rowHTML += "</tr>"
			rowHTML += "<tr class = row>";
			var j = resultData[i]["star_num"];
			for(var h = 0 ; h<j;h++){
				rowHTML += "<tr class = row>"
				rowHTML += "<th class = cell colspan = 5 id = \""+resultData[i][h.toString()]+"\" onClick = jumpToStar(this.id)>" +resultData[i][h.toString()] + "</th>";
				rowHTML += "</tr>"
			}
			
			rowHTML += "</tr>"
			starTableBodyElement.append(rowHTML);
		}
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

function jumpToStar(val){
	window.location.href = "singlestar.html?starName=" + val;
}

$(document).ready(function(){
    var URL = document.location.toString(); // 获取带参URL
    var para = URL.split("?");
    $.ajax({
    	dataType: "json",
  	  	method: "GET",
  	  	url: "/cs122b-Project2-FabFlix/SingleMovie?" + para[1],
  	  	success: (resultData) => handleStarResult(resultData),
  	  
  	  	error : function(xhr, status, errMsg)
        {
             alert("fail!");
        }
	});

});
