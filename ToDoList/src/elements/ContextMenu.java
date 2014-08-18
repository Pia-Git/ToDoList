package elements;

import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ContextMenu extends JPopupMenu{

	private static final long serialVersionUID = 1L;
	JMenuItem add;
	JMenuItem edit;
	JMenuItem delete;
	ToDoList frame;
	int mouseX;
	int mouseY;

	public ContextMenu(ToDoList frame, MouseEvent e){
		this.frame = frame;
		mouseX = e.getX();
		mouseY = e.getY();
		add = new JMenuItem("Add");
		edit = new JMenuItem("Edit");
		delete = new JMenuItem("Delete");
		add.addActionListener(arg0 -> {
			close();
			frame.showEntryPopup(true);
		});
		edit.addActionListener(arg0 -> {
			close();
			frame.showEntryPopup(false);
		});
		delete.addActionListener(arg0 -> {
			close();
			frame.delete();
		});
		add(add);
		add(edit);
		add(delete);
	}
	
	public void show(){
		this.show(frame, mouseX+10, mouseY+10);
	}
	
	public void close(){
		setVisible(false);
	}
}
