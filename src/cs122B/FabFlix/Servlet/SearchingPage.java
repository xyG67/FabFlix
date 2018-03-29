package cs122B.FabFlix.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;

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

import cs122B.FabFlix.Util.JDBCUtil;

/**
 * Servlet implementation class SearchingPage
 */
@WebServlet("/SearchingPage")
public class SearchingPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchingPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		ResultSet rs = null;
		
		PrintWriter out = response.getWriter();
		System.out.println("Servlet: SearchingPage.java");
		String sql = "";
		
		
		try {
			sql = JDBCUtil.loadOneSql("../sql/findGenres.sql");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(sql);
		try {
			//conn = JDBCUtil.getConn();
			Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/TestDB");
            conn = ds.getConnection();
			//load "findGeners"
			
			rs = JDBCUtil.excuteQuery(JDBCUtil.getStatement(conn), sql);
			
			JsonArray jsonArray = new JsonArray();
			while (rs.next()) {
				//out put a list of geners
				String geners = rs.getString("name");
				
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("geners", geners);
				jsonArray.add(jsonObject);
			}
			
			out.write(jsonArray.toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeConn(conn);
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
