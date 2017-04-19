package persistentdatabase.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Structure implements Persistable, Serializable {
	private static final long serialVersionUID = 1L;

	public Structure() {
		super();
	}
	
	public static String ENTITY_NAME = "Structure";
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private String _id;
	
	private String _PDBid;
	
	private String _name;
	
	public void setID(String id) {
		_id = id;
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
		return _id;
	}

	@Override
	public String getId() {
		return _id;
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
}
