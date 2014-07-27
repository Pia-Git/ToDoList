package cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import elements.Eintrag;

/**
 * Die Klasse FileServer speichert die Listeneinträge in einer XML-Datei ab.
 * 
 * @author Pia
 * 
 */
public class FileServer {

	/*FileOutputStream fop;
	File file;*/
	DocumentBuilder docBuilder;

	public FileServer() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		/*try {
			file = new File("c:/newfile.xml");
			fop = new FileOutputStream(file);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

	}
	
	public void saveFile (ArrayList<Eintrag> list, File file){
		System.out.println("save xml file method");
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("list");
		doc.appendChild(rootElement);
		int counter = 0;
		for(Eintrag e : list){
			//entry
			Element entry = doc.createElement("entry");
			rootElement.appendChild(entry);
			entry.setAttribute("id", String.valueOf(counter));
			//elements of entry
			Element todo = doc.createElement("todo");
			todo.appendChild(doc.createTextNode(e.getAufgabe()));
			entry.appendChild(todo);
			Element endtime = doc.createElement("endtime");
			endtime.appendChild(doc.createTextNode(e.getEndzeitpunkt()));
			entry.appendChild(endtime);
			Element remaining = doc.createElement("remaining");
			remaining.appendChild(doc.createTextNode(e.getRestzeit()));
			entry.appendChild(remaining);
			Element done = doc.createElement("done");
			done.appendChild(doc.createTextNode(String.valueOf(e.getErledigt())));
			entry.appendChild(done);
			counter++;
		}
		//write xml document
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
			System.out.println("File saved!");
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		} catch (TransformerException e1) {
			e1.printStackTrace();
		}
	}
	
	public void openFile () {
		System.out.println("open xml file method");
		
	}
}

