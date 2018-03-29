package cs122B.FabFlix.Util;

import java.util.HashSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import cs122B.FabFlix.Model.Stars;

public class SAXParserActorHelper extends DefaultHandler {
	private Stars actor;
	private String value = null;
	private String actor_name = null;
	HashSet<Stars> actor_list = new HashSet<Stars>(); //result data: 
	
	
	
	public HashSet<Stars> getActor_list() {
		return actor_list;
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
	}
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(qName.equals("actor")) {
			actor = new Stars(); 
		}
	}
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		if(qName.equals("stagename")) {
			actor_name = value;
			actor.setName(actor_name);
		}else if(qName.equals("dowstart")) {
			value = extractChar(value);
			
			actor.setBirthyear(value);
			
			if(actor.getBirthyear()==null || actor.getBirthyear().equals(" ")|| actor.getBirthyear().equals("  ") || actor.getBirthyear().equals(""))
				actor.setBirthyear("0");
			else if(actor.getName() == null)
				actor.setName("NULL");
			actor_list.add(actor);
		//System.out.println(actor_name +" "+ value);
		}
	}
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		value = new String(ch, start, length);
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
