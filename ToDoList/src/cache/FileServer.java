package cache;

import java.io.File;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import elements.Entry;

/**
 * Die Klasse FileServer speichert die Listeneinträge in einer XML-Datei ab.
 * 
 * @author Pia
 * 
 */
public class FileServer {

	private DocumentBuilder docBuilder;
	private ArrayList<Entry> docEntries;
	private File currentFile ;

	public FileServer() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public File getCurrentFile(){
		return currentFile;
	}
	
	public void saveFile (ArrayList<Entry> list, File file){
		System.out.println("save xml file method");
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("list");
		currentFile = file;
		doc.appendChild(rootElement);
		int counter = 0;
		for(Entry e : list){
			Element entry = doc.createElement("entry");
			rootElement.appendChild(entry);
			entry.setAttribute("id", String.valueOf(counter));
			//elements of entry
			Element todo = doc.createElement("todo");
			todo.appendChild(doc.createTextNode(e.getAufgabe()));
			entry.appendChild(todo);
			Element endtime = doc.createElement("endtime");
			String endzeit = e.getEndzeitpunkt();
			String restzeit = e.getRestzeit();
			if(e.getEndzeitpunkt() == ""){
				endzeit = "0";
				restzeit = "0";
			}
			endtime.appendChild(doc.createTextNode(endzeit));
			entry.appendChild(endtime);
			Element remaining = doc.createElement("remaining");
			remaining.appendChild(doc.createTextNode(restzeit));
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
	
	public void openFile (File file) {
		System.out.println("open xml file method");
		docEntries = new ArrayList<Entry>();
		currentFile = file;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("entry");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String todo = eElement.getElementsByTagName("todo").item(0).getTextContent();
					String end = eElement.getElementsByTagName("endtime").item(0).getTextContent();
					String done = eElement.getElementsByTagName("done").item(0).getTextContent();
					Entry newEntry = new Entry(todo, end, done);
					docEntries.add(newEntry);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Entry> getDocEntries(){
		return docEntries;
	}
}

