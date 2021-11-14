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
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

// User list
// User crud
public class AdminDashboardController implements Initializable {

	@FXML
	private Button btn_add_user;
	
	@FXML
	private Button btn_delete_user;

	@FXML
	private Button btn_update_user;

	@FXML
	private Button btn_back_to_login;

	@FXML
	private TableColumn<User, String> username_field;
    
	@FXML
	private TableColumn<User, String> password_field;
	
	@FXML
	private TableColumn<User, String> role_field;

	@FXML
	private TableView<User> tableview;
	
	@FXML
	private TextField txt_username;
	
	@FXML
	private TextField txt_password;
	
	@FXML
	private TextField txt_role;
	
	ObservableList<List<StringProperty>> data = FXCollections.observableArrayList();


	@Override
	public void initialize(URL location, ResourceBundle resources) {

		username_field.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
		password_field.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
		role_field.setCellValueFactory(new PropertyValueFactory<User, String>("role"));

		// load initial data
		tableview.setItems(getUsers());
		
		
	}
	
	public ObservableList<User> getUsers() {
		ObservableList<User> users = FXCollections.observableArrayList();
//		users.add(new User("vishal", "admin", "normal"));
		
		Connection conn = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://localhost/secura", "secura", "secura");
			System.out.println("Database connection established successfully.");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("Database connection failed.");
		}

		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT username, password, role FROM users");

			while (rs.next()) {
				users.add(new User(rs.getString(1), rs.getString(2), rs.getString(3)));
				System.out.println(rs.getString(1));
				
			}
		} catch (SQLException e) {

		}
		
		return users;
	}
	
	private List<User> parseUserList(){
		
		List<User> user_items = new ArrayList<>();
		
		Connection conn = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://localhost/secura", "secura", "secura");
			System.out.println("Database connection established successfully.");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("Database connection failed.");
		}

		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT username, password, role FROM users");

			while (rs.next()) {
//				user_items.add(new User("admin", "admin", "admin"));
//				items.add(rs.getString(1) + rs.getString(2) + "\t" + rs.getString(3));
//				items.add(new User("admin", "admin", "admin"));
				System.out.println(rs.getString(1));
				
			}
		} catch (SQLException e) {

		}
		
		return user_items;
        // parse and construct User datamodel list by looping your ResultSet rs
        // and return the list   
    }

	@FXML
	private void backToLogin(ActionEvent event) throws IOException {
		event.consume();
		
		final Node source = (Node) event.getSource();
	    final Stage stage = (Stage) source.getScene().getWindow();
	    stage.close();

	    Parent root = null;
	    root = (Parent)FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage0 = new Stage();
        stage0.setTitle("Secura");
        stage0.setScene(new Scene(root, 650, 450));
        stage0.show();
	}
	
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

            final Node source = (Node) event.getSource();
		    final Stage stage0 = (Stage) source.getScene().getWindow();
		    stage0.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@FXML
	private void saveUser(ActionEvent event) {
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
			String sql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
			stmt=conn.prepareStatement(sql);
		    stmt.setString(1, txt_username.getText());
		    stmt.setString(2, txt_password.getText());
		    stmt.setString(3, txt_role.getText());
		    stmt.executeUpdate();

		} catch (SQLException e) {

		}
	}

	@FXML
	private void deleteUser(ActionEvent event) {
		String username = tableview.getSelectionModel().getSelectedItem().getUsername();
//		System.out.println(username);
		
		if (username.isEmpty())
			return;
		
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
			String sql = "DELETE FROM users WHERE username = ?";
			stmt=conn.prepareStatement(sql);
		    stmt.setString(1, username);
		    stmt.executeUpdate();

		    tableview.setItems(getUsers());

		} catch (SQLException e) {

		}
	}

	@FXML
	private void editUser(ActionEvent event) {
		event.consume();
		
		Parent root;
        try {
            root = (Parent)FXMLLoader.load(getClass().getResource("UpdateUser.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Secura");
            stage.setScene(new Scene(root, 650, 450));
            String username = tableview.getSelectionModel().getSelectedItem().getUsername();
            String password= tableview.getSelectionModel().getSelectedItem().getUsername();
            String role= tableview.getSelectionModel().getSelectedItem().getUsername();
            User u = new User(username, password, role);
            
            stage.setUserData(u);
            stage.show();

            final Node source = (Node) event.getSource();
		    final Stage stage0 = (Stage) source.getScene().getWindow();
		    stage0.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
}