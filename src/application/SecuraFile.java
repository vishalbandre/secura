package application;

import javafx.beans.property.SimpleStringProperty;

public class SecuraFile {
	public Integer id;

	private SimpleStringProperty filename, filepath, timestamp;

	public SecuraFile(String filename, String filepath, String timestamp) {
		this.filename = new SimpleStringProperty(filename);
		this.filepath = new SimpleStringProperty(filepath);
		this.timestamp = new SimpleStringProperty(timestamp);
	}

	public Integer getId() {
		return id;
	}

	public String getFilename() {
		return filename.get();
	}

	public void setFilename(SimpleStringProperty filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath.get();
	}

	public void setFilepath(SimpleStringProperty filepath) {
		this.filepath = filepath;
	}

	public String getTimestamp() {
		return timestamp.get();
	}

	public void setTimestamp(SimpleStringProperty timestamp) {
		this.timestamp = timestamp;
	}
}
