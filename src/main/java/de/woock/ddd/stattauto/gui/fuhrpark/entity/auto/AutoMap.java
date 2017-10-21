package de.woock.ddd.stattauto.gui.fuhrpark.entity.auto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoMap implements Serializable {

	private byte[] map;
	
	public AutoMap(byte[] map) {
		this.map = map;
	}
	
	public AutoMap() {}
	
	public WritableImage getImage() {
		try {
			return SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(map)), new WritableImage(330, 330));
		} catch (IOException e) {
			return null;
		}
	}
}
