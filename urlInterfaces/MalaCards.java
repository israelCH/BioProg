/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package urlInterfaces;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import database.Query;

public class MalaCards {

	
	private static String buildQuery(Query query){
		
		List<String> _terms = query.getTerms();
		
		String queryStr = "";
		
		if (!_terms.isEmpty()){
			for (int i = 0; i < _terms.size() - 1; i++) {
				queryStr += _terms.get(i) + "+AND+";
			}
			queryStr += _terms.get(_terms.size() - 1);
		}
		
		return queryStr;
		
	}
	
	public static Document callMalaCards(Query query) throws MalformedURLException, IOException{
		
		final String SearchURL = query.getDataBase().getPath();
		
		String queryStr = SearchURL + buildQuery(query);
		
		System.out.println("Query URL: " + queryStr);
		
		Document doc = Jsoup.connect(queryStr).timeout(10*1000).get();
		System.out.println("Connected");
		
		return doc;
		
	}

	
}
