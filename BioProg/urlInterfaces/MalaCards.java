/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package urlInterfaces;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;

import database.Query;
//import sun.java2d.pipe.hw.ExtendedBufferCapabilities;


public class MalaCards {

	public static Document callMalaCards(Query query) throws MalformedURLException, IOException{
		
		final String SearchURL = query.getDataBase().getPath();
		
		String queryStr = SearchURL + buildQuery(query);
		
		System.out.println("Query URL: " + queryStr);
		try {
		URLConnection urlConn = new URL(queryStr).openConnection();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(urlConn.getInputStream());
		
				
	//	Document doc = Jsoup.connect(queryStr).timeout(100*1000).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();		
		
		System.out.println("MalaCards connection Worksé");
		
		return doc;
		}
		catch (Exception e){
			return null;
		}
	}

	private static String buildQuery(Query query){
		
		List<String> _terms = query.getTerms();
		
		String queryStr = "";
		
		if (!_terms.isEmpty()){
			for (int i = 0; i < _terms.size() - 1; i++) {
				queryStr += _terms.get(i) + "+%20+";
			}
			queryStr += _terms.get(_terms.size() - 1);
		}
		
		return queryStr;
		
	}	
}
