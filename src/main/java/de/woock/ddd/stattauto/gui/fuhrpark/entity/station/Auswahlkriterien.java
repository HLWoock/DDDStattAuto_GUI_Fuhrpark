package de.woock.ddd.stattauto.gui.fuhrpark.entity.station;

import lombok.Data;

@Data
public class Auswahlkriterien {

	private String stadt;
	private String kuerzel;
	private String stadtteil;
	private String standort;
	
	public Auswahlkriterien() { }

	public Auswahlkriterien(String stadt, String kuerzel, String stadtteil, String standort) {
		this.stadt     = stadt;
		this.kuerzel   = kuerzel;
		this.stadtteil = stadtteil;
		this.standort  = standort;
	}
}
