package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
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
 * Servlet implementation class SingleStar
 */
@WebServlet("/SingleStar")
public class SingleStar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleStar() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Servlet: Single Star");
		String starName = request.getParameter("starName");
		
		
		String[] searching = new String[2];
		searching[0] = "star";
		searching[1] = starName;
		request.getSession().setAttribute("single", starName);
		
		
		String sql = "SELECT distinct stars.name, stars.birthYear, movies.title, movies.year, movies.director, movies.id\r\n" + 
				"FROM stars, stars_in_movies, movies\r\n" + 
				"where stars.name = '"+ starName +"' and stars.id = stars_in_movies.starId and movieId = stars_in_movies.movieId "
						+ "limit 0,20";
		
		sql = "SELECT stars.name, stars.birthYear, movies.title, movies.year, movies.director, movies.id\n" + 
				"FROM moviedb.stars_in_movies natural join stars, movies\n" + 
				"where stars.id = stars_in_movies.starId and stars.name = '"+ starName +"' and movies.id = stars_in_movies.movieId\n"
				+ "limit 0,20";
		Connection conn = null;
		ResultSet rs = null;
		System.out.println(sql);
		PrintWriter out = response.getWriter();		
		
		try {
			//conn = JDBCUtil.getConn();
			//load "findGeners"
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            conn = ds.getConnection();
			rs = JDBCUtil.excuteQuery(JDBCUtil.getStatement(conn), sql);
			
			JsonArray jsonArray = new JsonArray();
			while (rs.next()) {
				//out put a list of geners
				String movieId = rs.getString("id");
				String star_Name = rs.getString("name");
				String birthYear = rs.getString("birthYear");
				String title = rs.getString("title");
				String year = rs.getString("year");
				String director = rs.getString("director");
				System.out.println(title);
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("name", star_Name);
				jsonObject.addProperty("title", title);
				jsonObject.addProperty("year", year);
				jsonObject.addProperty("director", director);
				jsonObject.addProperty("birthYear", birthYear);	
				jsonObject.addProperty("movieId", movieId);	
				
				jsonArray.add(jsonObject);
			}
			
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
