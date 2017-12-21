package persistentdatabase.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.bson.Document;

public class Protein implements Persistable, Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private String id;
	public static String ENTITY_NAME = "Protein";
	private static int counter =0;
	public Protein() {
		super();
		//id = generateId();
	}

	
	private String ProteinID;

	private String _caption;
	private String _title;
	private String _gi;
	private String _createDate;
	private String _updateDate;
	private String _flags;
	private String _taxID;
	private String _length;
	private String _status;
	private String _replacedBy;
	private String _comment;
	private String _accessionVersion;
	
	private List<String>  tags 	   = new ArrayList<String>();

	public void setProtID(String id) {
		ProteinID = id;
		this.id=id;
	}
	
	public Protein(Document doc){
		id = doc.getString("ProteinID");
		ProteinID = doc.getString("ProteinID");
		_caption = doc.getString("_caption");
		_title = doc.getString("_title");
		_gi = doc.getString("_gi");
		_createDate = doc.getString("_createDate");
		_updateDate = doc.getString("_updateDate");
		_flags = doc.getString("_flags");
		_taxID = doc.getString("_taxID");
		_length = doc.getString("_length");
		_status = doc.getString("_status");
		_replacedBy = doc.getString("_replacedBy");
		_comment = doc.getString("_comment");
		_accessionVersion = doc.getString("_accessionVersion");
		// tags...
		String temp = doc.getString("tags");
		if (temp != null) {	
			String[] tmp = temp.split(", ");
			for (int i = 0; i < tmp.length; i ++) {
				addTag(tmp[i]);
			}
		}
	}

//	private static int generateId() {
//		
//		return (counter = counter +1);
//	}

	public void setCaption(String caption) {
		_caption = caption;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setGi(String gi) {
		_gi = gi;
	}

	public void setCreateDate(String createDate) {
		_createDate = createDate;
	}

	public void setUpdateDate(String updateDate) {
		_updateDate = updateDate;
	}

	public void setFlags(String flags) {
		_flags = flags;
	}

	public void setTaxID(String taxID) {
		_taxID = taxID;
	}

	public void setLength(String length) {
		_length = length;
	}

	public void setStatus(String stat) {
		_status = stat;
	}

	public void setReplacedBy(String replacedBy) {
		_replacedBy = replacedBy;
	}

	public void setComment(String comment) {
		_comment = comment;
	}

	public void setAccessionVersion(String accessionVersion) {
		_accessionVersion = accessionVersion;
	}
	
	public void addTag(String tag)          	 { tags.add(tag);     	    }

	// -----------------------------------------

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return ProteinID;
	}

	public String getCaption() {
		return _caption;
	}

	public String getTitle() {
		return _title;
	}

	public String getGi() {
		return _gi;
	}

	public String getCreateDate() {
		return _createDate;
	}

	public String getUpdateDate() {
		return _updateDate;
	}

	public String getFlags() {
		return _flags;
	}

	public String getTaxID() {
		return _taxID;
	}

	public String getLength() {
		return _length;
	}

	public String getStatud() {
		return _status;
	}

	public String getReplacedBy() {
		return _replacedBy;
	}

	public String getComment() {
		return _comment;
	}

	public String getAccessionVersion() {
		return _accessionVersion;
	}

	@Override
	public String getIdIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEntityName() {
		// TODO Auto-generated method stub
		return ENTITY_NAME;
	}
	
	public List<String>   getLinkedTags()      { return tags;     }

	
	@Override
	public String toString() {
		String str;
		str = "ID: " + ProteinID + "\n" 
				+ "TITLE:  " + _title + "\n" ;
		str += _createDate.toString() + "\n" + "Tags: " + list2string();
		return str;
	}
	
	public Document toBson(){
		Document doc = new Document();
		
		doc.append("ProteinID", ProteinID);
		doc.append("id", id);
		doc.append("_caption", _caption);
		doc.append("_title", _title);
		doc.append("_gi", _gi);
		doc.append("_createDate", _createDate);
		doc.append("_updateDate", _updateDate);
		doc.append("_flags", _flags);
		doc.append("_taxID", _taxID);
		doc.append("_length", _length);
		doc.append("_status", _status);
		doc.append("_replacedBy", _replacedBy);
		doc.append("_comment", _comment);
		doc.append("_accessionVersion", _accessionVersion);
		doc.append("tags", list2string());

		return doc;
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
}