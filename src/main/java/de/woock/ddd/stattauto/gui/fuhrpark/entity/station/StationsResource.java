package de.woock.ddd.stattauto.gui.fuhrpark.entity.station;

import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StationsResource extends ResourceSupport {
	
	private Long             stationsId;
	private Auswahlkriterien auswahlkriterien;
	
	// alle getter für JavaFX TableView nötig. Nicht löschen!
	public String getStadt() {
		return auswahlkriterien.getStadt();
	}
	
	public String getStadtteil() {
		return auswahlkriterien.getStadtteil();
	}
	
	public String getStandort() {
		return auswahlkriterien.getStandort();
	}
	
	public String getKuerzel() {
		return auswahlkriterien.getKuerzel();
	}
}
