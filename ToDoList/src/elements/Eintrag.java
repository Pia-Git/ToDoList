package elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Die Klasse Eintrag represaentiert einen Listeneintrag, welches die
 * Aufgabenbeschreibung (String) und den Endzeitpunkt der Aufgabe (Date inkl.
 * Zeitangabe) beinhaltet.
 * 
 * @author Pia
 * 
 */
public class Eintrag {

	private String aufgabe;
	private String endzeitpunkt;
	private String time;
	private boolean isErledigt = false;
	private boolean withTime = true;

	public Eintrag(String str, Date t) {
		aufgabe = str;
		if (t == null) {
			withTime = false;
			endzeitpunkt = "";
			time = "";
		} else {
			withTime = true;
			endzeitpunkt = toString(t);
		}
	}

	public String getAufgabe() {
		return aufgabe;
	}

	public void setAufgabe(String aufgabe) {
		this.aufgabe = aufgabe;
	}

	public String getEndzeitpunkt() {
		return endzeitpunkt;
	}

	public String getTime() {
		return time;
	}

	public void setEndzeitpunkt(Date endzeitpunkt) {
		if (endzeitpunkt == null) {
			withTime = false;
			this.endzeitpunkt = "";
			time = "";
		} else {
			withTime = true;
			this.endzeitpunkt = toString(endzeitpunkt);
		}
	}

	public boolean getErledigt() {
		return isErledigt;
	}

	public boolean withTime() {
		return withTime;
	}

	public void setErledigt(boolean isErledigt) {
		this.isErledigt = isErledigt;
	}

	public String toString(Date enddatum) {
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy (HH:mm)");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		time = timeFormat.format(enddatum);
		return format.format(enddatum);
	}

}
