package elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

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
	private Date enddate;
	private String time;
	private String restzeit;
	private boolean isErledigt = false;
	private boolean withTime = true;

	public Eintrag(String str, Date t) {
		aufgabe = str;
		if (t == null) {
			withTime = false;
			endzeitpunkt = "";
			enddate = null;
			time = "";
		} else {
			withTime = true;
			endzeitpunkt = toString(t);
			enddate = t;
			berechneRestzeit(t);
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
	
	public Date getEnddate() {
		return enddate;
	}

	public String getTime() {
		return time;
	}
	
	public void setRestzeit(String restzeit) {
		if (this.endzeitpunkt == ""){
			this.restzeit = "";
		}
		else {
			this.restzeit = restzeit;
		}
	}
	
	public String getRestzeit() {
		return restzeit;
	}

	public void setEndzeitpunkt(Date endzeitpunkt) {
		if (endzeitpunkt == null) {
			withTime = false;
			this.endzeitpunkt = "";
			this.enddate = null;
			time = "";
		} else {
			withTime = true;
			this.endzeitpunkt = toString(endzeitpunkt);
			this.enddate = endzeitpunkt;
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
	
	public void berechneRestzeit(Date end) {
		Date now = new Date(); //current date
		System.out.println("aktuelles datum: "+now);
		System.out.println("end datum: "+end);
		long diff = end.getTime() - now.getTime(); //milliseconds
		long days = diff/(1000*60*60*24);
		long hours = (diff-days*1000*60*60*24)/(1000*60*60);
		long minutes = (diff-days*1000*60*60*24-hours*1000*60*60)/(1000*60);
		String rest = "";
		if(days == 0){
			if(hours == 0){
				rest = minutes + "m";
			}else{
				rest = hours + "h" + minutes + "m";
			}
		}else{
			rest = days + "d" + hours + "h" + minutes + "m";
		}
		setRestzeit(rest);
	}

}
