package application;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;

import javax.swing.SwingUtilities;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UserDashboardController implements Initializable {
	@FXML
	private Button btn_backup_file;

	@FXML
	private Button btn_delete_file;

	@FXML
	private Button btn_decrypt_file;
	
	@FXML
	private Button btn_back_to_login;
	
	@FXML
	private TableColumn<SecuraFile, String> filename;
    
	@FXML
	private TableColumn<SecuraFile, String> timestamp;

	@FXML
	private TableView<SecuraFile> tableview;
	
	public UserDashboardController() {
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
	private void backupFile(ActionEvent event) throws IOException {
		System.out.println("Back up file");

		final Node node = (Node) event.getSource();
		
		Stage stage2 = (Stage) node.getScene().getWindow();

		User u = (User) stage2.getUserData();

		String username = u.getUsername();

		FileChooser fileChooser = new FileChooser();

		File selectedFile = fileChooser.showOpenDialog(node.getScene().getWindow());

		if (selectedFile == null)
			return;

		System.out.print("File Meta: \nName: ");
		System.out.print(selectedFile.getName() + "\nPath: ");
		System.out.print(selectedFile.getAbsolutePath());

		String workingDirectory;
		// here, we assign the name of the OS, according to Java, to a variable...
		String OS = (System.getProperty("os.name")).toUpperCase();
		// to determine what the workingDirectory is.
		// if it is some version of Windows
		if (OS.contains("WIN")) {
			// it is simply the location of the "AppData" folder
			workingDirectory = System.getenv("AppData");
		}
		// Otherwise, we assume Linux
		else {
			// in either case, we would start in the user's home directory
			workingDirectory = System.getProperty("user.home");
		}

		File file = new File(workingDirectory + "/secura");

		if (!file.isDirectory()) {
			boolean success = file.mkdirs();
		}

		Path path = Paths.get(workingDirectory + "/secura/" + selectedFile.getName());

		String extension = "";

		int i = path.toString().lastIndexOf('.');
		if (i > 0) {
		    extension = path.toString().substring(i+1);
		}
		
		String key = "securasecurasecurasecura";
		
		Files.copy(selectedFile.toPath(), path, StandardCopyOption.REPLACE_EXISTING);

		File inputFile = new File(path.toString());
		File encryptedFile = new File(path.toString());
		File decryptedFile = new File(path.toString() + "decrypted");
		
		try {
            CryptoUtils.encrypt(key, inputFile, encryptedFile);
//            CryptoUtils.decrypt(key, encryptedFile, decryptedFile);
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
    			String sql = "INSERT INTO files (USERNAME, FILEPATH, FILENAME, ENCRYPTION_KEY) VALUES (?, ?, ?, ?)";
    			stmt=conn.prepareStatement(sql);
    			
    		    stmt.setString(1, username);
    		    stmt.setString(2, path.toString());
    		    stmt.setString(3, selectedFile.getName());
    		    stmt.setString(4, key);
    		    int id = stmt.executeUpdate();
    		    System.out.println("File Encrypted and Backed Up Successfully.");
    		    this.refresh();
    		} catch (SQLException e) {

    		}
        } catch (CryptoException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
		
//		Check if file is already existed in directory
//		File temp;
//		try {
//			temp = File.createTempFile(path.toString(), extension);
//
//			boolean exists = temp.exists();
//			
//			if(exists) {
//				System.out.println("File exists : " + exists);
//				return;
//			} else {
//				Files.copy(selectedFile.toPath(), path);
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@FXML
	private void deleteFile(ActionEvent event) throws IOException {
		System.out.println("Delete file");

		String filepath = tableview.getSelectionModel().getSelectedItem().getFilepath();
		
		if (filepath.isEmpty())
			return;
		
		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userRoot();

		String username = userPreferences.get("username", "");

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
			String sql = "DELETE FROM files WHERE FILEPATH = ?";
			stmt=conn.prepareStatement(sql);
		    stmt.setString(1, filepath);
		    stmt.executeUpdate();

		    File file = new File(filepath);
		    
		    if(file.delete())
		    	System.out.println("File Deleted Successfully.");
		    else
		    	System.out.println("File Deletion Failed. You can manually delete the file from disk later.");

		    tableview.setItems(getFiles(username));
		    
		} catch (SQLException e) {

		}
		
		
	}

	@FXML
	private void decryptFile(ActionEvent event) throws IOException, CryptoException {
		System.out.println("Decrypt File");

		String filename = tableview.getSelectionModel().getSelectedItem().getFilename();

		if (filename.isEmpty()) {
			System.out.println("File: " + filename);
			return;
		}

		Connection conn = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://localhost/secura", "secura", "secura");
			System.out.println("Database connection established.");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("Database connection failed.");
		}

//		Statement stmt = null;
//		ResultSet rs = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			System.out.println("Entered SQL");
			String sql = "SELECT FILENAME, FILEPATH FROM files WHERE filename=?";
//			rs = stmt.executeQuery("SELECT filename FROM files WHERE filename=?");
			stmt=conn.prepareStatement(sql);

		    stmt.setString(1, filename);

		    rs = stmt.executeQuery();
		    System.out.println("Query executed.");
		    System.out.println("Filename: " + filename);

		    String file = null;
		    String input_file = null;

			while (rs.next()) {
				file = rs.getString(1);
				input_file = rs.getString(2);
			}

			String key="securasecurasecurasecura";
			File inputFile = new File(input_file.toString());

			final Node node = (Node) event.getSource();
			
			DirectoryChooser directoryChooser = new DirectoryChooser();

//				Set Initial Directory
//				directoryChooser.setInitialDirectory(new File("data/json/invoices"));

			File selectedDirectory = directoryChooser.showDialog(node.getScene().getWindow());

			if (selectedDirectory == null)
				return;

			System.out.println("Selected Directory: " + selectedDirectory.toString());

			File decryptedFile = new File(selectedDirectory.toString()+ "/" + "Decrypted-" + filename);

			System.out.println("Input File: " + inputFile);
			System.out.println("Decrypted File: " + decryptedFile.toString());

			try {
				CryptoUtils.decrypt(key, inputFile, decryptedFile);
				System.out.println(selectedDirectory.toString()+ filename);
				SwingUtilities.invokeLater(new Runnable() {
				    public void run() {
				        try {
				        	Desktop.getDesktop().open(new File(selectedDirectory.toString()));
				        } catch (IOException e) {
				            e.printStackTrace();
				        }
				    }
				});
	      	} finally {
	      		
	      	}
			System.out.println("Looping complete.");
		} catch (SQLException e) {

		}
	}

	@FXML
	private void refresh() throws IOException {
		System.out.println("Refresh Table");
		
		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userRoot();

		String username = userPreferences.get("username", "");

		filename.setCellValueFactory(new PropertyValueFactory<SecuraFile, String>("filename"));
		timestamp.setCellValueFactory(new PropertyValueFactory<SecuraFile, String>("timestamp"));

		// load initial data
		tableview.setItems(getFiles(username));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		java.util.prefs.Preferences userPreferences = java.util.prefs.Preferences.userRoot();

		String username = userPreferences.get("username", "");

//		javafx.event.Event e = new javafx.event.Event(null);
//
//		final Node source = (Node) e.getSource();
//		
//		Stage stage2 = (Stage) source.getScene().getWindow();
//
//		User u = (User) stage2.getUserData();

		System.out.println("Initialized: " + username);

		filename.setCellValueFactory(new PropertyValueFactory<SecuraFile, String>("filename"));
		timestamp.setCellValueFactory(new PropertyValueFactory<SecuraFile, String>("timestamp"));

		// load initial data
		tableview.setItems(getFiles(username));
	}

