package de.woock.ddd.stattauto.gui.fuhrpark.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter {
	
	public static String getFormattedDate(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
		return formatter.format(date);
	}
}
