/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

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

import org.bson.Document;

@Entity
public class Article implements Persistable, Serializable{
	
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int counter =0;
	
	public Article(){
		super();
		//id = generateId();
	};
	
	public Article(Document doc){
		id = doc.getString("PMID");
		PMID = doc.getString("PMID");
		title = doc.getString("title");
		authors = doc.getString("authors");
		pDate = doc.getString("pDate");
		journal = doc.getString("journal");
		volume = doc.getString("volume");
		issue = doc.getString("issue");
		DOI = doc.getString("DOI");
		abst = doc.getString("abst");
		keywords = doc.getString("keywords");
		// tags...
		String temp = doc.getString("tags");
		if (temp != null) {	
			String[] tmp = temp.split(", ");
			for (int i = 0; i < tmp.length; i ++) {
				addTag(tmp[i]);
			}
		}

	}

@Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String id;

//private static String generateId()
//{
//	return (counter = counter +1);
//	 
//}
  
  public static String ENTITY_NAME = "Article";
  
  private String PMID;
  private String title;
  private String authors;
  private String pDate;
  private String journal;
  private String volume;
  private String issue;
  private String DOI;
  private String abst;
  private String keywords;
  
  private List<Disease> diseases   = new ArrayList<Disease>();
  private List<Aneurysm> aneurysms = new ArrayList<Aneurysm>();
  private List<Model>   models     = new ArrayList<Model>();
  private List<String>  tags 	   = new ArrayList<String>();

  public String toString(){
	  String start = "ID: "      + PMID       + "\n" +
				 "TITLE:  "  + title      + "\n";
	  
	  String middle = "Authors:" + authors + "\n";
	  
	  String end =  "JOURNAL: " + journal + " " + volume + "(" + issue + ")" 
	 			 + ", " + pDate + ", " + "DOI: " + DOI +"\n";
	  
	  String sign = abst + "Keyword: " + keywords + "\n" + "Tags: " + list2string();
	  
	  return start + middle + end + sign;
  }
  public void setId(String _id)            { id       = _id;      }
  public void setPMID(String _id)          { PMID       = _id;      }
  public void setTitle(String _title)      { title      = _title;   }
  public void setAuthor(String _author)    { authors = _author;  }
  public void setYear(String _year)        { pDate      = _year;    }
  public void setJournal(String _journal)  { journal    = _journal; }
  public void setVolume(String _volume)    { volume     = _volume;  }
  public void setIssue(String _issue)      { issue      = _issue;   }
  public void setDOI(String _DOI)          { DOI        = _DOI;     }
  public void setAbstract(String _abst)    { abst       = _abst;    }
  public void setKeywords(String _keys)    { keywords   = _keys;    }


  public String getId()				       { return PMID;			}
  
  public void addDisease(Disease disease)    { diseases.add(disease);   }
  public void addAneurysm(Aneurysm aneurysm) { aneurysms.add(aneurysm); }
  public void addModel(Model model)          { models.add(model);       }
  public void addTag(String tag)          	 { tags.add(tag);     	    }
  
	
  public List<Disease> getLinkedDiseases()   { return diseases; }
  public List<Aneurysm> getLinkedAneurysms() { return aneurysms; }
  public List<Model>   getLinkedModels()     { return models;   }
  public List<String>   getLinkedTags()      { return tags;     }

  
  public String getAbstract() { 
//	String abstStr = "";
//	try {
//		abstStr = new String(Files.readAllBytes(Paths.get(abst)));
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//	return abstStr;   
	 return abst;
  }

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	public String getTitle() {
		return title;
	}
	
	@Override
	public String getIdIdentifier() {
		return "PMID";
	}
	
	public String getKeywords() {
		return keywords;
	}
 
  public String list2string(){
	  String result = "";
	  for (String tag: tags ){
		  result += tag +", ";
	  }
	  if(result.length()>1)
		  result = result.substring(0, result.length()-2);
	  return result;
  }
	
	
	public Document toBson(){
		Document doc = new Document();

		doc.append("id", PMID);
		doc.append("PMID", PMID);
		doc.append("title", title);
		doc.append("authors", authors);
		doc.append("pSate", pDate);
		doc.append("journal", journal);
		doc.append("volume", volume);
		doc.append("issue", issue);
		doc.append("DOI", DOI);
		doc.append("abst", abst);
		doc.append("keywords", keywords);
		doc.append("tags", list2string());

		return doc;
	}
	

}