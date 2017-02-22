package server;

//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.w3c.dom.Document;

import database.Query;
import database.DataBase.DBType;
import database.Query.SearchType;
import parsers.NLMparser;
import persistentdatabase.main.PersistAgent;
import persistentdatabase.model.Book;
import urlInterfaces.Entrez;

import org.apache.commons.lang.SerializationUtils;

public class Server {
    public static void main(String[] args) throws Exception {
        System.out.println("The capitalization server is running.");
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
                    List<Book> res;
					try {
						input = (String) in.readObject();
	                    if (input != null && !input.equals("")) {
	                    	res = searchFunction(input);
	                    	for (Book book: res)
	                    		out.writeObject(book);
	                    		//out.writeObject(SerializationUtils.serialize(book));
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
    
    private static List<Book> searchFunction(String str) {
    	try {
    	//String result =""; 
		String[] terms = str.split("\\s+");
		Query query = new Query();
		query.setDatabase(DBType.NLM_catalog);
		query.setSearchType(SearchType.SEARCH);
		for (int i = 0; i<terms.length; i++)
			query.addTerm(terms[i]);		
		List<String> results = Entrez.searchEntrez(query);
				
		//PersistAgent persistAgent = new PersistAgent();
		query = new Query();
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
		
		// מפה זה אם רוצים להחזיר סטרינג אחד ארוך - אבל שינינו שיחזיר מערך אוביקטים
//		String backString = "";
//		for (Book book: books)
//			backString += book.getTitle() + ";";		
//    	return backString.substring(0,backString.length() - 1);	
    	
		} catch (Exception e1) {
			return null;
		}
    }
}
