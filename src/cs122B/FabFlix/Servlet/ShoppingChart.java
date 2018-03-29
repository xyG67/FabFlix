package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cs122B.FabFlix.Model.Movies;

/**
 * Servlet implementation class ShoppingChart
 */
@WebServlet("/ShoppingChart")
public class ShoppingChart extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingChart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Servlet: shopping cart");
		
		Hashtable<String, Integer> sc = new Hashtable<String, Integer>();
		sc = (Hashtable<String, Integer>) request.getSession().getAttribute("shoppingChart");
		
		PrintWriter out = response.getWriter();
		JsonArray jsonArray = new JsonArray();
		
		if(sc.isEmpty()) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("movies", "Please add movies to chart!");
			jsonObject.addProperty("number", "0");
			jsonArray.add(jsonObject);
		}else {
			for(String obj : sc.keySet()) {     
				  String key = obj;     
			      int value = sc.get(obj);     
			      //System.out.println(value);
			      JsonObject jsonObject = new JsonObject();
			      jsonObject.addProperty("title", key);
			      jsonObject.addProperty("number", value);
			      System.out.println("has: "+ key + " "+ value);
			      jsonArray.add(jsonObject);
			  } 
		}
		out.write(jsonArray.toString());
		out.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
