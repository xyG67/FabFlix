package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;

import cs122B.FabFlix.Model.Movies;
import cs122B.FabFlix.Model.Stars;
import cs122B.FabFlix.Util.MainDAO;
import cs122B.FabFlix.Util.SAXParserActorHelper;
import cs122B.FabFlix.Util.SAXParserMoviesHelper;
import cs122B.FabFlix.Util.SAXParserStarsHelper;

/**
 * Servlet implementation class HandlerParser
 */
@WebServlet("/HandlerParser")
public class HandlerParser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HandlerParser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("rawtypes")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String methodNumber = request.getParameter("methodNumber");
		String user = "root";
		String password = "1234";
		String URL = "jdbc:mysql://localhost:3306/moviedb?useSSL=false";
		
		XMLReader p;
		try {
			p = XMLReaderFactory.createXMLReader();
		
			if(methodNumber.equals("1")) {
				//parse actors63
				SAXParserActorHelper saxParserActor = new SAXParserActorHelper();
				p.setContentHandler(saxParserActor);
				//System.out.println(this.getServletContext().getRealPath(request.getRequestURI()));
				//String path = request.getContextPath();
				
				//System.out.println(Paths.get(".").toRealPath());
				String path = "E:\\JAVA\\workspace2.0\\test\\src\\test\\xml\\actors63.xml";
				path = "/home/download/xml/actors63.xml";
				p.parse(path);
				HashSet<Stars> actor_list = saxParserActor.getActor_list();
				HashSet<Stars> error_list = new HashSet<Stars>();
				String actor_name = null;
				String actor_birthyear = null;
				String sql = "{call add_actors63(?,?)}";
				try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					Connection conn = (Connection) java.sql.DriverManager.getConnection(URL, user, password);
					conn.setAutoCommit(false);
					CallableStatement state  = null;	
					long startTime=System.currentTimeMillis();
					state = (CallableStatement) conn.prepareCall(sql);
					int i = 0;
					
					for(Stars s:actor_list) {
						actor_name = s.getName();
						actor_birthyear = s.getBirthyear();
					      
					    if(actor_name.equals("NULL")) {
					    	error_list.add(s);
					    }else if(actor_birthyear.equals("0")) {
					    	error_list.add(s);
					    }
					    state.setString(1, actor_name);
					    state.setString(2, actor_birthyear);
					    state.addBatch();
					    if(i%1000==0&&i!=0) {
					    	state.executeBatch();
					    	state.clearBatch();
					    	i=0;
					    }
					    i++;
					}
					if(i!=0) {
						state.executeBatch();
					}
					state.close();
					conn.close();
					long endTime=System.currentTimeMillis();
					System.out.println("Total time: "+(startTime-endTime));
					System.out.println("Total cost: "+actor_list.size());
					
				} catch (Exception e1) {
					System.out.println(e1);
				}
				
				PrintWriter out = response.getWriter();	
			    JsonArray jsonArray = new JsonArray();
			    JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("OUTPUT", "Parsing actor63.xml successfully");
				jsonArray.add(jsonObject);
				for(Stars e : error_list) {
					JsonObject jsonObject2 = new JsonObject();
					String message = e.getName()+e.getBirthyear();
					jsonObject2.addProperty("message", message);
					jsonArray.add(jsonObject2);
				}
				out.write(jsonArray.toString());
				return;
				//6863 actors 		6858	
		}else if(methodNumber.equals("2")) {
				//parse casts124
			SAXParserStarsHelper saxParserStar = new SAXParserStarsHelper();
			p.setContentHandler(saxParserStar);
			String path = "E:\\JAVA\\workspace2.0\\test\\src\\test\\xml\\casts124.xml";
			path = "/home/download/xml/casts124.xml";
			p.parse(path);
			ArrayList<String[]> star_list = saxParserStar.getList();
			ArrayList<String[]> error_list = saxParserStar.getList();
			String movie = null;
			String star = null;
				
			Connection conn = null;
			CallableStatement state  = null;
			String sql = "{call add_cast124_fast(?,?)}";
			int j = 0;
			
			try {
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					conn = (Connection) java.sql.DriverManager.getConnection(URL, user, password);
					conn.setAutoCommit(false);
					state = (CallableStatement) conn.prepareCall(sql);
					long startTime=System.currentTimeMillis();
					for(int i = 0; i<star_list.size();i++) {
						String[] s = star_list.get(i);
						movie = s[0];
						star = s[1];
						//System.out.println(movie +" "+star);
						if(movie.equals("NULL")||star.equals("NULL")) {
							error_list.add(s);
						}
						
						state.setString(1, movie);
						state.setString(2, star);
						//state.registerOutParameter(3,Types.VARCHAR);
						//state.executeUpdate();
						state.addBatch();
						//String output = state.getString(3);

						if(j % 1000 == 0 && j != 0) {
							state.executeBatch();
							state.clearBatch();
							j=0;
						}
						j++;
					}
					if(j!=0) {
						state.executeBatch();
					}
					state.close();
					conn.close();
					long endTime=System.currentTimeMillis();
					System.out.println("Total time: "+(startTime-endTime));
					System.out.println("Total cost: "+star_list.size());
					
					PrintWriter out = response.getWriter();	
				    JsonArray jsonArray = new JsonArray();
				    JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("OUTPUT", "Parsing casts124.xml successfully");
					
					for(String[] e : error_list) {
						String message = e[0] + " " + e[1];
						jsonObject.addProperty("message", message);
						jsonArray.add(jsonObject);
					}
					
					out.write(jsonArray.toString());
				} 
			catch (Exception e) {
					e.printStackTrace();
			}
			return;	
		}else if(methodNumber.equals("3")) {
				//parse mains243
			SAXParserMoviesHelper saxParserMovie = new SAXParserMoviesHelper();
			p.setContentHandler(saxParserMovie);
			String path = "E:\\JAVA\\workspace2.0\\test\\src\\test\\xml\\mains243.xml";
			path = "/home/download/xml/mains243.xml";
			p.parse(path);
			
			HashSet<Movies> movie_list = saxParserMovie.getMoviesList();
			HashSet<Movies> error_list = new HashSet<Movies>();
			
			String movie_title = null;
			int movie_year;
			String movie_direc = null;
			String movie_genre = null;
			//insert movie and genre
			Connection conn = null;
			CallableStatement state = null;
			String sql = "{call add_main243_fast(?,?,?,?)}";				
			
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				conn = (Connection) java.sql.DriverManager.getConnection(URL, user, password);
				state = (CallableStatement) conn.prepareCall(sql);
				conn.setAutoCommit(false);
				long startTime=System.currentTimeMillis();
				int i = 0;
				for(Movies m:movie_list) {
					//System.out.print(m.getTitle()+" "+m.getYear()+" "+m.getDirector()+" ");
					movie_title = m.getTitle();
					movie_year = Integer.parseInt(m.getYear());
					movie_direc = m.getDirector();
					String[] curr = m.getGenres();
					movie_genre = curr[0];	
					if(movie_direc == null || movie_direc.equals("NULL")) {
						movie_direc = "NULL";
						error_list.add(m);
						//continue;
					}else if(movie_genre.equals("null")||movie_genre.equals("NULL")){
						movie_genre = "NULL";
						error_list.add(m);
						//continue;
					}
					state.setString(1, movie_title);
					state.setInt(2, movie_year);
					state.setString(3, movie_direc);
					state.setString(4, movie_genre);
					//state.executeUpdate();
					//String output = state.getString(5);
					//System.out.println(output);
					state.addBatch();
					if(i%1000==0 || i>0) {
						state.executeBatch();
						state.clearBatch();
						i=0;
					}
					i++;
				}
					if(i!=0) {
						state.executeBatch();
					}
					long endTime=System.currentTimeMillis();
					System.out.println("Total time: "+(startTime-endTime));
					System.out.println("Total insert: "+ movie_list.size());
					state.clearBatch();
					state.close();
					conn.close();
					
					PrintWriter out = response.getWriter();	
				    JsonArray jsonArray = new JsonArray();
				    JsonObject jsonObject = new JsonObject();
					jsonObject.addProperty("OUTPUT", "Parsing mains243.xml successfully");
					jsonArray.add(jsonObject);
					
					for(Movies e : error_list) {
						String message = e.getTitle() + e.getDirector()+e.getYear();
						String[] g = e.getGenres();
						message += g[0];
						jsonObject.addProperty("message", message);
						jsonArray.add(jsonObject);
					}
					
					out.write(jsonArray.toString());
					
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
