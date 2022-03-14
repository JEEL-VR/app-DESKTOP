package application;

import java.util.ArrayList;

import org.json.simple.JSONObject;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public class DesktopViewController {
	
	@FXML
	private GridPane listCourses;
	
	@FXML
	private Label descCourse;
	
	@FXML
	private Label descList;
	
	@FXML
	private Button buttonAdd;
	
	public void initialize() {
		Courses.getAllCourses();
		ArrayList<JSONObject> courses = Courses.getjCourses();
		for(int x = 0; x < courses.size(); x++) {
			Label example  = new Label(courses.get(x).get("title").toString());
			Button exButton = new Button();
			exButton.setText("Delete");
			listCourses.add(example, 0, x);
			listCourses.add(exButton, 1, x);
		}
	}
	
	public void showDetails() {
		if(descCourse.getText().equals("NOT Example")){
			descCourse.setText("Example");
		}else{
			descCourse.setText("NOT Example");
		};
	}
}
