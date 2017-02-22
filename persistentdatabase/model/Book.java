package persistentdatabase.model;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Book implements Persistable, Serializable {

	public Book() {
		super();
		_authorsList = new ArrayList<BookAuthor>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;

	public static String ENTITY_NAME = "Book";

	private String NlmID;

	private String _datePublished;
	private String _titleMain;
	//private String _titleSub;
	private String _publicationCountry;
	private String _language;
	private List<BookAuthor> _authorsList;
	private String _content;
	private String _abst;

	public void setNlmID(String id) {
		NlmID = id;
	}
	
	public void setDatePublished(String year, String month, String day) {
		_datePublished = day + "/" + month + "/" + year;      
	}
	
	public void setPublicationCountry(String country) {
		_publicationCountry = country;      
	}
	
	public void setTitleMain(String title) {
		_titleMain = title;      
	}
	
//	public void setTitleSub(String title) {
//		_titleSub = title;      
//	}
	
	public void setLanguage(String lan) {
		_language = lan;      
	}
	
	public void setContent(String con) {
		_content = con;      
	}
	
	public String getDatePublished() {
		return _datePublished;      
	}
	
	public String getPublicationCountry() {
		return _publicationCountry;      
	}
	
	public String getTitle() {
		return _titleMain;      
	}
	
//	public String getTitleSub() {
//		return _titleSub;      
//	}
	
	public String getLanguage() {
		return _language;      
	}
	
	public String getContent() {
		return _content;      
	}
	
	public void addBookAuthor(String last, String first, String init, String coll) {
		if(_authorsList == null) {
			_authorsList = new ArrayList<BookAuthor>();
		}
		_authorsList.add(new BookAuthor(last,first,init, coll));
	}
	
	public String getId() {
		return NlmID;
	}

	public String toString() {
		String str;
		str = "ID: " + NlmID + "\n" 
				+ "TITLE:  " + _titleMain + "\n" ;
		for(int i=0 ; i < _authorsList.size() ; i++){
			str += "AUTHOR: " + _authorsList.get(i).toString() + "\n";
		}
		str += _datePublished.toString();
		return str;
	}

	public String getAbstract() { 
			String abstStr = "";
			try {
				abstStr = new String(Files.readAllBytes(Paths.get(_abst)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return abstStr;
	}

	public void setAbstract(String abst)    { _abst       = abst;    }
	
	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public String getIdIdentifier() {
		return "NlmUniqueID";
	}
}
