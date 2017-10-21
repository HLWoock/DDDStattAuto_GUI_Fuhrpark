package de.woock.ddd.stattauto.gui.fuhrpark.service;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Station;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.StationsResource;

@FeignClient(name     = "FUHRPARK-SERVICE", 
             fallback = FuhrparkFallback.class)
public interface Fuhrpark {

	@RequestMapping(value="/Stationen/stationen", method = RequestMethod.GET)
	List<StationsResource> zeigeStationen();
	
	@RequestMapping(value="Stationen/station/id/{stationsId}")
	StationsResource stationFuerStationsId(@PathVariable("stationsId") Long stationsId);
	
	@RequestMapping(value="Stationen/station/kuerzel/{kuerzel}", method = RequestMethod.GET)
	Station stationFuerKuerzel(@PathVariable("kuerzel") String kuerzel);
	
	@RequestMapping(method = RequestMethod.POST, value = "Stationen/einrichten", consumes = "application/json")
    Station stationEinrichten(Station station);
}