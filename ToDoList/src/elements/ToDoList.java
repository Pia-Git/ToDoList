package elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
	Boolean isInitialized = false;
	String fileTitle;

	private static final long serialVersionUID = 1L;

	public ToDoList() {
		self = this;
		fs = new FileServer();
		setTitle("ToDoList");
		setSize(700, 400);
		centerWindow();
		setLayout(new BorderLayout());

		lt = new ListTable();
		tablelist = new JTable(lt);
		JScrollPane scroll = new JScrollPane(tablelist);

		// Button Panel
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(9, 1));
		JButton add = new JButton("+");
		JButton delete = new JButton("-");
		JButton edit = new JButton("edit");
		JButton deleteAll = new JButton("- (all)");
		JButton actual = new JButton("update");
		JButton open = new JButton("open");
		JButton save = new JButton("save");
		JButton newly = new JButton("new");
		JButton website = new JButton("web");
		pane.add(add);
		pane.add(delete);
		pane.add(edit);
		pane.add(deleteAll);
		pane.add(actual);
		pane.add(open);
		pane.add(save);
		pane.add(newly);
		pane.add(website);

		// auf Fenster setzen
		add(scroll, BorderLayout.CENTER);
		add(pane, BorderLayout.EAST);
		setVisible(true);
		
		init();

		// ActionListener for Buttons
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
					if (result == 0)
						lt.clearTable(); // tabelle leeren
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
				open();
			}
		});

		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				save();
			}
		});
		
		newly.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newFile();
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
				saveAuto();
				System.exit(0);
			}
		});
	}
	
	public void init(){
		//initialize todolist at the beginning
		File cacheFile = new File("save/cache.bin");
		System.out.println(cacheFile.getAbsolutePath());
		if(cacheFile.exists()){
			String filename = "";
			int i = cacheFile.getName().lastIndexOf('.');
	    	if (i > 0) {
	    	    filename = cacheFile.getName().substring(0, i);
	    	}
	    	//woher listenname????
			fs.openFile(cacheFile);
    		setFileTitle(filename);
    		lt.fillTable(fs.getDocEntries());
		}
		else{
			WelcomePopup wp = new WelcomePopup(this);
			if(!isInitialized){
				setFileTitle("New List");
				lt.setModified(true);
			}
		}
	}
	
	public void newFile(){
		JPanel myPanel = new JPanel();
		myPanel.setLayout(new GridLayout(1, 2));
		JLabel lab = new JLabel("Name: ");
		JTextField tf = new JTextField();
		myPanel.add(lab);
		myPanel.add(tf);
		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"New List", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION){
			if(!tf.getText().equals("")){
				//datei vorher abspeichern??? (vorige liste)
				lt.clearTable(); 
			    setFileTitle(tf.getText());
			    isInitialized = true;
			}
		}
	}
	
	public void save(){
		JFileChooser fc = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "ToDoLists", "todo");
	    fc.setFileFilter(filter);
		fc.setSelectedFile(new File(fileTitle));
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
	    			setFileTitle(f.getName());
	    			lt.setModified(false);
	    		}
	    	}
	    	else{
	    		fs.saveFile(lt.getTodolist(), newF);
	    		setFileTitle(f.getName());
	    		lt.setModified(false);
	    	}
	    }
	}
	
	public void saveAuto(){
		if(lt.getModified()){
			File f = fs.getCurrentFile();
			if(f != null && f.exists()){
				int response = JOptionPane.showConfirmDialog(this,
				          "The file " + f.getName() + 
				          " is modified. Do you want save changes?",
				          "Modified file", JOptionPane.YES_NO_OPTION,
				          JOptionPane.WARNING_MESSAGE);
			  		if (response == JOptionPane.YES_OPTION){
			  			System.out.println("Automatic: Save this file: " + f.getAbsolutePath());
			  			save();
			  		}
	    	}
	    	else{
    			int response = JOptionPane.showConfirmDialog(this,
				          "The file doesn't exist. Do you want save the new file?",
				          "New file", JOptionPane.YES_NO_OPTION,
				          JOptionPane.WARNING_MESSAGE);
			  		if (response == JOptionPane.YES_OPTION){
			  			System.out.println("Automatic: Save the new file!");
			  			save();
			  		}
	    	}
		}
	}
	
	/*public void save(Boolean auto){
		if(lt.getModified()){
			File f = fs.getCurrentFile();
			if(f != null && f.exists()){
				if(auto){
					int response = JOptionPane.showConfirmDialog(this,
					          "The file " + f.getName() + 
					          " is modified. Do you want save changes?",
					          "Modified file", JOptionPane.YES_NO_OPTION,
					          JOptionPane.WARNING_MESSAGE);
				  		if (response == JOptionPane.YES_OPTION){
				  			System.out.println("Automatic: Save this file: " + f.getAbsolutePath());
				  			fs.saveFile(lt.getTodolist(), f, false);
				  			lt.setModified(false);
				  		}
				}
				else{
					System.out.println("Save this file: " + f.getAbsolutePath());
		  			fs.saveFile(lt.getTodolist(), f, false);
		  			lt.setModified(false);
				}
	    	}
	    	else{
	    		if(auto){
	    			int response = JOptionPane.showConfirmDialog(this,
					          "The file doesn't exist. Do you want save the new file?",
					          "New file", JOptionPane.YES_NO_OPTION,
					          JOptionPane.WARNING_MESSAGE);
				  		if (response == JOptionPane.YES_OPTION){
				  			System.out.println("Automatic: Save the new file!");
				  			saveWithChooser();
				  		}
	    		}
	    		else{
	    			System.out.println("Save the new file!");
		  			saveWithChooser();
	    		}
	    	}
		}
	}*/
	
	public void open(){
		JFileChooser fc = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "ToDoLists", "todo");
	    fc.setFileFilter(filter);
	    int returnVal = fc.showOpenDialog(self);
	    //wenn "oeffnen"
	    if(returnVal == 0){
	    	File f = fc.getSelectedFile();
	    	System.out.println("You chose to open this file: " + f.getName());
	    	String ext = "";
	    	String filename = "";
	    	int i = f.getName().lastIndexOf('.');
	    	if (i > 0) {
	    	    ext = f.getName().substring(i+1);
	    	    filename = f.getName().substring(0, i);
	    	}
	    	System.out.println("File-Extension: "+ext);
	    	System.out.println("File-Name: "+filename);
	    	if(ext.equals("todo")){
	    		if(f.exists()){
		    		fs.openFile(f);
		    		setFileTitle(filename);
		    		lt.fillTable(fs.getDocEntries());
		    		isInitialized = true;
		    	}
	    	}
	    	else{
	    		JOptionPane.showMessageDialog(fc, "ToDoList can't open " + f.getName() + 
	    		          ". Only files with the .todo extension.", "No correct file",
	    		          JOptionPane.WARNING_MESSAGE);
	    	}
	    }
	}
	
	public void setFileTitle(String t){
		setTitle(t+" - ToDoList");
		fileTitle = t;
	}

	public void centerWindow() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
	}
}

