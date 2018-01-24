/*
 * Author: Elishai Ezra, Jerusalem College of Technology
 * Version: V1.0: Dec 1st, 2015
 * 
*/

package urlInterfaces;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import database.DataBase.DBType;
import database.Fields;
import database.Query;
import database.Fields.SearchFields;

public class Entrez {
	
	
	
	public static List<String> searchEntrez(Query query) throws MalformedURLException, IOException, InterruptedException {
		
		List<String> uilist = new ArrayList<String>();
		
		final String entrezSearchURL = "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/esearch.fcgi?";
		
		String queryStr = entrezSearchURL + buildQuery(query) + "&rettype=uilist";
		System.out.println("Query URL: " + queryStr);
		
		DataInputStream dataIn= new DataInputStream(new URL(queryStr).openStream());
		
		InputStreamReader dataStr = new InputStreamReader(dataIn);
		
		BufferedReader dataBfr = new BufferedReader(dataStr);
		
		String s = dataBfr.readLine();
		
		while (s != null){	
			if (s.startsWith("<Id>"))
				uilist.add(s.substring(4, s.length()-5));
			s = dataBfr.readLine();
		}
		
		return uilist;
	}

	public static Document callEntrez(Query query) 
			throws IOException, ParserConfigurationException, SAXException {
		
		final String entrezFetchURL ; 
		String queryStr;
		Document doc = null;

		if(query.getDataBase().getType() != DBType.MALA_CARDS){
			entrezFetchURL= "https://eutils.ncbi.nlm.nih.gov/entrez/eutils/";
			if (query.getDataBase().getType() == DBType.GENE || query.getDataBase().getType() == DBType.PROTEIN || query.getDataBase().getType() == DBType.STRUCTURE) {
			queryStr = entrezFetchURL + "esummary.fcgi?" + buildQuery(query);
			}
			else {
			queryStr = entrezFetchURL + "efetch.fcgi?" + buildQuery(query);
			}
			
			URLConnection urlConn = new URL(queryStr).openConnection();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(urlConn.getInputStream());
			
		}
		else{
			entrezFetchURL= query.getDataBase().getPath();
			queryStr = entrezFetchURL + buildQuery(query);
			
			try{
				org.jsoup.nodes.Document doci = org.jsoup.Jsoup.connect(queryStr).timeout(600000).header("Accept-Encoding", "gzip, deflate").maxBodySize(0).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
				W3CDom w3cDom = new W3CDom();
				doc = w3cDom.fromJsoup(doci);
			}
			catch (IOException ioe) {

			}
			
		}
		
		//String queryStr = entrezFetchURL + buildQuery(query);
		System.out.println("Query URL: " + queryStr);
		
//        
//        urlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
//        urlConn.setConnectTimeout(100*1000);
//        urlConn.connect();
//
//        
//		DocumentBuilder builder = factory.newDocumentBuilder();
//		InputStream ins = null;
		
		

//		doc = builder.parse(ins);

		return doc;
	}
	
	
	
	private static String buildQuery(Query query){
		String queryStr;
		
		if ((query.getDataBase().getType().equals(DBType.PUBMED)) ||
		    (query.getDataBase().getType().equals(DBType.PUBMED_CENTRAL)) ||
		    (query.getDataBase().getType().equals(DBType.NLM_catalog)) ||
		    (query.getDataBase().getType().equals(DBType.STRUCTURE)) ||
		    (query.getDataBase().getType().equals(DBType.GENE)) ||
		    (query.getDataBase().getType().equals(DBType.PROTEIN)) ){
			
			queryStr = "db=" + query.getDataBase().getPath();
			
			List<String> _ids = query.getIds();
			Fields _fields = query.getFields();
			List<String> _terms = query.getTerms();
			
			if (!_ids.isEmpty()){
				queryStr += "&id=";
				
				//for (int i = 0; i < 2 ; i++)
					for (int i = 0; i < _ids.size() - 1; i++)
					queryStr += _ids.get(i) + ",";
				queryStr +=   _ids.get(_ids.size()-1);
			}
			
			if ((!_fields.isEmpty()) || (!_terms.isEmpty()))
				queryStr += "&term=";
			
			if (!_fields.isEmpty()){
				for(Map.Entry<SearchFields,String> entry : _fields.getTermsMap().entrySet()) {
					String value = entry.getValue();
					queryStr += value;
					queryStr += _fields.getField(entry.getKey()) + "+";
				}
			}
			
			if (!_terms.isEmpty()){
				for (int i = 0; i < _terms.size() - 1; i++) {
					queryStr += _terms.get(i) + "+AND+";
				}
				queryStr += _terms.get(_terms.size() - 1);
			}
			
			queryStr += "&retmode=xml";
			return queryStr;
		} else {
			if(query.getDataBase().getType().equals(DBType.MALA_CARDS)) {
				List<String> _terms = query.getTerms();
				
				queryStr = "";
				
				if (!_terms.isEmpty()){
					for (int i = 0; i < _terms.size() - 1; i++) {
						queryStr += _terms.get(i) + "+%20+";
					}
					queryStr += _terms.get(_terms.size() - 1);
				}
				
				return queryStr;
			}
			else{
			System.err.println("Wrong Database");
			return null;
			}
		}
	}

}
