package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import persistentdatabase.model.Protein;

public class ProteinParser extends XMLparser {
	List<Protein> _proteins = new ArrayList<Protein>();
	
	public ProteinParser(Document doc) throws Exception {

		try {
			_doc = new ArrayList<Document>();
			_doc.add(doc);
		}
		catch(Exception e) {
			System.err.print("Retrieval failed");
		}
	}
	
	public List<Protein> getProteins(){
		return _proteins;
	}
	
	public void parse(){
		Element rootEl = _doc.get(0).getDocumentElement();
		List<Element> proteinElements = getChildrensByName(rootEl, "DocSum");
		
		if (proteinElements.size() == 0) {
			System.err.println("No proteins found in document.");
			return;
		}
		
		for(Element protein : proteinElements) 
			_proteins.add(parseSingleArticle(protein));
	}
	
	private Protein parseSingleArticle(Element proteinEl) {
		
		Protein protein = new Protein();
		Element elem = getChildByName(proteinEl, "Id");
		protein.setProtID(getTextContent(elem));
		
		List<Element> itemsList = getChildrensByName(proteinEl, "Item");
		for(Element item : itemsList) {
			switch (getAttrContent(item, "Name")) {
				case "Caption":
					protein.setCaption(getAttrContent(item, "Name"));
					break;
				case "Title":
					protein.setTitle(getAttrContent(item, "Name"));
					break;
				case "Gi":
					protein.setGi(Integer.parseInt(getAttrContent(item, "Name")));
					break;
				case "CreateDate":
					protein.setCreateDate(getAttrContent(item, "Name"));
					break;
				case "UpdateDate":
					protein.setUpdateDate(getAttrContent(item, "Name"));
					break;
				case "Flags":
					protein.setFlags(Integer.parseInt(getAttrContent(item, "Name")));
					break;
				case "TaxID":
					protein.setTaxID(Integer.parseInt(getAttrContent(item, "Name")));
					break;
				case "Length":
					protein.setLength(Integer.parseInt(getAttrContent(item, "Name")));
					break;
				case "Status":
					protein.setStatus(getAttrContent(item, "Name"));
					break;
				case "ReplacedBy":
					protein.setReplacedBy(getAttrContent(item, "Name"));
					break;
				case "Comment":
					protein.setComment(getAttrContent(item, "Name"));
					break;
				case "AccessionVersion":
					protein.setAccessionVersion(getAttrContent(item, "Name"));
					break;
			}
		}
		
		try {
			String projectPath = System.getProperty("user.dir");
			String filePath = projectPath + "/files/abstract/" + protein.getId() + "_abstract.txt";
			
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(file);
			
//			String prtAbstract =  absStr.toString().replace("\t", " ");
//			out.println(prtAbstract);
//			article.setAbstract(filePath);
			
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// article.setAbstract(absStr.toString().replace("\t", " "));
		
		return protein;

	}
}
