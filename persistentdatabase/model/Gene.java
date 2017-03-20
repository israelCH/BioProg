package persistentdatabase.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Gene implements Persistable, Serializable {
	private static final long serialVersionUID = 1L;

	public Gene() {
		super();
		_aliases = new ArrayList<String>();
		_locations = new ArrayList<GeneLocHist>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String id;

	public static String ENTITY_NAME = "Gene";

	private String GeneID;

	private String _name;
	private String _description;
	private String _status;
	private int _chromosom;
	private String _geneticSource;
	private String _mapLocation;
	private List<String> _aliases;
	private int _mim;
	private int _rangeStart;
	private int _rangeStop;
	private int _exonCount;
	private String _summary;
	private String _organismName;
	private String _organismCoName;
	private String _organismTaxID;
	private List<GeneLocHist> _locations;

	public void setGeneID(String id) {
		GeneID = id;
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
	
	public void setChromosom(Integer chro) {
		_chromosom = chro;
	}
	
	public void setGeneticSource(String gen) {
		_geneticSource = gen;
	}
	
	public void setMapLocation(String map) {
		_mapLocation = map;
	}
	
	public void addAlias(String al) {
		if (_aliases == null) {
			_aliases = new ArrayList<String>();
		}
		_aliases.add(al);
	}
	
	public void setMim(Integer mim) {
		_mim = mim;
	}
	
	public void setRangeStart(int start) {
		_rangeStart = start;
	}
	
	public void setRangeStop(int stop) {
		_rangeStop = stop;
	}
	
	public void setExon(int ex) {
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
	
	public void addLocation(String anr, String asv, String cha, String chsta, String chsto) {
		if (_locations == null) {
			_locations = new ArrayList<GeneLocHist>();
		}
		_locations.add(new GeneLocHist(anr, asv, cha, chsta, chsto));
	}
	
	//-----------------------------------------
	
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
	public int getChromosom() {
		return _chromosom;
	}
	
	public String getGenericSource() {
		return _geneticSource;
	}
	
	public String getMapLocation() {
		return _mapLocation;
	}
	
	public List<String> getAliases() {
		return _aliases;
	}
	
	public Integer getMims() {
		return _mim;
	}
	
	public int getRangeStart() {
		return _rangeStart;
	}
	
	public int getRangeStop() {
		return _rangeStop;
	}
	
	public int getExon() {
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
	
	public List<GeneLocHist> getLocation() {
		return _locations;
	}
	
	//-----------------------------------------


//	public String toString() {
//		String str;
//		str = "ID: " + NlmID + "\n" 
//				+ "TITLE:  " + _titleMain + "\n" ;
//		for(int i=0 ; i < _authorsList.size() ; i++){
//			str += "AUTHOR: " + _authorsList.get(i).toString() + "\n";
//		}
//		str += _datePublished.toString();
//		return str;
//	}

//	public String getAbstract() { 
//			String abstStr = "";
//			try {
//				abstStr = new String(Files.readAllBytes(Paths.get(_abst)));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			return abstStr;
//	}

	//public void setAbstract(String abst)    { _abst       = abst;    }
	
	@Override
	public String getEntityName() {
		return ENTITY_NAME;
	}

	@Override
	public String getIdIdentifier() {
		return "GeneUniqueID";
	}
}
