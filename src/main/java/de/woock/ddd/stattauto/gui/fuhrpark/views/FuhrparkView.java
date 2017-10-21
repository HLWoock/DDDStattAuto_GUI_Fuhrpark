package de.woock.ddd.stattauto.gui.fuhrpark.views;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import de.woock.ddd.stattauto.gui.fuhrpark.controller.AutoTableController;
import de.woock.ddd.stattauto.gui.fuhrpark.controller.StationenTableController;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.Auto;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.AutoMap;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.AutoResource;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Spezifikation;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Station;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.StationsResource;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

@Component
public class FuhrparkView implements MapComponentInitializedListener {

	@Autowired StationenTableController stationenTableController;
	@Autowired AutoTableController      autoTableController;
	
	@Autowired NewStationDialog           newStationView;
	
	private static Logger log = Logger.getLogger(FuhrparkView.class);

	public TableView<StationsResource> tblStationen = new TableView<>();
	public TableView<AutoResource<Auto>>        tblFahrzeuge = new TableView<>();
	
	public ObservableList<StationsResource> stationen;

	public GoogleMapView mapView;
	public GoogleMap     map;
	public Marker        marker;
	
	public TextField     txStationenSuche = new TextField();
	
	public ImageView imgStationsLage = null;
	public ImageView imgCar          = null;
	public ImageView imgLupe         = null;
	
	final Label    lbStationen       = new Label("Stationen");
	final Label    lbLage            = new Label("Spezifikation");
	final Label    lbStation         = new Label();
	final Label    lbZubehoer        = new Label();
	
	
	final TextArea txLage            = new TextArea("");

	final Button bnNewS              = new Button("Einrichten");
	final Button bnEditS             = new Button("Ändern");
	final Button bnDeleteS           = new Button("Aufgeben");
	final Button bnNewF              = new Button("Einrichten");
	final Button bnUeberfuehrenF     = new Button("Überführen");
	final Button bnEditF             = new Button("Ändern");
	final Button bnDeleteF           = new Button("Aufgeben");
	final Button bnDefektF           = new Button("Defekt Melden");
	final Button bnTuevF             = new Button("TÜV");

	
	@Override
	public void mapInitialized() {
	    MapOptions mapOptions = new MapOptions();
	
	    mapView.setMaxWidth(330.0);
	    mapView.setMaxHeight(330.0);
	    map = mapView.createMap(mapOptions);
	    
	    MarkerOptions markerOptions = new MarkerOptions();
	    markerOptions.visible(Boolean.TRUE)
	                 .title("Station");
	
	    marker = new Marker(markerOptions);
	    map.addMarker(marker);
	}

	public Pane initPane() {
		setFontsForLabls();
		
		initMap();
		initTableStations();
		initTableFahrzeuge();
		initImages();
		
		setTableListeners();
		setButtonListeners();
		setStationsSucheListener();

		return initGrid(initButtons(bnNewS, bnEditS, bnDeleteS), 
				        initButtons(bnNewF, bnUeberfuehrenF, bnEditF, bnDeleteF, bnDefektF, bnTuevF));
	}

	public void setValues(Object dto) {
		if (dto == null){
			lbZubehoer.setText("");
			imgCar.setImage(null);
		} else if (dto instanceof StationsResource) {
			lbStation.setText(((StationsResource) dto).getAuswahlkriterien().getStadtteil());
			tblFahrzeuge.setItems(autoTableController.holeVomFuhrparkSelektierteStationMitAutos());
		} else if (dto instanceof Spezifikation) {
			txLage.setText(((Spezifikation) dto).formatedText());
		} else if (dto instanceof AutoResource) {
			lbZubehoer.setText(((AutoResource<Auto>) dto).getFormatedDetails());
		} else if (dto instanceof AutoMap) {
			imgCar.setImage(((AutoMap) dto).getImage());
		}
	}

	private void initMap() {
		mapView = new GoogleMapView();
	    mapView.addMapInializedListener(this);
	    log.debug(mapView == null ? "null" : mapView);
	}

	private GridPane initGrid(final HBox hbStationButtons, final HBox hbFahrzeugeButtons) {
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(5);
		grid.setPadding(new Insets(0, 10, 0, 10));
		grid.setGridLinesVisible(false);
		
		grid.add(lbStationen, 0, 0);
		grid.add(txStationenSuche, 1, 0);
		grid.add(imgLupe, 2, 0);
		grid.add(tblStationen, 0, 1, 3, 6);
		grid.add(lbStation, 3, 1);
		grid.add(lbLage, 3, 2);
		grid.add(txLage, 3, 3);
		//grid.add(imgStationsLage, 4, 3);
		grid.add(mapView, 4, 3);
		grid.add(tblFahrzeuge, 3, 4, 1, 2);
		grid.add(hbStationButtons, 0, 7, 2, 1);
		grid.add(hbFahrzeugeButtons, 3, 7, 2, 1);
		grid.add(imgCar, 4, 4);
		grid.add(lbZubehoer, 4, 5);
		
		return grid;
	}

