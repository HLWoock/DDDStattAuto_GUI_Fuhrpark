package de.woock.ddd.stattauto.gui.fuhrpark.views;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Auswahlkriterien;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.GPS;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Spezifikation;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Station;
import de.woock.ddd.stattauto.gui.fuhrpark.service.FuhrparkService;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

@Component
public class NewStationDialog {
	
	@Autowired FuhrparkService fuhrparkService;
	@Autowired JmsTemplate     jmsTemplate;

	private TextField txLat          = new TextField();
	private TextField txLng          = new TextField();

	public void info() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Look, an Information Dialog");
		alert.setContentText("I have a great message for you!");

		alert.showAndWait();
	}
	

	public void newStation() {
		Dialog<Station> dialog = new Dialog<>();
		dialog.setTitle("Neue Station Einrichten");
		dialog.setHeaderText("Bitte komplett ausfüllen");
	
		// Set the icon (must be included in the project).
		dialog.setGraphic(new ImageView(this.getClass().getResource("graphics/login.png").toString()));
	
		// Set the button types.
		ButtonType bnEinrichten = new ButtonType("Einrichten", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(bnEinrichten, ButtonType.CANCEL);
	
		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 10, 10, 10));
	
		TextField txStadt        = new TextField(); txStadt       .setPromptText("Stadt");        grid.add(txStadt       , 0, 0);
		TextField txStadtteil    = new TextField(); txStadtteil   .setPromptText("Stadtteil");    grid.add(txStadtteil   , 0, 1);
		TextField txStandort     = new TextField(); txStandort    .setPromptText("Standort");     grid.add(txStandort    , 0, 2);
		TextField txKuerzel      = new TextField(); txKuerzel     .setPromptText("Kürzel");       grid.add(txKuerzel     , 0, 3);
		TextField txBeschreibung = new TextField(); txBeschreibung.setPromptText("Beschreibung"); grid.add(txBeschreibung, 0, 4);
		TextField txOepnv        = new TextField(); txOepnv       .setPromptText("ÖPNV");         grid.add(txOepnv       , 0, 5);
		                                            txLat         .setPromptText("Lat");          grid.add(txLat         , 0, 6);
		                                            txLng         .setPromptText("Lng");          grid.add(txLng         , 0, 7);
		
		
		// Enable/Disable login button depending on whether a username was entered.
		javafx.scene.Node loginButton = dialog.getDialogPane().lookupButton(bnEinrichten);
		loginButton.setDisable(true);
	
		// Do some validation (using the Java 8 lambda syntax).
		txLat.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});
	
		dialog.getDialogPane().setContent(grid);
	
		// Request focus on the username field by default.
		Platform.runLater(() -> txStadtteil.requestFocus());
	
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == bnEinrichten) {
		        return new Station(new Auswahlkriterien(txStadt.getText(), txKuerzel.getText(), txStadtteil.getText(), txStandort.getText()), 
		        		           new Spezifikation(txBeschreibung.getText(), 
		        		        		         new GPS(Double.valueOf(txLat.getText()), Double.valueOf(txLng.getText())), 
		        		        		             txOepnv.getText()));
		    }
		    return null;
		});
	
		Optional<Station> result = dialog.showAndWait();
	
		result.ifPresent(station -> fuhrparkService.neueStationEinrichten(station));
	}
}
