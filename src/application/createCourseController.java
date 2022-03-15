package application;



import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class createCourseController {
	private AnchorPane rootLayout;
	private Stage primaryStage;
	
	@FXML
	private TextArea description;
	
	@FXML
	private TextField name;
	
	@FXML 
	private Button insertButton;
	
	public  void initialize() {
		
	}
	
	public void insertCourse(ActionEvent event) throws FileNotFoundException, IOException, ParseException {
		
		if (description.getText().isEmpty() || name.getText().isEmpty()) {
			System.out.println("RELLENA TODOS LOS CAMPOS");
		} else {
			Courses.addCourse(name.getText(), description.getText());
			Stage stage = (Stage) insertButton.getScene().getWindow();
			stage.close();
			Desktop.initRootLayout();
		}

	}
}
