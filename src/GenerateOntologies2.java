/*
 * Parses xml files for keywords, filters them, and then generates
 * a list of ontologies in the form of URIs for each xml file based
 * on those keywords. Writes keywords to a MySQL database.
 * Path names for the xml files’ location, filter.txt, and MySQL credentials,
 * are specific to Grace’s computer.
 */

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
 
public class GenerateOntologies2 {
	private static final String XML_FILE_DIRECTORY = "/Users/grace/F/Real Life/Internship 2014/XMLParse/cinergi_metadata";
	private static final String FILTER = "/Users/grace/F/Real Life/Internship 2014/workspace/XmlParse/filter.txt";
	private static ArrayList<String> filteredWords = new ArrayList<String>();
	private static ArrayList<String> exactWords = new ArrayList<String>();
	private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/metadata";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "";
	private static final String SQL_INSERT_KEYWORDS = "INSERT INTO Keywords2"
			+ "(file_name, keyword) " + "VALUES"
			+ "(?, ?)";
	private static final String SQL_INSERT_ONTOLOGIES = "INSERT INTO Ontologies2"
			+ "(file_name, ontology) " + "VALUES"
			+ "(?, ?)";
	private static boolean recursive = true; // true: process all subdirectories as well
	private static boolean table_is_ontologies = true; // true: generate table for ontologies; false: generate table for keywords
	
  public static void main(String argv[]) throws IOException {
	try {
		ArrayList<String> list1 = new ArrayList<String>(listFiles(XML_FILE_DIRECTORY));
		howManyFiles(list1);
		generateFilter(FILTER);
		insertRecordIntoDbUserTable(list1);
		confirmationDone();
	} catch (SQLException e) {
		e.printStackTrace();
	}
  }

  /*
   * Goes through XML_FILE_DIRECTORY and returns a list of all files
   * by their absolute paths. Goes through subdirectories as well if
   * boolean recursive is true.
   */
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
				
