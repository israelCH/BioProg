package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;

//import com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType;

import database.Query;
import database.DataBase.DBType;
import database.Query.SearchType;
import parsers.GeneParser;
import parsers.MalaCardsParser;
import parsers.ProteinParser;
import parsers.PubmedParser;
import parsers.StructureParser;
import persistentdatabase.main.PersistAgentMongoDB;
import persistentdatabase.main.PersistSettings;
import persistentdatabase.model.Article;
import persistentdatabase.model.Disease;
import persistentdatabase.model.Gene;
import persistentdatabase.model.Protein;
import persistentdatabase.model.Structure;

import urlInterfaces.Entrez;

import javax.swing.JFrame;

public class Server {
	
	static PersistAgentMongoDB mongoAgent = null;
	static List <String> existObj = null;
	static List<Structure> structures = null;
	static List<Article> articles = null;
	static List<Protein> proteins = null;
	static List<Gene> genes = null;
	static List<Disease> diseases = null;
	
	//private EntityManager entityManager = EntityManagerUtil.getEntityManager();
	
    public static void main(String[] args) throws Exception {
        System.out.println("The BioProg server is running.");
        int clientNumber = 0;

    	ServerGui serverGui = new ServerGui();
    	serverGui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	serverGui.setVisible(true);
        
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
            System.out.println("New connection with client #" + clientNumber + " at socket " + socket);
        }
        
