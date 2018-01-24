package parsers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import persistentdatabase.model.Gene;

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
	
	public void parse() {
		Element rootEl = _doc.get(0).getDocumentElement();
		
		DOMImplementationLS print = (DOMImplementationLS)rootEl.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
		LSSerializer lss = print.createLSSerializer();
		lss.getDomConfig().setParameter("xml-declaration", false);
		String str = lss.writeToString(rootEl);
		
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
		try{
		Gene gene = new Gene();
		Element elem2;
		gene.setGeneID(getAttrContent(geneEl, "uid"));
		
		Element elem = getChildByName(geneEl, "Name");
		gene.setName(getTextContent(elem));
		
		elem = getChildByName(geneEl, "Description");
		gene.setDesc(getTextContent(elem));
		
		elem = getChildByName(geneEl, "Status");
		gene.setStatus(getTextContent(elem));
		
		elem = getChildByName(geneEl, "Chromosome");
		if (getTextContent(elem) != "")
		gene.setChromosom(getTextContent(elem));
		
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
			if (getTextContent(elem2) != "")
				gene.setMim(getTextContent(elem2));
		
		elem = getChildByName(geneEl, "GenomicInfo");
		if (elem != null) {
			elem2 = getChildByName(elem, "GenomicInfoType");
			if (elem2 != null) {
				elem = getChildByName(elem2, "ChrStart");
				if (elem != null)
					if (getTextContent(elem) != "")
						gene.setRangeStart(getTextContent(elem));
				
				elem = getChildByName(elem2, "ChrStop");
				if (elem != null)
					if (getTextContent(elem) != "")
						gene.setRangeStop(getTextContent(elem));
				
				elem = getChildByName(elem2, "ExonCount");
				if (elem != null)
					if (getTextContent(elem) != "")
						gene.setExon(getTextContent(elem));
			}
		}
		
		elem = getChildByName(geneEl, "Summary");
		gene.setSummary(getTextContent(elem));
		
		elem = getChildByName(geneEl, "Organism");
		gene.setOrganism(getTextContent(getChildByName(elem, "ScientificName")),
				getTextContent(getChildByName(elem, "CommonName")), 
				getTextContent(getChildByName(elem, "TaxID")) );
		
	/*	elem = getChildByName(geneEl, "LocationHist");
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
		/*
		
/*		try {
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
*/		
		return gene;
		}
		catch (Exception ex){
			return null;
		
		}

	}
}
