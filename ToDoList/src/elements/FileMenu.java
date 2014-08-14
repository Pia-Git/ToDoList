package elements;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class FileMenu extends JMenu{

	private static final long serialVersionUID = 1L;
	JMenuItem neu;
	JMenuItem open;
	JMenuItem save;
	JMenuItem saveAs;
	ToDoList frame;
	
	public FileMenu(String title, ToDoList frame){
		super(title);
		this.frame = frame;
		neu = new JMenuItem("New");
		open = new JMenuItem("Open...");
		save = new JMenuItem("Save");
		saveAs = new JMenuItem("Save as...");
		
		neu.addActionListener(arg0 -> {
			frame.newFile();
		});
		open.addActionListener(arg0 -> {
			frame.open();
		});
		save.addActionListener(arg0 -> {
			frame.saveAuto(true);
		});
		saveAs.addActionListener(arg0 -> {
			frame.saveAs();
		});
		
		add(neu);
		add(open);
		add(save);
		add(saveAs);
	}
}
