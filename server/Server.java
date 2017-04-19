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
import parsers.MalaCardsParser;
import parsers.NLMparser;
import parsers.ProteinParser;
import parsers.PubmedParser;
import parsers.StructureParser;
import persistentdatabase.main.PersistAgent;
import persistentdatabase.main.PersistAgentFactory;

import persistentdatabase.model.Article;
import persistentdatabase.model.Book;
import persistentdatabase.model.Disease;
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
                    List<Structure> resSturcture;
                    List<Article> resPubmed;
                    List<Protein> resProtein;
                    List<Gene> resGene;
                    List<Disease> resMalaCards;
					try {
						input = (String) in.readObject(); // קריאה ראשונה של קוד פעולה
	                    if (input != null && !input.equals("")) {
	                    	switch (input) {
							case "01": // STRUCTURE חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resSturcture = searchFunction(DBType.STRUCTURE, input);
		                    	out.writeObject(resSturcture);
							case "02": // PUBMED חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resPubmed = searchFunction(DBType.PUBMED, input);
		                    	out.writeObject(resPubmed);
							case "03": // PROTEIN חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resProtein = searchFunction(DBType.PROTEIN, input);
		                    	out.writeObject(resProtein);
							case "04": // GENE חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resGene = searchFunction(DBType.GENE, input);
		                    	out.writeObject(resGene);
							case "05": // MALA_CARDS חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resMalaCards = searchFunction(DBType.MALA_CARDS, input);
		                    	out.writeObject(resMalaCards);
		                    	
		                    	break;
		                    	
							case "11": // שמירה בדטא-בייס פנימי
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
    
    private static <T> List<T> searchFunction(DBType type, String str) {
    	try {
    	//String result =""; 
		String[] terms = str.split("\\s+");
		Query query = new Query();
		query.setDatabase(type);
		query.setSearchType(SearchType.SEARCH);
		for (int i = 0; i<terms.length; i++)
			query.addTerm(terms[i]);		
		List<String> results = Entrez.searchEntrez(query);
				
		//PersistAgent persistAgent = new PersistAgent();
		query = new Query();
		query.setDatabase(type);
		query.setSearchType(SearchType.FETCH);
		for (String id: results)
			query.addId(id);
		
		// Calling Entrez
		Document xmlDocs = Entrez.callEntrez(query);
		
		// Parse answer into a list
		switch (type) {
		case STRUCTURE:
			StructureParser parserStru = new StructureParser(xmlDocs);
			parserStru.parse();
			List<Structure> structures = parserStru.getPdbs();
			return (List<T>) structures;
		case PUBMED:
			PubmedParser parserPub = new PubmedParser(xmlDocs);
			parserPub.parse();
			List<Article> articles = parserPub.getArticles();
			return (List<T>) articles;
		case PROTEIN:
			ProteinParser parserPro = new ProteinParser(xmlDocs);
			parserPro.parse();
			List<Protein> proteins = parserPro.getProteins();
			return (List<T>) proteins;
		case GENE:
			GeneParser parserGene = new GeneParser(xmlDocs);
			parserGene.parse();
			List<Gene> genes = parserGene.getGenes();
			return (List<T>) genes;
//		case MALA_CARDS:
//			MalaCardsParser parserMala = new MalaCardsParser(xmlDocs, new Query());
//			parserMala.parse();
//			List<Disease> diseases = parserMala.getDiseases();
//			return (List<T>) diseases;

		default:
			return null;
		}
    	
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
