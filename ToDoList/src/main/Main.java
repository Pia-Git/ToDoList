package main;

import java.util.Locale;

import elements.ToDoList;

/**
 * Die Klasse Main fuehrt die Applikation aus.
 * 
 * @author Pia
 * 
 */
public class Main {

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);
		ToDoList app = new ToDoList();
		app.start();
	}

}

