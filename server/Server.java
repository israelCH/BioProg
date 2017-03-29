package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.w3c.dom.Document;

import database.Query;
import database.DataBase.DBType;
import database.Query.SearchType;
import parsers.GeneParser;
import parsers.NLMparser;
import parsers.ProteinParser;
import parsers.StructureParser;
import persistentdatabase.main.PersistAgent;
import persistentdatabase.main.PersistAgentFactory;
import persistentdatabase.model.Book;
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;
import urlInterfaces.Entrez;

//import org.apache.commons.lang.SerializationUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class Server {
	
	//private EntityManager entityManager = EntityManagerUtil.getEntityManager();
	
    public static void main(String[] args) throws Exception {
        System.out.println("The BioProg server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9898);
        try {
            while (true) {
                new SearchQuery(listener.accept(), clientNumber++).start();// run()...
            }
        } finally {
            listener.close();
        }
    }
    
    private static class SearchQuery extends Thread {
    	private Socket socket;
        private int clientNumber;

        public SearchQuery(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            System.out.println("New connection with client# " + clientNumber + " at " + socket);
        }
        
        public void run() {
            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    String input;
                    List<Structure> res;
					try {
						input = (String) in.readObject(); // קריאה ראשונה של קוד פעולה
	                    if (input != null && !input.equals("")) {
	                    	switch (input) {
							case "01": // חיפוש באינטרנט
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
		                    	res = searchFunction(input);
		                    	out.writeObject(res);
							case "02": // שמירה בדטא-בייס פנימי
								List<Book> data = (List<Book>) in.readObject();
								saveLocalFunction(data);
							default:
								break;
							}
	                    }
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            } catch (IOException e) {
            	System.out.println("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                	System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
            }
        }
    }
    
    private static List<Structure> searchFunction(String str) {
    	try {
    	//String result =""; 
		String[] terms = str.split("\\s+");
		Query query = new Query();
		query.setDatabase(DBType.STRUCTURE);
		query.setSearchType(SearchType.SEARCH);
		for (int i = 0; i<terms.length; i++)
			query.addTerm(terms[i]);		
		List<String> results = Entrez.searchEntrez(query);
				
		//PersistAgent persistAgent = new PersistAgent();
		query = new Query();
		query.setDatabase(DBType.STRUCTURE);
		query.setSearchType(SearchType.FETCH);
		for (String id: results)
			query.addId(id);
		
		// Calling Entrez
		Document xmlDocs = Entrez.callEntrez(query);
		
		// Parse answer into a list of books
		StructureParser parser = new StructureParser(xmlDocs);
		parser.parse();
		List<Structure> structures = parser.getPdbs();
		return structures;
    	
		} catch (Exception e1) {
			return null;
		}
    }
    
    private static Boolean saveLocalFunction(List<Book> books) {
    	try {
    		EntityManager entityManager = PersistAgentFactory.getEntityManager();
    		entityManager.getTransaction().begin();
    		entityManager.persist(books.get(0));

    		return true;
		} catch (Exception e1) {
			return false;
		}
    }
}
