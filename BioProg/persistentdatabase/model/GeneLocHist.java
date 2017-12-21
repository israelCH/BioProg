package persistentdatabase.model;

import java.io.Serializable;

public class GeneLocHist implements Serializable {
	public GeneLocHist(String anr, String asv, String cha, String chsta, String chsto) {
		super();
		annotRelease = anr;
		asemVer = asv;
		charAcc = cha;
		charStart = chsta;
		charStop = chsto;
	}
	private String annotRelease;
	private String asemVer;
	private String charAcc;
	private String charStart;
	private String charStop;
	
	public String getAnnotRelease() { return annotRelease; }
	public String getAsemVer() { return asemVer; }
	public String getCharAcc() { return charAcc; }
	public String getCharStart() { return charStart; }
	public String getCharStop() { return charStop; }
	
	public String getStringObject() {
		String ret = "{ ";
		ret += "'annotRelease' : " + annotRelease + "," ;
		ret += "'asemVer' : " + asemVer + "," ;
		ret += "'charAcc' : " + charAcc + "," ;
		ret += "'charStart' : " + charStart + "," ;
		ret += "'charStop' : " + charStop + "," ;
		
		return ret;
		
	}
}
