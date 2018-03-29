function adSearch2(){
	
	var search_title = document.getElementById("title").value;
	var search_year = document.getElementById("year").value;
	var search_director = document.getElementById("director").value;
	var search_star = document.getElementById("star").value;
	window.location.href = "browsingpage.html?movieTitle=" + search_title+"&year=" + search_year + "&director=" + search_director + "&star="+ search_star+"&page=20"+"&range=1"+"&sort=title";
}

function test(){
	alert(1);
}