package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import cs122B.FabFlix.Model.Movies;
import cs122B.FabFlix.Util.JDBCUtil;

/**
 * Servlet implementation class HandleSession
 */
@WebServlet("/HandleSession")
public class HandleSession extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public HandleSession() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Servlet: HandleSession");
		response.setContentType("text/plain");
		
		String modifyType = request.getParameter("modifyType");
		System.out.println("modifyType"+ modifyType);
		Hashtable<String, Integer> sc = new Hashtable<String, Integer>();
		// find movie
		if (modifyType.equals("3")) {
			String movieId = request.getParameter("movieId");
			System.out.println("trans data: " + movieId);
			Movies m = new Movies();
			Connection conn = null;
			ResultSet rs = null;
			String sql = "select Idmain, title, year, director, rating, group_concat(distinct genres.name separator '; ') as genre, group_concat(distinct stars.name separator'; ') as star\r\n"
					+ "from (select movies.id as Idmain, movies.title, movies.year, movies.director, r.rating\r\n"
					+ "from movies , (select movieId, rating \r\n" + "from ratings)as r\r\n"
					+ "where movies.id = r.movieId and movies.id = '" + movieId + "'\r\n"
					+ ") as tempmovies, genres_in_movies, genres, stars_in_movies, stars\r\n"
					+ "where genres_in_movies.movieId = Idmain and genres.id = genres_in_movies.genreId and stars_in_movies.movieId = Idmain and stars.id = stars_in_movies.starId\r\n"
					+ "group by  Idmain, title, year, director, rating\r\n" + "order by rating desc;";

			try {
				conn = JDBCUtil.getConn();
				rs = JDBCUtil.excuteQuery(JDBCUtil.getStatement(conn), sql);

				JsonArray jsonArray = new JsonArray();
				while (rs.next()) {
					// out put a list of geners
					String id = rs.getString("Idmain");
					String title = rs.getString("title");
					String year = rs.getString("year");
					String director = rs.getString("director");
					String genre = rs.getString("genre");
					String star = rs.getString("star");

					System.out.println("add " + id + " " + title);

					m.setId(id);
					m.setTitle(title);
					m.setYear(year);
					m.setDirector(director);
					m.setGenres(genre.split(";"));
					m.setStars(star.split(";"));

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				JDBCUtil.closeResultSet(rs);
				JDBCUtil.closeConn(conn);
			}

			// add session
			

			if (request.getSession().getAttribute("shoppingChart") == null) {
				sc.put(m.getTitle(), 1);
				request.getSession().setAttribute("shoppingChart", sc);
			} else {
				sc = (Hashtable<String, Integer>) request.getSession().getAttribute("shoppingChart");
				// check if key is exist
				if (sc.containsKey(m.getTitle())) {
					int i = sc.get(m.getTitle());
					sc.put(m.getTitle(), i + 1);
				} else {
					sc.put(m.getTitle(), 1);
				}

				request.getSession().setAttribute("shoppingChart", sc);
			}

			PrintWriter out = response.getWriter();
			out.print("Hello" + movieId);
			out.close();
			response.getWriter().append("Served at: ").append(request.getContextPath());
		}else {
			String movieTitle = request.getParameter("movieTitle");
			sc = (Hashtable<String, Integer>) request.getSession().getAttribute("shoppingChart");
			System.out.println("title is "+movieTitle);
			int i  = sc.get(movieTitle);
			
			if(modifyType.equals("0")) {
				//delete key
				System.out.println("delete key: " + movieTitle);
				sc.remove(movieTitle);
			}else if(modifyType.equals("1")) {
				//-1
				
				if(i == 1) {
					System.out.println("delete "+ movieTitle + " to cart");
					sc.remove(movieTitle);
				}else {
					System.out.println("remove one  "+ movieTitle + " to cart");
					sc.put(movieTitle, i-1);
				}
			}else if(modifyType.equals("2")) {
				//+1
				System.out.println("add one  "+ movieTitle + " to cart");
				sc.put(movieTitle, i+1);
			}
			request.getSession().setAttribute("shoppingChart", sc);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Servlet: HandleSession");

	}

}
