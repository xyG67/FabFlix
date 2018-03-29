package cs122B.FabFlix.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import com.mysql.jdbc.CallableStatement;
import com.mysql.jdbc.Connection;

import cs122B.FabFlix.Model.Movies;

public class MainDAO {
	public String[] searchMaxId(Statement stmt) {
		String[] res = new String[2];
		
		try {
			ResultSet rs = stmt.executeQuery("select endId from maxid where tablename = 'genres'");
			while(rs.next()) {
				res[0] = rs.getString("endId");
			}
			rs = stmt.executeQuery("select endId from maxid where tablename = 'movies'");
			while(rs.next()) {
				res[1] = rs.getString("endId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public void insertMainBatch(Connection conn, HashSet<Movies> movie_list, String maxGenre, String maxMovie) {
		String sql = "insert into movies values(?,?,?,?)";
		String sql2 = "insert into genres values(?,?)";
		String movie_title, movie_direc, movie_genre;
		int movie_year;
		HashSet<Movies> error_list = new HashSet<Movies>();
		int genreId = (Integer.parseInt(maxGenre)+1);
		int movieId = (Integer.parseInt(maxMovie)+1);
		PreparedStatement psmt = null;
		PreparedStatement psmt2 = null;
		//conn.setAutoCommit(false);
		int i = 0;
		try {
			psmt = conn.prepareStatement(sql);
			psmt2 = conn.prepareStatement(sql2);
			for(Movies m:movie_list) {
				//System.out.print(m.getTitle()+" "+m.getYear()+" "+m.getDirector()+" ");
				movie_title = m.getTitle();
				movie_year = Integer.parseInt(m.getYear());
				movie_direc = m.getDirector();
				String[] curr = m.getGenres();
				movie_genre = curr[0];	
				if(movie_genre.equals("null")||movie_genre == null ||movie_direc == null || movie_direc.equals("none")||movie_genre.equals("none")||movie_direc.equals("null")) {
					//movie_direc = "none";
					error_list.add(m);
					continue;
				}						
				
				psmt.setString(1, "tt"+movieId);
				psmt.setString(2, movie_title);
				psmt.setInt(3, movie_year);
				psmt.setString(4, movie_direc);
				//psmt.setString(3, movie_direc);
				movieId+=1;
				psmt.addBatch();
				psmt2.setString(1, genreId+"");
				psmt2.setString(2, movie_genre);
				genreId+=1;
				psmt2.addBatch();
				
				if(i%1000==0&&i!=0) {
					psmt.executeBatch();
					psmt2.executeBatch();
					psmt.clearBatch();
					psmt2.clearBatch();
					i=0;
				}
				
				i++;
			}
			if(i!=0) {
				psmt.executeBatch();
				psmt2.executeBatch();
				psmt.clearBatch();
				psmt2.clearBatch();
			}
			
			psmt.close();
			psmt2.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
