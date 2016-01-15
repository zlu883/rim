package nz.ac.auckland.rim;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.*;
import java.util.List;

public class XmlParser {

	private File _xmlFile;
	private Document _doc;
	
	public XmlParser(String fileName) {
		try {
			_xmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			_doc = dBuilder.parse(_xmlFile);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_doc.getDocumentElement().normalize();
	}
	
	public List<String> getElementsTextByTag(String tag) {
		NodeList nList = _doc.getElementsByTagName(tag);
		for (int i = 0; i < nList.getLength(); i++) {
			if (nList.item(i).getT)
		}
	}
	
	
}
