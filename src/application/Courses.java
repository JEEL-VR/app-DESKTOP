package application;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Courses {
	static ArrayList<JSONObject> jCourses;

	public static void main(String[] args) throws Exception {
		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();

		MongoDBConn conn = new MongoDBConn();
		ArrayList<String> courses = conn.conn("courses");
		courses.forEach(c -> {
			try {
				parseCourseObject(c);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public static void getAllCourses() {
		JSONParser jsonParser = new JSONParser();

		MongoDBConn conn = new MongoDBConn();
		ArrayList<String> courses = conn.conn("courses");
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

	private static JSONObject parseCourseObject(String course) throws ParseException {
		// Get course object within list
		JSONObject oneCourse = (JSONObject) new JSONParser().parse(course);
		return oneCourse;
	}

	public static ArrayList<JSONObject> getjCourses() {
		return jCourses;
	}
}
