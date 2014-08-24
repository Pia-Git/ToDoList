package elements;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableRowSorter;

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
	JMenuBar bar;
	FileMenu fileMenu;
	EntryMenu editMenu;
	Timer timer;
	int selectedRow = -1;

	private static final long serialVersionUID = 1L;

	public ToDoList() {
		self = this;
		fs = new FileServer();
		setTitle("ToDoList");
		setSize(700, 400);
		centerWindow();
		setLayout(new BorderLayout());
		ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("image/logo.png"));
		setIconImage(icon.getImage());

		lt = new ListTable(this);
		tablelist = new JTable(lt);
		tablelist.setRowSorter(new TableRowSorter<ListTable>(lt));
		tablelist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tablelist.getColumnModel().getColumn(2).setCellRenderer(new CellRenderer());
		tablelist.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				//if right-click
				if(e.getButton() == MouseEvent.BUTTON3){
					//select row
					Point p = e.getPoint();
					int rowNumber = tablelist.rowAtPoint(p);
					tablelist.getSelectionModel().setSelectionInterval(rowNumber, rowNumber);
					ContextMenu contextMenu = new ContextMenu(self,e);
					contextMenu.show();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
		bar = new JMenuBar();
		fileMenu = new FileMenu("File", this);
		editMenu = new EntryMenu("Edit", this);
		bar.add(fileMenu);
		bar.add(editMenu);
		setJMenuBar(bar);
		JScrollPane scroll = new JScrollPane(tablelist);
		add(scroll, BorderLayout.CENTER);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				close();
			}
		});
	}
	
	public void start(){
		setVisible(true);
		init();
		startTimer();
	}
	
	public void close(){
		saveAuto(false);
		ToDoList.this.dispose();
	}
	
	public void startTimer(){
		timer = new Timer(/*60000*/ 1000, arg0 -> {
			selectedRow = tablelist.getSelectedRow();
			lt.fireTableDataChanged();
			if(!(selectedRow == -1)){
				tablelist.setRowSelectionInterval(selectedRow, selectedRow);
			}
		});
		timer.start();
	}
	
	public void showEntryPopup(Boolean add){
		if(add){
			EntryPopup popup = new EntryPopup(tablelist, true);
			popup.showPopup();
		}else{
			if (tablelist.getSelectedRow() > -1) {
				EntryPopup popup = new EntryPopup(tablelist, false);
				popup.showPopup();
			}
		}
	}
	
	public void delete(){
		if (tablelist.getSelectedRow() > -1) {
			int result = JOptionPane.showConfirmDialog(null,
					"Do you really want to delete the selected entry?",
					"Delete entry", JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (result == 0){
				int selectRow = tablelist.convertRowIndexToView(tablelist.getSelectedRow());
				lt.removeRow(selectRow);
			}
		}
	}
	
	public void deleteAll(){
		if (!lt.isEmpty()) {
			int result = JOptionPane.showConfirmDialog(null,
					"Do you really want to delete complete list?",
					"Delete all", JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE);
			if (result == 0)
				lt.clearTable(); // tabelle leeren
		}
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
				saveAuto(false);
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
	
	public void saveAuto(Boolean yes){
		if(lt.getModified()){
			File f = fs.getCurrentFile();
			if(f != null && f.exists()){
				if(yes){
					System.out.println("Save this file: " + f.getAbsolutePath());
		  			save(f);
				}else{
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
	
	public void openAbout(){
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(2,1));
		JLabel version = new JLabel("ToDoList v1.0");
		JLabel author = new JLabel("Author: Pia Sinzig");
		pane.add(version);
		pane.add(author);
		JOptionPane.showMessageDialog(this, pane, "About ToDoList",
		          JOptionPane.PLAIN_MESSAGE);
	}
	
	public void open(){
		JFileChooser fc = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	            "ToDoLists", "todo");
	    fc.setFileFilter(filter);
	    int returnVal = fc.showOpenDialog(self);
	    //wenn "oeffnen"
	    if(returnVal == 0){
	    	saveAuto(false);
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

