/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
 * Before you begin, please add the following jar files to
 * your project's build path:
 * -> commons-csv-1.2.jar           @ https://commons.apache.org/proper/commons-csv/
 * -> derby.jar                     @ https://db.apache.org/derby/
 * -> eclipselink.jar     			@ https://eclipse.org/eclipselink/downloads/
 * -> javax.persistence_2.1.0.jar   @ http://mvnrepository.com/artifact/org.eclipse.persistence/javax.persistence/2.1.0
 * -> jsoup-1.8.3.jar				@ http://jsoup.org/download
 * 
 * The following modification to the persistence.xml (@ META-INF folder)
 * are also needed:
 * -> change the value of the property: 'javax.persistence.jdbc.url' to
 *    your project path 
 * 
 * If this framework is used for the construction of a new databse
 * please note the following:
 * -> modify the classes' list in the persistence.xml file (META-INF folder)
 *    to consist your entity classes
 * -> each of your entity classes has to be annotated with @<Entity name>, 
 *    and implement the 'Persistable' class
 * 
 */

package client;

//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;

import com.sun.corba.se.spi.orbutil.fsm.Guard.Result;

import database.DataBase.DBType;
import database.Fields.SearchFields;
import database.Query;
import database.Query.SearchType;
import urlInterfaces.Biomodels;
import urlInterfaces.Entrez;
import urlInterfaces.MalaCards;
import parsers.BiomodelsParser;
import parsers.CSVparser;
import parsers.MalaCardsParser;
import parsers.NLMparser;
import parsers.PubmedParser;
import persistentdatabase.main.PersistAgent;
import persistentdatabase.model.Aneurysm;
import persistentdatabase.model.Article;
import persistentdatabase.model.Book;
import persistentdatabase.model.Disease;
import persistentdatabase.model.Model;
import persistentdatabase.model.Persistable;

import org.apache.commons.lang.SerializationUtils;


public class Client {
	
	// path to local repository
	final static String modelsPath = "/Users/Zorkd/Dropbox/1AFiles/NBEL/aneurysm/Models/";
	final static String logPath    = System.getProperty("user.dir");
	
	private static String ServerAddress;
	private static int ServerPort;
	Socket sock;
	ObjectOutputStream output;
	ObjectInputStream input;
	
	public static void main(String[] args) throws Exception{	
		
	}
	
	// Scan aneurysms folder (that contains the aneurisk repository for data retrieval
	private static String[] getAneurysmModelsIds(){
		
		File file = new File(modelsPath);
		String[] directories = file.list(new FilenameFilter() {
		  public boolean accept(File current, String name) {
		    return new File(current, name).isDirectory();
		  }
		});
		
		return directories;
	}
	
	private static void addToLog(String log) throws FileNotFoundException{
		
		String filePath = logPath + "/files/log.txt";
		
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		PrintWriter out = new PrintWriter(file);
		out.println(log + "/n");
		out.close();
		
	}
	
	public String testQuary ()throws Exception
	{
		String result =""; 
				
		PersistAgent persistAgent = new PersistAgent();
		Query query = new Query();
		query.setDatabase(DBType.PUBMED);
		query.addId("23371018");
		query.addId("10227670");
		query.setSearchType(SearchType.FETCH);
		
		// Calling Entrez
		Document xmlDocs = Entrez.callEntrez(query) ;
		
		// Parse answer into a list of articles
		PubmedParser parser = new PubmedParser(xmlDocs);
		parser.parse();
		List<Article> articles = parser.getArticles();
		
		for (Article article: articles){
			persistAgent.PersistObject(article);
		}
		
		// print persisted articles
		persistAgent.showObjects(Article.ENTITY_NAME);
		
		// get persisted articles back and print their abstracts from files
		List<Persistable> articlesPer = persistAgent.getObjectsList(Article.ENTITY_NAME);
		for (int i = 0; i < articlesPer.size(); i++){
			Article article = (Article) articlesPer.get(i);
			result += article.getAbstract() + "---------------------------------------" +"\r\n"  ;
		}
		return result;
	}
	
	public String testQuary2 (String str)throws Exception
	{
		String result =""; 
		String[] terms = str.split("\\s+");
		Query query6 = new Query();
		query6.setDatabase(DBType.PUBMED);
		for (int i = 0; i<terms.length; i++)
			query6.addTerm(terms[i]);
		query6.setSearchType(SearchType.SEARCH);
		List<String> results = Entrez.searchEntrez(query6);
				
		PersistAgent persistAgent = new PersistAgent();
		Query query = new Query();
		query.setDatabase(DBType.PUBMED);
		for (String id: results)
			query.addId(id);

		query.setSearchType(SearchType.FETCH);
		
		// Calling Entrez
		Document xmlDocs = Entrez.callEntrez(query) ;
		
		// Parse answer into a list of articles
		PubmedParser parser = new PubmedParser(xmlDocs);
		parser.parse();
		List<Article> articles = parser.getArticles();
		
		for (Article article: articles){
			persistAgent.PersistObject(article);
		}
		
		// print persisted articles
		persistAgent.showObjects(Article.ENTITY_NAME);
		
		// get persisted articles back and print their abstracts from files
		List<Persistable> articlesPer = persistAgent.getObjectsList(Article.ENTITY_NAME);
		for (int i = 0; i < articlesPer.size(); i++){
			Article article = (Article) articlesPer.get(i);
			result += article.getAbstract() + "---------------------------------------" +"\r\n"  ;
		}
		return result;
	}
	
