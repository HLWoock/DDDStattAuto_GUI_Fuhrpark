package de.woock.ddd.stattauto.gui.fuhrpark.entity.station;

import lombok.Data;

@Data
public class Spezifikation {
	private String     beschreibung;
	private GPS        position;
	private String     oepnv;
	
	public Spezifikation () { }

	public Spezifikation(String beschreibung, GPS position, String oepnv) {
		this.beschreibung = beschreibung;
		this.position     = position;
		this.oepnv        = oepnv;
	}
	
	public String formatedText() {
		return String.format("Beschreibung:\n%s\n\n÷PNV:\n%s\n\nPosition:\n%s", beschreibung, oepnv, position);
	}

}
