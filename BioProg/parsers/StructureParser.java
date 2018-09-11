package parsers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import persistentdatabase.model.Structure;

public class StructureParser extends XMLparser {

	List<Structure> _pdbs = new ArrayList<Structure>();
	
	public StructureParser(Document doc) throws Exception {

		try {
			_doc = new ArrayList<Document>();
			_doc.add(doc);
		}
		catch(Exception e) {
			System.err.print("Retrieval failed");
		}
	}
	
	public List<Structure> getPdbs(){
		return _pdbs;
	}
	
	public void parse(){
		Element rootEl = _doc.get(0).getDocumentElement();
		List<Element> structureElements = getChildrensByName(rootEl, "DocSum");
		
		if (structureElements.size() == 0) {
			System.err.println("No structures found in document.");
			return;
		}
		
		for(Element structureEl : structureElements) 
			_pdbs.add(parseSinglePdb(structureEl));
	}
	
	private Structure parseSinglePdb(Element structureEl) {
		
		Structure structure = new Structure();
		Element elem = getChildByName(structureEl, "Id");
		structure.setID(getTextContent(elem));
		
		List<Element> itemsList = getChildrensByName(structureEl, "Item");
		
		for(Element item : itemsList) {
			switch (getAttrContent(item, "Name")) {
				case "PdbAcc":
					structure.setPdbID(getTextContent(item));
					break;
				case "PdbDescr":
					structure.setName(getTextContent(item));
					break;
			}
		}
		
/*		try {
			String projectPath = System.getProperty("user.dir");
			String filePath = projectPath + "/files/abstract/" + structure.getId() + "_abstract.txt";
			
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(file);
			
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
*/		
		return structure;

	}
}
