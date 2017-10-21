package de.woock.ddd.stattauto.gui.fuhrpark.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Spezifikation;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Station;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.StationsResource;
import de.woock.ddd.stattauto.gui.fuhrpark.service.FuhrparkService;
import de.woock.ddd.stattauto.gui.fuhrpark.views.FuhrparkView;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

@SuppressWarnings("rawtypes")
@Controller
public class StationenTableController implements ListChangeListener, EventHandler<KeyEvent> {
	
	@Autowired FuhrparkService    fuhrparkService;
	@Autowired FuhrparkView       stationenView;

	ObservableList<StationsResource> stationen = FXCollections.observableArrayList();

	@Override
	public void onChanged(Change c) {
		StationsResource stationsResource = stationenView.tblStationen.getSelectionModel().getSelectedItem();
		Spezifikation spezifikation;
		
		if (stationsResource != null && stationsResource.hasLinks()) {
			spezifikation = fuhrparkService.spezifikation(stationsResource);
		} else {
			spezifikation = fuhrparkService.zeigeStationFuerStationsKuerzel(stationsResource.getKuerzel())
					                       .getSpezifikation();
		}
		
		stationenView.setValues(stationsResource);
		stationenView.setValues(null);
		if (spezifikation != null) {
			stationenView.setValues(spezifikation);
		}
		
		MapOptions mapOptions = new MapOptions();

		mapOptions.center(new LatLong(spezifikation.getPosition().getLng(), spezifikation.getPosition().getLat()))
	              .mapType(MapTypeIdEnum.ROADMAP)
	              .overviewMapControl(false)
	              .panControl(false)
                  .rotateControl(false)
	              .scaleControl(false)
	              .streetViewControl(false)
	              .zoomControl(false)
	              .zoom(15);

	    stationenView.mapView.setMaxWidth (330.0);
	    stationenView.mapView.setMaxHeight(330.0);
	    stationenView.map = stationenView.mapView.createMap(mapOptions);
	    
	    MarkerOptions markerOptions = new MarkerOptions();

	    double lng = spezifikation.getPosition().getLng();
	    double lat = spezifikation.getPosition().getLat();
	    markerOptions.position(new LatLong(lng, lat))
	                 .visible(Boolean.TRUE)
	                 .title("Station");

	    stationenView.marker.setOptions(markerOptions);
	    stationenView.map.addMarker(stationenView.marker);
	}
	
	@Override
	public void handle(KeyEvent event) {
		String suchText = stationenView.txStationenSuche.getText();
		
		ObservableList<StationsResource> gefilterteStationen = FXCollections.observableArrayList();
		stationen.stream().filter(s -> s.getAuswahlkriterien().getStadt().contains(suchText) 
				                    || s.getAuswahlkriterien().getStadtteil().contains(suchText)
				                    || s.getAuswahlkriterien().getStandort().contains(suchText)
				                    || s.getAuswahlkriterien().getKuerzel().contains(suchText))
		                  .forEach(s -> gefilterteStationen.add(s));
		
		stationenView.tblStationen.setItems(gefilterteStationen);
	}

	public ObservableList<StationsResource> holeVomFuhrparkStationen() {
		fuhrparkService.zeigeStationen().forEach(s -> stationen.add(s));
		return stationen;
	}
	
	public StationsResource holeVomFuhrparkStation(Long stationsId) {
		return fuhrparkService.zeigeStationFuerStationsId(stationsId);
	}	
}
