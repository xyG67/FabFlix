package cs122B.FabFlix.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXParserStarsHelper extends DefaultHandler {
	private String star = null;
	private String value = null;
	private String curr_movie = null;
	private String[] movie_star = new String[2];
	private ArrayList<String[]> list = new ArrayList<>();
	
	public void print() {
		for(int i = 0; i<list.size();i++) {
			String[] s = list.get(i);
			String movie = s[0];
			String star = s[1];
			System.out.println(movie +" "+star);
		}
	}
	
	public ArrayList<String[]> getList() {
		return list;
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		System.out.println("Begining parsing files...");
	}
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		System.out.println("End parsing files...");
		//print();
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		//value = "";
		if(qName.equals("t")) {
			
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if(qName.equals("t")) {
			if(value == null) 
				value = "NULL";
			curr_movie = value;
		}if(qName.equals("a")) {
			//star = value;
			if(value == null) 
				value = "NULL";
			star = value;
			movie_star = new String[2];
			movie_star[0] = curr_movie;
			movie_star[1] = star;
			//System.out.println(curr_movie+ " "+star);
			list.add(movie_star);
			//System.out.println(curr_movie + ": "+star);
	}
//		
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		value = new String(ch, start, length);
	}		
}