	public String testQuary3 (String str)throws Exception
	{
		String result =""; 
		String[] terms = str.split("\\s+");
		Query query6 = new Query();
		query6.setDatabase(DBType.PUBMED);
		for (int i = 0; i<terms.length; i++)
			query6.addTerm(terms[i]);
		query6.setSearchType(SearchType.SEARCH);
		List<String> results = Entrez.searchEntrez(query6);
				
		PersistAgent persistAgent = new PersistAgent();
		Query query = new Query();
		query.setDatabase(DBType.PUBMED);
		for (String id: results)
			query.addId(id);

		query.setSearchType(SearchType.FETCH);
		
		// Calling Entrez
		Document xmlDocs = Entrez.callEntrez(query) ;
		
		// Parse answer into a list of articles
		PubmedParser parser = new PubmedParser(xmlDocs);
		parser.parse();
		List<Article> articles = parser.getArticles();
		
		for (Article article: articles){
			//persistAgent.PersistObject(article);
			result += article.toString() + "---------------------------------------" +"\r\n"  ;
		}
		
		// print persisted articles
		//persistAgent.showObjects(Article.ENTITY_NAME);
		
		// get persisted articles back and print their abstracts from files
		//List<Persistable> articlesPer = persistAgent.getObjectsList(Article.ENTITY_NAME);
		//for (Persistable article : articlesPer) {
			//result += article.toString() + "---------------------------------------" +"\r\n"  ;
		//}
		
	   
		return result;
	}
	
	public List<Article> testQuary4 (String str)throws Exception
	{
		
		//String result =""; 
		String[] terms = str.split("\\s+");
		Query query6 = new Query();
		query6.setDatabase(DBType.PUBMED);
		for (int i = 0; i<terms.length; i++)
			query6.addTerm(terms[i]);
		query6.setSearchType(SearchType.SEARCH);
		List<String> results = Entrez.searchEntrez(query6);
				
		//PersistAgent persistAgent = new PersistAgent();
		Query query = new Query();
		query.setDatabase(DBType.PUBMED);
		for (String id: results)
			query.addId(id);

		query.setSearchType(SearchType.FETCH);
		
		// Calling Entrez
		Document xmlDocs = Entrez.callEntrez(query) ;
		
		// Parse answer into a list of articles
		PubmedParser parser = new PubmedParser(xmlDocs);
		parser.parse();
		List<Article> articles = parser.getArticles();
		
		//for (Article article: articles){
		//	 Button btn = new Button(comp, SWT.NONE);
		//	 btn.setText(article.getId());
			
			
			
		//}
		
		
		
	   
		return articles;
	}
	
	public List<Book> testQuary5 (String str)throws Exception
	{		
		String result =""; 
		String[] terms = str.split("\\s+");
		Query query6 = new Query();
		query6.setDatabase(DBType.NLM_catalog);
		query6.setSearchType(SearchType.SEARCH);
		for (int i = 0; i<terms.length; i++)
			query6.addTerm(terms[i]);		
		List<String> results = Entrez.searchEntrez(query6);
				
		PersistAgent persistAgent = new PersistAgent();
		Query query = new Query();
		query.setDatabase(DBType.NLM_catalog);
		query.setSearchType(SearchType.FETCH);
		for (String id: results)
			query.addId(id);
		
		// Calling Entrez
		Document xmlDocs = Entrez.callEntrez(query);
		
		// Parse answer into a list of books
		NLMparser parser = new NLMparser(xmlDocs);
		parser.parse();
		List<Book> books = parser.getBooks();
		
		return books;
	}
	
	public List<Book> onlineSearch (String str)throws Exception // ���� ����
	{
		// ����� ��� ����� ������ ��� �������� ������ ������
		output.writeObject(str); // ������ ����� �� ����� ������
		// ���� ���� ��� - ���� ������ �� �������
		//String answer = input.readLine(); // ���� ��� ����� ���� ����� �-������
		
		List<Book> answer = new ArrayList<Book>();
		Book ans = (Book) input.readObject(); // ���� ��� ����� ���� ����� �-������
		while(ans != null) {
			answer.add(ans);
			ans = (Book) input.readObject();
		}
		
		//List<Book> answer = (List<Book>) input.readObject();
		return answer;
		
		//Book[] list = answer.split(";");
		//List<Book> results= Arrays.asList(list);	
		//return results;
	}
	
	public void InitialConnection() throws Exception {
		System.out.println("before socket.");
		ServerAddress = "127.0.0.1"; // <----- localhost
		ServerPort = 9898;
		sock = new Socket(ServerAddress,ServerPort);
		System.out.println("trying open output.");
		output = new ObjectOutputStream(sock.getOutputStream());
		System.out.println("trying open input.");
		input = new ObjectInputStream(sock.getInputStream());
		System.out.println("input and output r opened.");
	}
}
