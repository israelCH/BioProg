package parsers;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import persistentdatabase.model.Article;
import persistentdatabase.model.Book;

public class NLMparser extends XMLparser{
	
	List<Book> _books = new ArrayList<Book>();
	
	public NLMparser(Document doc) throws Exception {

		try{
			_doc = new ArrayList<Document>();
			_doc.add(doc);
		}
		catch(Exception e){
			System.err.print("Retrieval failed");
		}
	}
	
	public List<Book> getBooks(){
		return _books;
	}
	
	public void parse(){
		Element rootEl = _doc.get(0).getDocumentElement();
		List<Element> nlmBookElements = getChildrensByName(rootEl, "NLMCatalogRecord");
		
		if (nlmBookElements.size() == 0) {
			System.err.println("No articles found in document.");
			return; // �� �� ����� �� ��� ��������
		}
		
		for(Element nlmBook : nlmBookElements) 
			_books.add(parseSingleArticle(nlmBook));
	}
	
	private Book parseSingleArticle(Element nlmCatalogEl) {
		
		Book book = new Book();
		
		Element nlmEl = getChildByName(nlmCatalogEl, "NlmUniqueID");
		book.setNlmID(getTextContent(nlmEl));
		
		nlmEl = getChildByName(nlmCatalogEl, "DateCreated");
		book.setDatePublished(getTextContent(getChildByName(nlmEl,"Year")), getTextContent(getChildByName(nlmEl,"Month")), getTextContent(getChildByName(nlmEl,"Day")));

		book.setTitleMain(getTextContent(getChildByName(getChildByName(nlmCatalogEl, "TitleMain"),"Title")));
		book.setTitleSub(getTextContent(getChildByName(getChildByName(nlmCatalogEl, "TitleRelated"),"Title")));
		
		book.setPublicationCountry(getTextContent(getChildByName(getChildByName(nlmCatalogEl, "PublicationInfo"),"Country")));
		
		book.setLanguage(getTextContent(getChildByName(nlmCatalogEl, "Language")));
		
		List<Element> auth = getChildrensByName(getChildByName(nlmCatalogEl,"AuthorList"),"Author");
		for(Element author : auth) {
			book.addBookAuthor(getTextContent(getChildByName(nlmCatalogEl, "LastName")),
							getTextContent(getChildByName(nlmCatalogEl, "ForeName")),
							getTextContent(getChildByName(nlmCatalogEl, "Initials")),
							getTextContent(getChildByName(nlmCatalogEl, "DatesAssociatedWithName")),
							getTextContent(getChildByName(nlmCatalogEl, "Role")));
		}
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
			
		Element authorsEl = getChildByName(articleEl, "AuthorList");
		Element authorEl = getChildByName(authorsEl, "Author");
		Element lastNameEl = getChildByName(authorEl, "LastName");
		String lastName = getTextContent(lastNameEl);
		Element foreNameEl = getChildByName(authorEl, "ForeName");
		String foreName = getTextContent(foreNameEl);
		String authors = lastName + " " + foreName;
		
		String DOI = "?";
		Element DOI_El = getChildByName(articleEl, "ELocationID");
		if (!(DOI_El==null))
			DOI = getTextContent(DOI_El);
		
		Element articleTitleEl = getChildByName(articleEl, "ArticleTitle");
		String title = getTextContent(articleTitleEl);
		
		Element abstractEl = getChildByName(articleEl, "Abstract");
		StringBuilder absStr = new StringBuilder();

		if (abstractEl != null) {
			NodeList abstractChildren = abstractEl.getChildNodes();
			for(int i=0; i<abstractChildren.getLength(); i++) {
				Node child = abstractChildren.item(i);
				if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("AbstractText")) {
					absStr.append( getTextContent( (Element)child));
				}
			}
		}
		if (absStr.length() == 0)
			absStr = new StringBuilder("?");
		
		Article article = new Article();
		article.setDOI(DOI);
		article.setAuthor(authors);
		article.setId(idStr);
		article.setJournal(journalTitle);
		article.setVolume(volume);
		article.setIssue(issue);
		article.setYear(year);
		article.setTitle(title.replace("\t", " "));
		
		try {
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
		
		return article;

	}
	
}
