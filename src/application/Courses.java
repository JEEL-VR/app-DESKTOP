package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Courses {
	static ArrayList<JSONObject> jCourses;
	static JSONObject jCourse;

	public static void main(String[] args) throws Exception {
	}

	public static void getAllCourses() {
		JSONParser jsonParser = new JSONParser();
		ArrayList<String> courses = MongoDBConn.conn("courses");
		jCourses = new ArrayList<JSONObject>();
		courses.forEach(c -> {
			try {
				jCourses.add(parseCourseObject(c));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	public static void updateCourse(JSONObject item) {
		MongoDBConn.update("courses", item);
	}
	public static void getUser(String id) throws ParseException {
		//TEST : "622f69692d0eb62c6f5befd1"
		JSONParser jsonParser = new JSONParser();
		ArrayList<String> users = MongoDBConn.connByID("users", id);
		jCourse = new JSONObject();
		jCourse = parseCourseObject(users.get(0));
	}
	
	public static void deleteCourse(String id) {
		MongoDBConn.delete("courses", id);
		Desktop.initRootLayout();
	}

	private static JSONObject parseCourseObject(String course) throws ParseException {
		// Get course object within list
		JSONObject oneCourse = (JSONObject) new JSONParser().parse(course);
		return oneCourse;
	}


	public static void addCourse(String title, String desc) throws FileNotFoundException, IOException, ParseException {
		// parsing file "JSONExample.json"
		Gson gson = new Gson();
		Object ob = gson.fromJson(new FileReader("course.json"), Object.class);

		// typecasting ob to JSONObject
		JsonElement jel = gson.toJsonTree(ob);
		JsonObject js = (JsonObject) jel;
		updateValues(js, "title", title);
		updateValues(js, "description", desc);
		
		FileWriter updateCourse = new FileWriter("course.json");
		gson.toJson(js, updateCourse);
		updateCourse.flush();
		updateCourse.close();
		JSONParser parserJSON = new JSONParser();
		FileReader reader = new FileReader("course.json");
		Object obj = parserJSON.parse(reader);
		Document doc = Document.parse(obj.toString());
		MongoDBConn.insert("courses", doc);
	}
	
	private static void updateValues(JsonObject js, String item, String value) {
		for (Map.Entry<String, JsonElement> entry : js.entrySet()) {
	        JsonElement element = entry.getValue();
	        if (element.isJsonPrimitive()) {
	        	if(entry.getKey().equals(item)) {
	        		js.addProperty(entry.getKey(), value);	        		
	        		System.out.println(item.toUpperCase() + " actualizado correctamente");
	        	}
	        }
	    }
	}
	
	
	public static JSONObject getUserCourse(JSONObject course) { // Courses.getCourse();
		JSONObject subscribers = new JSONObject();
		subscribers = (JSONObject) course.get("subscribers");
		return subscribers;
	}
	
	public static ArrayList<JSONObject> getjCourses() {
		return jCourses;
	}

	public static JSONObject getjCourse() {
		return jCourse;
	}
	
	
	 //PARA ACTUALIZAR DATOS ANIDADOS
//	  private static void parseJsonArray(JsonArray asJsonArray) { for (int index =
//	  0; index < asJsonArray.size(); index++) { JsonElement element =
//	  asJsonArray.get(index); if (element.isJsonArray()) {
//	  parseJsonArray(element.getAsJsonArray()); } else if (element.isJsonObject())
//	  { updateValues(element.getAsJsonObject(), ); }
//	  
//	  } }
	 
}
