package de.woock.ddd.stattauto.gui.fuhrpark.entity.station;

import java.io.Serializable;

import lombok.Data;

@Data
public class GPS implements Serializable {
	double lat;
	double lng;
	
	public GPS() {}
	
	public GPS(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
}
