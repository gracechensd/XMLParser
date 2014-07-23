import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
 
public class GenerateCsvFile {
 
  public static void main(String argv[]) {
	generateCsvFile("/Users/grace/F/Real Life/Internship 2014/XML Parse/test.xml"); 
  }
	 
  private static void generateCsvFile(String sFileName) {
	try
	{
 
	File fXmlFile = new File(sFileName);
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();
 
	NodeList nList = doc.getElementsByTagName("dc:subject");
 
	ArrayList<String> results = new ArrayList<String>();
	results.add(fXmlFile.getName());
	
	for (int i = 0; i < nList.getLength(); i++) {
		Node nNode = nList.item(i);
 
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			
			results.add(eElement.getTextContent());
		}
	} //end of for loop
	
	FileWriter writer = new FileWriter(sFileName.substring(0, (sFileName.length()) - 3) + "csv");

	for (int j = 0; j < results.size() - 1; j++) {
		writer.append(results.get(j));
		writer.append(',');
	}
	writer.append(results.get( results.size() - 1 ));
		writer.append('\n');

    writer.flush();
    writer.close();
	
    for(int j = 0; j < results.size(); j++) {   
        System.out.println(results.get(j));
     }
    System.out.println(sFileName.substring(0, (sFileName.length()) - 3) + "csv was created.");
    
    }  //end of main method
	
	catch (Exception e) {
	e.printStackTrace();
	}

  }
 
}