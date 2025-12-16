module exercicio_JavaFX {
	
	requires javafx.controls;
	requires javafx.fxml;
	requires org.controlsfx.controls;
	
	opens application;
	opens controller;
	opens model;
	
}