        public void run() {
            try {            	
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    String input;
                    Integer swClose = 0;
                    Disease inp;
                    PersistSettings ps;
                    List<Structure> resSturcture;
                    List<Article> resPubmed;
                    List<Protein> resProtein;
                    List<Gene> resGene;
                    List<Disease> resMalaCards;
					try {
						input = (String) in.readObject(); // קריאה ראשונה של קוד פעולה
	                    if (input != null && !input.equals("")) {
	                    	if (! input.equals("setSettings") && ! input.equals("getSettings")) {
	                    			swClose = 1; // מסמן שצריך לסגור את החיבור בסוף
	                    			mongoAgent = new PersistAgentMongoDB();  // 06.09.18
	                    	}
	                    	switch (input) {
							case "structure": // STRUCTURE חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resSturcture = searchFunction(DBType.STRUCTURE, input);
		                    	out.writeObject(resSturcture);		                    	
		                    	break;
							case "pubmed": // PUBMED חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								articles = null; // איפוס עבור חיפושים חוזרים
								resPubmed = searchFunction(DBType.PUBMED, input);
		                    	out.writeObject(resPubmed);		                    	
		                    	break;
							case "protein": // PROTEIN חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								proteins = null; //איפוס עבור תוצאות חוזרות
								resProtein = searchFunction(DBType.PROTEIN, input);
		                    	out.writeObject(resProtein);		                    	
		                    	break;
							case "gene": // GENE חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resGene = searchFunction(DBType.GENE, input);
		                    	out.writeObject(resGene);		                    	
		                    	break;
							case "malacards": // MALA_CARDS חיפוש בבסיס נתונים
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resMalaCards = searchFunction(DBType.MALA_CARDS, input);
		                    	out.writeObject(resMalaCards);		                    	
		                    	break;
							case "structureMongo": // STRUCTURE חיפוש מונגו
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resSturcture = searchMongo(DBType.STRUCTURE, input);
		                    	out.writeObject(resSturcture);		                    	
		                    	break;
							case "pubmedMongo": // PUBMED חיפוש מונגו
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								articles = null; // איפוס עבור חיפושים חוזרים
								resPubmed = searchMongo(DBType.PUBMED, input);
		                    	out.writeObject(resPubmed);
		                    	break;
							case "proteinMongo": // PROTEIN חיפוש מונגו
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								proteins = null; //איפוס עבור תוצאות חוזרות
								resProtein = searchMongo(DBType.PROTEIN, input);
		                    	out.writeObject(resProtein);		                    	
		                    	break;
							case "geneMongo": // GENE חיפוש מונגו
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resGene = searchMongo(DBType.GENE, input);
		                    	out.writeObject(resGene);		                    	
		                    	break;
							case "malacardsMongo": // MALA_CARDS חיפוש מונגו
								input = (String) in.readObject(); // קריאה שניה לקבל את המחרוזת לחיפוש
								resMalaCards = searchMongo(DBType.MALA_CARDS, input);
		                    	out.writeObject(resMalaCards);		                    	
		                    	break;
							case "minicard": // השלמת נתוני malacard לפי ID 
								inp = (Disease) in.readObject(); 
								Disease miniDis = getMiniCard(inp);
								for(Disease dis:diseases) {
									if (dis.getId().equals(miniDis.getId())) {
										dis.setSummerias(miniDis.getSummaries());
									}
								}
								out.writeObject(miniDis);		                    	
		                    	break;

		                    	
							case "save": // שמירה בדטא-בייס פנימי
								if (saveLocalFunction())
									out.writeObject(true);
								else
									out.writeObject(false);
		                    	break;
		                    	
							case "setSettings": // שמירת החיבור למונגו
								input = (String) in.readObject(); // קורא את המפתח הנדרש
								ps = new PersistSettings("Server");
								if (ps.saveProp("MongoURI", input).equals(""))
									out.writeObject(true);
								else
									out.writeObject(false);
		                    	break;
		                    	
							case "getSettings": // קריאת החיבור למונגו
								ps = new PersistSettings("Server");
								input = ps.getProp("MongoURI");
								out.writeObject(input);
		                    	break;
		                    	
							default:
								break;
							}
	                    	if (swClose == 1) {
	                    		mongoAgent.closeConnection(); // 06.09.18
	                    	}
	                    }
					}
					catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
            } 
            catch (EOFException e) {
            	System.out.println("Eof error:" + e);
            	
            }
            catch (IOException e) {
            	System.out.println("Error handling client# " + clientNumber + ": " + e);
            } 

            finally {
                try {
                    socket.close();
                } catch (IOException e) {
                	System.out.println("Couldn't close a socket, what's going on?");
                }
                System.out.println("Connection with client# " + clientNumber + " closed");
            }
        }
    }
    
    //############ sharch in MongoDb #######################
    private static <T> List<T> searchMongo(DBType type, String str) {
    	try {  			
			//mongoAgent  = new PersistAgentMongoDB();
			List<org.bson.Document> lst = mongoAgent.findInMongo(str, type);
			
			// Parse answer into a list
			switch (type) {
			case STRUCTURE:
				structures = new ArrayList<Structure>();
				for (org.bson.Document doc : lst) {
					structures.add(new Structure(doc));
				}
				return (List<T>) structures;
			case PUBMED:
				articles = new ArrayList<Article>();
				for (org.bson.Document doc : lst) {
					articles.add(new Article(doc));
				}
				return (List<T>) articles;
			case PROTEIN:
				proteins = new ArrayList<Protein>();
				for (org.bson.Document doc : lst) {
					proteins.add(new Protein(doc));
				}
				return (List<T>) proteins;
			case GENE:
				genes = new ArrayList<Gene>();
				for (org.bson.Document doc : lst) {
					genes.add(new Gene(doc));
				}
				return (List<T>) genes;
			case MALA_CARDS:
				diseases = new ArrayList<Disease>();
				for (org.bson.Document doc : lst) {
					diseases.add(new Disease(doc));
				}
				return (List<T>) diseases;

			default:
				return null;
		}
    	
		} catch (Exception e1) {
			return null;
		} /* finally {
            try {
                mongoAgent.closeConnection();
            } catch (Exception e2) {
            	return null;
            }
        } */
    }
    
    
    private static <T> List<T> searchFunction(DBType type, String str) {
    	try {  
    		existObj = null; // בכדי למנוע תוצאות קודמות
		String[] terms = str.split("\\s+");
		Query query = new Query();
		query.setDatabase(type);
		query.setSearchType(SearchType.SEARCH);
		for (int i = 0; i<terms.length; i++)
			query.addTerm(terms[i]);
		Document xmlDocs = null;
		if (type.equals(DBType.MALA_CARDS) ) {
			//W3CDom w3cDom = new W3CDom();
			// Calling MalaCards
			xmlDocs = Entrez.callEntrez(query);
		} else {
			List<String> results = Entrez.searchEntrez(query);
			
			//mongoAgent  = new PersistAgentMongoDB();  06.09.18
			 query = new Query();
			query.setDatabase(type);
			query.setSearchType(SearchType.FETCH);
			for (String id: results){
				if (mongoAgent.checkIfExist(id, type)){
					if (existObj == null)
						existObj = new ArrayList<String>() ;	

					existObj.add(id);
					
					}
				else
					query.addId(id);
			}
			// Calling Entrez
			if(! query.ifIdsEmpty())
			xmlDocs = Entrez.callEntrez(query);
		}
		
		// Parse answer into a list
		switch (type) {
		case STRUCTURE:
			if(! query.ifIdsEmpty()){
				StructureParser parserStru = new StructureParser(xmlDocs);
				parserStru.parse();
				structures = parserStru.getPdbs();
				for (Structure struc : structures){
					struc.addTag(str);
				}
			}
			if (existObj != null){
				for (String id: existObj){
					if (structures == null)
						structures = new ArrayList<>();
					structures.add(new Structure(mongoAgent.getDoc(id, DBType.STRUCTURE, str)));
				}
			}
			return (List<T>) structures;
		case PUBMED:
			if(! query.ifIdsEmpty()){
			PubmedParser parserPub = new PubmedParser(xmlDocs);
			parserPub.parse();
			articles = parserPub.getArticles();
				for(Article art: articles){
					art.addTag(str);
				}
			}
			if (existObj != null){
				for (String id: existObj){
					if (articles == null)
						articles = new ArrayList<>();
				articles.add(new Article (mongoAgent.getDoc(id, DBType.PUBMED, str)));
				}
			}
			return (List<T>) articles;
		case PROTEIN:
			if(! query.ifIdsEmpty()){
			ProteinParser parserPro = new ProteinParser(xmlDocs);
			parserPro.parse();
			proteins = parserPro.getProteins();
			for(Protein prot: proteins){
				prot.addTag(str);
			}
			}
			if (existObj != null){
				for (String id: existObj){
					if (proteins == null)
						proteins = new ArrayList<>();
					proteins.add(new Protein (mongoAgent.getDoc(id, DBType.PROTEIN, str)));
				}
			}
			return (List<T>) proteins;
		case GENE:
			if(! query.ifIdsEmpty()){
			GeneParser parserGene = new GeneParser(xmlDocs);
			parserGene.parse();
			genes = parserGene.getGenes();
			for(Gene gen: genes){
				gen.addTag(str);
			}
			}
			if (existObj != null){
				for (String id: existObj){
					if (genes == null)
						genes = new ArrayList<>();
					genes.add(new Gene (mongoAgent.getDoc(id, DBType.GENE, str)));
				}
			}
			return (List<T>) genes;
		case MALA_CARDS:
			MalaCardsParser parserMala = new MalaCardsParser(xmlDocs , str);
			//MalaCardsParser parserMala = new MalaCardsParser((org.jsoup.nodes.Document)xmlDocs);
			parserMala.parse();
			diseases = parserMala.getDiseases();
			for(Disease dis: diseases){
				dis.addTag(str);
				dis.syncMongo(mongoAgent);
			}
			// בשמירה לא צריך כי כבר בחיפוש הראשוני שולפים הכל והמונגו לא מוסיף לנו כלום
			return (List<T>) diseases;

		default:
			return null;
		}
    	
		} catch (Exception e1) {
			return null;
		}
    }
    
    private static Disease getMiniCard(Disease dis) {
    	final String entrezFetchURL = "http://previous.malacards.org/card/"; 
		String queryStr = entrezFetchURL + dis.getId();
		System.out.println("Query URL: " + queryStr);
		Document doc = null;
		try {
			org.jsoup.nodes.Document doci = org.jsoup.Jsoup.connect(queryStr).timeout(600000).header("Accept-Encoding", "gzip, deflate").maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
			W3CDom w3cDom = new W3CDom();
			doc = w3cDom.fromJsoup(doci);
			MalaCardsParser mcp = new MalaCardsParser(doc);
			Disease ds = mcp.parseMinicard();
			dis.setSummerias(ds.getSummaries());
	    	dis.setDrugs(ds.getDrugs());
	    	dis.setTherapeutics(ds.getTherapeutics());
			
			//mongoAgent  = new PersistAgentMongoDB();  06.09.18
			org.bson.Document doc2 = mongoAgent.getTDoc(dis.getId(), DBType.MALA_CARDS);
			
			if (doc2 != null) {
				mongoAgent.saveDoc(dis.toBson(), DBType.MALA_CARDS);
			}
		
	    	return dis;
		}
		catch (Exception ex) {
			return null;
		} /* finally {
            try {
                mongoAgent.closeConnection();
            } catch (Exception e2) {
            	return null;
            }
        } */
    }
    
    
	//----------------------------------------------------------------------------------------		
		
	private static Boolean saveLocalFunction() {
    	try {
    		PersistAgentMongoDB agent = new PersistAgentMongoDB();//create connection
    		List<org.bson.Document> tmp = new ArrayList<org.bson.Document>();
    		
    		// save Pubmed
    		if (articles != null){
    		for (Article art: articles){
    			if (! agent.checkIfExist(art.getId(), DBType.PUBMED))
    				tmp.add(art.toBson());
    		
    		}
    		if (tmp != null)
    			agent.saveDocs(tmp, DBType.PUBMED);
    		tmp.clear();
    		}
    		//save Protein
    		if(proteins != null){
    		for (Protein pro: proteins){
    			if (! agent.checkIfExist(pro.getId(), DBType.PROTEIN))
    				tmp.add(pro.toBson());
    			
    		}
    		if (tmp != null)
    			agent.saveDocs(tmp, DBType.PROTEIN);
    		tmp.clear();
    		}
    		// save Gene
    		if (genes != null){
    		for (Gene gen: genes){
    			if (! agent.checkIfExist(gen.getId(), DBType.GENE))
    				tmp.add(gen.toBson());
    		}
    		if (tmp != null)
    			agent.saveDocs(tmp, DBType.GENE);
    		tmp.clear();
    		}
       		// save Structure
    		if (structures != null){
    		for (Structure struc: structures){
    			if (! agent.checkIfExist(struc.getId(), DBType.STRUCTURE))
    				tmp.add(struc.toBson());
    		}
    		if (tmp != null)
    			agent.saveDocs(tmp, DBType.STRUCTURE);
    		tmp.clear();
    		}
    		
    		// save Malacards
    		if(diseases != null){
    		for (Disease mala: diseases){
    			if (! agent.checkIfExist(mala.getId(), DBType.MALA_CARDS))
    				tmp.add(mala.toBson());
    		}
    		if (tmp != null)
    			agent.saveDocs(tmp, DBType.MALA_CARDS);
    		tmp.clear();
    		}
    		agent.closeConnection();// end connection

    		return true;
		} catch (Exception e1) {
			return false;
		}
    }
}
