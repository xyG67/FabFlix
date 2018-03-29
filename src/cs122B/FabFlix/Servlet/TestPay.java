package cs122B.FabFlix.Servlet;
import Create.DBConnect;
import cs122B.FabFlix.Model.Card;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class TestPay
 */
@WebServlet("/TestPay")
public class TestPay extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TestPay() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Servlet: TestPay");
		String fget=request.getParameter("fname");
		String lget=request.getParameter("lname");
		String expget=request.getParameter("expiration");
		String idget=request.getParameter("id");

		DBConnect conn =new DBConnect();
		ResultSet res = null;
		String query=null;				

		query="select(SELECT firstName FROM creditcards  WHERE id= '"+ idget +"')as first, "
				+ "(SELECT lastName FROM creditcards  WHERE id='"+ idget +"') as last,"
				+ "(SELECT expiration FROM creditcards  WHERE id='"+ idget +"') as expiration";
		res = conn.getData(query);
		
		
		try {
			while(res.next()){
				try {
					String fdb=res.getString("first");
					String ldb=res.getString("last");
					String expdb=res.getString("expiration");
					
					if(expdb== null){
						System.out.println("CreditCard not exist!");
						request.getSession().setAttribute("id", new Card(idget));
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject.addProperty("status", "fail");
						responseJsonObject.addProperty("message", "Credit Card " + idget + " doesn't exist");
						response.getWriter().write(responseJsonObject.toString());
						
					}
					else if (!fdb.equals(fget)){
						System.out.println("First Name Wrong!");
						request.getSession().setAttribute("id", new Card(idget));
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject.addProperty("status", "fail");
						responseJsonObject.addProperty("message", "incorrect first name");
						response.getWriter().write(responseJsonObject.toString());
					}
					else if (!ldb.equals(lget)){
						System.out.println("Last Name Wrong!");
						request.getSession().setAttribute("id", new Card(idget));
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject.addProperty("status", "fail");
						responseJsonObject.addProperty("message", "incorrect last name");
						response.getWriter().write(responseJsonObject.toString());
					}
					else if (!expdb.equals(expget)){
						System.out.println("Expiration Date Wrong!");
						request.getSession().setAttribute("id", new Card(idget));
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject.addProperty("status", "fail");
						responseJsonObject.addProperty("message", "incorrect expiration date");
						response.getWriter().write(responseJsonObject.toString());
					}
					else {
						System.out.println("Welcome!");
						request.setAttribute("res", res);
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject.addProperty("status", "success");
						responseJsonObject.addProperty("message", "success");
						response.getWriter().write(responseJsonObject.toString());
					}	
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		

	}
		
}

