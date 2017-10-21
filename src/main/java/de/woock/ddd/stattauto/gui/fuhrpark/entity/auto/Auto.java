package de.woock.ddd.stattauto.gui.fuhrpark.entity.auto;

import java.io.Serializable;

import de.woock.ddd.stattauto.gui.fuhrpark.entity.station.GPS;
import lombok.Data;

@Data
public class Auto implements Serializable {

	Long           id;
	String         kennung;
	String         typ;
	FahrzeugKlasse klasse;
	String         details;
	Bild           bild;
	GPS            position;
	
	public Auto(){
	}

	public Auto(String kennung, String typ, FahrzeugKlasse klasse, String details, Bild bild, GPS position) {
		this.kennung  = kennung;
		this.typ      = typ;
		this.klasse   = klasse;
		this.details  = details;
		this.bild     = bild;
		this.position = position;
	}
	
	public String getFormatedDetails() {
		return String.format(String.format("Details:\n%s", details));
	}
}
