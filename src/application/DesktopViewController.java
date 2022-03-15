package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.sun.prism.paint.Color;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DesktopViewController {

	@FXML
	private GridPane listCourses;

	@FXML
	private Label descCourse;

	@FXML
	private Label descList;

	@FXML
	private Button buttonAdd;

	@FXML
	private Label titleCourse;

	public void initialize() throws FileNotFoundException{
		showCourses();
	}
	public void showCourses() throws FileNotFoundException{
		Courses.getAllCourses();
		ArrayList<JSONObject> courses = Courses.getjCourses();
		for (int x = 0; x < courses.size(); x++) {

			// Name of course
			Label title = new Label(courses.get(x).get("title").toString());

			// Image of button
			FileInputStream input = new FileInputStream("delete.png");
			Image image = new Image(input, 10, 10, false, false);
			ImageView imageView = new ImageView(image);

			// Button for delete
			Button delButton = new Button("", imageView);
			

			// Delete course
			JSONObject id = (JSONObject) courses.get(x).get("_id");
			delButton.setId(id.get("$oid").toString());
			delButton.setOnMouseClicked(e -> Courses.deleteCourse(id.get("$oid").toString()));
			
			// Details course
			String desc = courses.get(x).get("description").toString();
			String strTitle = courses.get(x).get("title").toString();
			title.setOnMouseClicked(e ->{
				titleCourse.setText(strTitle);
				descCourse.setText(desc);
			});
			
			// make GridPane
			listCourses.add(title, 0, x);
			listCourses.add(delButton, 1, x);
			listCourses.setAlignment(Pos.CENTER);
		}
	}
	public void showNewCourse(ActionEvent event) {
		AnchorPane root;
		try {
			Stage stageMain = (Stage) buttonAdd.getScene().getWindow();
			stageMain.close();

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("createCourse.fxml"));
			root = loader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.setTitle("MOODLE - NOU CURS");
			stage.setResizable(false);
			createCourseController controller = loader.getController();
			stage.showAndWait();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
