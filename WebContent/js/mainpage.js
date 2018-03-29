var xmlHttpRequest;
if (window.XMLHttpRequest){
	xmlHttpRequest=new XMLHttpRequest();
}else if (window.ActiveXObject){
	xmlHttpRequest=new ActiveXObject("Microsoft.XMLHTTP");
}


function handleStarResult(resultData) {
	console.log("handleStarResult: populating geners table from resultData");

	// populate the geners table
	var starTableBodyElement = jQuery("#gener_table_body");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<input type = button id = gener_choosed class = \"button small orange\" value = "+resultData[i]["geners"]+" onclick= handleGenerBrowse(this.value)>";
		starTableBodyElement.append(rowHTML);
	}
	
	var search_by_name = jQuery("#search_by_name");
	for(var i = 0 ; i < 10; i++){
		rowHTML = "";
		rowHTML += "<input type = button id = title_choosed class = \"button small blue\" value = "+i+" onclick= handleCharacterBrowse(this.value)>";
		starTableBodyElement.append(rowHTML);
	}
	for(var i = 0 ; i<26 ; i++){
		rowHTML = "";
		var ele = String.fromCharCode(65+i);
		rowHTML += "<input type = button id = title_choosed class = \"button small green\" value = "+ele+" onclick= handleCharacterBrowse(this.value)>" ;
		starTableBodyElement.append(rowHTML);
	}
}


function handleGenerBrowse(val){
	var genre = val;
	$.ajax({
		type: "POST",
		url:"/cs122b-Project2-FabFlix/SearchByGenres",
		data:{genre:genre},
		success: function(result){
			self.location="browsingpage.html?"+"page=20"+"&range=1"+"&sort=title"; 
		}
	});
}

function handleCharacterBrowse(val){
	var title = val;
	$.ajax({
		type: "POST",
		url:"/cs122b-Project2-FabFlix/SearchByTitle",
		data:{title:title},
		success: function(result){
			self.location="browsingpage.html?"+"page=20"+"&range=1"+"&sort=title"; 
		}
	});
}

// makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
	  dataType: "json",
	  method: "GET",
	  url: "/cs122b-Project2-FabFlix/MainPage",
	  success: (resultData) => handleStarResult(resultData),
	  
	  error : function(xhr, status, errMsg)
      {
           alert("fail!");
      }

});


