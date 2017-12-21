package persistentdatabase.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.bson.Document;

@Entity
public class Gene implements Persistable, Serializable {
	private static final long serialVersionUID = 1L;

	public Gene() {
		
		super();
		//id = generateId();

		//_locations = new ArrayList<GeneLocHist>();
	}
	private static int counter =0;
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;
	private static int generateId()
	{
		return (counter = counter +1);
		 
	}

	public static String ENTITY_NAME = "Gene";

	private String GeneID;

	private String _name;
	private String _description;
	private String _status;
	private String _chromosom;
	private String _geneticSource;
	private String _mapLocation;
	private String _aliases;
	private String _mim;
	private String _rangeStart;
	private String _rangeStop;
	private String _exonCount;
	private String _summary;
	private String _organismName;
	private String _organismCoName;
	private String _organismTaxID;
	//private List<GeneLocHist> _locations;
	
	private List<String>  tags 	   = new ArrayList<String>();
	
	public void addTag(String tag)          	 { tags.add(tag);     	    }
	
	public Gene (Document doc){
		id = doc.getString("GeneID");
		GeneID = doc.getString("GeneID");
		_name = doc.getString("_name");
		_description = doc.getString("_description");
		_status = doc.getString("_status");
		_chromosom = doc.getString("_chromosom");
		_geneticSource = doc.getString("_geneticSource");
		_mapLocation = doc.getString("_mapLocation");
		_aliases = doc.getString("_aliases");//list
		_mim = doc.getString("_mim");
		_rangeStart = doc.getString("_rangeStart");
		_rangeStop = doc.getString("_rangeStop");
		_exonCount = doc.getString("_exonCount");
		_summary = doc.getString("_summary");
		_organismName = doc.getString("_organismName");
		_organismCoName = doc.getString("_organismCoName");
		_organismTaxID = doc.getString("_organismTaxID");
		// tags...
		String temp = doc.getString("tags");
		if (temp != null) {	
			String[] tmp = temp.split(", ");
			for (int i = 0; i < tmp.length; i ++) {
				addTag(tmp[i]);
			}
		}
		//_locations = doc.getString("_locations");//list

	}
	
	public List<String>   getLinkedTags()      { return tags;     }

	public void setGeneID(String id) {
		GeneID = id;
		this.id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setDesc(String desc) {
		_description = desc;
	}

	public void setStatus(String stat) {
		_status = stat;
	}

	public void setChromosom(String chro) {
		_chromosom = chro;
	}

	public void setGeneticSource(String gen) {
		_geneticSource = gen;
	}

	public void setMapLocation(String map) {
		_mapLocation = map;
	}

	public void addAlias(String al) {
		_aliases += al;
		}

	public void setMim(String mim) {
		_mim = mim;
	}

	public void setRangeStart(String start) {
		_rangeStart = start;
	}

	public void setRangeStop(String stop) {
		_rangeStop = stop;
	}

	public void setExon(String ex) {
		_exonCount = ex;
	}

	public void setSummary(String sum) {
		_summary = sum;
	}

	public void setOrganism(String name, String coName, String id) {
		_organismName = name;
		_organismCoName = coName;
		_organismTaxID = id;
	}
	

/*	public void addLocation(String anr, String asv, String cha, String chsta, String chsto) {
		if (_locations == null) {
			_locations = new ArrayList<GeneLocHist>();
		}
		_locations.add(new GeneLocHist(anr, asv, cha, chsta, chsto));
	}
	*/

	// -----------------------------------------

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return GeneID;
	}

	public String getName() {
		return _name;
	}

	public String getDesc() {
		return _description;
	}

	public String getStatud() {
		return _status;
	}

	public String getChromosom() {
		return _chromosom;
	}

	public String getGenericSource() {
		return _geneticSource;
	}

	public String getMapLocation() {
		return _mapLocation;
	}

	public String getAliases() {
		return _aliases;
	}

	public String getMims() {
		return _mim;
	}

	public String getRangeStart() {
		return _rangeStart;
	}

	public String getRangeStop() {
		return _rangeStop;
	}

	public String getExon() {
		return _exonCount;
	}

	public String getSummary() {
		return _summary;
	}

	public String getOrganismName() {
		return _organismName;
	}

	public String getOrganismCoName() {
		return _organismCoName;
	}

	public String getOrganismTaxID() {
		return _organismTaxID;
	}

/*	public List<GeneLocHist> getLocation() {
		return _locations;
	}
*/
	// -----------------------------------------

	public String toString() {
		
		return "Name:" +"\t" + _name + "\n"
		+ "Description:" +"\t" +  _description + "\n"
		+ "Status:" +"\t" + _status + "\n"
		+ "Chromosom:" +"\t" + _chromosom + "\n"
		+ "GeneticSource" + _geneticSource + "\n"
		+ "Map Location:" +"\t" + _mapLocation + "\n"
		+ "Aliases:" +"\t" + _aliases + "\n"
		+ "Mim:" +"\t" +  _mim + "\n"
		+ "Range Start:" +"\t" + _rangeStart + "\n"
		+ "Range Stop:" +"\t" + _rangeStop + "\n"
		+ "Exon Count:" +"\t" + _exonCount + "\n"
		+ "Summary:" +"\t" + _summary + "\n"
		+ "Organism Name:" +"\t" + _organismName + "\n"
		+ "Organism CoName:" +"\t" +  _organismCoName + "\n"
		+ "Organism TaxID:" +"\t" + _organismTaxID
		+ "\n" + "Tags: " + list2string();
	}

	// public String getAbstract() {
	// String abstStr = "";
	// try {
	// abstStr = new String(Files.readAllBytes(Paths.get(_abst)));
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return abstStr;
	// }

	// public void setAbstract(String abst) { _abst = abst; }

	/*
	 * (non-Javadoc)
	 * 
	 * @see persistentdatabase.model.Persistable#getEntityName()
	 */
	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public String getIdIdentifier() {
		return "GeneUniqueID";
	}
	
	public Document toBson(){
		Document doc = new Document();

		doc.append("id", id);
		doc.append("GeneID", GeneID);
		doc.append("_name", _name);
		doc.append("_description", _description);
		doc.append("_status", _status);
		doc.append("_chromosom", _chromosom);
		doc.append("_geneticSource", _geneticSource);
		doc.append("_mapLocation", _mapLocation);
		doc.append("_aliases", _aliases);
		doc.append("_mim", _mim);
		doc.append("_rangeStart", _rangeStart);
		doc.append("_rangeStop", _rangeStop);
		doc.append("_exonCount", _exonCount);
		doc.append("_summary", _summary);
		doc.append("_organismName", _organismName);
		doc.append("_organismCoName", _organismCoName);
		doc.append("_organismTaxID", _organismTaxID);
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
