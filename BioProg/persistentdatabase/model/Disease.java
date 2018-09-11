/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package persistentdatabase.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.bson.Document;

import database.DataBase.DBType;
import persistentdatabase.main.PersistAgentMongoDB;
import persistentdatabase.main.PersistSettings;

@Entity
public class Disease implements Persistable, Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String staticSummery = "";
	private static String staticDrugs = "";
	private static List<String> staticAliases;
	private static int counter =0;
	static PersistAgentMongoDB mongoAgent = null;


	@Id
	 @GeneratedValue(strategy = GenerationType.TABLE)
	 private String id;
	 
	 public static String ENTITY_NAME = "Disease";
	 
	 private String MCID = "";
	 private String name;
	 private String URLlink = "";
	 private String summaries = "";
	 private String drugs = "";
	 private String therapeutics = "";
	 private String keyword = "";
	 
	 
	 
	 private List<String> aliases = new ArrayList<String>();
	 private List<String>  genes  = new ArrayList<String>();
	// private List<Model>    models    = new ArrayList<Model>();
	 private List<String>  tags 	   = new ArrayList<String>();	
	 
	 public void setMCID(String id)		{	MCID = id; URLlink = "http://www.malacards.org/card/" + MCID; this.id=id;		}
	 public void setName(String _name)  {	 name = _name;	 }
	// public void setURLlink(String link){			  }
	 public void setSummerias(String summ){	 summaries = summ; }
	 public void setKeyword(String keyW){	 keyword = keyW; }
	 public void setDrugs(String drug)	{	 drugs = drug; }
	 public void setTherapeutics(String ter)	{	 therapeutics = ter; }

	 @Override
	 public String getId()		       { return MCID;	}
	 public String getName()			{	 return name; }
	 public String getUrlink()			{	 return URLlink; }
	 public String getSummaries()			{	 return summaries; }
	 public String getDrugs()			{	 return drugs; }
	 public String getTherapeutics()			{	 return therapeutics; }
	 public String getKeyWord()			{	 return keyword; }

	 public void addAliase(String ali) 		{ aliases.add(ali); }
	 public void addAliases(List<String> ali){ aliases.addAll(ali); }
	 public void addGene(String gene)   	 { genes.add(gene);   }
	 public void addTag(String tag)          	 { tags.add(tag);     	    }
//	 public void addModel(Model model)          { models.add(model);       }
	 
	 public List<String> getAliases()	{ return aliases; }
	 public List<String> getGenes()   { return genes; }
	 public List<String>   getLinkedTags()      { return tags;     }

	// public List<Model>   getLinkedModels()     { return models;   }
		 
	 public Disease() {
			super();
			//id = generateId();
		}

	 
	 public Disease(Document doc){
			id = doc.getString("MCID");
			MCID = doc.getString("MCID");
			name = doc.getString("name");
			summaries = doc.getString("summaries");
			URLlink = doc.getString("URLlink");
			drugs = doc.getString("drugs");
			therapeutics = doc.getString("therapeutics");
			keyword = doc.getString("keyword");
			// tags...
			String temp = doc.getString("tags");
			if (temp != null) {	
				String[] tmp = temp.split(", ");
				for (int i = 0; i < tmp.length; i ++) {
					addTag(tmp[i]);
				}
			}

		}
	 
	 public String toString(){
		 return "MCID:" + MCID + "\n" + 
				"Name: " + name + "\n" +
				"Link at MalaCards: " + URLlink + "\n" +
				"Summaries: " + summaries + "\n" + "Tags: " + list2string();
			//	"Drugs: " + drugs + "\n"+
			//"Therapeutics: " + therapeutics;// + "\n";
				//"Aliases: " + listToString(aliases);// + "\n" +
			//	"Genes: " + listToString(genes) ;
		 
	 }

	 @Override
	 public String getEntityName() {
	 	return ENTITY_NAME;
	 }

	 @Override
	 public String getIdIdentifier() {
		return "name";
	 }

	private String listToString(List<String> list){
		String tmp = "";
		for (String str : list) {
			tmp += str +", ";
		}
		if (tmp.length()> 1)
			tmp = tmp.substring(0, tmp.length()-3); //delete last comma  .
		return tmp;
	}
	
	public Document toBson(){
		Document doc = new Document();

		doc.append("id", id);
		doc.append("MCID", MCID);
		doc.append("name", name);
		doc.append("summaries", summaries);
		doc.append("URLlink", URLlink);
		doc.append("drugs", drugs);
		doc.append("therapeutics", therapeutics);
		doc.append("keyword", keyword);
		doc.append("tags", list2string());


		return doc;
	}
	
//private static int generateId() {
//		
//		return (counter = counter +1);
//	}
	
//	public void byRefCopy(){
//		staticAliases = aliases;
//		staticDrugs = drugs;
//		staticSummery = summaries;
//	}
//	
//	public void byRefPaste(){
//		
//		aliases = staticAliases;
//		drugs = staticDrugs;
//		summaries = staticSummery;
//		
//	}
	
	public String list2string(){
		  String result = "";
		  for (String tag: tags ){
			  result += tag +", ";
		  }
		  if(result.length()>1)
			  result = result.substring(0, result.length()-2);
		  return result;
	  }
	
	public void syncMongo() {
		Document doc = null;		
		
			//mongoAgent  = new PersistAgentMongoDB();
			doc = mongoAgent.getTDoc(getId(), DBType.MALA_CARDS);
			if (doc != null) {
				if (!doc.getString("summaries").equals("")) {
					setSummerias(doc.getString("summaries"));
				}
				String temp = doc.getString("tags");
				Boolean flag = false;
				if (temp != null) {	
					String[] tmp = temp.split(", ");
					for (int i = 0; i < tmp.length; i ++) {
						if(tmp[i].equals(tags.get(0)))
							flag = true;
						else {
							addTag(tmp[i]);
						}
							
					}
					if(!flag) {
						temp += ", "+tags.get(0);
						doc.remove("tags");
						doc.append("tags", temp);
						mongoAgent.saveDoc(doc, DBType.MALA_CARDS);
					}
				}
				
				}
		
	}

}
