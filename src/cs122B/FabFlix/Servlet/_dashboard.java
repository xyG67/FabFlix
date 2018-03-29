package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import Create.DBConnect;
import Create.VerifyRechapchaUtil;
import cs122B.FabFlix.Model.User;

/**
 * Servlet implementation class _dashboard
 */
@WebServlet("/_dashboard")
public class _dashboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public _dashboard() {
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
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
		
		
	    System.out.println("Servlet: _DashBoard");
	    String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
		// Verify CAPTCHA.
		boolean valid = VerifyRechapchaUtil.verify(gRecaptchaResponse);
		System.out.println("valid = " + valid);
		
		if (!valid) {
			System.out.println("Please check the box below");
			response.sendRedirect("./error.html");
			return;
		}
		
		System.out.println("valid = true");
		
		String email=request.getParameter("email");
		String password=request.getParameter("password");
		DBConnect conn =new DBConnect();
		ResultSet res = null;
		String query=null;				

		query=	"select(SELECT password FROM employees  WHERE email= '"+ email+"') as password";
		res = conn.getData(query);
		
		
		try {
			while(res.next()){
				try {
					String result=res.getString("password");
					
					if(result== null){
						System.out.println("Email not exist!");
						request.getSession().setAttribute("email", new User(email));
						JsonObject responseJsonObject = new JsonObject();
						responseJsonObject.addProperty("status", "fail");
						responseJsonObject.addProperty("message", "user " + email + " doesn't exist");
						response.getWriter().write(responseJsonObject.toString());
						
					}
					else if (!result.equals(password)){
						System.out.println("Password is incorrect!");
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
