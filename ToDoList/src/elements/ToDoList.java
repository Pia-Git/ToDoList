package elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;

import cache.FileServer;

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
	File currentFile;
	ToDoList self;
	FileServer fs;

	private static final long serialVersionUID = 1L;

	public ToDoList() {
		self = this;
		fs = new FileServer();
		setTitle("ToDoList");
		setSize(700, 400);
		centerWindow();
		setLayout(new BorderLayout());

		lt = new ListTable(this, null); // zuletzt bearbeitete ToDoListe wird geoeffnet
		tablelist = new JTable(lt);
		JScrollPane scroll = new JScrollPane(tablelist);

		// Button Panel
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(8, 1));
		JButton add = new JButton("+");
		JButton delete = new JButton("-");
		JButton edit = new JButton("edit");
		JButton deleteAll = new JButton("- (all)");
		JButton actual = new JButton("update");
		JButton open = new JButton("open");
		JButton save = new JButton("save");
		JButton website = new JButton("web");
		pane.add(add);
		pane.add(delete);
		pane.add(edit);
		pane.add(deleteAll);
		pane.add(actual);
		pane.add(open);
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
				EntryPopup popup = new EntryPopup(tablelist, true);
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
					EntryPopup popup = new EntryPopup(tablelist, false);
				}
			}
		});

		deleteAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!lt.isEmpty()) {
					int result = JOptionPane.showConfirmDialog(null,
							"Do you want really delete complete list?",
							"Delete all", JOptionPane.YES_NO_OPTION,
							JOptionPane.PLAIN_MESSAGE);
					if (result == 0) {
						lt.clearTable(); // tabelle leeren
					}
				}
			}
		});

		actual.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				lt.fireTableDataChanged();
			}
		});
		
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
			    FileNameExtensionFilter filter = new FileNameExtensionFilter(
			            "ToDoLists", "todo");
			    fc.setFileFilter(filter);
			    int returnVal = fc.showOpenDialog(self);
			    //wenn "oeffnen"
			    if(returnVal == 0){
			    	File f = fc.getSelectedFile();
			    	System.out.println("You chose to open this file: " + f.getName());
			    	if(f.exists()){
			    		fs.openFile(f);
			    		lt.fillTable(fs.getDocEntries());
			    	}
			    }
			}
		});

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!lt.isEmpty()) {
					JFileChooser fc = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter(
				            "ToDoLists", "todo");
				    fc.setFileFilter(filter);
					fc.setSelectedFile(new File("New List"));
				    int returnVal = fc.showSaveDialog(self);
				    //wenn "speichern"
				    if(returnVal == 0){
				    	File f = fc.getSelectedFile();
				    	System.out.println("You chose to save this file: " + f.getAbsolutePath());
				    	File newF = new File(f.getAbsoluteFile()+".todo");
				    	//overwrite?
				    	if(newF.exists()){
				    		int response = JOptionPane.showConfirmDialog(fc,
				    		          "The file " + f.getName() + 
				    		          " already exists. Do you want overwrite the existing file?",
				    		          "Overwrite file", JOptionPane.YES_NO_OPTION,
				    		          JOptionPane.WARNING_MESSAGE);
				    		if (response == JOptionPane.YES_OPTION){
				    			fs.saveFile(lt.getTodolist(), newF);
				    		}
				    	}
				    	else{
				    		fs.saveFile(lt.getTodolist(), newF);
				    	}
				    }
				}
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

