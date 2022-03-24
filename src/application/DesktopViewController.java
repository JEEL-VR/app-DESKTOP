package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.sun.prism.paint.Color;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
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
	
	@FXML
	private ListView usersList;
	
	@FXML
	private ListView teachersList;
	
	@FXML
	private ListView studentsList;
	
	@FXML
	private Button usersToTeachers;
	
	@FXML
	private Button usersToStudents;
	
	@FXML
	private Button studentsToUsers;
	
	@FXML
	private Button teachersToUsers;
	
	
	static ArrayList<JSONObject> users;
	static ArrayList<JSONObject> courses;
	
	public static void main(String[] args) {
		
	}
	
	public void initialize() throws FileNotFoundException{		
		Users.getAllUsers();
		users = Users.getjUsers();
		
		Courses.getAllCourses();
		courses = Courses.getjCourses();
		
		showCourses();
	}
	
	public static ArrayList<ArrayList> showUsers(String ID) throws ParseException {
		//TEST: "622e37a987d3c0393ed98672"

		ArrayList<ArrayList> Lists = new ArrayList<ArrayList>();
		for (int c = 0; c < courses.size(); c++) {
			JSONObject courseID = (JSONObject) courses.get(c).get("_id");
			if (courseID.get("$oid").toString().equals(ID)) {
				// Lista de users de ESTE curso
				ArrayList<JSONObject> totalUsers = new ArrayList<JSONObject>(users);

				// Usuarios participantes / asigandos -- PROFE / ESTUDIANTES
				ArrayList<String> participants = new ArrayList<String>();

				// OBTENEMOS LOS DATOS DEL CURSO
				JSONObject usersCourse = new JSONObject();
				for(int u = 0; u < courses.size(); u++) {
					JSONObject IDcourse = (JSONObject) courses.get(u).get("_id");
					if(IDcourse.get("$oid").toString().equals(courseID.get("$oid").toString())) {
						usersCourse = Courses.getUserCourse(courses.get(c));
					}
				}
				
				// Usuarios ESTUDIANTES
				ArrayList<String> students = (ArrayList<String>) usersCourse.get("students");
				ArrayList<JSONObject> studentsJSON = new ArrayList<JSONObject>();

				// Usuarios PROFESORES
				ArrayList<String> teachers = (ArrayList<String>) usersCourse.get("teachers");
				ArrayList<JSONObject> teachersJSON = new ArrayList<JSONObject>();
				
				if (students.size() > 0) {
					for (int s = 0; s < students.size(); s++) {
						for(int u = 0; u < users.size(); u++) {
							JSONObject userID = (JSONObject) users.get(u).get("_id");
							if(userID.get("$oid").toString().equals(students.get(s))) {
								studentsJSON.add(users.get(u));
							}
						}
						participants.add(students.get(s));
					}
				}
				if (teachers.size() > 0) {
					for (int t = 0; t < teachers.size(); t++) {
						for(int u = 0; u < users.size(); u++) {
							JSONObject userID = (JSONObject) users.get(u).get("_id");
							if(userID.get("$oid").toString().equals(teachers.get(t))) {
								teachersJSON.add(users.get(u));
							}
						}
						participants.add(teachers.get(t));
					}
				}

				// COMPROBAMOS LOS USUARIOS SIN ROL
				if (participants.size() > 0) {
					for (int u = 0; u < users.size(); u++) {
						JSONObject userID = (JSONObject) users.get(u).get("_id");
						if (participants.contains(userID.get("$oid").toString())) {
							totalUsers.remove(users.get(u));
						}
					}
				}
				Lists.add(totalUsers);
				Lists.add(studentsJSON);
				Lists.add(teachersJSON);
			}
		}
		return Lists;
	}
	
	public void showCourses() throws FileNotFoundException{
		descCourse.setWrapText(true);
		titleCourse.setWrapText(true);
		for (int x = 0; x < courses.size(); x++) {
			JSONObject thisCourse = courses.get(x);
			// Name of course
			Label title = new Label(thisCourse.get("title").toString());
			title.setMinWidth(180);
			
			// Image of button
			FileInputStream input = new FileInputStream("delete.png");
			Image image = new Image(input, 10, 10, false, false);
			ImageView imageView = new ImageView(image);

			// Button for delete
			Button delButton = new Button("", imageView);
			
			

			// Delete course
			int f = x;
			JSONObject id = (JSONObject) thisCourse.get("_id");
			delButton.setId(id.get("$oid").toString());
			delButton.setOnMouseClicked(e -> {
				if (dialog().get() == ButtonType.OK) {
					Courses.deleteCourse(id.get("$oid").toString());
				}
			});
			
			// Details course
			JSONObject usersCourse = new JSONObject();
			for(int u = 0; u < courses.size(); u++) {
				JSONObject userID = (JSONObject) courses.get(u).get("_id");
				if(userID.get("$oid").toString().equals(id.get("$oid").toString())) {
					usersCourse = Courses.getUserCourse(thisCourse);
				}
			}
			
			String desc = thisCourse.get("description").toString();			
			String strTitle = thisCourse.get("title").toString();
			
			usersToTeachers.setDisable(true);
			usersToStudents.setDisable(true);
			studentsToUsers.setDisable(true);
			teachersToUsers.setDisable(true);
			
			title.setOnMouseClicked(e ->{
				titleCourse.setText(strTitle);
				descCourse.setText(desc);
				ArrayList<ArrayList> allUsers = new ArrayList<ArrayList>();
				try {
					allUsers = showUsers(id.get("$oid").toString());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				  
				
				addItemsToListView(usersList, allUsers.get(0));			
				addItemsToListView(studentsList, allUsers.get(1));
				addItemsToListView(teachersList, allUsers.get(2));
								
				usersToTeachers.setDisable(true);
				usersToStudents.setDisable(true);
				studentsToUsers.setDisable(true);
				teachersToUsers.setDisable(true);
				
				usersList.setOnMouseClicked(b ->{
					usersToTeachers.setDisable(false);
					usersToStudents.setDisable(false);
					studentsToUsers.setDisable(true);
					teachersToUsers.setDisable(true);
					studentsList.getSelectionModel().clearSelection();
					teachersList.getSelectionModel().clearSelection();
				});
				
				studentsList.setOnMouseClicked(b ->{
					usersToTeachers.setDisable(true);
					usersToStudents.setDisable(true);
					studentsToUsers.setDisable(false);
					teachersToUsers.setDisable(true);
					teachersList.getSelectionModel().clearSelection();
					usersList.getSelectionModel().clearSelection();
				});
				
				teachersList.setOnMouseClicked(b ->{
					usersToTeachers.setDisable(true);
					usersToStudents.setDisable(true);
					studentsToUsers.setDisable(true);
					teachersToUsers.setDisable(false);
					studentsList.getSelectionModel().clearSelection();
					usersList.getSelectionModel().clearSelection();
				});				
				
				usersToStudents.setOnMouseClicked(a ->{
					JSONObject user = (JSONObject) usersList.getSelectionModel().getSelectedItem();
					JSONObject userID = (JSONObject) user.get("_id");
					if(usersList.getSelectionModel().getSelectedItem() != null) {
						JSONObject subscribersObject = (JSONObject) thisCourse.get("subscribers");
						JSONArray studentsArray = (JSONArray) subscribersObject.get("students");
						studentsArray.add(userID.get("$oid"));
						thisCourse.put("students",studentsArray);
						studentsList.getItems().add(usersList.getSelectionModel().getSelectedItem());
						usersList.getItems().remove(usersList.getSelectionModel().getSelectedIndex());
					}
					usersList.getSelectionModel().clearSelection();
					usersToTeachers.setDisable(true);
					usersToStudents.setDisable(true);
					studentsToUsers.setDisable(true);
					teachersToUsers.setDisable(true);
//					JSONObject subscribersObject = (JSONObject) thisCourse.get("subscribers");
//					JSONArray studentsJSON = (JSONArray) subscribersObject.get("students");
//					ArrayList<String> studentsArray = new ArrayList<String>();
//					for(int s = 0; s < studentsJSON.size(); s++) {
//						studentsArray.add(studentsJSON.get(s).toString());
//					}
					Courses.updateCourse(thisCourse);				
				});
				
				studentsToUsers.setOnMouseClicked(a ->{
					JSONObject user = (JSONObject) studentsList.getSelectionModel().getSelectedItem();
					JSONObject userID = (JSONObject) user.get("_id");
					if(studentsList.getSelectionModel().getSelectedItem() != null) {
						JSONObject subscribersObject = (JSONObject) thisCourse.get("subscribers");
						JSONArray studentsArray = (JSONArray) subscribersObject.get("students");
						studentsArray.remove(userID.get("$oid"));
						thisCourse.put("students",studentsArray);
						usersList.getItems().add(studentsList.getSelectionModel().getSelectedItem());
						studentsList.getItems().remove(studentsList.getSelectionModel().getSelectedIndex());
					}					
					studentsList.getSelectionModel().clearSelection();
					usersToTeachers.setDisable(true);
					usersToStudents.setDisable(true);
					studentsToUsers.setDisable(true);
					teachersToUsers.setDisable(true);
//					JSONObject subscribersObject = (JSONObject) thisCourse.get("subscribers");
//					JSONArray studentsJSON = (JSONArray) subscribersObject.get("students");
//					ArrayList<String> studentsArray = new ArrayList<String>();
//					for(int s = 0; s < studentsJSON.size(); s++) {
//						studentsArray.add(studentsJSON.get(s).toString());
//					}
					Courses.updateCourse(thisCourse);
				});				
				
				usersToTeachers.setOnMouseClicked(a ->{
					JSONObject user = (JSONObject) usersList.getSelectionModel().getSelectedItem();
					JSONObject userID = (JSONObject) user.get("_id");
					
					if(usersList.getSelectionModel().getSelectedItem() != null) {
						JSONObject subscribersObject = (JSONObject) thisCourse.get("subscribers");
						JSONArray teachersArray = (JSONArray) subscribersObject.get("teachers");
						teachersArray.add(userID.get("$oid"));
						thisCourse.put("students",teachersArray);
						teachersList.getItems().add(usersList.getSelectionModel().getSelectedItem());
						usersList.getItems().remove(usersList.getSelectionModel().getSelectedIndex());
					}
					usersList.getSelectionModel().clearSelection();
					usersToTeachers.setDisable(true);
					usersToStudents.setDisable(true);
					studentsToUsers.setDisable(true);
					teachersToUsers.setDisable(true);
//					JSONObject subscribersObject = (JSONObject) thisCourse.get("subscribers");
//					JSONArray teachersJSON = (JSONArray) subscribersObject.get("students");
//					ArrayList<String> teachersArray = new ArrayList<String>();
//					for(int s = 0; s < teachersJSON.size(); s++) {
//						teachersArray.add(teachersJSON.get(s).toString());
//					}
					Courses.updateCourse(thisCourse);
				});
				
				teachersToUsers.setOnMouseClicked(a ->{
					JSONObject user = (JSONObject) teachersList.getSelectionModel().getSelectedItem();
					JSONObject userID = (JSONObject) user.get("_id");
					if(teachersList.getSelectionModel().getSelectedItem() != null) {
						JSONObject subscribersObject = (JSONObject) thisCourse.get("subscribers");
						JSONArray teachersArray = (JSONArray) subscribersObject.get("teachers");
						teachersArray.remove(userID.get("$oid"));
						thisCourse.put("teachers", teachersArray);
						usersList.getItems().add((JSONObject) teachersList.getSelectionModel().getSelectedItem());
						teachersList.getItems().remove(teachersList.getSelectionModel().getSelectedIndex());
					}
					teachersList.getSelectionModel().clearSelection();
					usersToTeachers.setDisable(true);
					usersToStudents.setDisable(true);
					studentsToUsers.setDisable(true);
					teachersToUsers.setDisable(true);
//					JSONObject subscribersObject = (JSONObject) thisCourse.get("subscribers");
//					JSONArray teachersJSON = (JSONArray) subscribersObject.get("teachers");
//					ArrayList<String> teachersArray = new ArrayList<String>();
//					for(int s = 0; s < teachersJSON.size(); s++) {
//						teachersArray.add(teachersJSON.get(s).toString());
//					}
					Courses.updateCourse(thisCourse);
				});				
			});
			
			// make GridPane
			listCourses.add(title, 0, x);
			listCourses.add(delButton, 1, x);
			
			listCourses.setHalignment(delButton, HPos.LEFT);
			listCourses.setValignment(delButton, VPos.CENTER);
			
			listCourses.setHalignment(delButton, HPos.RIGHT);
			listCourses.setValignment(delButton, VPos.CENTER);
		}
			
	}
	
	public static void addItemsToListView(ListView<JSONObject> list, ArrayList<JSONObject> array){
		list.getItems().clear();
		list.getItems().addAll(array);
		list.setCellFactory(lv -> new ListCell<JSONObject>(){
			@Override
			public void updateItem(JSONObject users, boolean empty) {
				super.updateItem(users, empty);
				setText(empty ? null : users.get("username").toString());
			}
		});
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
	
	public static Optional<ButtonType> dialog() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("CONFIRMACION");
		alert.setContentText("ESTAS SEGUR D'ELIMINAR AQUEST CURS?");
		alert.setHeaderText(null);
		return alert.showAndWait();
	}
	
	public static ArrayList<JSONObject> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<JSONObject> users) {
		this.users = users;
	}

	public static ArrayList<JSONObject> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<JSONObject> courses) {
		this.courses = courses;
	}
	
	

}
