package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

// User list
// User crud

public class AdminDashboardControllerAr2 implements Initializable {
	
	@FXML
	private Button btn_add_user;
	
	@FXML
	private Button btn_delete_user;
	
	@FXML
	private Button btn_update_user;

//	@FXML
//	private TableView<?> tableview;
	
	@FXML
	private TableView<ObservableList<String>> tableView;
	
	private ObservableList<ObservableList<String>> data;
	
	@FXML
	private TableColumn<User, String> username_field;

	@FXML
	private TableColumn<User, String> password_field;

	@FXML
	private TableColumn<User, String> role_field;
	
	@FXML
	private ListView<String> userlist;
	
	private String username;
	
	private ObservableList<String> items = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		userlist.getItems().addAll(items);
		userlist.setItems(items);
		
		userlist.getItems().add("Username");
		
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
				items.add(rs.getString(1) + "\t | " + rs.getString(2) + "\t | " + rs.getString(3));
				items.add(rs.getString(1));
				System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {

		}
		
		userlist.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<String>() {

			@Override
			public void onChanged(Change<? extends String> c) {
				username = userlist.getSelectionModel().getSelectedItem();
				System.out.println(username);
			}
			
		});
	}
}