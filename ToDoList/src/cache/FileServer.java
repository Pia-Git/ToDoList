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
	private File currentFile;
	private String listName;

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
	
	public void setCurrentFile(File f){
		currentFile = f;
	}
	
	public String getFileName(File f){
		String filename = "";
		int i = f.getName().lastIndexOf('.');
    	if (i > 0) {
    	    filename = f.getName().substring(0, i);
    	}
    	return filename;
	}
	
	public String getFileExtension(File f){
		String ext = "";
    	int i = f.getName().lastIndexOf('.');
    	if (i > 0) {
    	    ext = f.getName().substring(i+1);
    	}
    	return ext;
	}
	
	public void saveFile (ArrayList<Entry> list, File file){
		System.out.println("save xml file method");
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("list");
		rootElement.setAttribute("path", file.getAbsolutePath());
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
		saveCache(doc);
		writeXML(doc, file);
		System.out.println("File saved!");
	}
	
	public void writeXML (Document doc, File file){
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// Output to console for testing
			//StreamResult result = new StreamResult(System.out);
			StreamResult result = new StreamResult(file);
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e1) {
			e1.printStackTrace();
		} catch (TransformerException e1) {
			e1.printStackTrace();
		}
	}
	
	public void saveCache(Document doc){
		File cache = new File("save/cache.bin");
		if(!cache.exists()){
			File folder = new File("save");
			folder.mkdirs();
		}
		writeXML(doc, cache);
		System.out.println("Cache: File saved!");
	}
	
	public void openFile (File file) {
		System.out.println("open xml file method");
		docEntries = new ArrayList<Entry>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			System.out.println("Node List Attribute: ");
			Element root = (Element) doc.getElementsByTagName("list").item(0);
			String filepath = root.getAttribute("path");
			currentFile = new File(filepath);
			listName = getFileName(currentFile);
			System.out.println(listName);
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
	
	public String getListName(){
		return listName;
	}
	
	public ArrayList<Entry> getDocEntries(){
		return docEntries;
	}
}

