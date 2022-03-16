package application;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Teachers {
	public static void main(String[] args) throws Exception {
		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();

		MongoDBConn conn = new MongoDBConn();
		ArrayList<String> courses = conn.conn("teachers");
		courses.forEach(c -> {
			try {
				parseCourseObject(c);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	private static void parseCourseObject(String course) throws ParseException {
		// Get course object within list
		JSONObject oneCourse = (JSONObject) new JSONParser().parse(course);
		Object name = oneCourse.get("name");
		Object email = oneCourse.get("email");
		System.out.println(name + " -> " + email);
	}
}
