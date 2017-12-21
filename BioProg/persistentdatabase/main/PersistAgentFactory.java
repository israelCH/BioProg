package persistentdatabase.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistAgentFactory {
	private static final EntityManagerFactory emFactory;
	
	static {
		   emFactory = Persistence.createEntityManagerFactory("persistentdatabase.main");
	}
	public static EntityManager getEntityManager(){
		return emFactory.createEntityManager();
	}
	public static void close(){
		emFactory.close();
	}
}
