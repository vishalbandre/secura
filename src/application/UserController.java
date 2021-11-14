package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

// User list
// User crud

public class UserController implements Initializable {
	
	@FXML
	private TextField txt_username;

	@FXML
	private TextField txt_old_username;

	@FXML
	private TextField txt_password;
	
	@FXML
	private TextField txt_role;
	
	@FXML
	private Label lbl_continue;
	
	@FXML
	private Button btn_ok_update;
	
	@FXML
	private Button btn_cancel_update;

	@FXML
	private void addUser(ActionEvent event) {
		event.consume();
		
		Parent root;
        try {
            root = (Parent)FXMLLoader.load(getClass().getResource("AddUser.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Secura");
            stage.setScene(new Scene(root, 650, 450));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}

	@FXML
	private void saveUser(ActionEvent event) throws IOException {
		Connection conn = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://localhost/secura", "secura", "secura");
			System.out.println("Database connection established.");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("Database connection failed.");
		}

		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {

		    System.out.println("Inserted");
			String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
			stmt=conn.prepareStatement(sql);
			
		    stmt.setString(1, txt_username.getText());
		    stmt.setString(2, txt_password.getText());
		    stmt.setString(3, txt_role.getText());
		    int id = stmt.executeUpdate();
		    System.out.println("User Created.");
		    
//		    Platform.exit();
		    final Node source = (Node) event.getSource();
		    final Stage stage = (Stage) source.getScene().getWindow();
		    stage.close();

		    Parent root = null;
		    root = (Parent)FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
            Stage stage0 = new Stage();
            stage0.setTitle("Secura");
            stage0.setScene(new Scene(root, 650, 450));
            stage0.show();

		} catch (SQLException e) {

		}
	}
	

	@FXML
	private void cancelUpdate(ActionEvent event) throws IOException {
//	    Platform.exit();
	    final Node source = (Node) event.getSource();
	    final Stage stage = (Stage) source.getScene().getWindow();
	    stage.close();

	    Parent root = null;
	    root = (Parent)FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
        Stage stage0 = new Stage();
        stage0.setTitle("Secura");
        stage0.setScene(new Scene(root, 650, 450));
        stage0.show();
	}

	@FXML
	private void initializeUser(ActionEvent event) throws IOException {
//		System.out.println("Initialization Started.");
		lbl_continue.setVisible(false);
		btn_ok_update.setVisible(false);
		btn_cancel_update.setVisible(false);
		txt_old_username.setVisible(false);

		Node node = (Node) event.getSource();
		Stage stage2 = (Stage) node.getScene().getWindow();
		
		User u = (User) stage2.getUserData();
		
		String username = u.getUsername();
		txt_old_username.setText(username);
		
		System.out.println(username + " Received Successfully.");
		
		Connection conn = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://localhost/secura", "secura", "secura");
			System.out.println("Database connection established.");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("Database connection failed.");
		}
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT username, password, role FROM users WHERE username=?";
			stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, txt_old_username.getText());
			
			rs = stmt.executeQuery();

			while (rs.next()) {
				txt_username.setText(rs.getString(1));
				txt_password.setText(rs.getString(2));
				txt_role.setText(rs.getString(3));
//				System.out.println("Done");
//				System.out.print("Username: " + rs.getString(2));
//				System.out.print("Password: " + rs.getString(3));
//				System.out.print("Role: " + rs.getString(4));
			}
		} catch (SQLException e) {

		}
		System.out.println("Initialization ended.");
	}

	@FXML
	private void updateUser(ActionEvent event) throws IOException {
		System.out.println("Updating...");
		String username = txt_old_username.getText();

		Connection conn = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://localhost/secura", "secura", "secura");
			System.out.println("Database connection established.");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("Database connection failed.");
		}
		
		PreparedStatement stmt = null;
		ResultSet rs = null;

//		try {
//			String sql = "SELECT username, password, role FROM users WHERE username=?";
//			stmt = conn.prepareStatement(sql);
//			
//			System.out.println("Supplied user name: ");
//			System.out.println(username + " Next ");
//			stmt.setString(1, txt_old_username.getText());
//			System.out.println(txt_old_username.getText());
//			
//			rs = stmt.executeQuery();
//			
//			System.out.println("Update Query Executed");
//			
//			while (rs.next()) {
//				System.out.println(rs.getString(1));
//				System.out.println("Third: " + rs.getString(3));
////				txt_username.setText(rs.getString(1));
////				txt_password.setText(rs.getString(2));
////				txt_role.setText(rs.getString(3));
////				System.out.println(rs.getString(1));
////				System.out.println(rs.getString(1));
////				System.out.println("Here inside");
//				
//			}
//		} catch (SQLException e) {
//
//		}
//		
//		System.out.println("Here 3");
//		System.out.println(txt_username.getText());

		stmt = null;
		rs = null;
		try {

			if (txt_username.getText().isEmpty() || txt_password.getText().isEmpty() || txt_role.getText().isEmpty())
				return;

		    String sql = "UPDATE users SET username=?, password=?, role=? WHERE username=?";
			stmt=conn.prepareStatement(sql);
			
		    stmt.setString(1, txt_username.getText());
		    stmt.setString(2, txt_password.getText());
		    stmt.setString(3, txt_role.getText());
		    stmt.setString(4, username);
		    
		    int id = stmt.executeUpdate();
		    System.out.println("User Updated.");
		    
//		    Platform.exit();
		    final Node source = (Node) event.getSource();
		    final Stage stage = (Stage) source.getScene().getWindow();
		    stage.close();

		    Parent root = null;
		    root = (Parent)FXMLLoader.load(getClass().getResource("AdminDashboard.fxml"));
            Stage stage0 = new Stage();
            stage0.setTitle("Secura");
            stage0.setScene(new Scene(root, 650, 450));
            stage0.show();

		} catch (SQLException e) {

		}
		
		System.out.println("Updation completed.");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Updating started.");

	}	
}