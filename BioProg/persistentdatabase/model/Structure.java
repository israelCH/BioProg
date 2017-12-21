package persistentdatabase.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.bson.Document;

public class Structure implements Persistable, Serializable {
	private static final long serialVersionUID = 1L;

	public Structure() {
		super();
		//_id = generateId();
	}
	
	public static String ENTITY_NAME = "Structure";
	private static int counter =0;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	
	private String _PDBid;
	
	private String _name;
	
	private List<String>  tags 	   = new ArrayList<String>();
	
	public void setID(String id) {
		this.id = id;
	}
	
	public void setPdbID(String id) {
		_PDBid = id;
	}
	
	public void setName(String name) {
		_name = name;
	}

	// -----------------------------------------
	
	@Override
	public String getIdIdentifier() {
		return id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}
	
	public String getPdbID() {
		return _PDBid;
	}
	
	public String getName() {
		return _name;
	}
	
	// -----------------------------------
	
	@Override
	public String toString() {
		String url = "https://www.ncbi.nlm.nih.gov/Structure/icn3d/full.html?mmtfid=" + _PDBid; 
		return "<a href='" + url + "'> " + _name + " </a>";
	}
	
	public Document toBson(){
		Document doc = new Document();

		doc.append("id", id);
		doc.append("_name", _name);
		doc.append("_PDBid", _PDBid);
		doc.append("tags", list2string());


		
		return doc;
	}
	
	public Structure (Document doc){
		id = doc.getString("id");
		_name = doc.getString("_name");
		_PDBid = doc.getString("_PDBid");
		// tags...
		String temp = doc.getString("tags");
		if (temp != null) {	
			String[] tmp = temp.split(", ");
			for (int i = 0; i < tmp.length; i ++) {
				addTag(tmp[i]);
			}
		}
	}
	
	public List<String>   getLinkedTags()      { return tags;     }

	
	public void addTag(String tag)          	 { tags.add(tag);     	    }
	
//	private static int generateId() {
//		
//		return (counter = counter +1);
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
}
