package elements;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JCalendar;

/**
 * Die Klasse CalendarPopup represaentiert einen Popup fuer die Auswahl des
 * Enddatums fuer die Aufgabe.
 * 
 * @author Pia
 * 
 */
public class CalendarPopup extends JDialog {

	private static final long serialVersionUID = 1L;
	JTextField field;
	JCalendar calendar;
	Point location;

	public CalendarPopup(final JTextField tf) {
		field = tf;
		calendar = new JCalendar();
		setTitle("Bitte Datum auswählen");
		setModal(true);
		setSize(350, 350);
		centerWindow();

		// set old date from textfield
		String old = field.getText();
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
		try {
			Date oldDate = format.parse(old);
			calendar.setDate(oldDate);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// JDayChooser jdc = new JDayChooser();
		calendar.addPropertyChangeListener("calendar",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						final Calendar c = (Calendar) e.getNewValue();
						Date newDate = c.getTime(); // new Date
						Date currentDate = new Date(); // current Date
						Calendar gc = new GregorianCalendar();
						gc.setTime(currentDate);
						int year = gc.get(Calendar.YEAR);
						int month = gc.get(Calendar.MONTH);
						int day = gc.get(Calendar.DAY_OF_MONTH);
						gc.set(year, month, day, 0, 0, 0);
						gc.set(Calendar.MILLISECOND, 0);
						currentDate = gc.getTime();
						if (newDate.getTime() >= currentDate.getTime()) {
							DateFormat format = new SimpleDateFormat(
									"dd.MM.yyyy");
							format.format(newDate);
							System.out.println(format.format(c.getTime()));
							field.setText(format.format(c.getTime()));
							dispose();
						}
					}
				});
		JPanel pane = new JPanel();
		pane.add(calendar);
		setContentPane(calendar);
		setVisible(true);
	}

	public void centerWindow() {
		// set window in the middle of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
	}
}

