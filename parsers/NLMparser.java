package parsers;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
			return; // אם לא מצאנו אז נצא מהרוטינה
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
			book.addBookAuthor(getTextContent(getChildByName(author, "LastName")),
							getTextContent(getChildByName(author, "ForeName")),
							getTextContent(getChildByName(author, "Initials")),
							getTextContent(getChildByName(author, "DatesAssociatedWithName")),
							getTextContent(getChildByName(author, "Role")));
		}
		
		nlmEl = getChildByName(nlmCatalogEl, "ContentsNote");
		if (nlmEl != null)
			book.setContent(getTextContent(nlmEl));
		else
			nlmEl = getChildByName(getChildByName(nlmCatalogEl, "OtherAbstract "),"AbstractText");
		if (nlmEl != null)
			book.setContent(getTextContent(nlmEl));
		
		try {
			String projectPath = System.getProperty("user.dir");
			String filePath = projectPath + "/files/abstract/" + book.getIdIdentifier() + "_NlmBook.txt";
			
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			PrintWriter out = new PrintWriter(file);
			
			String prtAbstract =  book.getAbstract();
			out.println(prtAbstract);
			book.setAbstract(filePath);
			
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return book;

	}
	
}
