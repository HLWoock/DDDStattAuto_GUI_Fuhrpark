package de.woock.ddd.stattauto.gui.fuhrpark.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.Auto;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.AutoMap;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.auto.AutoResource;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Spezifikation;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Station;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.StationsResource;  

@Service
public class FuhrparkService {
	
	private static Logger log = Logger.getLogger(FuhrparkService.class);
	
	@Autowired private Fuhrpark fuhrpark;

	public List<StationsResource> zeigeStationen() {
		log.debug(String.format("call service %s", "zeigeStationen()"));
		List<StationsResource> stationen = fuhrpark.zeigeStationen();
		log.debug(String.format("#%d Stationen vom Server geholt", stationen.size()));
		return stationen;
	}
	
	public StationsResource zeigeStationFuerStationsId(Long stationsId) {
		StationsResource stationsResource = fuhrpark.stationFuerStationsId(stationsId);
		log.debug(String.format("StationsResource vom Server geholt: %s", stationsResource));
		return stationsResource;
	}


	public Station zeigeStationFuerStationsKuerzel(String kuerzel) {
		Station station = fuhrpark.stationFuerKuerzel(kuerzel);
		log.debug(String.format("Station vom Server geholt: %s", station));
		return station;
	}

	public Spezifikation spezifikation(StationsResource stationsResource) {
		Spezifikation spezifikation = null;
		if (stationsResource != null && stationsResource.getLink("spezifikation") != null) {
			spezifikation = new RestTemplate().getForObject(stationsResource.getLink("spezifikation").getHref(), Spezifikation.class);
		} else {
			log.debug(String.format("StationsResource enthält keine Spezifikatio als Resource: %s", stationsResource.getKuerzel()));
			spezifikation = new Spezifikation();
		}
		log.debug(String.format("Spezifikation vom Server geholt: %s", spezifikation));
		return spezifikation;
	}

	public AutoMap autoMap(AutoResource<Auto> autoResource) {
		return (autoResource != null) ? new RestTemplate().getForObject(autoResource.getLink("map").getHref(), AutoMap.class)
				                      : null;
	}
	
	public List<AutoResource<Auto>> autos(StationsResource stationsResource) {
		List<AutoResource<Auto>> autos = new ArrayList<>();
		if (stationsResource.getLink("fahrzeuge") != null) {
			autos = new RestTemplate().exchange(stationsResource.getLink("fahrzeuge").getHref(), HttpMethod.GET, HttpEntity.EMPTY, 
				                                new ParameterizedTypeReference<List<AutoResource<Auto>>>() {})
				                     .getBody();
		}
		
		return autos;
	}
	
	public void neueStationEinrichten(Station station) {
		fuhrpark.stationEinrichten(station);
	}

}


