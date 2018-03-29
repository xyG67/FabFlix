package cs122B.FabFlix.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCUtil {
	
	public JDBCUtil() {
	}
	
	public static Connection getConn() throws Exception {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/moviedb?serverTimezone=UTC";
		String user = "root";
		String password = "1234";
		
		try {
			Class.forName(driver).newInstance();
			System.out.println("1");
			}
			catch(Exception e) {
			System.out.println("can't load mysql driver");
			}
		
		Connection conn = null;
		conn = DriverManager.getConnection(url, user, password); 
		System.out.println("2");
		return conn;
	}
	
	public static Statement getStatement(Connection conn) {
		Statement select = null;
		try {
			select = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return select;
	}
	
	public static ResultSet excuteQuery(Statement stmt, String sql) {
		ResultSet res = null;
		try {
			res = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static void closeStatement(Statement stmt) {
		if(stmt != null) {
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
				rs = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void closeConn(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String loadOneSql(String sqlFile) throws Exception {
		String path = sqlFile;
		InputStream sqlFileIn = JDBCUtil.class.getResourceAsStream(path);
		StringBuffer sqlSb = new StringBuffer();
		byte[] buff = new byte[1024];
		int byteRead = 0;
		while ((byteRead = sqlFileIn.read(buff)) != -1) {
			sqlSb.append(new String(buff, 0, byteRead,"utf-8"));
		}
		sqlFileIn.close();
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public static List<String> loadSql(String sqlFile) throws Exception {
		List<String> sqlList = new ArrayList<String>();
		try {
			InputStream sqlFileIn = new FileInputStream("E:\\JAVA\\JEE-workspace\\CS122B\\WebContent\\sql\\findMovieFast.sql");
			StringBuffer sqlSb = new StringBuffer();
			byte[] buff = new byte[1024];
			int byteRead = 0;
			while ((byteRead = sqlFileIn.read(buff)) != -1) {
				sqlSb.append(new String(buff, 0, byteRead,"utf-8"));
			}
			sqlFileIn.close();
		// Windows 下换行是 \r\n, Linux 下是 \n
			String[] sqlArr = sqlSb.toString()
					.split("(;\\s*\\r\\n)|(;\\s*\\n)");
			for (int i = 0; i < sqlArr.length; i++) {
				String sql = sqlArr[i].replaceAll("--.*", "").trim();
				if (!sql.equals("")) {
					sqlList.add(sql);
				}
			}
		return sqlList;
		} catch (Exception ex) {
			throw new Exception(ex.getMessage());
		}
		}
	
	private void connectJDBC() throws Exception {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql:///moviedb?autoReconnect=true&useSSL=false";
		String user = "root";
		String password = "1234";
		String sql = "";
		
		try {
			Class.forName(driver).newInstance();
			}
			catch(Exception e) {
			System.out.println("can't load mysql driver");
			}
		Connection connection = DriverManager.getConnection(url, user, password);
		Statement select = connection.createStatement();
		ResultSet result = select.executeQuery("select * from stars");
		
		System.out.println("The result of query");
		ResultSetMetaData metadata = result.getMetaData();
		System.out.println("There are " + metadata.getColumnCount() + " columns");
		
		for (int i = 1; i <= metadata.getColumnCount(); i++) {
			System.out.println("Type of column "+ i + " is " + metadata.getColumnTypeName(i));
		}
		
		 while (result.next()) {
			 System.out.println("Id = " + result.getString(1));
			 System.out.println("Name = " + result.getString(2) + result.getString(3));
			 System.out.println("DOB = " + result.getString(3));
			// System.out.println("photoURL = " + result.getString(5));
			 System.out.println();
		 }
	}
}
