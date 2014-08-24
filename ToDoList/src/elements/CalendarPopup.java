package elements;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.toedter.calendar.JCalendar;
import com.toedter.components.JSpinField;

/**
 * Die Klasse CalendarPopup represaentiert einen Popup fuer die Auswahl des
 * Enddatums fuer die Aufgabe.
 * 
 * @author Pia
 * 
 */
public class CalendarPopup{

	JTextField field;
	JCalendar calendar;
	Point location;
	JPanel pane;
	Date newDate;
	Date currentDate;

	public CalendarPopup(final JTextField tf) {
		field = tf;
		calendar = new JCalendar();
		currentDate = new Date();
		pane = new JPanel();
		CalendarPopup self = this;
		
		JPanel dayPanel = new JPanel();
		dayPanel.setLayout(new GridLayout(1,3));
		JLabel lab = new JLabel("finished in ");
		JSpinField sf = new JSpinField(0,365);
		sf.setPreferredSize(new Dimension(49,26));
		
		// set old date from textfield
		String old = field.getText();
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date oldDate = format.parse(old);
			if(oldDate.getTime() >= currentDate.getTime()){
				newDate = oldDate;
				calendar.setDate(oldDate);
				sf.setValue(this.getDiffDays()+1);
			}else{
				newDate = currentDate;
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		sf.addPropertyChangeListener(new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						int days = sf.getValue();
						Calendar c = Calendar.getInstance();
						c.add(Calendar.DATE, days);
						calendar.setDate(c.getTime());
						newDate = c.getTime();
					}
				});
		
		JLabel lab2 = new JLabel(" day/s");
		dayPanel.add(lab);
		dayPanel.add(sf);
		dayPanel.add(lab2);

		calendar.addPropertyChangeListener("calendar",
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent e) {
						final Calendar c = (Calendar) e.getNewValue();
						Date newD = c.getTime(); // new Date
						Calendar gc = new GregorianCalendar();
						gc.setTime(currentDate);
						int year = gc.get(Calendar.YEAR);
						int month = gc.get(Calendar.MONTH);
						int day = gc.get(Calendar.DAY_OF_MONTH);
						gc.set(year, month, day, 0, 0, 0);
						gc.set(Calendar.MILLISECOND, 0);
						if (newD.getTime() < currentDate.getTime()) {
							//Date is not possible!
							JOptionPane.showMessageDialog(null, "Date is not possible. Please choose another one.", "Impossible Date",
				    		          JOptionPane.WARNING_MESSAGE);
							calendar.setDate(currentDate);
							sf.setValue(0);
						}
						else{
							newDate = newD;
							sf.setValue(self.getDiffDays());
						}
					}
				});
		
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.5;
		pane.add(lab,c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.5;
		pane.add(sf,c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0.5;
		pane.add(lab2,c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 2;
		pane.add(calendar,c);
	}
	
	public int getDiffDays(){
		int diffDays = (int)((newDate.getTime() - currentDate.getTime())/(1000*60*60*24));
		return diffDays;
	}
	
	public void showPopup(){
		int result = JOptionPane.showConfirmDialog(null, pane,
				"Please choose Date", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_OPTION) {
			if (newDate.getTime() >= currentDate.getTime()) {
				DateFormat format = new SimpleDateFormat(
						"dd/MM/yyyy");
				format.format(newDate);
				System.out.println(format.format(newDate));
				field.setText(format.format(newDate));
			}
		}
	}

}

