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

public class UserDashboard implements Initializable {	
	@FXML
	private TableColumn<User, String> username_field;
    
	@FXML
	private TableColumn<User, String> password_field;
	
	@FXML
	private TableColumn<User, String> role_field;

	@FXML
	private TableView<User> tableview;
		
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
	
	
}