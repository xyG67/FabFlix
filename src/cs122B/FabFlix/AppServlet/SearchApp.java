package cs122B.FabFlix.AppServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.jdbc.PreparedStatement;

import cs122B.FabFlix.Util.JDBCUtil;

/**
 * Servlet implementation class SearchApp
 */
@WebServlet("/SearchApp")
public class SearchApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchApp() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String movieTitle = request.getParameter("movieTitle");
		String search_year = request.getParameter("year");
		String search_director = request.getParameter("director");
		String search_star = request.getParameter("star");
		String page = request.getParameter("page");
		String range = request.getParameter("range");
		String sort = request.getParameter("sort");
		
		if(Integer.parseInt(range)<=0) range="1";
		int numberOfDataPerPage = Integer.parseInt(page);
		int pointerOfPage = Integer.parseInt(range);
		int upperBound = numberOfDataPerPage*pointerOfPage;
		int lowerBound = numberOfDataPerPage*pointerOfPage-numberOfDataPerPage;
		System.out.println(upperBound+ " - " + lowerBound);
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sql = "";
		
		try {
			conn = JDBCUtil.getConn();
			sql = "select Idmain as id, title, year, director, rating, group_concat(distinct genres.name separator '; ') as genres, group_concat(distinct stars.name separator'; ') as star "+
					"from (select movies.id as Idmain, movies.title, movies.year, movies.director, r.rating "
					+ "from movies , (select movieId, rating "
					+ " from ratings)as r "
					+ " where movies.id = r.movieId and (MATCH (title) AGAINST (? IN BOOLEAN MODE)) and (movies.year = ? or ? = '') and (movies.director like CONCAT('%', ?, '%') or ? = '')  "
					+ " order by " +sort+" desc "
					+ " limit "+lowerBound+", "+page+") as tempmovies, genres_in_movies, genres, stars_in_movies, stars "
					+ "where genres_in_movies.movieId = Idmain and genres.id = genres_in_movies.genreId and stars_in_movies.movieId = Idmain and stars.id = stars_in_movies.starId and (stars.name like CONCAT('%', ?, '%') or ? = '') "
					+ "group by  Idmain, title, year, director, rating "
					+ " order by "+sort+" desc";
			//System.out.println(sql);
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
				pst.setString(1, "+"+movieTitle+"*");
			pst.setString(2, search_year);
			pst.setString(3, search_year);
			pst.setString(4, search_director);
			pst.setString(5, search_director);
			pst.setString(6, search_star);
			pst.setString(7, search_star);
		    System.out.println(pst.toString());
		    
			rs = pst.executeQuery();
			
			JsonArray jsonArray = new JsonArray();
			int j = 0;
			while (rs.next()) {
				j++;
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
			System.out.println("You got "+ j);
			out.write(jsonArray.toString());
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeConn(conn);
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
