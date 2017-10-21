package de.woock.ddd.stattauto.gui.fuhrpark.entity.auto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoResource<A> extends ResourceSupport implements Serializable{
	private Long           AutoId;
	private String         kennung;
	private String         typ;
	private FahrzeugKlasse klasse;
	private String         details;
	private Gps            position;
	
	public String getFormatedDetails() {
		return String.format(String.format("Details:\n%s", details));
	}

}
