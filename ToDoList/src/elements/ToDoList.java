package elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
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
import javax.swing.filechooser.FileNameExtensionFilter;

import cache.FileServer;

/**
 * Die Klasse ToDoListe represaentiert die GUI, die die Liste als JTable
 * anzeigt. Per Button kann der Anwender neue Listeneintraege erstellen, bereits
 * vorhandene Eintraege bearbeiten oder oeffnen, loeschen, abspeichern oder die gesamte Liste
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

		lt = new ListTable(this);
		tablelist = new JTable(lt);
		JScrollPane scroll = new JScrollPane(tablelist);

		// Button Panel
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(10, 1));
		JButton add = new JButton("add Entry");
		JButton delete = new JButton("delete Entry");
		JButton edit = new JButton("edit Entry");
		JButton deleteAll = new JButton("delete all");
		JButton actual = new JButton("update");
		JButton open = new JButton("open List");
		JButton saveAs = new JButton("save as");
		JButton save = new JButton("save");
		JButton newly = new JButton("new List");
		JButton website = new JButton("web");
		pane.add(add);
		pane.add(delete);
		pane.add(edit);
		pane.add(deleteAll);
		pane.add(actual);
		pane.add(open);
		pane.add(saveAs);
		pane.add(save);
		pane.add(newly);
		pane.add(website);

		// auf Fenster setzen
		add(scroll, BorderLayout.CENTER);
		add(pane, BorderLayout.EAST);

		// ActionListener for Buttons
		add.addActionListener(arg0 -> {
			EntryPopup popup = new EntryPopup(tablelist, true);
			popup.showPopup();
		});

		delete.addActionListener(arg0 -> {
			if (tablelist.getSelectedRow() > -1) {
				lt.removeRow(tablelist.getSelectedRow());
			}
		});

		edit.addActionListener(arg0 -> {
			if (tablelist.getSelectedRow() > -1) {
				EntryPopup popup = new EntryPopup(tablelist, false);
				popup.showPopup();
			}
		});

		deleteAll.addActionListener(arg0 -> {
			if (!lt.isEmpty()) {
				int result = JOptionPane.showConfirmDialog(null,
						"Do you really want to delete complete list?",
						"Delete all", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE);
				if (result == 0)
					lt.clearTable(); // tabelle leeren
			}
		});

		actual.addActionListener(arg0 -> {
			lt.fireTableDataChanged();
		});
		
		open.addActionListener(arg0 -> {
			open();
		});
		
		saveAs.addActionListener(arg0 -> {
			saveAs();
		});

		save.addActionListener(arg0 -> {
			saveAuto();
		});
		
		newly.addActionListener(arg0 -> {
			newFile();
		});

		website.addActionListener(arg0 -> {
			//todo
		});

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				saveAuto();
				ToDoList.this.dispose();
			}
		});
	}
	
	public void start(){
		setVisible(true);
		init();
	}
	
	public void init(){
		//initialize todolist at the beginning
		File cacheFile = new File("save/cache.bin");
		System.out.println(cacheFile.getAbsolutePath());
		if(cacheFile.exists()){
			fs.openFile(cacheFile);
    		setFileTitle(fs.getListName());
    		lt.fillTable(fs.getDocEntries());
		}
		else{
			WelcomePopup wp = new WelcomePopup(this);
			wp.showPopup();
			if(!isInitialized){
				setFileTitle("New List");
				lt.setModified(true);
				setFileModified();
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
				saveAuto();
				lt.clearTable(); 
			    setFileTitle(tf.getText());
			    fs.setCurrentFile(null);
			    isInitialized = true;
			}
		}
	}
	
	public void saveAs(){
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
	    			save(newF);
	    		}
	    	}
	    	else{
	    		save(newF);
	    	}
	    }
	}
	
	public void save(File f){
		fs.saveFile(lt.getTodolist(), f);
		setFileTitle(fs.getFileName(f));
		lt.setModified(false);
	}
	
	public void saveAuto(){
		if(lt.getModified()){
			File f = fs.getCurrentFile();
			if(f != null && f.exists()){
				int response = JOptionPane.showConfirmDialog(this,
				          "The file " + f.getName() + 
				          " is modified. Do you want to save the changes?",
				          "Modified file", JOptionPane.YES_NO_OPTION,
				          JOptionPane.WARNING_MESSAGE);
			  		if (response == JOptionPane.YES_OPTION){
			  			System.out.println("Automatic: Save this file: " + f.getAbsolutePath());
			  			save(f);
			  		}
	    	}
	    	else{
    			int response = JOptionPane.showConfirmDialog(this,
				          "The file doesn't exist. Do you want to save the new file?",
				          "New file", JOptionPane.YES_NO_OPTION,
				          JOptionPane.WARNING_MESSAGE);
			  		if (response == JOptionPane.YES_OPTION){
			  			System.out.println("Automatic: Save the new file!");
			  			saveAs();
			  		}
	    	}
		}
	}
	
	public void open(){
		JFileChooser fc = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "ToDoLists", "todo");
	    fc.setFileFilter(filter);
	    int returnVal = fc.showOpenDialog(self);
	    //wenn "oeffnen"
	    if(returnVal == 0){
	    	saveAuto();
	    	File f = fc.getSelectedFile();
	    	System.out.println("You chose to open this file: " + f.getName());
	    	String filename = fs.getFileName(f);
	    	String ext = fs.getFileExtension(f);
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
	
	public void setFileModified(){
		setTitle(fileTitle+"* - ToDoList");
	}

	public void centerWindow() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
	}
}

