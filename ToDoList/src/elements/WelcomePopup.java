package elements;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * Die Klasse WelcomePopup represaentiert einen Start-Popup, der aufploppt, wenn man das Programm 
 * zum ersten Mal startet. Zur Auswahl steht das Erstellen einer neuen Liste oder das Oeffnen einer
 * bereits vorhandenen Liste.
 * 
 * @author Pia
 */
public class WelcomePopup extends JDialog {

	private static final long serialVersionUID = 1L;
	JButton newFile;
	JButton oldFile;
	ToDoList tdl;
	WelcomePopup self;
	
	public WelcomePopup(ToDoList td){
		super(td);
		self = this;
		tdl = td;
		setTitle("Welcome to ToDoList!");
		setModal(true);
		setSize(300, 100);
		centerWindow();
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(1, 2));
		JButton newFile = new JButton("New file");
		JButton oldFile = new JButton("Open existing file");
		pane.add(newFile);
		pane.add(oldFile);
		add(pane);
		
		newFile.addActionListener(arg0 -> {
			tdl.newFile();
			self.dispose();
		});

		oldFile.addActionListener(arg0 -> {
			tdl.open();
			self.dispose();
		});
	}
	
	public void showPopup(){
		setVisible(true);
	}
	
	public void centerWindow() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2
				- this.getSize().height / 2);
	}
}
