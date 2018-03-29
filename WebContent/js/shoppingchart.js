function handleStarResult(resultData) {
	console.log("handleStarResult: populating geners table from resultData");

	// populate the geners table
	var starTableBodyElement = jQuery("#shopping_history");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr>";
		rowHTML += "<td class = gener_choosed>"+resultData[i]["title"]+"</td>";
		rowHTML += "<td>"+ "<input type = button class = shoppingbutton id = \""+resultData[i]["title"]+"\" value = \"-\" onclick= modifyData(this.id,\"1\")>" +"</td>";
		rowHTML += "<td id = gener_choosed >"+resultData[i]["number"]+"</td>";
		rowHTML += "<td>"+ "<input type = button class = shoppingbutton id = \""+resultData[i]["title"]+"\" value = \"+\" onclick= modifyData(this.id,\"2\")>" +"</td>";
		rowHTML += "<td>"+ "<input type = button class = shoppingbutton id = \""+resultData[i]["title"]+"\" value = delete onclick= modifyData(this.id,\"0\")>" +"</td>";
		rowHTML += "</tr>";
		starTableBodyElement.append(rowHTML);
	}
}

function modifyData(val, int){
	//alert(val);
	var nval = val.replace(" ","+");
	
//	alert(int);
	jQuery.ajax({
		type: 'GET',
		url:"/cs122b-Project2-FabFlix/HandleSession",
		data:{movieTitle:val,modifyType:int},
		success: function(result){
			alert("add movie" + nval);
		},
		error : function(error) {  
			alert("fail to add chart");
        } 
	});
	
	window.location.replace("shoppingchart.html");
}

function jump(){
	window.location.replace("payinfo.html");
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