package de.woock.ddd.stattauto.gui.fuhrpark.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import de.woock.ddd.stattauto.gui.fuhrpark.DddStattAuto_Gui_Fuhrpark;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.Auswahlkriterien;
import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.StationsResource;
import de.woock.ddd.stattauto.gui.fuhrpark.service.FuhrparkService;
import javafx.application.Application;
import javafx.collections.ObservableList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StationenTableControllerTest {

	@MockBean
	private static FuhrparkService fuhrparkService;
	
	@Autowired
	private StationenTableController stationenTableController;
	
	private StationsResource stationsResourceHH;
	private StationsResource stationsResourceHS;
	
	@BeforeClass
	public static void setUpClass() throws InterruptedException {
	    // init Java FX
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


	private void initMock() {
		List<StationsResource> stationenResources = new ArrayList<StationsResource>();
		stationsResourceHH = new StationsResource();
		stationsResourceHH.setStationsId(0L);
		stationsResourceHH.setAuswahlkriterien(new Auswahlkriterien("Hamburg", "HH", "Harburg", "Rathaus"));
		
		stationsResourceHS = new StationsResource();
		stationsResourceHS.setStationsId(1L);
		stationsResourceHS.setAuswahlkriterien(new Auswahlkriterien("Hamburg", "HS", "Schanze", "Schulterblatt"));

		stationenResources.add(stationsResourceHH);
		stationenResources.add(stationsResourceHS);
		// RemoteService has been injected into the reverser bean
		given(fuhrparkService.zeigeStationen()).willReturn(stationenResources);
	}	

	
	@Test
	public void listOfStationsResources_ConvertedInto_ListOfObservables() {
		initMock();

		ObservableList<StationsResource> observableStationsResources = stationenTableController.holeVomFuhrparkStationen();
		assertThat(observableStationsResources).isNotNull()
		                                       .isNotEmpty()
		                                       .hasSize(2)
		                                       .contains(stationsResourceHH)
		                                       .contains(stationsResourceHS);

		assertThat(observableStationsResources.get(0)).hasFieldOrPropertyWithValue("stationsId", 0L);
		
		observableStationsResources.stream().forEach(s -> assertThat(s).hasFieldOrPropertyWithValue("stadt", "Hamburg"));
	}
}