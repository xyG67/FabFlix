package cs122B.FabFlix.Servlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonObject;

import cs122B.FabFlix.Model.User;

/**
 * Servlet implementation class TestLog
 */
@WebServlet("/TestLog")
public class TestLog extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TestLog() {
    	super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Servlet: TestLog");
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		String query=null;				
		ResultSet res = null;

		query=	"select(SELECT password FROM customers  WHERE email= '"+ email+"') as password";

		// conn =new DBConnect();
		Connection conn = null;
        try {
        	Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            conn = ds.getConnection();
            Statement st = conn.createStatement();
    		res = st.executeQuery(query);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		//res = conn.getData(query);
		
		
		try {
			while(res.next()){
				try {
					String result=res.getString("password");
					
					if(result== null){
						System.out.println("Email not exist!");
//						request.setAttribute("res", res);
//						request.getRequestDispatcher("login.html").forward(request, response);
						request.getSession().setAttribute("email", new User(email));
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject.addProperty("status", "fail");
						responseJsonObject.addProperty("message", "user " + email + " doesn't exist");
						response.getWriter().write(responseJsonObject.toString());
						
					}
					else if (!result.equals(password)){
						System.out.println("Password is incorrect!");
//						request.setAttribute("res", res);
//						request.getRequestDispatcher("login.html").forward(request, response);
						request.getSession().setAttribute("email", new User(email));
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject.addProperty("status", "fail");
						responseJsonObject.addProperty("message", "incorrect password");
						response.getWriter().write(responseJsonObject.toString());
					}
					else {
						System.out.println("Welcome!");
						request.setAttribute("res", res);
						
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject.addProperty("status", "success");
						responseJsonObject.addProperty("message", "success");
						response.getWriter().write(responseJsonObject.toString());
						//request.getRequestDispatcher("mainpage.html").forward(request, response);
					}	
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		

	}

}
