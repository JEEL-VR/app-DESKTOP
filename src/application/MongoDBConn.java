package application;

import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import io.github.cdimascio.dotenv.Dotenv;

public class MongoDBConn {
	private final static Dotenv DOTENV = Dotenv.load();

	public static ArrayList<String> conn(String colName) {
		MongoClientURI uri = new MongoClientURI(DOTENV.get("DATABASE_URL"));

		try (MongoClient mongoClient = new MongoClient(uri)) {
			System.out.println("Connexion creada correctamente FIND ALL " + colName);
			MongoDatabase db = mongoClient.getDatabase("classVRroom");
			MongoCollection<Document> collection = db.getCollection(colName);
			FindIterable<Document> result = collection.find();
			ArrayList<String> colResult = new ArrayList<String>();
			for (Document doc : result) {
				colResult.add(doc.toJson());
			}
			;
			return colResult;
		}
	}
	
	public static void delete(String colName, String ID) {
		MongoClientURI uri = new MongoClientURI(DOTENV.get("DATABASE_URL"));

		try (MongoClient mongoClient = new MongoClient(uri)) {
			System.out.println("Connexion creada correctamente DELETE");
			MongoDatabase db = mongoClient.getDatabase("classVRroom");
			MongoCollection<Document> collection = db.getCollection(colName);
			collection.deleteOne(new Document("_id", new ObjectId(ID)));
			System.out.println("Documento " + ID + " eliminado correctamente");
		}
	}
	
	public static void insert(String colName, Document doc) {
		MongoClientURI uri = new MongoClientURI(DOTENV.get("DATABASE_URL"));

		try (MongoClient mongoClient = new MongoClient(uri)) {
			System.out.println("Connexion creada correctamente INSERT");
			MongoDatabase db = mongoClient.getDatabase("classVRroom");
			MongoCollection<Document> collection = db.getCollection(colName);
			collection.insertOne(doc);
			System.out.println("Documento insertado correctamente correctamente");
		}
	}

	public static ArrayList<String> connByItem(String colName, String item, String value) {
		MongoClientURI uri = new MongoClientURI(DOTENV.get("DATABASE_URL"));

		try (MongoClient mongoClient = new MongoClient(uri)) {
			System.out.println("Connexion creada correctamente SEARCH BY ITEM");
			MongoDatabase db = mongoClient.getDatabase("classVRroom");
			MongoCollection<Document> collection = db.getCollection(colName);
			FindIterable<Document> result = collection.find(eq(item, value));
			ArrayList<String> colResult = new ArrayList<String>();
			for (Document doc : result) {
				colResult.add(doc.toJson());
			}
			;
			return colResult;
		}
	}
	
	public static ArrayList<String> connByID(String colName, String ID) {
		MongoClientURI uri = new MongoClientURI(DOTENV.get("DATABASE_URL"));

		try (MongoClient mongoClient = new MongoClient(uri)) {
			System.out.println("Connexion creada correctamente SERACH BY ID " + colName);
			MongoDatabase db = mongoClient.getDatabase("classVRroom");
			MongoCollection<Document> collection = db.getCollection(colName);
			FindIterable<Document> result = collection.find(new Document("_id", new ObjectId(ID)));
			ArrayList<String> colResult = new ArrayList<String>();
			for (Document doc : result) {
				colResult.add(doc.toJson());
			}
			;
			return colResult;
		}
	}
	
	public static void update(String colName, JSONObject item){
		MongoClientURI uri = new MongoClientURI(DOTENV.get("DATABASE_URL"));
		try (MongoClient mongoClient = new MongoClient(uri)) {
			System.out.println("Connexion creada correctamente UPDATE " + colName);
			MongoDatabase db = mongoClient.getDatabase("classVRroom");
			MongoCollection<Document> collection = db.getCollection(colName);
			
			JSONObject subscribers = (JSONObject) item.get("subscribers");
			JSONObject id = (JSONObject) item.get("_id");
			String itemID = id.get("$oid").toString();
		
			Document query = new Document("_id", new ObjectId(itemID));
			Document setData = new Document().append("subscribers", new Document().append("students", subscribers.get("students")).append("teachers", subscribers.get("teachers")));
			Document update = new Document();
			update.append("$set", setData);
			collection.updateOne(query, update);
		}
	}
}
