package nz.ac.auckland.rim.data;

import nz.ac.auckland.rim.RIMException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;

import java.io.*;

/**
 * Class that provides utility methods for parsing XML files.
 * 
 * @author Jonny Lu
 *
 */
public class XmlParser {
	
	/**
	 * Attempts to read the given file as XML.
	 * @param fileName the path of the file
	 * @return a Document object representing the XML file
	 */
	public static Document parseToDocument(String fileName) {
		try {
			File xmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			return dBuilder.parse(xmlFile);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			e.printStackTrace();
			throw new RIMException("Error parsing file as XML: " + fileName);
		}
	}
	
}
