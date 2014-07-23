import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
 
public class GenerateCsvFileMultiple {
 
  public static void main(String argv[]) throws IOException {
	ArrayList<String> list1 = new ArrayList<String>(listFiles("/Users/grace/F/Real Life/Internship 2014/XMLParse/cinergi_metadata"));
	generateCsvFile(list1);
  }
	 
  private static ArrayList<String> listFiles(String path){
	  
	// Directory path here
			String files;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			ArrayList<String> xmlList = new ArrayList<String>();

			int j = 0;
			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].isFile())
				{
					files = listOfFiles[i].getName();
					if (files.endsWith(".xml"))
					{
						xmlList.add(listOfFiles[i].getAbsolutePath());
						System.out.println(listOfFiles[i].getName());
						j++;
					}
				}
			}
			if (j == 0)
			{
				System.out.println("No files found!");
			}
			else
			{
				System.out.println(j + " files found");
			}
			
			return xmlList;
	  
  }
  
  private static void generateCsvFile(ArrayList<String> xmlList) {
	try {
	  FileWriter writer = new FileWriter("/Users/grace/F/Real Life/Internship 2014/XMLParse/results.csv");
	  writer.append("File Name");
	  writer.append(',');
	  writer.append("Keywords");
	  writer.append('\n');

	  writer.flush();
	  writer.close(); //open and close new csv file
	  
	  for (int a = 0; a < xmlList.size(); a++) {
		  ArrayList<String> keywords = new ArrayList<String>(generateKeywords(xmlList.get(a)));
		  //generate list of keywords for a xml file
		  appendKeywords(keywords);
	  } //end appending keywords for each xml file loop
	  
	} //end try
	
	catch (Exception e) {
		e.printStackTrace();
		}
  }
  
  private static ArrayList<String> generateKeywords(String sFileName) {
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
	return results;
    }  //end of try
	
	catch (Exception e) {
	e.printStackTrace();
	return null;
	}

  }
 
  private static void appendKeywords(ArrayList<String> keywordList) {
	try {  
	  FileWriter writer = new FileWriter("/Users/grace/F/Real Life/Internship 2014/XMLParse/results.csv", true);
	  
	  if (keywordList.size() > 1) {
		  for (int b = 0; b < keywordList.size() - 1; b++) {
			  writer.append(keywordList.get(b));
			  writer.append(',');
		  }
		  writer.append(keywordList.get( keywordList.size() - 1 ));
		  writer.append('\n');
	  }
	  else if (keywordList.size() == 1) {
		  writer.append(keywordList.get(0));
		  writer.append(',');
		  writer.append("");
		  writer.append('\n');
	  }
	  
	  writer.flush();
	  writer.close();
	} //end try
	
	catch (Exception e) {
		e.printStackTrace();
		}
  }
  
} //end class