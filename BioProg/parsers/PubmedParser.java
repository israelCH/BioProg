package parsers;

/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import persistentdatabase.model.Article;

public class PubmedParser extends XMLparser{
	
	List<Article> _articles = new ArrayList<Article>();
	
	public PubmedParser(Document doc) throws Exception {

		try {
			_doc = new ArrayList<Document>();
			_doc.add(doc);
		}
		catch(Exception e) {
			System.err.print("Retrieval failed");
		}
	}
	
	public List<Article> getArticles(){
		return _articles;
	}
	
	public void parse(){
		Element rootEl = _doc.get(0).getDocumentElement();
		List<Element> pubmedArticleElements = getChildrensByName(rootEl, "PubmedArticle");
		
		if (pubmedArticleElements.size() == 0) 
			System.err.println("No articles found in document.");
		
		for(Element pubmedArticle : pubmedArticleElements) 
			_articles.add(parseSingleArticle(pubmedArticle));
	}
	
	
	private Article parseSingleArticle(Element pubmedArticleEl) {
		
		Element medlineCitationEl = getChildByName(pubmedArticleEl, "MedlineCitation");
		
		
		Element idEl = getChildByName(medlineCitationEl, "PMID");
		String  idStr = getTextContent(idEl);
		
		Element articleEl = getChildByName(medlineCitationEl, "Article");
		Element journalEl = getChildByName(articleEl, "Journal");
		Element journalIssueEl = getChildByName(journalEl, "JournalIssue");
		Element journalVolumeEl = getChildByName(journalIssueEl, "Volume");
		Element issueEl = getChildByName(journalIssueEl, "Issue");
		
		String volume = "?";
		if (journalVolumeEl != null)
			volume = getTextContent(journalVolumeEl);
		
		String issue = "?";
		if (issueEl != null)
			issue = getTextContent(issueEl);
		
		Element pubDateEl = getChildByName(journalIssueEl, "PubDate");
		Element yearEl = getChildByName(pubDateEl, "Year");
		String year = "";
		if (yearEl != null)
			year = getTextContent(yearEl);
				
		Element journalTitleEl = getChildByName(journalEl, "Title");
		String journalTitle = getTextContent(journalTitleEl);
		
		Element abstractEl = getChildByName(articleEl, "Abstract");
		String allTxt = "";
		if (abstractEl != null){
			List<Element> abstractElements = getChildrensByName(abstractEl, "AbstractText");
			String lbl, context;
			for (Element abs: abstractElements){
				lbl = getAttrContent(abs, "Label");
				context = getTextContent(abs);
				allTxt += lbl + " : " + context + "\n";
			}
		}
		Element authorsEl = getChildByName(articleEl, "AuthorList");
		String authors = "";
		if(authorsEl != null){
		List<Element> authorsElem = getChildrensByName(authorsEl, "Author");
		for(Element autor : authorsElem){
			Element collNameEl = getChildByName(autor, "CollectiveName");
			if(collNameEl != null)
			authors += getTextContent(collNameEl) + ", ";
			else {
			Element lastNameEl = getChildByName(autor, "LastName");
			
			String lastName = "",foreName = ""; 
			
			if(lastNameEl != null)
			 lastName = getTextContent(lastNameEl);
			Element foreNameEl = getChildByName(autor, "ForeName");
			if(foreNameEl != null)
			foreName = getTextContent(foreNameEl);
			authors += lastName + " " + foreName + ", ";
			}
		}
		}
		
		String DOI = "?";
		Element DOI_El = getChildByName(articleEl, "ELocationID");
		if (!(DOI_El==null))
			DOI = getTextContent(DOI_El);
		
		Element articleTitleEl = getChildByName(articleEl, "ArticleTitle");
		String title = getTextContent(articleTitleEl);
		
		Element keywEl = getChildByName(medlineCitationEl, "KeywordList");
		String ke2 = "";
		if(keywEl != null) {
			List<Element> keys = getChildrensByName(keywEl, "Keyword");
			for (Element key : keys) {
				ke2 += getTextContent(key) + ", ";
			}
			if (ke2.length() > 1)
				ke2 = ke2.substring(0, ke2.length() - 3);
		}
		
//		Element abstractEl = getChildByName(articleEl, "Abstract");
//		StringBuilder absStr = new StringBuilder();
//
//		if (abstractEl != null) {
//			NodeList abstractChildren = abstractEl.getChildNodes();
//			for(int i=0; i<abstractChildren.getLength(); i++) {
//				Node child = abstractChildren.item(i);
//				if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("AbstractText")) {
//					absStr.append( getTextContent( (Element)child));
//				}
//			}
//		}
//		if (absStr.length() == 0)
//			absStr = new StringBuilder("?");
		
		Article article = new Article();
		article.setDOI(DOI);
		article.setAuthor(authors);
		article.setId(idStr);
		article.setPMID(idStr);
		article.setJournal(journalTitle);
		article.setVolume(volume);
		article.setIssue(issue);
		article.setYear(year);
		article.setTitle(title.replace("\t", " "));
		article.setAbstract(allTxt);
		article.setKeywords(ke2);
		
/*		try {
			String projectPath = System.getProperty("user.dir");
			String filePath = projectPath + "/files/abstract/" + idStr + "_abstract.txt";
			
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(file);
			
			String prtAbstract =  absStr.toString().replace("\t", " ");
			out.println(prtAbstract);
			article.setAbstract(filePath);
			
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// article.setAbstract(absStr.toString().replace("\t", " "));
*/		
		return article;

	}
	
}
