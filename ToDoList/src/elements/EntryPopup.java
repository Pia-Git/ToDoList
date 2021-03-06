package elements;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.toedter.components.JSpinField;

/**
 * Die Klasse EintragPopup represaentiert einen Popup fuer die Erstellung eines
 * Listeneintrages in Form einer JOptionPane. Diese beinhaltet JTextfields fuer
 * die Aufgabenbeschreibung, Datum- und Zeitangabe.
 * 
 * @author Pia
 * 
 */
public class EntryPopup {

	JTextField stringField;
	JTextField dateField;
	JSpinField hourField;
	JSpinField minuteField;
	JCheckBox checkTime;
	DateFormat dateFormat;
	JTable table;
	ListTable listTable;
	JPanel pane;
	boolean isTime = true;
	boolean newEintrag = true;
	int selectedRow = -1;
	int minute;
	int hour;

	public EntryPopup(JTable tab, boolean neu) {

		newEintrag = neu;
		stringField = new JTextField(20);
		dateField = new JTextField(10);
		hourField = new JSpinField(0,23);
		hourField.setPreferredSize(new Dimension(39,26));
		minuteField = new JSpinField(0,59);
		minuteField.setPreferredSize(new Dimension(39,26));
		checkTime = new JCheckBox();
		dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		table = tab;
		listTable = (ListTable) tab.getModel();

		this.fillProperties();

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(3, 1));
		JPanel task = new JPanel();
		task.add(new JLabel("ToDo:"));
		task.add(stringField);
		JPanel timeOnOff = new JPanel();
		timeOnOff.add(new JLabel("Time On/Off"));
		timeOnOff.add(checkTime);
		JPanel endTime = new JPanel();
		endTime.add(new JLabel("Finish date:"));
		endTime.add(dateField);
		endTime.add(new JLabel("End time:"));
		endTime.add(hourField);
		endTime.add(new JLabel("h"));
		endTime.add(minuteField);
		endTime.add(new JLabel("m"));
		myPanel.add(task);
		myPanel.add(timeOnOff);
		myPanel.add(endTime);
		
		pane = myPanel;

		// listeners

		dateField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isTime){
					CalendarPopup popup = new CalendarPopup(dateField);
					popup.showPopup();
				}
			}
		});

		checkTime.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				JCheckBox cb = (JCheckBox) event.getSource();
				if (cb.isSelected()) {
					isTime = true;
					dateField.setEnabled(true);
					hourField.setEnabled(true);
					minuteField.setEnabled(true);
				} else {
					isTime = false;
					dateField.setEnabled(false);
					hourField.setEnabled(false);
					minuteField.setEnabled(false);
				}
			}
		});
	}
	
	public void showPopup(){
		String title = "";
		if(this.newEintrag)
			title = "Create Entry";
		else
			title = "Edit Entry";

		int result = JOptionPane.showConfirmDialog(null, pane,
				title, JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			if (!stringField.getText().equals("")) {
				Date d = null;
				// falls Zeit angegeben
				if (isTime) {
					try {
						d = dateFormat.parse(dateField.getText());
						Calendar gc = new GregorianCalendar();
						gc.setTime(d);
						int year = gc.get(Calendar.YEAR);
						int month = gc.get(Calendar.MONTH);
						int day = gc.get(Calendar.DAY_OF_MONTH);
						// get hours and minutes
						int hour = hourField.getValue();
						int min = minuteField.getValue();
						gc.set(year, month, day, hour, min, 0);
						// setz Datum mit Zeit
						d = gc.getTime();
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

				// Eintrag in Tabelle hinzufuegen falls neuer Eintrag
				if (this.newEintrag) {
					Entry e = new Entry(stringField.getText(), d);
					listTable.addEntry(e);
				}
				// ansonsten Eintrag veraendern
				else {
					Entry editE = listTable.getEintragAt(selectedRow);
					editE.setAufgabe(this.stringField.getText());
					editE.setEndzeitpunkt(d);
					if(d != null){
						editE.berechneRestzeit(d);
					}
					listTable.editEntry(selectedRow, editE);
				}
			}
		}
	}
	
	public void getCurrentTime() {
		Date date = new Date();
		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		String time = timeFormat.format(date);
		String[] timeArray = time.split(":");
		hour = Integer.parseInt(timeArray[0]);
		minute = Integer.parseInt(timeArray[1]);
	}

	public void fillProperties() {
		if (this.newEintrag) {
			// Default
			Date date = new Date();
			this.dateField.setText(this.dateFormat.format(date));
			this.getCurrentTime();
			this.hourField.setValue(hour);
			this.minuteField.setValue(minute);
			this.checkTime.setSelected(true);
		} else {
			selectedRow = table.convertRowIndexToView(table.getSelectedRow());
			Entry editE = listTable.getEintragAt(selectedRow);
			this.stringField.setText(editE.getAufgabe());
			if (!editE.withTime()) {
				Date date = new Date();
				isTime = false;
				this.dateField.setText(this.dateFormat.format(date));
				this.getCurrentTime();
				this.hourField.setValue(hour);
				this.minuteField.setValue(minute);
				this.dateField.setEnabled(false);
				this.hourField.setEnabled(false);
				this.minuteField.setEnabled(false);
			}
			else{
				this.dateField.setText(this.dateFormat.format(editE.getEnddate()));
				this.hourField.setValue(editE.getHour());
				this.minuteField.setValue(editE.getMinute());
			}
			this.checkTime.setSelected(editE.withTime());
		}
	}
}

