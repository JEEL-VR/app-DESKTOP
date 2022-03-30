package application;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Users {
	static ArrayList<JSONObject> jUsers;
	static JSONObject jUser;
	
	public static void main(String[] args) throws Exception {
		getUser("622f69692d0eb62c6f5befd1");
		System.out.println(Users.getjUser());
	}
	
	public static void getAllUsers() {
		JSONParser jsonParser = new JSONParser();
		ArrayList<String> courses = MongoDBConn.conn("users");
		jUsers = new ArrayList<JSONObject>();
		courses.forEach(c -> {
			try {
				jUsers.add(parseCourseObject(c));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	public static void getUser(String id) throws ParseException {
		//TEST : "622f69692d0eb62c6f5befd1"
		JSONParser jsonParser = new JSONParser();
		ArrayList<String> users = MongoDBConn.connByID("users", id);
		jUser = new JSONObject();
		jUser = parseCourseObject(users.get(0));
	}
	
	private static JSONObject parseCourseObject(String course) throws ParseException {
		// Get course object within list
		JSONObject oneCourse = (JSONObject) new JSONParser().parse(course);
		return oneCourse;
	}

	public static ArrayList<JSONObject> getjUsers() {
		return jUsers;
	}

	public static void setjUsers(ArrayList<JSONObject> jUsers) {
		Users.jUsers = jUsers;
	}

	public static JSONObject getjUser() {
		return jUser;
	}

	public static void setjUser(JSONObject jUser) {
		Users.jUser = jUser;
	}
	
	
	
	
}
