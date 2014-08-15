package main;

import java.awt.Color;
import java.util.Locale;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import elements.ToDoList;

/**
 * Die Klasse Main fuehrt die Applikation aus.
 * 
 * @author Pia
 * 
 */
public class Main {

	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            UIManager.put("Table.background", Color.WHITE);
		            UIManager.put("Table.alternateRowColor", Color.WHITE);
		            break;
		        }
		    }
		} catch (Exception e) {
		    // if Nimbus is not available
			try {
				UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				e1.printStackTrace();
			}
		}
		Locale.setDefault(Locale.ENGLISH);
		ToDoList app = new ToDoList();
		app.start(); 
	}

}

