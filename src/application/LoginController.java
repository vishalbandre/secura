package application;

import java.io.IOException;
import java.util.prefs.BackingStoreException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController {
	
	@FXML
	private TextField txt_admin_username;
	
	@FXML
	private PasswordField txt_admin_password;
	
	@FXML
	private TextField txt_username;
	
	@FXML
	private PasswordField txt_password;
	
	@FXML
	private void adminLogin(ActionEvent event) throws Exception {
		event.consume();
//		System.out.println("Secura Admin Login");
		
		DBConnection db = new DBConnection();
		db.connectToDB();
		
		String username = txt_admin_username.getText();
		String password = txt_admin_password.getText();
		
//		if (username.equals("admin") && password.equals("admin")) {
		if(db.checkLogin(username, password)) {
			System.out.println("Admin Login Successful.");
			
			Parent root;
	        try {
	            root = (Parent)FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
	            
	            java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userRoot();

	            try {
	    			userPreferences.clear();
	    		} catch (BackingStoreException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	            
	            userPreferences.put("username", username);

	            Stage stage = new Stage();
	            stage.setTitle("Secura");
	            stage.setScene(new Scene(root, 650, 350));
	            User u = new User(username, null, null);
	            stage.setUserData(u);
	            stage.show();

//	    	    Platform.exit();
	    	    final Node source = (Node) event.getSource();
	    	    final Stage stage2 = (Stage) source.getScene().getWindow();
	    	    stage2.close();
	            
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
			
		} else
			System.out.println("Admin Login Failed.");
		
		db.close();
	}
	
	@FXML
	private void userLogin(ActionEvent event) throws Exception {
		event.consume();
		
		DBConnection db = new DBConnection();
		db.connectToDB();
		
		String username = txt_username.getText();
		String password = txt_password.getText();
		
		System.out.println(username + " - " + password);
		
//		if (username.equals("normal") && password.equals("normal"))
//			System.out.println("Login Successful.");
//		else
//			System.out.println("Login Failed.");
		
		if(db.checkLogin(username, password)) {
			System.out.println("User Login Successful.");

			Parent root;
	        try {
	            root = (Parent)FXMLLoader.load(getClass().getResource("UserDashboard.fxml"));
	            
//	            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDashboard.fxml"));
	            
	            Stage stage = new Stage(StageStyle.DECORATED);
//	            stage.setScene(
//	              new Scene(loader.load())
//	            );
	            
//	            UserDashboardController controller1 = (UserDashboardController) loader.getController();
//	            controller.setUsername(username);
	            
//	            UserDashboardController controller = new UserDashboardController(username);
//	            controller.setUsername(username);
//	            loader.setController(controller);
//	            loader.load();
//	            controller.username = username;
//	            loader.setController(controller);
//	            root = loader.load();

	            java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userRoot();

	            try {
	    			userPreferences.clear();
	    		} catch (BackingStoreException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	            
	            userPreferences.put("username", username);

	            stage.setTitle("Secura");
	            stage.setScene(new Scene(root, 650, 450));
	            User u = new User(username, null, null);
	            stage.setUserData(u);
	            stage.show();

//	    	    Platform.exit();
	    	    final Node source = (Node) event.getSource();
	    	    final Stage stage2 = (Stage) source.getScene().getWindow();
	    	    stage2.close();   
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
			
		} else
			System.out.println("User Login Failed.");
	}

	@FXML
	private void userLoginAr(ActionEvent event) throws Exception {
		event.consume();
		
		DBConnection db = new DBConnection();
		db.connectToDB();
		
		String username = txt_username.getText();
		String password = txt_password.getText();
		
		System.out.println(username + " - " + password);
		
//		if (username.equals("normal") && password.equals("normal"))
//			System.out.println("Login Successful.");
//		else
//			System.out.println("Login Failed.");
		
		if(db.checkLogin(username, password)) {
			System.out.println("User Login Successful.");

			Parent root;
	        try {
	            root = (Parent)FXMLLoader.load(getClass().getResource("UserDashboard.fxml"));

	            Stage stage = new Stage();
	            stage.setTitle("Secura");
	            stage.setScene(new Scene(root, 650, 450));
	            User u = new User(username, null, null);
	            stage.setUserData(u);
	            stage.show();

//	    	    Platform.exit();
	    	    final Node source = (Node) event.getSource();
	    	    final Stage stage2 = (Stage) source.getScene().getWindow();
	    	    stage2.close();   
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
			
		} else
			System.out.println("User Login Failed.");
	}	
}
