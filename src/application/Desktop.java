package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Desktop extends Application {
	private static Stage primaryStage;
	private static AnchorPane rootLayout;
	private ObservableList<Courses> coursesData = FXCollections.observableArrayList();
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initRootLayout();
		
	}
	
	public static void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Desktop.class.getResource("deskView.fxml"));
			rootLayout = (AnchorPane) loader.load();
			rootLayout.setMaxSize(689, 591);
			Scene scene = new Scene(rootLayout);
			primaryStage.setTitle("MOODLE");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
 