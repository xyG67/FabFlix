package Create;

import java.util.*;
import java.io.*;
import java.sql.*;


public class DBConnect {
		public Connection con;
		public Statement st;
		public ResultSet rs;
	
		public DBConnect(){
			try{
				Class.forName("com.mysql.jdbc.Driver");
				this.con=DriverManager.getConnection("jdbc:mysql://localhost:3306/moviedb","root","1234");
				this.st=con.createStatement();		
			}catch(Exception ex){
				System.out.println("Error: " + ex);
			}
		}
		
		public ResultSet getData(String query){
			try{
				this.rs=st.executeQuery(query);
			//	System.out.println("records from database");	
			}catch(Exception ex){
				System.out.println(ex);
			}
			return this.rs;
		}
		
		public void close() throws SQLException {
			con.close();
		}
	}