module secura {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.graphics;
	requires javafx.base;
	requires java.desktop;
	requires java.prefs;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
