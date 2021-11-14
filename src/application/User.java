package application;

import javafx.beans.property.SimpleStringProperty;

public class User {
	public Integer id;
	
//	public String usename;
//	
//	public String password;
//	
//	public String role;

	private SimpleStringProperty username, password, role;

	public User(String usename, String password, String role) {
		this.username = new SimpleStringProperty(usename);
		this.password = new SimpleStringProperty(password);
		this.role = new SimpleStringProperty(role);
	}

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username.get();
	}

	public void setUsername(SimpleStringProperty username) {
		this.username = username;
	}

	public String getPassword() {
		return password.get();
	}

	public void setPassword(SimpleStringProperty password) {
		this.password = password;
	}

	public String getRole() {
		return role.get();
	}

	public void setRole(SimpleStringProperty role) {
		this.role = role;
	}

		
}
