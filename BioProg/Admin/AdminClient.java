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

package Admin;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import database.DataBase;
import database.DataBase.DBType;
import persistentdatabase.main.PersistSettings;
import persistentdatabase.model.Article;
import persistentdatabase.model.Disease;
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;

//import org.apache.commons.lang.SerializationUtils;


public class AdminClient {
	
	// path to local repository
	//final static String modelsPath = "/Users/Zorkd/Dropbox/1AFiles/NBEL/aneurysm/Models/";
	final static String logPath    = System.getProperty("user.dir");
	
	private static String ServerAddress;
	private static int ServerPort;
	Socket sock;
	ObjectOutputStream output;
	ObjectInputStream input;
	
	public static void main(String[] args) throws Exception{
		
	}
	
	public <T> List<T> onlineSearch (String str, DBType type)throws Exception // ���� ����
	{
		// ����� ��� ����� ������ ��� �������� ������ ������
		output.writeObject((new DataBase(type)).getValue()); // ������ ��� ����� �����

		output.writeObject(str); // ������ ����� �� ����� ������
		// ���� ���� ��� - ���� ������ �� �������
		Object obj = null;
		switch (type) {
		case STRUCTURE:
			List<Structure> ansStru = (List<Structure>) input.readObject();
			return (List<T>) ansStru;
		case PUBMED:
			 obj = input.readObject();
			List<Article> ansPub = (List<Article>) obj;
			return (List<T>) ansPub;
		case PROTEIN:
			 obj = input.readObject();
			List<Protein> ansPro = (List<Protein>) obj;
			return (List<T>) ansPro;
		case GENE:
			obj = input.readObject();
			List<Gene> ansGene = (List<Gene>) obj;
			return (List<T>) ansGene;
		case MALA_CARDS:
			obj = input.readObject();
			List<Disease> ansMalacards = (List<Disease>) obj;
			return (List<T>) ansMalacards;
			
		default:
			return null;
		}
	}
	
	public Boolean onlineSaveLocal ()throws Exception // ���� ����
	{
		// ����� ��� ����� ������ ��� �������� ������ ������
		output.writeObject("save"); // ������ ����� �� ����� ������
		// ����� �� ����� ���� �� ���� �� ��
		return (Boolean) input.readObject();
	}
	
	public void InitialConnection() throws Exception {
		ServerAddress = new PersistSettings("Admin").getProp("ServerIp");
		if (ServerAddress.substring(0, 5).equals("error")){ // �� ���� ����� �� ���� ����
			throw new Exception("IP not Configured !!");
		}
		
		//ServerAddress = "127.0.0.1"; // <----- localhost
		ServerPort = 9898;
		try {
			sock = new Socket(ServerAddress,ServerPort);
		}
		catch (Exception ex) {
			throw new Exception("Server not running !! cannot connect with server...");
		}
		//sock.setSoTimeout(10000);
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
	
	public Boolean updateMongoDbConnection (String con) throws Exception // ���� ����
	{
		// ����� ��� ����� ������ ��� �������� ������ ������
		output.writeObject("setSettings"); // ������ ����� ������
		output.writeObject(con); // ������ �� ����� ������ ������
		// ����� �� ����� ���� �� ���� �� ��
		return (Boolean) input.readObject();
	}
	
	public String getMongoDbConnection () throws Exception // ���� ����
	{
		// ����� ��� ����� ������ ��� �������� ������ ������
		output.writeObject("getSettings"); // ����� ����� �����
		return (String) input.readObject();
	}
}
