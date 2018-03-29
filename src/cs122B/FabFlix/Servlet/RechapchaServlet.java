package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Create.VerifyRechapchaUtil;

/**
 * Servlet implementation class RechapchaServlet
 */
@WebServlet("/RechapchaServlet")
public class RechapchaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RechapchaServlet() {
        super();
    }
    
    public String getServletInfo()
    {
       return "Servlet connects to MySQL database and displays result of a SELECT";
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		PrintWriter out = response.getWriter();
	    System.out.println("Servlet: rechaptcha");
	    String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
		// Verify CAPTCHA.
		boolean valid = VerifyRechapchaUtil.verify(gRecaptchaResponse);
		System.out.println("valid = " + valid);
		if (!valid) {
			response.sendRedirect("./error.html");
			return;
		}
		
		String loginUser = "root";
        String loginPasswd = "1234";
        String loginUrl = "jdbc:mysql://localhost:3306/moviedb";

        response.setContentType("text/html");
        
        try
        {
           //Class.forName("org.gjt.mm.mysql.Driver");
           Class.forName("com.mysql.jdbc.Driver").newInstance();

           Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
           // Declare our statement
           Statement statement = dbcon.createStatement();

           String email = request.getParameter("name");
           String password = request.getParameter("password");
           String query=	"select(SELECT password FROM employees  WHERE email= '"+ email+"') as password";

           // Perform the query
           ResultSet rs = statement.executeQuery(query);

           // Iterate through each row of rs
           while (rs.next())
           {
         	  try {
					String result=password;
					
					if(result== null){
						System.out.println("Email not exist!");
//						request.setAttribute("res", res);
//						request.getRequestDispatcher("login.html").forward(request, response);
						response.sendRedirect("./login-employee.html");
					}
					else if (!result.equals(password)){
						System.out.println("Password is incorrect!");
//						request.setAttribute("res", res);
//						request.getRequestDispatcher("login.html").forward(request, response);
						response.sendRedirect("./login-employee.html");
					}
					else {
						System.out.println("Welcome!");
						//request.setAttribute("res", request);
						response.sendRedirect("./main_employee.html");
						
					}	
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
           }
           
           rs.close();
           statement.close();
           dbcon.close();
           //response.sendRedirect("../main.html");
         }
     catch (SQLException ex) {
           while (ex != null) {
                 System.out.println ("SQL Exception:  " + ex.getMessage ());
                 ex = ex.getNextException ();
             }  // end while
         }  // end catch SQLException

     catch(java.lang.Exception ex)
         {
             out.println("<HTML>" +
                         "<HEAD><TITLE>" +
                         "MovieDB: Error" +
                         "</TITLE></HEAD>\n<BODY>" +
                         "<P>SQL error in doGet: " +
                         ex.getMessage() + "</P></BODY></HTML>");
             return;
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
