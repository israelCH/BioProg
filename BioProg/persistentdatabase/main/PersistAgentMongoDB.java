package persistentdatabase.main;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.operation.CreateIndexesOperation;

import Admin.AdminClient;
import database.DataBase;
import database.DataBase.DBType;

import persistentdatabase.model.Persistable;

public class PersistAgentMongoDB {

	private static MongoClientURI uri;
	private static MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	
	public PersistAgentMongoDB(String MongoUri) {
		if (uri == null)
			//uri = new MongoClientURI(new DataBase(DBType.MONGODB).getPath());
			uri = new MongoClientURI(MongoUri);
		if (mongoClient == null){
			mongoClient = new MongoClient(uri);
			System.out.println("### new mongo uri connection ###");
		}
		if (mongoDatabase == null)
			mongoDatabase = mongoClient.getDatabase("BioProg");

	}
	
	private MongoCollection<Document> getColl(DataBase db){

		MongoCollection<Document> retColl = mongoDatabase.getCollection(db.getCollectionName());
		return retColl;
	}	
		
		public boolean chackIfExist(String id, DBType type){
			DataBase db = new DataBase(type);
			MongoCollection<Document> collection = getColl(db);
			Document doc = new Document(db.getUniqueId(),id);
			Document res = collection.find(doc).first();
			if (res == null)
				return false;
			
			return true;
		}
		
		
		
		public Document getDoc(String id, DBType type, String key){
			DataBase db = new DataBase(type);
			MongoCollection<Document> collection = getColl(db);
			Document doc = new Document(db.getUniqueId(),id);
			Document res = collection.find(doc).first();
			Boolean flag = true;
			String temp = res.getString("tags");
			if (temp != null) {
				String[] tmp = temp.split(", ");
				tags:
				for (int i = 0; i < tmp.length; i ++) {
					if(tmp[i].equals(key)){
						flag = false;
						break tags;
					}
				}
				if (flag == true) {
					temp += ", " + key;
				}
			}
			else{
			temp = key;
			}
			res.remove("tags");
			res.append("tags", temp);
			saveDoc(res, type);
			return res;
		}
		int i = 500;
		public Document getTDoc(String id, DBType type){
			DataBase db = new DataBase(type);
			MongoCollection<Document> collection = getColl(db);
			Document doc = new Document(db.getUniqueId(),id);
			Document res = collection.find(doc).first();
			return res;
		}
		
		public boolean saveDoc(Document doc, DBType type){
			try{
			MongoCollection<Document> collection = getColl(new DataBase(type));
			String a = (new DataBase(type) ).getUniqueId();
			String b = doc.getString(a);
		//	Document bdoc = new Document((new DataBase(type) ).getUniqueId() , doc.getString("id"));
			Document bdoc = new Document(a,b);
			UpdateOptions opt = new UpdateOptions();
			opt.upsert(true);
			if(collection.findOneAndReplace(bdoc, doc)== null){
				collection.insertOne(doc);
				
			}
			return true;
			 }
			catch (Exception e) {
				return false;			}
			 
		}
		
		public boolean saveDocs(List<Document> docs, DBType type){
			try{
			MongoCollection<Document> collection = getColl(new DataBase(type));

			collection.insertMany(docs);
			 return true;
			 }
			catch (Exception e) {
				return false;			}
			 
		}
		
		public List<Document> findInMongo(String key, DBType type){
			FindIterable<Document> result;
			List<Document> result2 = new ArrayList<>();
			MongoCollection<Document> collection = getColl(new DataBase(type));
			String[] terms = key.split("\\s+");
			Document doc = new Document("$**" , "text");
			collection.createIndex( doc );
			Document docF = new Document("$text" , new Document("$search" , "/" + key + "/")); //"faecium" <-- מילה עובדת בפרוטאין
			result = collection.find(docF);
			for (Document res : result) {
				result2.add(res);
			}
			return result2;
		}
		
	public void closeConnection(){
		mongoClient.close();	
		System.out.println("### mongo connection closed sucsessfuly ###");

	}
		
	}

