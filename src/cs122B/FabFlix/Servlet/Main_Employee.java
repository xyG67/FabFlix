package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;

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
import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;

/**
 * Servlet implementation class Main_Employee
 */
@WebServlet("/Main_Employee")
public class Main_Employee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Main_Employee() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("Servlet: Main for admin");
		String methodNumber = request.getParameter("methodNumber");
		System.out.println(methodNumber);
		//------------------
		String user = "root";
		String password = "1234";
		String URL = "jdbc:mysql://localhost:3306/moviedb?useSSL=false";
		String sql = "";
		//------------------
		if(methodNumber.equals("1")) {
			//insert new artist
			String star_name = request.getParameter("star_name");
			String birthyear = request.getParameter("birthyear");
			sql = "{call add_star(?,?,?)}";
			System.out.println(sql);
			try{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				//Connection conn = (Connection) java.sql.DriverManager.getConnection(URL, user, password);
				
				
				Context initCtx = new InitialContext();
	            Context envCtx = (Context) initCtx.lookup("java:comp/env");
	            DataSource ds = (DataSource) envCtx.lookup("jdbc/Master");
	            Connection conn = (Connection) ds.getConnection();
	            
				CallableStatement state = (CallableStatement) conn.prepareCall(sql);
				state.setString(1, star_name);
				state.setString(2, birthyear);
				state.registerOutParameter(3,Types.VARCHAR);
				
				PrintWriter out = response.getWriter();	
			    state.executeUpdate();
			    String output = state.getString(3);
			    System.out.println(output);
			    
				JsonArray jsonArray = new JsonArray();
			    JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("OUTPUT", output);
				jsonArray.add(jsonObject);
				out.write(jsonArray.toString());
			    
				conn.close();
				
			}catch(Exception e) {
				System.out.println(e);
			}
			return;
		}else if(methodNumber.equals("2")) {
			//show metadata
			try{
				//Class.forName("com.mysql.jdbc.Driver").newInstance();
				//Connection conn = (Connection) java.sql.DriverManager.getConnection(URL, user, password);
				
				Context initCtx = new InitialContext();
	            Context envCtx = (Context) initCtx.lookup("java:comp/env");
	            DataSource ds = (DataSource) envCtx.lookup("jdbc/Master");
	            Connection conn = (Connection) ds.getConnection();
				
				DatabaseMetaData databaseMetaData = conn.getMetaData();
				Statement stmt = conn.createStatement();
				ResultSet rs = databaseMetaData.getTables(conn.getCatalog(), "root", null, new String[]{"TABLE"});
				JsonArray jsonArray = new JsonArray();
				ArrayList<String> table = new ArrayList<>();
				PrintWriter out = response.getWriter();	
				while (rs.next()) {
					table.add(rs.getString("TABLE_NAME"));
				}
				for(String curr:table) {
					//System.out.println(curr);
					//COLUMN_NAME, IS_NULLABLE(YES/NO), DATA_TYPE, CHARACTER_MAXIMUM_LENGTH
					sql = "SELECT COLUMN_NAME, IS_NULLABLE, DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'moviedb' AND TABLE_NAME = '"+ curr +"'";
					System.out.println(sql);
					rs = stmt.executeQuery(sql);
					while(rs.next()) {
						String COLUMN_NAME = rs.getString("COLUMN_NAME");
						String IS_NULLABLE = rs.getString("IS_NULLABLE");
						String DATA_TYPE = rs.getString("DATA_TYPE");
						//
						JsonObject jsonObject = new JsonObject();
						jsonObject.addProperty("TABLE_NAME", curr);
						jsonObject.addProperty("COLUMN_NAME", COLUMN_NAME);
						jsonObject.addProperty("IS_NULLABLE", IS_NULLABLE);
						jsonObject.addProperty("DATA_TYPE", DATA_TYPE);
						jsonArray.add(jsonObject);
					}
				}
				out.write(jsonArray.toString());
			}catch (SQLException ex) {
			    while (ex != null) {
			        System.out.println("SQL Exception:  " + ex.getMessage());
			        ex = ex.getNextException();
			        }
			}catch (Exception ex){
				return;
			}
		}else if(methodNumber.equals("3")) {
			//add movie
			String movie_title = request.getParameter("movie_title");
			String movie_year = request.getParameter("movie_year");
			String movie_director = request.getParameter("movie_director");
			String movie_genre = request.getParameter("movie_genre");
			String movie_star = request.getParameter("movie_star");
			System.out.println(movie_title+" "+ movie_year + " "+ movie_director + " "+ movie_genre + " "+ movie_star);
			PrintWriter out = response.getWriter();	
			//write an procedure called add_movie!! implete
			String call = "{call add_movie(?,?,?,?,?,?)}";
			Connection conn = null;
			try {
				//Class.forName("com.mysql.jdbc.Driver").newInstance();
				//conn = (Connection) java.sql.DriverManager.getConnection(URL, user, password);
				
				Context initCtx = new InitialContext();
	            Context envCtx = (Context) initCtx.lookup("java:comp/env");
	            DataSource ds = (DataSource) envCtx.lookup("jdbc/Master");
	            conn = (Connection) ds.getConnection();
				
				CallableStatement state = (CallableStatement) conn.prepareCall(call);
				
			    state.setString(1, movie_title);
			    state.setInt(2, Integer.parseInt(movie_year));
			    state.setString(3, movie_director);
			        
			    state.setString(4, movie_genre);
			    state.setString(5, movie_star);
			    state.registerOutParameter(6, Types.VARCHAR);
			    
			    state.executeUpdate();
			    
			    String output = state.getString(6);
			    System.out.println(output);
			    
			    JsonArray jsonArray = new JsonArray();
			    JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("OUTPUT", "add_Movie: "+output);
				jsonArray.add(jsonObject);
				out.write(jsonArray.toString());	    
			    
				conn.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				e.printStackTrace();
			}			
		}
	}
	
	public static long getUnsignedIntt (int data){     
        return data&0x0FFFFFFFF;
     }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
