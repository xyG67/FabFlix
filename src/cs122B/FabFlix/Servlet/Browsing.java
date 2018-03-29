package cs122B.FabFlix.Servlet;



import java.io.IOException;

import java.io.PrintWriter;

import java.sql.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;

import com.google.gson.JsonObject;

import cs122B.FabFlix.Util.JDBCUtil;



/**

 * Servlet implementation class Browsing

 */

@WebServlet("/Browsing")

public class Browsing extends HttpServlet {

	private static final long serialVersionUID = 1L;

    public Browsing() {
        super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//Position
		System.out.println("Servlet: Browsing");
		//Initialize
		Connection conn = null;
		ResultSet rs = null;
		String sql = "";
		//Check if searching by advanced search
		String movieTitle = request.getParameter("movieTitle");
		String search_year = request.getParameter("year");
		String search_director = request.getParameter("director");
		String search_star = request.getParameter("star");
		//control session
		String page = (String) request.getSession().getAttribute("page");
		String range = (String) request.getSession().getAttribute("range");
		String sort = (String) request.getSession().getAttribute("sort");
		//System.out.println("number: "+page+ " now pages "+ range + " sort: "+ sort);
		if(page == null) {	page = "20"; }
		if(range == null) {	range = "1"; }
		if(sort == null) { 	sort = "title";	}
		
		int upperBound = Integer.parseInt(page)*(Integer.parseInt(range));
		int lowerBound = Integer.parseInt(page)*(Integer.parseInt(range)-1);

		System.out.println(upperBound+ " - " + lowerBound);

		if(sort.equals("title"))sort = "movies.title";
		else sort = "movies.year";

		PrintWriter out = response.getWriter();	
		
		//test time
		long startTime = System.nanoTime();
		try {
			//conn = JDBCUtil.getConn();
			//
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            conn = ds.getConnection();

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		//System.out.println("movie title is: "+movieTitle + " year " + search_year);

		if (movieTitle == null&& search_year==null && search_director==null && search_star == null) {
			//Check browsing type
			System.out.println("Search by browsing");
			String[] searching = (String[]) request.getSession().getAttribute("searching");
			System.out.println(searching[0]+": "+searching[1]);
			String trigger = searching[0];
			if(trigger.equals("genre")) {   //search by genres
				sql = "select movies.id, movies.title, movies.year, movies.director, genres, group_concat(distinct stars separator'; ') as star\r\n" + 
						"from (SELECT genres.name as genres, genreId, genres_in_movies.movieId, stars.name as stars\r\n" + 
						"FROM moviedb.genres, genres_in_movies, stars_in_movies, stars\r\n" + 
						"WHERE genres.name = '"+searching[1]+"' and genres_in_movies.genreId = genres.id and genres_in_movies.movieId = stars_in_movies.movieId and stars.id = stars_in_movies.starId) as r, movies\r\n" + 
						"where r.movieId = movies.id\r\n" + 
						"group by movies.title, movies.id"+" order by "+sort+" limit "+lowerBound+", "+upperBound;
				System.out.println(sql);
				
				rs = JDBCUtil.excuteQuery(JDBCUtil.getStatement(conn), sql);
			}else if(trigger.equals("title")) {	//search by title
				sql = "select Idmain as id, title, year, director, rating, group_concat(distinct genres.name separator '; ') as genres, group_concat(distinct stars.name separator'; ') as star\r\n" + 
						"from (select movies.id as Idmain, movies.title, movies.year, movies.director, r.rating\r\n" + 
						"from movies , (select movieId, rating \r\n" + 
						"from ratings)as r\r\n" + 
						"where movies.id = r.movieId and movies.title like '"+searching[1]+"%'\r\n" + 
						"order by "+sort+" desc\r\n" + 
						"limit "+lowerBound+", "+upperBound+") as tempmovies, genres_in_movies, genres, stars_in_movies, stars\r\n" + 
						"where genres_in_movies.movieId = Idmain and genres.id = genres_in_movies.genreId and stars_in_movies.movieId = Idmain and stars.id = stars_in_movies.starId\r\n" + 
						"group by  Idmain, title, year, director, rating\r\n";
				
				
				rs = JDBCUtil.excuteQuery(JDBCUtil.getStatement(conn), sql);		
			}
		}else {	//search by detail
			System.out.println("Search by detail");
			PreparedStatement pst = null;
			System.out.println("movie title is: "+movieTitle + " year " + search_year +" director: "+ search_director + " star: "+ search_star);
			if(sort.equals("movies.title")) {
				sort = "title";
			}else {
				sort = "year";
			}
//			sql = "select Idmain as id, title, year, director, rating, group_concat(distinct genres.name separator '; ') as genres, group_concat(distinct stars.name separator'; ') as star "+
//					"from (select movies.id as Idmain, movies.title, movies.year, movies.director, r.rating "
//					+ "from movies , (select movieId, rating "
//					+ " from ratings)as r "
//					+ " where movies.id = r.movieId and (movies.title like CONCAT('%', ?, '%') or ? = '') and (movies.year = ? or ? = '') and (movies.director like CONCAT('%', ?, '%') or ? = '')  "
//					+ " order by " +sort+" desc "
//					+ " limit "+lowerBound+", "+upperBound+") as tempmovies, genres_in_movies, genres, stars_in_movies, stars "
//					+ "where genres_in_movies.movieId = Idmain and genres.id = genres_in_movies.genreId and stars_in_movies.movieId = Idmain and stars.id = stars_in_movies.starId and (stars.name like CONCAT('%', ?, '%') or ? = '') "
//					+ "group by  Idmain, title, year, director, rating "
//					+ " order by "+sort+" desc";
			
			sql = "select Idmain as id, title, year, director, rating, group_concat(distinct genres.name separator '; ') as genres, group_concat(distinct stars.name separator'; ') as star "+
					"from (select movies.id as Idmain, movies.title, movies.year, movies.director, r.rating "
					+ "from movies , (select movieId, rating "
					+ " from ratings)as r "
					+ " where movies.id = r.movieId and (MATCH (title) AGAINST (? IN BOOLEAN MODE)) and (movies.year = ? or ? = '') and (movies.director like CONCAT('%', ?, '%') or ? = '')  "
					+ " order by " +sort+" desc "
					+ " limit "+lowerBound+", "+upperBound+") as tempmovies, genres_in_movies, genres, stars_in_movies, stars "
					+ "where genres_in_movies.movieId = Idmain and genres.id = genres_in_movies.genreId and stars_in_movies.movieId = Idmain and stars.id = stars_in_movies.starId and (stars.name like CONCAT('%', ?, '%') or ? = '') "
					+ "group by  Idmain, title, year, director, rating "
					+ " order by "+sort+" desc";
			//search by detail		
			try {
				pst = (PreparedStatement) conn.prepareStatement(sql);
				if(movieTitle.contains(" ")) {
					String[] curr = movieTitle.split(" ");
					String newMovie = "";
					for(String temp:curr) {
						if(temp.equals("of")||temp.equals("the")||temp.equals("an"))
							continue;
						newMovie += "+"+temp+"* ";
					}
					pst.setString(1, newMovie);
				}else
					pst.setString(1, movieTitle);
				pst.setString(2, search_year);
				pst.setString(3, search_year);
				pst.setString(4, search_director);
				pst.setString(5, search_director);
				pst.setString(6, search_star);
				pst.setString(7, search_star);
			    System.out.println(pst.toString());
				rs = pst.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			JsonArray jsonArray = new JsonArray();
			while (rs.next()) {
				String id = rs.getString("id");
				String title = rs.getString("title");
				String year = rs.getString("year");
				String director = rs.getString("director");
				String genre = rs.getString("genres");
				String star = rs.getString("star");

				JsonObject jsonObject = new JsonObject();

				jsonObject.addProperty("id", id);
				jsonObject.addProperty("title", title);
				jsonObject.addProperty("year", year);
				jsonObject.addProperty("director", director);
				jsonObject.addProperty("genre", genre);
				jsonObject.addProperty("star", star);
				jsonArray.add(jsonObject);
			}
			long endTime = System.nanoTime();
			long elapsedTime = endTime - startTime;
			System.out.println(elapsedTime);
			out.write(jsonArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeConn(conn);
		}
		out.close();

	}



	/**

	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)

	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}



}