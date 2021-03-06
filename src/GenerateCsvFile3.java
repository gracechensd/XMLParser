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
 
public class GenerateCsvFile3 {
 
  public static void main(String argv[]) throws IOException {
	ArrayList<String> list1 = new ArrayList<String>(listFiles("/Users/grace/F/Real Life/Internship 2014/XMLParse/cinergi_metadata"));
	howManyFiles(list1);
	generateCsvFile(list1);
	confirmationDone();
  }
	 
  private static ArrayList<String> listFiles(String path){
	  
	// Directory path here
			String files;
			File folder = new File(path);
			File[] listOfFiles = folder.listFiles();
			
			ArrayList<String> xmlList = new ArrayList<String>();

			for (int i = 0; i < listOfFiles.length; i++)
			{
				if (listOfFiles[i].isFile())
				{
					files = listOfFiles[i].getName();
					if (files.endsWith(".xml"))
					{
						xmlList.add(listOfFiles[i].getAbsolutePath());
					}
				} //end adding files to xmlList
				
				if (listOfFiles[i].isDirectory())
				{
					xmlList.addAll(listFiles(listOfFiles[i].getAbsolutePath()));
				} //end recursion for directories
			} //end for
			
			return xmlList;
	  
  }
  
  private static void howManyFiles(ArrayList<String> bigList) {
	  if (bigList.size() == 0)
	  {
		  System.out.println("No files found!");
	  }
	  else
	  {
		  System.out.println(bigList.size() + " files found");
	  }
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
		  filter(keywords);
		  ArrayList<String> finalKeywords = new ArrayList<String>(eliminateDuplicates(keywords));
		  appendKeywords(finalKeywords);
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
		  
		  ArrayList<String> results = new ArrayList<String>();
		  results.add(fXmlFile.getName());
		  
		  
		  // start keyword generation for dc:subject
		  NodeList nList = doc.getElementsByTagName("dc:subject");
		  for (int i = 0; i < nList.getLength(); i++) {
			  Node nNode = nList.item(i);
			  if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element eElement = (Element) nNode;
				  results.add(eElement.getTextContent());
			  }
		  } //end dc:subject
		  
		  // start keyword generation for gmd:keyword
		  NodeList nList2 = doc.getElementsByTagName("gmd:keyword");
		  for (int i = 0; i < nList2.getLength(); i++) {
			  Node nNode2 = nList2.item(i);
			  if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
				  Element eElement2 = (Element) nNode2;
				  
				  // run through list of gco:CharacterString within each gmd:keyword
				  NodeList nList22 = eElement2.getElementsByTagName("gco:CharacterString");
				  for (int j = 0; j < nList22.getLength(); j++) {
					  Node nNode22 = nList22.item(j);
					  if (nNode22 != null && nNode22.getNodeType() == Node.ELEMENT_NODE) {
						  Element eElement22 = (Element) nNode22;
						  results.add(eElement22.getTextContent());
					  }
				  } //end gco:CharacterString loop
				  
			  }
		  } //end gmd:keyword
		  
		  
		// start keyword generation for theme
		  NodeList nList3 = doc.getElementsByTagName("themekey");
		  for (int i = 0; i < nList3.getLength(); i++) {
			  Node nNode3 = nList3.item(i);
			  if (nNode3 != null && nNode3.getNodeType() == Node.ELEMENT_NODE) {
				  Element eElement3 = (Element) nNode3;
				  results.add(eElement3.getTextContent().trim());
			  }
		  } //end theme
		  
		  
		  return results;
	  }  //end of try

	  catch (Exception e) {
		  e.printStackTrace();
		  return null;
	  }

  }
 
  private static ArrayList<String> filter (ArrayList<String> wordList)
  {
	ArrayList<String> filteredWords = new ArrayList<String>();
	filteredWords.add("usgin");
	filteredWords.add("document:text");
	filteredWords.add("document:image");
	filteredWords.add("Downloadable");
	  
	for (int i = wordList.size() - 1; i >= 0; i--) {
		for (int j = 0; j < filteredWords.size(); j++) {
			if(wordList.get(i).contains(filteredWords.get(j))) {
				wordList.remove(i);
				break;
			}
		}
	}
	
	ArrayList<String> exactWords = new ArrayList<String>();
	exactWords.add("data");
	exactWords.add("usa");
	exactWords.add("usgs");
	  
	for (int i = wordList.size() - 1; i >= 0; i--) {
		for (int j = 0; j < exactWords.size(); j++) {
			if(wordList.get(i).toLowerCase().equals(exactWords.get(j))) {
				wordList.remove(i);
				break;
			}
		}
	}
	
	for (int i = wordList.size() - 1; i >= 0; i--) {
		if (wordList.get(i).matches("[0-9]+") && wordList.get(i).length() > 2) {
			wordList.remove(i);
		}
	}
	
    return wordList;
  }
  
  private static ArrayList<String> eliminateDuplicates (ArrayList<String> wordList) {
	  ArrayList<String> newList= new ArrayList<String>();

	  for (int i = 0; i < wordList.size(); i++) {
	      if (!newList.contains(wordList.get(i).toLowerCase())) {
	          newList.add(wordList.get(i));
	      }
	  } //end for loop
	  return newList;
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
  
  private static void confirmationDone() {
	  System.out.println("The .csv file has been successfully generated.");
  }
} //end class