//	public ObservableList<SecuraFile> getFiles() {
//		ObservableList<SecuraFile> files = FXCollections.observableArrayList();
//		
//		Connection conn = null;
//
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//
//			conn = DriverManager.getConnection("jdbc:mysql://localhost/secura", "secura", "secura");
//			System.out.println("Database connection established successfully.");
//		} catch (SQLException | ClassNotFoundException e) {
//			System.out.println(e.getMessage());
//			System.out.println("Database connection failed.");
//		}
//
//		Statement stmt = null;
//		ResultSet rs = null;
//		try {
//			stmt = conn.createStatement();
////			rs = stmt.executeQuery("SELECT filename, created_at FROM files");
//
//			String sql = "SELECT FILENAME FROM files WHERE username=?";
//			rs = stmt.executeQuery("SELECT filename FROM files WHERE filename=?");
//			stmt=conn.prepareStatement(sql);
//
////		    stmt.setString(1, username);
//
////		    rs = stmt.executeQuery();
//
//			while (rs.next()) {
////				files.add(new SecuraFile(rs.getString(1), rs.getString(2)));
//				System.out.println(rs.getString(1));
//			}
//		} catch (SQLException e) {
//
//		}
//
//		return files;
//	}
	
	public ObservableList<SecuraFile> getFiles(String username) {
		ObservableList<SecuraFile> files = FXCollections.observableArrayList();
		
		Connection conn = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			conn = DriverManager.getConnection("jdbc:mysql://localhost/secura", "secura", "secura");
			System.out.println("Database connection established successfully.");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("Database connection failed.");
		}

//		Statement stmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
//			rs = stmt.executeQuery("SELECT filename, created_at FROM files");
			
			String sql = "SELECT FILENAME, FILEPATH, CREATED_AT FROM files WHERE USERNAME=?";
			stmt=conn.prepareStatement(sql);

		    stmt.setString(1, username);

		    rs = stmt.executeQuery();

			while (rs.next()) {
				files.add(new SecuraFile(rs.getString(1), rs.getString(2), rs.getString(3)));
				System.out.println(rs.getString(1));
				
			}
		} catch (SQLException e) {

		}
		
		return files;
	}
	
	
}
