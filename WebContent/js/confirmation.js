
function handleStarResult(resultData) {
	console.log("handleStarResult: populating geners table from resultData");

	// populate the geners table
	var starTableBodyElement = jQuery("#shopping_history");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr>";
		rowHTML += "<td id = gener_choosed>"+resultData[i]["title"]+"</td>";
		rowHTML += "<td id = gener_choosed >"+resultData[i]["number"]+"</td>";
		rowHTML += "</tr>";
		starTableBodyElement.append(rowHTML);
	}
}

jQuery.ajax({
	  dataType: "json",
	  method: "GET",
	  url: "/cs122b-Project2-FabFlix/ShoppingChart",
	  success: (resultData) => handleStarResult(resultData),
	  
	  error : function(xhr, status, errMsg)
      {
           alert("fail!");
      }

});