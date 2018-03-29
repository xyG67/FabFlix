package cs122B.FabFlix.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs122B.FabFlix.Util.JDBCUtil;

public class Movies {
	private String id;
	private String title;
	private String year;
	private String director;
	private String[] genres = {"null"};  
	private String[] stars;  
	private float rating;
	private String number;
	
	
	public Movies(){
		
	}
	
	Movies(String id, String title, String year, String director){
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String i) {
		this.year = i;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}	

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String[] getGenres() {
		return genres;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	public String[] getStars() {
		return stars;
	}

	public void setStars(String[] stars) {
		this.stars = stars;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public static List<Movies> getMoviesList(String sql){
		List<Movies> list = new ArrayList<Movies>();
		Connection conn = null;
		ResultSet rs = null;
		//String sql = "select * from movies";
		try {
			conn = JDBCUtil.getConn();
			rs = JDBCUtil.excuteQuery(JDBCUtil.getStatement(conn), sql);
			while(rs.next()) {
				Movies m = new Movies();
				m.setId(rs.getString("Idmain"));
				m.setTitle(rs.getString("title"));
				m.setYear(rs.getString("year"));
				m.setDirector(rs.getString("director"));
				//m.setGenres(rs.getString("gener"));
				m.setRating(rs.getFloat("rating"));
				//m.setStars(rs.getString("star"));
				list.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.closeResultSet(rs);
			JDBCUtil.closeConn(conn);
		}
		return list;
	}
}
