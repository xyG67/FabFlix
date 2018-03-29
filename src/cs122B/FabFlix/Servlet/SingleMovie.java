package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
 * Servlet implementation class SingleMovie
 */
@WebServlet("/SingleMovie")
public class SingleMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleMovie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Servlet: Single Movie");
		String MovieId = request.getParameter("movieId");
		
		String[] searching = new String[2];
		searching[0] = "movie";
		searching[1] = MovieId;
		request.getSession().setAttribute("single", searching);
		
		
//		String sql = "select Idmain, title, year, director, rating, group_concat(distinct genres.name separator '; ') as genre, group_concat(distinct stars.name separator'; ') as star\r\n" + 
//				"from (select movies.id as Idmain, movies.title, movies.year, movies.director, r.rating\r\n" + 
//				"from movies , (select movieId, rating \r\n" + 
//				"from ratings)as r\r\n" + 
//				"where movies.id = r.movieId and movies.id = '"+ MovieId +"'\r\n" + 
//				") as tempmovies, genres_in_movies, genres, stars_in_movies, stars\r\n" + 
//				"where genres_in_movies.movieId = Idmain and genres.id = genres_in_movies.genreId and stars_in_movies.movieId = Idmain and stars.id = stars_in_movies.starId\r\n" + 
//				"group by  Idmain, title, year, director, rating\r\n" + 
//				"order by rating desc;";
		
		Connection conn = null;
		ResultSet rs = null;
		PrintWriter out = response.getWriter();		
		JsonArray jsonArray = new JsonArray();
		
		try {
			//-------------------change------------------
			
			long startTime = System.nanoTime();

			Context initCtx = new InitialContext();
            if (initCtx == null)
                out.println("initCtx is NULL");

            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            if (envCtx == null)
                out.println("envCtx is NULL");

            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            if (ds == null)
                out.println("ds is null.");
            
            conn = ds.getConnection();
            if (conn == null)
                out.println("dbcon is null.");
            
            
            String sql = "select Idmain, title, year, director, rating, group_concat(distinct genres.name separator '; ') as genre, group_concat(distinct stars.name separator'; ') as star\r\n" + 
    				"from (select movies.id as Idmain, movies.title, movies.year, movies.director, r.rating\r\n" + 
    				"from movies , (select movieId, rating \r\n" + 
    				"from ratings)as r\r\n" + 
    				"where movies.id = r.movieId and movies.id = ?\r\n" + 
    				") as tempmovies, genres_in_movies, genres, stars_in_movies, stars\r\n" + 
    				"where genres_in_movies.movieId = Idmain and genres.id = genres_in_movies.genreId and stars_in_movies.movieId = Idmain and stars.id = stars_in_movies.starId\r\n" + 
    				"group by  Idmain, title, year, director, rating\r\n" + 
    				"order by rating desc;";
            
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, MovieId);

            // Perform the query
            rs = psmt.executeQuery();
			
			//-------------------change------------------
			//conn = JDBCUtil.getConn();
			//load "findGeners"
			
			//rs = JDBCUtil.excuteQuery(JDBCUtil.getStatement(conn), sql);
			
			while (rs.next()) {
				//out put a list of geners
				String movieId = rs.getString("Idmain");
				String title = rs.getString("title");
				String year = rs.getString("year");
				String director = rs.getString("director");
				String genre = rs.getString("genre");
				String star = rs.getString("star");
				
				String[] stars = star.split("; ");
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("id", movieId);
				jsonObject.addProperty("title", title);
				jsonObject.addProperty("year", year);
				jsonObject.addProperty("director", director);
				jsonObject.addProperty("genre", genre);
				int star_num = stars.length;
				jsonObject.addProperty("star_num", Integer.toString(star_num));
				for(int i = 0; i<star_num ; i++){
					String one_star = stars[i];
					jsonObject.addProperty(Integer.toString(i),one_star);
					System.out.println(Integer.toString(i)+" "+ one_star);
				}
				
				
				//jsonObject.addProperty("star", stars);
				
				jsonArray.add(jsonObject);
			}
			long endTime = System.nanoTime();
			long elapsedTime = endTime - startTime; // elapsed time in nano seconds. Note: print the values in nano seconds 

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
