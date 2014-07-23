import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
 
public class ReadXMLFile {
 
  public static void main(String argv[]) {
 
    try {
 
	File fXmlFile = new File("/Users/grace/F/Real Life/Internship 2014/XML Parse/test.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();
 
	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
 
	NodeList nList = doc.getElementsByTagName("rdf:Description");
 
	// System.out.println("----------------------------");
 
	
	for (int i = 0; i < nList.getLength(); i++) {
 
		Node nNode = nList.item(i);
 
		System.out.println("\nCurrent Element :" + nNode.getNodeName());
 
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
			Element eElement = (Element) nNode;
 
			// System.out.println("Subject : " + eElement.getAttribute("creator"));
			 System.out.println("Subject : " + eElement.getElementsByTagName("dc:subject").item(0).getTextContent());
			// System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
			// System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
			// System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
 
		}
		
	}
	
    } catch (Exception e) {
	e.printStackTrace();
	}
    
  }
 
}