package parsers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

//import javafx.css.ParsedValue;
import persistentdatabase.model.Gene;
//import persistentdatabase.model.GeneLocHist;

public class GeneParser extends XMLparser {
	List<Gene> _genes = new ArrayList<Gene>();
	
	public GeneParser(Document doc) throws Exception {

		try {
			_doc = new ArrayList<Document>();
			_doc.add(doc);
		}
		catch(Exception e) {
			System.err.print("Retrieval failed");
		}
	}
	
	public List<Gene> getGenes(){
		return _genes;
	}
	
	public void parse(){
		Element rootEl = _doc.get(0).getDocumentElement();
		Element subRootEl = getChildByName(rootEl, "DocumentSummarySet");
		List<Element> geneElements = getChildrensByName(subRootEl, "DocumentSummary");
		
		if (geneElements.size() == 0) {
			System.err.println("No genes found in document.");
			return;
		}
		
		for(Element geneElement : geneElements) 
			_genes.add(parseSingleArticle(geneElement));
	}
	
	private Gene parseSingleArticle(Element geneEl) {
		Gene gene = new Gene();
		Element elem2;
		Element elem = getChildByName(geneEl, "Name");
		gene.setName(getTextContent(elem));
		
		elem = getChildByName(geneEl, "Description");
		gene.setDesc(getTextContent(elem));
		
		elem = getChildByName(geneEl, "Status");
		gene.setStatus(getTextContent(elem));
		
		elem = getChildByName(geneEl, "Chromosome");
		gene.setChromosom(Integer.parseInt(getTextContent(elem)));
		
		elem = getChildByName(geneEl, "GeneticSource");
		gene.setGeneticSource(getTextContent(elem));
		
		elem = getChildByName(geneEl, "MapLocation");
		gene.setMapLocation(getTextContent(elem));
		
		elem = getChildByName(geneEl, "OtherAliases");
		String temp = getTextContent(elem);
		String[] terms = temp.split(",");
		for (int i = 0; i < terms.length; i++) {
			gene.addAlias(terms[i]);
		}
		
		elem = getChildByName(geneEl, "Mim");
		elem2 = getChildByName(elem, "Int");
		if (elem2 != null)
			gene.setMim(Integer.parseInt(getTextContent(elem2)));
		
		elem = getChildByName(geneEl, "GenomicInfo");
		elem2 = getChildByName(elem, "GenomicInfoType");
		elem = getChildByName(elem2, "ChrStart");
		gene.setRangeStart(Integer.parseInt(getTextContent(elem)));
		elem = getChildByName(elem2, "ChrStop");
		gene.setRangeStop(Integer.parseInt(getTextContent(elem)));
		elem = getChildByName(elem2, "ExonCount");
		gene.setExon(Integer.parseInt(getTextContent(elem)));
		
		elem = getChildByName(geneEl, "Summary");
		gene.setSummary(getTextContent(elem));
		
		elem = getChildByName(geneEl, "Organism");
		gene.setOrganism(getTextContent(getChildByName(elem, "ScientificName")),
				getTextContent(getChildByName(elem, "CommonName")), 
				getTextContent(getChildByName(elem, "TaxID")) );
		
		elem = getChildByName(geneEl, "LocationHist");
		List<Element> elemList = getChildrensByName(elem, "LocationHistType");
		
		if (elemList.size() != 0) {
			for(Element loc : elemList) {
				gene.addLocation(getTextContent(getChildByName(loc, "AnnotationRelease")), 
						getTextContent(getChildByName(loc, "AssemblyAccVer")), 
						getTextContent(getChildByName(loc, "ChrAccVer")), 
						getTextContent(getChildByName(loc, "ChrStart")), 
						getTextContent(getChildByName(loc, "ChrStop")) );
			}
		}
		
		try {
			String projectPath = System.getProperty("user.dir");
			String filePath = projectPath + "/files/abstract/" + gene.getId() + "_abstract.txt";
			
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
		
		return gene;

	}
}
