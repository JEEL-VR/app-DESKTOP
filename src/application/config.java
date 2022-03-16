package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class config {
	private static String DATABASE_URL;
	
	public static void main(String[] args) {
		fileConfig();
	}
	
	public static void fileConfig() {
		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream("config")) {
			prop.load(fis);
		} catch (IOException e) {
			System.out.println(e);
		}
		
		DATABASE_URL = prop.getProperty("DATABASE_URL");
		System.out.println(DATABASE_URL);
	}

	public static String getDATABASE_URL() {
		return DATABASE_URL;
	}

	public void setDATABASE_URL(String dATABASE_URL) {
		DATABASE_URL = dATABASE_URL;
	}
	
	

}
