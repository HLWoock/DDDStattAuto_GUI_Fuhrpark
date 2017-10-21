package de.woock.ddd.stattauto.gui.fuhrpark.controller;

import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.Auto;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.AutoMap;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.AutoResource;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Station;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.StationsResource;
import de.woock.ddd.stattauto.gui.fuhrpark.service.FuhrparkService;
import de.woock.ddd.stattauto.gui.fuhrpark.views.FuhrparkView;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

@SuppressWarnings("rawtypes")
@Service
public class AutoTableController implements ListChangeListener, EventHandler<MouseEvent> {
	
	private final static Logger log = Logger.getLogger(AutoTableController.class);

	@Autowired FuhrparkService fuhrparkService;
	@Autowired FuhrparkView    stationenView;
	@Autowired JmsTemplate     jmsTemplate;
		
	@Override
	public void onChanged(Change c) {
		AutoResource<Auto> autoResource = stationenView.tblFahrzeuge.getSelectionModel().getSelectedItem();
		
		AutoMap         map = fuhrparkService.autoMap(autoResource);
		
		stationenView.setValues(map);
		stationenView.setValues(autoResource);
	}
	
	public ObservableList<AutoResource<Auto>> holeVomFuhrparkSelektierteStationMitAutos() {
		StationsResource stationsResource = stationenView.tblStationen.getSelectionModel().getSelectedItem();
		if (stationsResource != null) {
			ObservableList<AutoResource<Auto>> autos = FXCollections.observableArrayList();
			List<AutoResource<Auto>> autoListe = fuhrparkService.autos(stationsResource);
			autoListe.forEach(f -> autos.add(f));
			return autos;
		} else {
			return null;
		}
	}

	@Override
	public void handle(MouseEvent event) {
		log.debug("sende Nachricht");
		AutoResource<Auto> autoResource = stationenView.tblFahrzeuge.getSelectionModel().getSelectedItem();
		// Message<AutoResource<Auto>> msg = MessageBuilder.withPayload(autoResource).build();
		jmsTemplate.setDefaultDestinationName("DefektmeldungAuto");
		jmsTemplate.send(new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				ObjectMessage objectMessage = session.createObjectMessage(autoResource);
				return objectMessage;
			}
		});
	}
}
