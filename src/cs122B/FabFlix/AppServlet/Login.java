package cs122B.FabFlix.AppServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import cs122B.FabFlix.Util.JDBCUtil;

/**
 * Servlet implementation class Login
 */
@WebServlet("/LoginApp")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());		
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String email = "";
		String pass  = "";
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		email = request.getParameter("email");
		pass = request.getParameter("pass");	
		String sql = "select(SELECT password FROM customers  WHERE email= '"+ email+"') as password";
		
		try {
			  conn = (Connection) JDBCUtil.getConn();
			  stmt = (Statement) JDBCUtil.getStatement(conn);
			  rs = JDBCUtil.excuteQuery(stmt, sql);
			  while(rs.next()) {
				  String result = rs.getString("password");
				  if(result == null) {
					  out.println("Email not exist!");
				  }else if (!result.equals(pass)) {
					  out.println("Password is incorrect!");
				  }else {
					  out.println("Welcome!");
				  }
			  }
			  rs.close();
			  stmt.close();
			  conn.close();
		} catch (Exception e) {
			out.println("false");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
