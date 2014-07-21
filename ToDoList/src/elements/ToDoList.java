package elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * Die Klasse ToDoListe represaentiert die GUI, die die Liste als JTable
 * anzeigt. Per Button kann der Anwender neue Listeneintraege erstellen, bereits
 * vorhandene Eintraege bearbeiten, loeschen, abspeichern oder die gesamte Liste
 * als Website anzeigen lassen.
 * 
 * 
 * @author Pia
 * 
 */
public class ToDoList extends JFrame {

	JTable tablelist;
	ListTable lt;

	private static final long serialVersionUID = 1L;

	public ToDoList() {
		setTitle("ToDoListe");
		setSize(700, 400);
		centerWindow();
		setLayout(new BorderLayout());

		lt = new ListTable(this, null); // zuletzt bearbeitete ToDoListe wird
										// geöffnet
		tablelist = new JTable(lt);
		JScrollPane scroll = new JScrollPane(tablelist);

		// Button Panel
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(7, 1));
		JButton add = new JButton("+");
		JButton delete = new JButton("-");
		JButton edit = new JButton("edit");
		JButton deleteAll = new JButton("- (all)");
		JButton actual = new JButton("update");
		JButton save = new JButton("save");
		JButton website = new JButton("web");
		pane.add(add);
		pane.add(delete);
		pane.add(edit);
		pane.add(deleteAll);
		pane.add(actual);
		pane.add(save);
		pane.add(website);

		// auf Fenster setzen
		add(scroll, BorderLayout.CENTER);
		add(pane, BorderLayout.EAST);
		setVisible(true);

		// ActionListener für Buttons
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EintragPopup popup = new EintragPopup(tablelist, true);
			}
		});

		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tablelist.getSelectedRow() > -1) {
					lt.removeRow(tablelist.getSelectedRow());
				}
			}
		});

		edit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (tablelist.getSelectedRow() > -1) {
					EintragPopup popup = new EintragPopup(tablelist, false);
				}
			}
		});

		deleteAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!lt.isEmpty()) {
					int result = JOptionPane.showConfirmDialog(null,
							"Wirklich die komplette Liste löschen?",
							"Liste löschen", JOptionPane.YES_NO_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					if (result == 0) {
						lt.clearTable(); // tabelle leeren
						System.out.println("Liste gelöscht!");
					}
				}
			}
		});

		actual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});

		website.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			}
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	public void centerWindow() {
		// set window in the middle of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
	}
}

