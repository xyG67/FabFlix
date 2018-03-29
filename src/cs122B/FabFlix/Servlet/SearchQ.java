package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

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

/**
 * Servlet implementation class SearchQ
 */
@WebServlet("/SearchQ")
public class SearchQ extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	

    public SearchQ() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> moviesTable = new HashMap<>();
		HashMap<String, String> starsTable = new HashMap<>();
		
		try {
			//
			String query = request.getParameter("query");
			String[] split_query = query.split("\\s+");
			String sql_= "";
			for(String curr:split_query) {
				if(curr.equals("of")||curr.equals("the")||curr.equals("an"))
					continue;
				sql_ += "+"+curr+"* ";
			}
			
			String sql = "SELECT * FROM movies WHERE MATCH (title) AGAINST ('"+sql_+"' IN BOOLEAN MODE) limit 5;";
			//System.out.println(sql);
			//Class.forName("com.mysql.jdbc.Driver");
//			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb?serverTimezone=UTC","root","1234");
			
			
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            Connection conn = ds.getConnection();
			
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				String title = rs.getString("title");
				String id = rs.getString("id");
				System.out.println(title+" "+id);
				moviesTable.put(id, title);
			}
						
			sql = "SELECT * FROM stars WHERE MATCH (name) AGAINST ('"+sql_+"' IN BOOLEAN MODE) limit 5;";
			System.out.println(sql);
			rs = st.executeQuery(sql);
			while(rs.next()) {
				String name = rs.getString("name");
				String id = rs.getString("id");
				System.out.println("Star: "+ name+ " "+ id);
				starsTable.put(id, name);
			}
			
			JsonArray jsonArray = new JsonArray();
			
			// return the empty json array if query is null or empty
			if (query == null || query.trim().isEmpty()) {
				response.getWriter().write(jsonArray.toString());
				return;
			}	
			
			for (String id : moviesTable.keySet()) {
				String movieTitle = moviesTable.get(id);
				for(int i = 0; i<split_query.length; i++) {
					if (movieTitle.toLowerCase().contains(split_query[i].toLowerCase())) {
						jsonArray.add(generateJsonObject(id, movieTitle, "Movies"));
						break;
					}
				}
				//jsonArray.add(generateJsonObject(id, movieTitle, "Movies"));
				
			}
			
			for (String id : starsTable.keySet()) {
				String heroName = starsTable.get(id);
				for(int i = 0; i<split_query.length; i++) {
					if (heroName.toLowerCase().contains(split_query[i].toLowerCase())) {
						jsonArray.add(generateJsonObject(id, heroName, "Stars"));
						break;
					}
				}
			}
			
			response.getWriter().write(jsonArray.toString());
			return;
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private static JsonObject generateJsonObject(String heroID, String heroName, String categoryName) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("value", heroName);
		
		JsonObject additionalDataJsonObject = new JsonObject();
		additionalDataJsonObject.addProperty("category", categoryName);
		additionalDataJsonObject.addProperty("heroID", heroID);
		
		jsonObject.add("data", additionalDataJsonObject);
		return jsonObject;
	}

}
