package persistentdatabase.main;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class PersistSettings {

	String rootPath;// = System.getProperty("user.home");
	String appConfigPath;// = rootPath + "/Documents/BioProg_srv.ini";
	
	public PersistSettings(String name) { // CONSTRUCTOR
		rootPath = System.getProperty("user.home");
		appConfigPath = rootPath + "/Documents/BioProg_" + name + ".ini";
	}
	
	public String saveProp(String key, String value) {			
		try {
			Properties pro = new Properties();
			pro.put(key,value);
			pro.store(new FileWriter(appConfigPath), "store to properties file");
			return "";
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			return ex.toString();
		}		
	}

	
	public String getProp(String key) {			
		try {
			Properties pro2 = new Properties();
			pro2.load(new FileInputStream(appConfigPath));
			return pro2.getProperty(key);
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			return "error:" + ex.toString();
		}
	}
}
