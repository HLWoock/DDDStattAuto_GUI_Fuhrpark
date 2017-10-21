package de.woock.ddd.stattauto.gui.fuhrpark.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Auswahlkriterien;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.GPS;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Spezifikation;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Station;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.StationsResource;

@Service
public class FuhrparkFallback implements Fuhrpark {
	
	private static Logger log = Logger.getLogger(FuhrparkFallback.class);

	@Autowired DiscoveryClient dc;
	@Autowired @LoadBalanced RestTemplate restTemplate;
	
	@Override
	public List<StationsResource> zeigeStationen() {
		log.debug(String.format("Fallback of %s", "zeigeStationen"));
//		return restTemplate.exchange(getUri() + "Stationen/", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<StationsResource<Station>>>() {}).getBody();
		AsyncRestTemplate asycTemp = new AsyncRestTemplate();
//		
		ListenableFuture<ResponseEntity<List<StationsResource>>> exchange = asycTemp.exchange(getUri() + "Stationen/stationen", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<StationsResource>>() {});
		try {
			return exchange.get().getBody();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return new ArrayList<StationsResource>();
	}

	@Override
	public Station stationFuerKuerzel(String kuerzel) {
		log.debug(String.format("Fallback of %s", "stationFuerKuerzel"));
		return new Station();
	}

	@Override
	public Station stationEinrichten(Station station) {
		log.debug(String.format("Fallback of %s", "stationEinrichten"));
		return null;
	}

	@Override
	public StationsResource stationFuerStationsId(Long stationsId) {
		log.debug(String.format("Fallback of %s(%d)", "stationFuerStationsId", stationsId));
		Auswahlkriterien auswahlkriterien = new Auswahlkriterien("Test", "TT", "Test", "Test");
		StationsResource stationsResource = new StationsResource();
		stationsResource.setStationsId(1L);
		stationsResource.setAuswahlkriterien(auswahlkriterien);
		stationsResource.add(new Link("http://localhost:8181/Stationen/station/id/1/fahrzeuge", "fahrzeuge"));
		
		return stationsResource;
	}
	
	private String getUri() {
		List<ServiceInstance> instances = dc.getInstances("FUHRPARK-SERVICE");
		  
		ServiceInstance serviceInstance = instances.get(0);
		String uri = String.format("%s/", serviceInstance.getUri());
		log.debug("Fallback: " + uri);

		return uri;
	}

}
