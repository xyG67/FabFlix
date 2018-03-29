package cs122B.FabFlix.Util;

import java.util.HashMap;
import java.util.HashSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cs122B.FabFlix.Model.*;


public class SAXParserMoviesHelper extends DefaultHandler {
	String value = null;
	Movies m = null;
	Stars s = null;
	Genres g = null;
	int num = 0;
	HashSet<Movies> movie_list = new HashSet<Movies>();//result data!! a set of movie including year, director, genres(ArrayList)...
	HashSet<Stars> star_list = new HashSet<Stars>();
	HashSet<Genres> genre_list = new HashSet<Genres>();
	
	HashMap<Movies, HashSet<Genres>> genreInMovie = new HashMap<Movies, HashSet<Genres>>();
	private HashSet<Genres> genre_movie;
	HashMap<Movies, HashSet<Stars>> starInMovie = new HashMap<Movies, HashSet<Stars>>();
	private HashSet<Genres> star_movie;
	
	String curr_Director = null;
	
	public HashSet<Genres> getGenresList(){
		return genre_list;
	}
	
	public HashSet<Movies> getMoviesList(){
		return movie_list;
	}
	
	public HashSet<Stars> getStarsList(){
		return star_list;
	}

	public HashMap<Movies, HashSet<Genres>> getGenreInMovie() {
		return genreInMovie;
	}

	public HashMap<Movies, HashSet<Stars>> getStarInMovie() {
		return starInMovie;
	}
	
	public void startElement(String uri, String localName, String qName, Attributes attr) throws SAXException {
		//System.out.println("<"+qName+">");
		
		super.startElement(uri, localName, qName, attr);
		if(qName.equals("film")) {
			m = new Movies();
			//genre_movie = new HashSet<Genres>();
		}	
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		//System.out.println("<"+qName+">");
		
		super.endElement(uri, localName, qName);
		//
		if(qName.equals("film")) {
			movie_list.add(m);
			//printMovieList();
			num++;
		}
		else if(qName.equals("dirn")) {
			if(value == null || value.contains(" ")) {
				value = "NULL";
			}
			curr_Director = value;
			m.setDirector(curr_Director);
		}else if(qName.equals("year") || qName.equals("released")) {
			m.setYear(extractChar(value));			
		}else if(qName.equals("t")) {
			m.setTitle(value);
		}else if(qName.equals("cat")) {
			//System.out.println("cat "+ value);
			if(value==null || value.contains(" ")) {
				value = "NULL";
			}
			String[] currd = {value};
			m.setGenres(currd);
		}
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		//System.out.println(new String(ch, start, length));
//		
		this.value = new String(ch, start, length);
		if(value == null) this.value = "";
	}
	
	public void startDocument() throws SAXException {
		super.startDocument();
		System.out.println("Start parsing document...");
	}
	
	public void endDocument() throws SAXException {
		super.endDocument();
		System.out.println(num);
		System.out.println("End parsing document...");
		
	}
	
	public void printMovieList() {
	//result data, add to JDBC each 
		//System.out.print(m.getTitle()+ " "+ m.getYear()+ " "+ m.getDirector());
			for(String g: m.getGenres()) {
				//System.out.print(" "+g+ " ");
			}
			//System.out.println("");
	}	
	
	public String extractChar(String s) {
		String res = "";
		if(s != null && !"".equals(s)){
			for(int i=0;i<s.length();i++){
				if(s.charAt(i)>=48 && s.charAt(i)<=57){
					res+=s.charAt(i);
				}
			}
		}
		return res;
	}
}
