package de.woock.ddd.stattauto.gui.fuhrpark.event;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import de.woock.ddd.stattauto.gui.fuhrpark.controller.StationenTableController;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.Auto;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.AutoResource;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Station;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.StationsResource;
import de.woock.ddd.stattauto.gui.fuhrpark.views.FuhrparkView;

@Component
public class NachrichtenReceiver {
	
	private static Logger log = Logger.getLogger(NachrichtenReceiver.class);
	
	@Autowired FuhrparkView stationenView;
	@Autowired StationenTableController stationenTableController;

	@JmsListener(destination = "DefektmeldungAuto", containerFactory = "myFactory")
	public void receiveMessageDefektMeldungAuto(AutoResource<Auto> auto) {
		log.debug(String.format("Event: DefektmeldungAuto empfangen: %s", auto));
		AutoResource<Auto> selectedItem = stationenView.tblFahrzeuge.getSelectionModel().getSelectedItem();
		stationenView.tblFahrzeuge.refresh();
		selectedItem.setDetails("defekt");
		System.out.println("Received <" + auto.getFormatedDetails() + ">");
	}
	
	@JmsListener(destination = "NeueStation", containerFactory = "myFactory")
	public void receiveMessageNeueStation(Long stationsId) {
		log.debug(String.format("Event: NeueStation empfangen: %d", stationsId));
		StationsResource stationsResource = stationenTableController.holeVomFuhrparkStation(stationsId);
		stationenView.stationen.add(stationsResource);
		stationenView.tblStationen.refresh();
		//stationenView.tblStationen.getSelectionModel().select(stationsResource);
	}
}
