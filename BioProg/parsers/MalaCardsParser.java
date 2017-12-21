/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package parsers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.jsoup.Jsoup;
//import org.jsoup.helper.StringUtil;
//import org.jsoup.select.NodeTraversor;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

//import com.sun.org.apache.xalan.internal.xsltc.dom.DOMBuilder;

import persistentdatabase.model.Disease;
import database.Query;

public class MalaCardsParser extends XMLparser {
	
	private Document _doc;
	private Document _docMini;
	private String[] _terms;
	private List<Disease> _diseases;
	private String _keyword;
	
	
	
	public MalaCardsParser (Document doc, String key){
	
	//public MalaCardsParser (Document doc, Query query){
		//Element rootEl = doc.getDocumentElement();
	//	rootEl = rootEl.getElementById("body");
		_doc = doc;
		_keyword = key;
		
	//	_terms = terms;
	//	_diseases = new ArrayList<Disease>();
	}
	
	public MalaCardsParser (Document doc){
			_docMini = doc;
	}
	
	public void parse() {
	// get page title
		Element rootEl = _doc.getDocumentElement();
		Element elm = getChildByName(rootEl, "body");
		Element table = getChildrensByName(elm, "div").get(0);
		table = getChildByName(table, "div");
		table = getChildrensByName(table, "div").get(3);
		table = getChildByName(table, "div");
		table = getChildByName(table, "table");
	
//		DOMImplementationLS print = (DOMImplementationLS)table.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
//		LSSerializer lss = print.createLSSerializer();
//		lss.getDomConfig().setParameter("xml-declaration", false);
//		String str = lss.writeToString(table);
		String title = getAttrContent(table, "class");
		System.out.println("title : " + title);
		table = getChildByName(table, "tbody");
		List<Element> tblRows = getChildrensByName(table, "tr");
		tblRows.remove(0);	//מחיקת שורת כותרת
		List<Element> rowTds;
		Disease disease;
		_diseases = new ArrayList<Disease>();
		Element tr;
		for (int i = 0; i< tblRows.size() && i < 100; i += 2) {
			tr = tblRows.get(i);
			rowTds = getChildrensByName(tr, "td");
			disease = new Disease();
			disease.setMCID(rowTds.get(3).getTextContent());
			elm = getChildByName(rowTds.get(4), "a");
			disease.setName(elm.getTextContent());
			disease.setKeyword(_keyword);
			_diseases.add(disease);
			
		}
		
		


		final int LIMIT = 20;
		int count = 0;
		
		// get all links
/*		Elements links = _doc.select("a[href]");
		Disease disease;
		for (int ind = 0; ind < _terms.length; ind ++) {
			for (Element link : links) {
				if ( link.attr("href").contains(_terms[ind]) && (count < LIMIT) && (_terms[ind] != "")){
					disease = new Disease();
					disease.setURLlink( link.attr("href"));
					disease.setName(link.text());
					_diseases.add(disease);
					count++;
				}
			}
		}*/

	  }
	
	public List<Disease> getDiseases(){ //יש לבצע משיכת נתונים מהמונגו
		return _diseases;
	}
	
	public Disease parseMinicard() { //http://www.malacards.org/card/BLD053
		
		// צריך לתרגם את הדף המלא ולא מיני דף
		
		Disease dis = new Disease();
		Element rootEl = _docMini.getDocumentElement();
		Element elm = getChildByName(rootEl, "body");
		Element table = getChildrensByName(elm, "div").get(0); // itemscope
		table = getChildrensByName(table, "div").get(0); // container
		table = getChildrensByName(table, "div").get(3); // content
		table = getChildrensByName(table, "div").get(0); // top-card
		List<Element> divs = getChildrensByName(table, "div"); // 3-sumeries, 6-therapeutics
		//go to 3-summary
		table = getChildByName(divs.get(2), "table"); 
		table = getChildByName(table, "tbody"); 
		table = getChildByName(table, "tr"); 
		table = getChildByName(table, "td"); 
		table = getChildByName(table, "div"); //section-data
		table = getChildByName(table, "div");  //summary
		DOMImplementationLS print = (DOMImplementationLS)table.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
		LSSerializer lss = print.createLSSerializer();
		lss.getDomConfig().setParameter("xml-declaration", false);
		String str = lss.writeToString(table).replaceAll("\\<.*?>", "");
		dis.setSummerias(Jsoup.parse(lss.writeToString(table)).text());
		
/*		//go to 6-therapeutics
		table = getChildByName(divs.get(5), "table"); 
		table = getChildByName(table, "tbody"); 
		table = getChildByName(table, "tr"); 
		table = getChildByName(table, "td"); 
		table = getChildByName(table, "div"); //section-data
		List<Element> tables = getChildrensByName(table, "table");
		table = getChildByName(tables.get(0), "tbody");  //drugs
		List<Element> trs = getChildrensByName(table, "tr");
		String drgs = "";
		for(int i =0; i<trs.size();i++){
			if(trs.get(i).getAttribute("class").equals( ""))
				drgs += getChildrensByName(trs.get(i), "td").get(2).getTextContent() + ", ";
		}
		dis.setDrugs(drgs);
		drgs = "";
		table = getChildByName(tables.get(1), "tbody");  //therapeutics
		trs = getChildrensByName(table, "tr");
		for(int i =0; i<trs.size();i++){
			drgs += getChildrensByName(trs.get(i), "td").get(1).getTextContent() + ", ";
		}
		dis.setTherapeutics(drgs);
*/		
		return dis;
	}
	
}
