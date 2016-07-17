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

	  @Id
	  @GeneratedValue(strategy = GenerationType.TABLE)
	  private String id;
	  
	  public static String ENTITY_NAME = "Book";
	  
	  private String NlmID;
	  
	  private Date DateCreated;
	  private Date DateRevised;
	  private Date DateAuthorized;
	  private Date DateCompleted;
	  
	  private String TitleMain;
	  private String TitleRelated;
	  
	  private List<BookAuthor> AuthorsList;
	  
	  private String abst;
	  
	  private String ResourceType;
	  private String ResourceIssuance;
	  private String ResourceUnit;
	  private String ResourceContentType;
	  private String ResourceMediaType;
	  private String ResourceCarrierType;
	  
	  private String PublicationCountry;
	  private String PublicationPlaceCode;
	  private String PublicationImprintType;
	  private String PublicationImprintFunctionType;
	  private String PublicationImprintPlace;
	  private String PublicationImprintEntity;
	  private String PublicationImprintDateIssued;
	  private String PublicationImprintFull;
	  
	  private String Language;
	  private List<String> ICCNs;
	  private List<String> ISBNs;
	  
	  public String getId()   { return NlmID; }
	  
	  public String toString(){
		  return "ID: "      + NlmID       + "\n" +
				 "TITLE:  "  + TitleMain      + "\n" +
				 "AUTHOR: "  + Authors + "\n" +
				 "JOURNAL: " + journal + " " + volume + "(" + issue + ")" 
				 			 + ", " + pDate + ", " + "DOI: " + DOI;
	  }
	  
	  public String getAbstract() { 
			String abstStr = "";
			try {
				abstStr = new String(Files.readAllBytes(Paths.get(abst)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return abstStr;          
	  
		@Override
		public String getEntityName() {
			return ENTITY_NAME;
		}
		
		@Override
		public String getIdIdentidier() {
			return "NlmUniqueID";
		}
}