	private void setFontsForLabls() {
		lbLage     .setFont(new Font("Arial", 16));
		lbStationen.setFont(new Font("Arial", 20));
		lbStation  .setFont(new Font("Arial", 18));
		lbZubehoer .setFont(new Font("Arial", 18));
		txLage     .setFont(new Font("Arial", 14));
	}

	private HBox initButtons(Button... arg) {
		final HBox hbbuttons = new HBox(5);
		hbbuttons.setPadding(new Insets(5.0));
		Arrays.asList(arg).forEach(hbbuttons.getChildren()::add);
		return hbbuttons;
	}

	private void initImages() {
		imgLupe         = new ImageView(new Image(FuhrparkView.class.getResourceAsStream("graphics/lupe.png")));
		imgCar          = new ImageView(new Image(FuhrparkView.class.getResourceAsStream("graphics/autoauswaehlen.png")));
		imgStationsLage = new ImageView(new Image(FuhrparkView.class.getResourceAsStream("graphics/stationauswaehlen.png")));
	}

	private void initTableStations() {
		stationen = stationenTableController.holeVomFuhrparkStationen();
		tblStationen.setItems(stationen);
		tblStationen.setEditable(true);
		
		TableColumn<StationsResource, String> colStadt     = new TableColumn<>("Stadt");
		TableColumn<StationsResource, String> colStadtteil = new TableColumn<>("Stadtteil");
		TableColumn<StationsResource, String> colStandort  = new TableColumn<>("Standort");
		TableColumn<StationsResource, String> colKuerzel   = new TableColumn<>("Kürzel");
		
		colStadt    .setCellValueFactory(new PropertyValueFactory<>("stadt"));
		colStadtteil.setCellValueFactory(new PropertyValueFactory<>("stadtteil"));
		colStandort .setCellValueFactory(new PropertyValueFactory<>("standort"));
		colKuerzel  .setCellValueFactory(new PropertyValueFactory<>("kuerzel"));
		
		colStadt    .setMinWidth(100);
		colStadtteil.setMinWidth(100);
		colStandort .setMinWidth(200);
		colKuerzel  .setMinWidth(50);
		
		tblStationen.getColumns().addAll(colStadt, colStadtteil, colStandort, colKuerzel);
		tblStationen.getSelectionModel().setCellSelectionEnabled(true);
	}

	private void initTableFahrzeuge() {
		tblFahrzeuge.setItems(autoTableController.holeVomFuhrparkSelektierteStationMitAutos());
		tblFahrzeuge.setEditable(true);
		
		TableColumn<AutoResource<Auto>, String> colKennung = new TableColumn<>("Kennung");
		TableColumn<AutoResource<Auto>, String> colTyp     = new TableColumn<>("FahrzeugTyp");
		TableColumn<AutoResource<Auto>, String> colKlasse  = new TableColumn<>("Fahrzeugklasse");
		TableColumn<AutoResource<Auto>, String> colDetails = new TableColumn<>("Details");
		
		colKennung.setCellValueFactory(new PropertyValueFactory<>("kennung"));
		colTyp    .setCellValueFactory(new PropertyValueFactory<>("typ"));
		colKlasse .setCellValueFactory(new PropertyValueFactory<>("klasse"));
		colDetails.setCellValueFactory(new PropertyValueFactory<>("details"));
		
		colKennung.setMinWidth(50);
		colTyp    .setMinWidth(150);
		colKlasse .setMinWidth(100);
		colDetails.setMinWidth(250);
		
		tblFahrzeuge.getColumns().addAll(colKennung, colTyp, colKlasse, colDetails);
		tblFahrzeuge.getSelectionModel().setCellSelectionEnabled(true);	}

	private void setTableListeners() {
		tblStationen.getSelectionModel().getSelectedCells().addListener(stationenTableController);
		tblFahrzeuge.getSelectionModel().getSelectedCells().addListener(autoTableController);
	}

	private void setButtonListeners() {
		bnDefektF.setOnMouseClicked(autoTableController);
		bnNewS.setOnAction(e -> newStationView.newStation());
	}

	private void setStationsSucheListener() {
		txStationenSuche.setOnKeyPressed(stationenTableController);
	}
}
