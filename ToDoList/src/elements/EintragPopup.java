package elements;

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

/**
 * Die Klasse EintragPopup represaentiert einen Popup fuer die Erstellung eines
 * Listeneintrages in Form einer JOptionPane. Diese beinhaltet JTextfields fuer
 * die Aufgabenbeschreibung, Datum- und Zeitangabe.
 * 
 * @author Pia
 * 
 */
public class EintragPopup {

	JTextField stringField;
	JTextField dateField;
	JTextField timeField;
	JCheckBox checkTime;
	DateFormat dateFormat;
	JTable table;
	ListTable listTable;
	boolean isTime = true;
	boolean newEintrag = true;

	public EintragPopup(JTable tab, boolean neu) {

		newEintrag = neu;
		stringField = new JTextField(20);
		dateField = new JTextField(10);
		timeField = new JTextField(10);
		checkTime = new JCheckBox();
		dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		table = tab;
		listTable = (ListTable) tab.getModel();

		this.fillProperties();

		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(2, 1));
		JPanel task = new JPanel();
		task.add(new JLabel("ToDo:"));
		task.add(stringField);
		JPanel endTime = new JPanel();
		endTime.add(new JLabel("Enddatum:"));
		endTime.add(dateField);
		endTime.add(new JLabel("Uhrzeit:"));
		endTime.add(timeField);
		endTime.add(checkTime);
		myPanel.add(task);
		myPanel.add(endTime);

		// listeners

		dateField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("click on dateField!");
				CalendarPopup popup = new CalendarPopup(dateField);
			}

		});

		checkTime.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				JCheckBox cb = (JCheckBox) event.getSource();
				if (cb.isSelected()) {
					isTime = true;
					dateField.setEnabled(true);
					timeField.setEnabled(true);
					System.out.println("ui selected checkbox!");
				} else {
					isTime = false;
					dateField.setEnabled(false);
					timeField.setEnabled(false);
				}
			}
		});

		// wenn auf OK geklickt

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Listeneintrag erstellen", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			if (!stringField.getText().equals("")) {
				Date d = null;
				// falls Zeit angegeben
				if (isTime) {
					try {
						d = dateFormat.parse(dateField.getText());
						if (!timeField.getText().equals("")) {
							Calendar gc = new GregorianCalendar();
							gc.setTime(d);
							int year = gc.get(Calendar.YEAR);
							int month = gc.get(Calendar.MONTH);
							int day = gc.get(Calendar.DAY_OF_MONTH);
							// get hours and minutes from textfield
							String[] time = timeField.getText().split(":");
							int hour = Integer.parseInt(time[0]);
							int min = Integer.parseInt(time[1]);
							gc.set(year, month, day, hour, min, 0);
							// setz Datum mit Zeit
							d = gc.getTime();
						}
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
				}

				// Eintrag in Tabelle hinzufuegen falls neuer Eintrag
				if (this.newEintrag) {
					Eintrag e = new Eintrag(stringField.getText(), d);
					listTable.addEintrag(e);
				}
				// ansonsten Eintrag veraendern
				else {
					// todo
					Eintrag editE = listTable.getEintragAt(table
							.getSelectedRow());
					editE.setAufgabe(this.stringField.getText());
					editE.setEndzeitpunkt(d);
					listTable.editEintrag(table.getSelectedRow(), editE);
				}
			}
		}
	}

	public void fillProperties() {
		if (this.newEintrag) {
			// Default
			Date date = new Date();
			this.dateField.setText(this.dateFormat.format(date));
			this.timeField.setText("23:59");
			this.checkTime.setSelected(true);
		} else {
			Eintrag editE = listTable.getEintragAt(table.getSelectedRow());
			this.stringField.setText(editE.getAufgabe());
			this.dateField.setText(editE.getEndzeitpunkt());
			this.timeField.setText(editE.getTime());
			if (!editE.withTime()) {
				Date date = new Date();
				this.dateField.setText(this.dateFormat.format(date));
				this.timeField.setText("23:59");
				this.dateField.setEnabled(false);
				this.timeField.setEnabled(false);
			}
			this.checkTime.setSelected(editE.withTime());
		}
	}
}

