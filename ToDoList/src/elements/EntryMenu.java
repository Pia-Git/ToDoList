package elements;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTable;

public class EntryMenu extends JMenu{

	private static final long serialVersionUID = 1L;
	JMenuItem add;
	JMenuItem edit;
	JMenuItem delete;
	JMenuItem deleteAll;
	ToDoList frame;
	
	public EntryMenu(String title, ToDoList frame){
		super(title);
		this.frame = frame;
		add = new JMenuItem("Add");
		/*neu.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_2, ActionEvent.ALT_MASK));*/
		edit = new JMenuItem("Edit");
		delete = new JMenuItem("Delete");
		deleteAll = new JMenuItem("Delete all");
		JTable table = frame.getTablelist();
		
		add.addActionListener(arg0 -> {
			EntryPopup popup = new EntryPopup(table, true);
			popup.showPopup();
		});
		edit.addActionListener(arg0 -> {
			if (table.getSelectedRow() > -1) {
				EntryPopup popup = new EntryPopup(table, false);
				popup.showPopup();
			}
		});
		delete.addActionListener(arg0 -> {
			frame.delete();
		});
		deleteAll.addActionListener(arg0 -> {
			frame.deleteAll();
		});
		
		add(add);
		add(edit);
		add(delete);
		add(deleteAll);
	}
}
