package de.woock.ddd.stattauto.gui.fuhrpark.controller;

import org.assertj.core.api.BDDAssertions;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.github.tomakehurst.wiremock.client.WireMock;

import de.woock.ddd.stattauto.gui.fuhrpark.DddStattAuto_Gui_Fuhrpark;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.StationsResource;
import de.woock.ddd.stattauto.gui.fuhrpark.service.FuhrparkService;
import javafx.application.Application;

@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureWireMock(port = 9191)
public class DddStattAutoGuiFuhrparkApplicationTests {
	
	@Autowired
	FuhrparkService fuhrparkService;
	
	@BeforeClass
	public static void setUpClass() throws InterruptedException {
	    // Initialise Java FX

	    System.out.println("About to launch FX App");
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(DddStattAuto_Gui_Fuhrpark.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	    Thread.sleep(500);
	}	
	
	@Test
	public void zeigeEineStation() {
		WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/Stationen/station/id/1"))
		        .willReturn(WireMock.aResponse()
		        		            .withBodyFile("station.json")
		        		            .withFixedDelay(5000)
		        		            .withHeader("Content-Type", "application/json")
		        		            .withStatus(201)));
		
		StationsResource stationsResource = fuhrparkService.zeigeStationFuerStationsId(1L);

		ResponseEntity<StationsResource> entity = new RestTemplate().exchange("http://localhost:9191/Stationen/station/id/1", 
				                                                             HttpMethod.GET, 
				                                                             null, 
				                                                             new ParameterizedTypeReference<StationsResource>() {});
		BDDAssertions.then(stationsResource.getAuswahlkriterien().getStadt())
		             .isEqualTo("Test");
		BDDAssertions.then(entity.getStatusCodeValue())
		             .isEqualTo(201);
		BDDAssertions.then(((StationsResource)entity.getBody()).getAuswahlkriterien().getStadt()
				      .equals("Test"));
	}

}