				if (recursive == true && listOfFiles[i].isDirectory())
				{
					xmlList.addAll(listFiles(listOfFiles[i].getAbsolutePath()));
				} //end recursion for directories
			} //end for
			
			return xmlList; // list of absolute paths of each xml file
  }
  
 /*
  * Checks how many files, returns string if no files found, else returns number of files
  */
  private static void howManyFiles(ArrayList<String> bigList) {
	  if (bigList.size() == 0) {
		  System.out.println("No files found!");
	  }
	  else {
		  System.out.println(bigList.size() + " files found");
	  }
  }

  /*
   * Generates filter to be used in the method filter()
   * based on contents of filter.txt.
   * Words tagged with # are used in partial filter,
   * and words tagged with $ are used in exact filter.
   */
  private static void generateFilter(String fileName) {
	  try(BufferedReader br = new BufferedReader(new FileReader(FILTER))) {
		  for(String line; (line = br.readLine()) != null; ) {
			  if (line.startsWith("# ")) {
				  filteredWords.add(line.substring(2,line.length())); // add to partial match
			  }
			  if (line.startsWith("$ ")) {
				  exactWords.add(line.substring(2,line.length())); // add to exact match
			  }
		  }
		  br.close();
		  
	  } catch (FileNotFoundException e) {
		  e.printStackTrace();
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
  }
  
  /*
   * Generates a ArrayList<String> containing the file name and all of its keywords.
   * Keywords are those marked with dc:subject, gmd:keyword, or
   * themekey in the xml file.
   */
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
		  // results.add(fXmlFile.getName()); // for just file name
		  results.add(sFileName.substring(67)); // for relative path
		  
		  // start keyword generation for dc:subject
		  NodeList nList = doc.getElementsByTagName("dc:subject");
		  for (int i = 0; i < nList.getLength(); i++) {
			  Node nNode = nList.item(i);
			  if (nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
				  Element eElement = (Element) nNode;
				  results.add(eElement.getTextContent().trim());
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
						  results.add(eElement22.getTextContent().trim());
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
 
  /*
   * Runs split(), filter(), and eliminateDuplicates() methods to curate keywords
   */
  private static ArrayList<String> curate (ArrayList<String> wordList) {
	  ArrayList<String> wordList2 = new ArrayList<String>(split(wordList));
	  ArrayList<String> wordList3 = new ArrayList<String>(filter(wordList2));
	  ArrayList<String> wordList4 = new ArrayList<String>(eliminateDuplicates(wordList3));
	  checkLength(wordList4);
	  return wordList4;
  }
  
  /*
   * For keywords that contain multiple keywords within,
   * splits them into separate keywords based on commas and greater-than signs.
   */
  private static ArrayList<String> split (ArrayList<String> wordList) {
	  ArrayList<String> newWordList = new ArrayList<String>();
	  for (int i = 0; i < wordList.size(); i++) {
		  if (wordList.get(i).contains(", ")) { // splits entries that are separated by commas
			  String str = wordList.get(i);
			  ArrayList<String> smallArray = new ArrayList<String>(Arrays.asList(str.split("\\s*,\\s*")));
			  newWordList.addAll(smallArray);
		  }
		  else if (wordList.get(i).contains(" > ")) { // splits entries that contain ">"
			  String str = wordList.get(i);
			  ArrayList<String> smallArray = new ArrayList<String>(Arrays.asList(str.split("\\s*>\\s*")));
			  newWordList.addAll(smallArray);
		  }
		  else {
			  newWordList.add(wordList.get(i));
		  }
	  }
	  return newWordList;
  }
  
  /*
   * Using the filter produced by generateFilter(), eliminates
   * keywords by partial and exact match.
   * Also filters out a few other particular formats.
   */
  private static ArrayList<String> filter (ArrayList<String> wordList) {

	  for (int i = wordList.size() - 1; i >= 0; i--) { // filter partial match
		  for (int j = 0; j < filteredWords.size(); j++) {
			  if(wordList.get(i).toLowerCase().contains(filteredWords.get(j))) {
				  wordList.remove(i);
				  break;
			  }
		  }
	  }

	  for (int i = wordList.size() - 1; i >= 0; i--) { // filter exact match
		  for (int j = 0; j < exactWords.size(); j++) {
			  if(wordList.get(i).toLowerCase().equals(exactWords.get(j))) {
				  wordList.remove(i);
				  break;
			  }
		  }
	  }

	  for (int i = wordList.size() - 1; i >= 1; i--) {
		  if (wordList.get(i).matches("[0-9]+")) {
			  wordList.remove(i);
		  }
	  } //filters out purely numeric keywords for all but file name

	  for (int i = wordList.size() - 1; i >= 1; i--) {
		  if (wordList.get(i).matches("[Q]{1}[0-9]{7}|[A-Z]{4}[0-9]{1}|[A-Z]{1}[0-9]{1}[A-Z]{1}[0-9]{3}[A-Z]{1}")) {
			  wordList.remove(i);
		  }
	  } // filters out formats Q1234567 and ABCD1 and T8N110W for all but file name
	  
	  return wordList;
  }
  
  /*
   * Checks length of each keyword for any abnormally long keywords
   * that may indicate the keyword is actually comprised of multiple
   * keywords in one entry. Returns file name and keyword if found.
   */
  private static void checkLength (ArrayList<String> wordList) {
	  for (int i = 0; i < wordList.size(); i++) {
			if (wordList.get(i).length() > 150) {
				System.out.println(wordList.get(0) + ", " + wordList.get(i) + ", " + wordList.get(i).length());
			}
		}
  }
  
  /*
   * Eliminates duplicates in the given ArrayList. Case-insensitive
   */
  private static ArrayList<String> eliminateDuplicates (ArrayList<String> wordList) {
	  ArrayList<String> newList= new ArrayList<String>();
	  newList.add(wordList.get(0)); // initialize newList with the file name; size = 1

	  boolean duplicate = false;
	  
	  for (int i = 1; i < wordList.size(); i++) { //compare next keyword
		  for (int j = 0; j < newList.size(); j++) { //to all previous keywords
			  if (newList.get(j).toLowerCase().equals(wordList.get(i).toLowerCase()) ) {
				  duplicate = true;
				  break; // exits inner loop, no need to check for further duplicates beyond this one because they've already been checked
			  }
		  }
		  if (duplicate == false) { //only add keyword to newlist if not a duplicate
			  newList.add(wordList.get(i));
		  }
		  duplicate = false; // reset after evaluating each old word
	  } //end for loop
	  
	  return newList;
  }
  
  /*
   * Returns list of ontology URIs based on keywords.
   * Gets ontology URIs from the tikki scigraph
   * First entry of list is still the file name.
   */
  private static ArrayList<String> listURIs (ArrayList<String> keywords) {
	  try {
		  ArrayList<String> URIs = new ArrayList<String>();
		  URIs.add(keywords.get(0)); //initialize URIs with file name

		  for (int i = 1; i < keywords.size(); i++) { //add URIs from each keyword

			  //call URL/KEYWORD (will be XML)
			  URL tikki = new URL("http://tikki.neuinfo.org:9000/scigraph/vocabulary/term/" + keywords.get(i));		  
			  HttpURLConnection connection =
					  (HttpURLConnection) tikki.openConnection();
			  connection.setRequestMethod("GET");
			  connection.setRequestProperty("Accept", "application/xml");
			  int responseCode = connection.getResponseCode();
			  
			  if (responseCode < 400) {

				  InputStream xml = connection.getInputStream();

				  //read XML result from URL
				  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				  DocumentBuilder db = dbf.newDocumentBuilder();
				  Document doc = db.parse(xml);

				  doc.getDocumentElement().normalize();

				  //search for "uri=" in "concept" nodes
				  NodeList listOfConcepts = doc.getElementsByTagName("concept");
				  for (int j = 0; j < listOfConcepts.getLength(); j++) { //add each URI
					  URIs.add(listOfConcepts.item(j).getAttributes().getNamedItem("uri").getNodeValue());
				  } //end for inner
			  } //end if for existing URLs

		  } //end for outer
		  return URIs;
	  } //end try

	  catch (Exception e) {
		  e.printStackTrace();
		  ArrayList<String> fileName = new ArrayList<String>();
		  fileName.add(keywords.get(0)); //initialize URIs with file name
		  return fileName;
	  }

  } //end listURIs
  
  /*
   * Produces list of keywords OR ontologies, and inserts them into
   * the MySQL database. Uses generateKeywords() and curate(), and if
   * the ontologies table is to be written, listURIs and eliminateDuplicates.
   */
  private static void insertRecordIntoDbUserTable(ArrayList<String> xmlList) throws SQLException {
	  	// generates rows in table, each with file name and one keyword/ontology
	  
		Connection dbConnection = null;
		PreparedStatement statement = null;

		try {
			dbConnection = getDBConnection();
			
			int batchSize = 0;
			
			for (int a = 0; a < xmlList.size(); a++) { // for each xml file
				ArrayList<String> appendList = new ArrayList<String>();
				
				ArrayList<String> keywords = new ArrayList<String>(generateKeywords(xmlList.get(a))); //gets the file's keywords
				ArrayList<String> finalKeywords = new ArrayList<String>(curate(keywords)); //filter keywords
				
				if (table_is_ontologies == true) { //append ontologies if true; also only produces ontologies if true
					ArrayList<String> URIs = new ArrayList<String>(listURIs(finalKeywords)); //generate file name + list of uris based on keywords
					ArrayList<String> finalURIs = new ArrayList<String>(eliminateDuplicates(URIs));
					appendList.addAll(finalURIs);
					statement = dbConnection.prepareStatement(SQL_INSERT_ONTOLOGIES, Statement.RETURN_GENERATED_KEYS);
				}
				else { //if table_is_ontologies is false, appends keywords
					appendList.addAll(finalKeywords);
					statement = dbConnection.prepareStatement(SQL_INSERT_KEYWORDS, Statement.RETURN_GENERATED_KEYS);
				}
				
				//start appending keywords/ontologies of that xml file to database
				if (appendList.size() == 1) { //if xml file has no keywords/ontologies and thus list only consists of the file name
					statement.setString(1, appendList.get(0));
					statement.setString(2, "");
					statement.addBatch();
					batchSize++;
				}

				for (int i = 1; i < appendList.size(); i++) { //for each keyword/uri following the file name
					statement.setString(1, appendList.get(0)); //set file name
					statement.setString(2, appendList.get(i)); //set keyword/uri
					statement.addBatch();
					batchSize++;
					if ((i) % 1000 == 0) {
						statement.executeBatch(); // Execute every 1000 items.
					}
				}
				if (batchSize >= 1000) {
					statement.executeBatch();
					batchSize = 0;
				}
				statement.executeBatch();
				//end appending keywords for each xml file loop
			} //end loop of generating table for all xml files and ontologies
			statement.executeBatch();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (statement != null) {
				statement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
	}

  /*
   * Gets database connection.
   */
  private static Connection getDBConnection() {

		Connection dbConnection = null;

		try {
			Class.forName(DB_DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}

		try {
			dbConnection = DriverManager.getConnection(
					DB_CONNECTION, DB_USER,DB_PASSWORD);
			return dbConnection;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return dbConnection;
	}
  
  /*
   * Prints a confirmation message once the program is done running.
   */
  private static void confirmationDone() {
	  System.out.println("The records have been inserted into the Keywords table.");
  }
} //end class