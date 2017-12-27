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

package user;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import database.DataBase;
import database.DataBase.DBType;
import persistentdatabase.model.Article;
import persistentdatabase.model.Book;
import persistentdatabase.model.Disease;
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;

//import org.apache.commons.lang.SerializationUtils;


public class UserClient {
	
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
	
	public <T> List<T> MongoSearch (String str, DBType type)throws Exception // פונה לשרת
	{
		// יצרנו כבר פתיחת תקשורת חוץ בפונקצית איתחול תקשורת
		output.writeObject((new DataBase(type)).getValue() + "Mongo"); // שולחים קוד שאומר חיפוש

		output.writeObject(str); // שולחים בפועל את הטקסט לחיפוש
		// השרת מחכה לנו - מעבד ומחזיר את הנתונים
		Object obj = null;
		switch (type) {
		case STRUCTURE:
			List<Structure> ansStru = (List<Structure>) input.readObject();
			if (ansStru.size() > 0)
				return (List<T>) ansStru;
				else
					return null;
		case PUBMED:
			 obj = input.readObject();
			List<Article> ansPub = (List<Article>) obj;
			if (ansPub.size() > 0)
				return (List<T>) ansPub;
				else
					return null;
		case PROTEIN:
			 obj = input.readObject();
			List<Protein> ansPro = (List<Protein>) obj;
			if (ansPro.size() > 0)
				return (List<T>) ansPro;
				else
					return null;
		case GENE:
			obj = input.readObject();
			List<Gene> ansGene = (List<Gene>) obj;
			if (ansGene.size() > 0)
				return (List<T>) ansGene;
				else
					return null;
		case MALA_CARDS:
			obj = input.readObject();
			List<Disease> ansMalacards = (List<Disease>) obj;
			if (ansMalacards.size() > 0)
			return (List<T>) ansMalacards;
			else
				return null;
			
		default:
			return null;
		}
	}
	
	public Boolean onlineSaveLocal ()throws Exception // פונה לשרת
	{
		// יצרנו כבר פתיחת תקשורת חוץ בפונקצית איתחול תקשורת
		output.writeObject("save"); // שולחים בפועל את הטקסט לחיפוש
		// נחזיר את תגובת השרת אם נשמר או לא
		return (Boolean) input.readObject();
	}
	
	public void InitialConnection() throws Exception {
		//System.out.println("before socket.");
		ServerAddress = "127.0.0.1"; // <----- localhost
		//ServerAddress = "192.168.203.3"; // <----- remoteServer
		ServerPort = 9898;
		sock = new Socket(ServerAddress,ServerPort);
		//System.out.println("trying open output.");
		output = new ObjectOutputStream(sock.getOutputStream());
		//System.out.println("trying open input.");
		input = new ObjectInputStream(sock.getInputStream());
		//System.out.println("input and output r opened.");
	}
	
	public Disease GetMiniCard (Disease dis){
		
		try {
			output.writeObject("minicard");		
			output.writeObject(dis);
//			dis.setSummerias((String)input.readObject());
//			dis.setDrugs((String)input.readObject());
//			dis.addAliases((List<String>)input.readObject());
//			dis.byRefCopy();
			dis = (Disease)input.readObject();
			return dis;
		} catch (Exception e) {
			return null;
		}

	}
}
