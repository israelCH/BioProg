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

import database.DataBase;
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
import persistentdatabase.model.Gene;
import persistentdatabase.model.Model;
import persistentdatabase.model.Persistable;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;

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
//	private static String[] getAneurysmModelsIds(){
//		
//		File file = new File(modelsPath);
//		String[] directories = file.list(new FilenameFilter() {
//		  public boolean accept(File current, String name) {
//		    return new File(current, name).isDirectory();
//		  }
//		});
//		
//		return directories;
//	}
	
//	private static void addToLog(String log) throws FileNotFoundException{
//		
//		String filePath = logPath + "/files/log.txt";
//		
//		File file = new File(filePath);
//		file.getParentFile().mkdirs();
//		PrintWriter out = new PrintWriter(file);
//		out.println(log + "/n");
//		out.close();
//		
//	}
	
	public <T> List<T> onlineSearch (String str, DBType type)throws Exception // פונה לשרת
	{
		// יצרנו כבר פתיחת תקשורת חוץ בפונקצית איתחול תקשורת
		output.writeObject((new DataBase(type)).getValue()); // שולחים קוד שאומר חיפוש

		output.writeObject(str); // שולחים בפועל את הטקסט לחיפוש
		// השרת מחכה לנו - מעבד ומחזיר את הנתונים
		switch (type) {
		case STRUCTURE:
			List<Structure> ansStru = (List<Structure>) input.readObject();
			return (List<T>) ansStru;
		case PUBMED:
			List<Article> ansPub = (List<Article>) input.readObject();
			return (List<T>) ansPub;
		case PROTEIN:
			List<Protein> ansPro = (List<Protein>) input.readObject();
			return (List<T>) ansPro;
		case GENE:
			List<Gene> ansGene = (List<Gene>) input.readObject();
			return (List<T>) ansGene;
		case MALA_CARDS:
			
			break;

		default:
			break;
		}
		
		return null;
	}
	
//	public List<Structure> onlineSearch (String str, DBType type)throws Exception // פונה לשרת
//	{
//		// יצרנו כבר פתיחת תקשורת חוץ בפונקצית איתחול תקשורת
//		output.writeObject("01"); // שולחים קוד שאומר חיפוש
//		output.writeObject(str); // שולחים בפועל את הטקסט לחיפוש
//		// השרת מחכה לנו - מעבד ומחזיר את הנתונים		
//		List<Structure> answer = (List<Structure>) input.readObject();
//		return answer;
//		
//		//Book[] list = answer.split(";");
//		//List<Book> results= Arrays.asList(list);	
//		//return results;
//	}
	
	public Boolean onlineSaveLocal (List<Book> data)throws Exception // פונה לשרת
	{
		// יצרנו כבר פתיחת תקשורת חוץ בפונקצית איתחול תקשורת
		output.writeObject("02-" + data); // שולחים בפועל את הטקסט לחיפוש
		// השרת מחכה לנו - מעבד ומחזיר את הנתונים		
		List<Book> answer = (List<Book>) input.readObject();
		return true;
		
		//Book[] list = answer.split(";");
		//List<Book> results= Arrays.asList(list);	
		//return results;
	}
	
	public void InitialConnection() throws Exception {
		//System.out.println("before socket.");
		ServerAddress = "127.0.0.1"; // <----- localhost
		ServerPort = 9898;
		sock = new Socket(ServerAddress,ServerPort);
		//System.out.println("trying open output.");
		output = new ObjectOutputStream(sock.getOutputStream());
		//System.out.println("trying open input.");
		input = new ObjectInputStream(sock.getInputStream());
		//System.out.println("input and output r opened.");
	}
}
