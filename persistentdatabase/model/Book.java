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
		AuthorsList = new ArrayList<BookAuthor>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;

	public static String ENTITY_NAME = "Book";

	private String NlmID;

	private Date DatePublished;
	private String PublicationCountry;
	private String TitleMain;
	private String TitleSub;
	private List<BookAuthor> AuthorsList;
	private String abst;
	private String Language;

	public void setDatePublished(Date date) {
		DatePublished = date;      
	}
	
	public void setPublicationCountry(String country) {
		PublicationCountry = country;      
	}
	
	public void setTitleMain(String title) {
		TitleMain = title;      
	}
	
	public void setTitleSub(String title) {
		TitleSub = title;      
	}
	
	public void setLanguage(String lan) {
		Language = lan;      
	}
	
	public String getDatePublished() {
		return DatePublished.toString();      
	}
	
	public String getPublicationCountry() {
		return PublicationCountry;      
	}
	
	public String getTitleMain() {
		return TitleMain;      
	}
	
	public String getTitleSub() {
		return TitleSub;      
	}
	
	public String getLanguage() {
		return Language;      
	}
	
	public void addBookAuthor(String last, String first, String init, String date, String role) {
		AuthorsList.add(new BookAuthor(last,first,init,date,role));
	}
	
	public String getId() {
		return NlmID;
	}

	public String toString() {
		String str;
		str = "ID: " + NlmID + "\n" 
				+ "TITLE:  " + TitleMain + "-" + TitleSub + "\n" ;
		for(int i=0 ; i < AuthorsList.size() ; i++){
			str += "AUTHOR: " + AuthorsList.get(i).toString() + "\n";
		}
		str += DatePublished.toString();
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
