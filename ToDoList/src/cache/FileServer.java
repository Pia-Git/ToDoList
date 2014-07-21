package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Die Klasse FileServer speichert die Listeneinträge in einer XML-Datei ab.
 * 
 * @author Pia
 * 
 */
public class FileServer {

	FileOutputStream fop;
	File file;

	public FileServer() {

		try {
			file = new File("c:/newfile.xml");
			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

