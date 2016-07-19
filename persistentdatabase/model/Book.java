package persistentdatabase.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book implements Persistable {

	public Book() {
		super();
		_authorsList = new ArrayList<BookAuthor>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;

	public static String ENTITY_NAME = "Book";

	private String NlmID;

	private Date _datePublished;
	private String _publicationCountry;
	private String _titleMain;
	private String _titleSub;
	private List<BookAuthor> _authorsList;
	private String abst;
	private String _language;

	public void setDatePublished(Date date) {
		_datePublished = date;      
	}
	
	public void setPublicationCountry(String country) {
		_publicationCountry = country;      
	}
	
	public void setTitleMain(String title) {
		_titleMain = title;      
	}
	
	public void setTitleSub(String title) {
		_titleSub = title;      
	}
	
	public void setLanguage(String lan) {
		_language = lan;      
	}
	
	public String getDatePublished() {
		return _datePublished.toString();      
	}
	
	public String getPublicationCountry() {
		return _publicationCountry;      
	}
	
	public String getTitleMain() {
		return _titleMain;      
	}
	
	public String getTitleSub() {
		return _titleSub;      
	}
	
	public String getLanguage() {
		return _language;      
	}
	
	public void addBookAuthor(String last, String first, String init, String date, String role) {
		_authorsList.add(new BookAuthor(last,first,init,date,role));
	}
	
	public String getId() {
		return NlmID;
	}

	public String toString() {
		String str;
		str = "ID: " + NlmID + "\n" 
				+ "TITLE:  " + _titleMain + "-" + _titleSub + "\n" ;
		for(int i=0 ; i < _authorsList.size() ; i++){
			str += "AUTHOR: " + _authorsList.get(i).toString() + "\n";
		}
		str += _datePublished.toString();
		return str;
	}

	public String getAbstract() { 
			String abstStr = "";
			try {
				abstStr = new String(Files.readAllBytes(Paths.get(abst)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return abstStr;
	}

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public String getIdIdentidier() {
		return "NlmUniqueID";
	}
}
