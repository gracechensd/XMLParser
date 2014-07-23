import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
 
public class ReadXMLFileSimple {
 
  public static void main(String argv[]) {
 
    try {
 
	File fXmlFile = new File("/Users/grace/F/Real Life/Internship 2014/XML Parse/test.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();
 
	System.out.println("File: " + fXmlFile.getName());
 
	NodeList nList = doc.getElementsByTagName("dc:subject");
 
	ArrayList<String> results = new ArrayList<String>();
	
	for (int i = 0; i < nList.getLength(); i++) {
		Node nNode = nList.item(i);
 
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			
			results.add(eElement.getTextContent());
 
			// System.out.println("Subject : " + eElement.getAttribute("creator"));
			// System.out.println("Subject : " + eElement.getElementsByTagName("dc:subject").item(0).getTextContent());
			// System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
			// System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
			// System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
		}
	}
	
    for(int j = 0; j < results.size(); j++) {   
        System.out.println(results.get(j));
    }
    
    } catch (Exception e) {
	e.printStackTrace();
	}

  }
 
}