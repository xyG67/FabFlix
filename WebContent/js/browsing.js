thisURL = document.URL; 

function handleStarResult(resultData) {
	console.log("handleStarResult: populating search result from resultData");
	
	// populate the search result
	var starTableBodyElement = jQuery("#search_movie_result");
	for (var i = 0; i < resultData.length; i++) {
		var rowHTML = "";
		rowHTML += "<tr class = row >";
		rowHTML += "<th class = cell id = "+ resultData[i]["id"] +" onClick = singleMovie(this.id)>" +resultData[i]["title"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["year"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["director"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["genre"] + "</th>";
		rowHTML += "<th class = cell>" +resultData[i]["star"] + "</th>";
		rowHTML += "<th class = cell id = "+ resultData[i]["id"] +" onClick = addChart(this.id)>" +"add to chart" + "</th>";
		rowHTML += "</tr>"
		starTableBodyElement.append(rowHTML);
	}
}
/*
 * view detail of single movie by click at movie title
 */
function singleMovie(val){
    window.location.href = "singlemovie.html?movieId=" + val;
}
/*
 * add one movie to chart by click add to chart
 */
function addChart(val){
	//alert(val);
	jQuery.ajax({
		type: 'GET',
		url:"/cs122b-Project2-FabFlix/HandleSession",
		data:{movieId:val,modifyType:3},
		success: function(result){
			alert("add movie to cart successfully!");
		},
		error : function(error) {  
			alert("fail to add chart");
        } 
	});
}
/*
 * Page number -1
 */
function prevPage(){
	jQuery.ajax({
		type: 'GET',
		url:"/cs122b-Project2-FabFlix/SetSortAndPageSession",
		data:{showList:'1'},
		success: function(result){
		},
	});
	window.location.replace("browsingpage.html");
}
/*
 *Page number +1 
 */
function nextPage(){
	jQuery.ajax({
		type: 'GET',
		url:"/cs122b-Project2-FabFlix/SetSortAndPageSession",
		data:{showList:'2'},
		success: function(result){
		},
	});
	window.location.replace("browsingpage.html");
}
/*
 * Change the total number of movie in one page
 */
function changePage(){
	var numberOnePage = document.getElementById("numberOnePage").value
	jQuery.ajax({
		type: 'GET',
		url:"/cs122b-Project2-FabFlix/SetSortAndPageSession",
		data:{showList:'3',newPageNum:numberOnePage},
		success: function(result){
		},
	});
	window.location.replace("browsingpage.html");
}
/*
 * Sort by title
 */
function sortByTitle(){
	jQuery.ajax({
		type: 'GET',
		url:"/cs122b-Project2-FabFlix/SetSortAndPageSession",
		data:{showList:'4'},
		success: function(result){
		},
	});
	window.location.replace("browsingpage.html");
}
/*
 * Sort by year
 */
function sortByYear(){
	jQuery.ajax({
		type: 'GET',
		url:"/cs122b-Project2-FabFlix/SetSortAndPageSession",
		data:{showList:'5'},
		success: function(result){
		},
	});
	window.location.replace("browsingpage.html");
}
/*
 * initize the webpage
 */
$().ready(function(){
    var URL = document.location.toString(); // 获取带参URL
    var para = URL.split("?");

    $.ajax({
    	dataType: "json",
  	  	method: "GET",
  	  	url: "/cs122b-Project2-FabFlix/Browsing?" + para[1],
  	  	
  	  	success: (resultData) => handleStarResult(resultData),
  	  
  	  	error : function(xhr, status, errMsg)
        {
             alert("fail!");
        }
	});
    
});

