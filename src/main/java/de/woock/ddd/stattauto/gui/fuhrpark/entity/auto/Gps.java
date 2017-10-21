package de.woock.ddd.stattauto.gui.fuhrpark.entity.auto;

import java.io.Serializable;

import lombok.Data;

@Data
public class Gps implements Serializable {
	double lat;
	double lng;
	
	public Gps() {}
	
	public Gps(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
}
