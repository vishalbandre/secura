package application;

//Accesses a database with student and phone information

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DBConnection {

	private Connection connect = null;
	private Statement statement = null;
	private PreparedStatement preparedStatement = null;
	private ResultSet resultSet = null;

	final private String host = "localhost";
	final private String user = "secura";
	final private String passwd = "secura";
	final private String database = "secura";

	public void connectToDB() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Setup the connection with the DB
			connect = DriverManager.getConnection("jdbc:mysql://" + host + "/"
					+ database + "?" + "user=" + user + "&password=" + passwd);

		} catch (Exception e) {
			throw e;
		}
	}
	
	public Boolean checkLogin(String user, String pass) throws Exception {
		try {
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select * from " + database + ".users");
			while (resultSet.next()) {
				int Id = resultSet.getInt("Id");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");
				String role = resultSet.getString("role");
				
				if(user.equals(username) && pass.equals(password))
					return true;

			}
		} catch (Exception e) {
			throw e;
		}
		return false;
	}

	public void readUsers() throws Exception {
		try {
			statement = connect.createStatement();
			resultSet = statement
					.executeQuery("select * from " + database + ".users");
			while (resultSet.next()) {
				int Id = resultSet.getInt("Id");
				String username = resultSet.getString("username");
				String password = resultSet.getString("password");
				String role = resultSet.getString("role");

				System.out.println(String.format(
						"Id: %d username: %5s  password: %5s role: %5s", Id, username, password, role));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// You need to close the resultSet
	public void close() {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

			if (statement != null) {
				statement.close();
			}

			if (connect != null) {
				connect.close();
			}
		} catch (Exception e) {

		}
	}

}