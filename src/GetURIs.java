
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchkeywords;

import au.com.bytecode.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Amar Haqqi
 */
public class GetURIs {

    public static void getoutput() throws IOException, SAXException, ParserConfigurationException {

        // need these to read from results which has keywords and then filename1, filename2, etc.
        BufferedReader reader;
        Path resultsfile;
        resultsfile = Paths.get("C:\\Users\\amarstudyhpw7\\Documents\\results_small.csv"); //use this for testing on own computer
        //resultsfile = Paths.get("C:\\Users\\amarstudyhpw7\\Documents\\Keywords.csv"); //use this on bigger computer
        reader = Files.newBufferedReader(resultsfile);

        String currLine;

        //array list that will hold all lines of Keywords file
        ArrayList<String> filecontents = new ArrayList<String>();

        //add all lines of Keywords into arraylist filecontents
        while ((currLine = reader.readLine()) != null) {
            filecontents.add(currLine);
        }

        // now all lines of Keywords.csv are in filecontents list
        // list of keywords where index of keyword in keywords = index of line in filecontents
        ArrayList<String> keywords = new ArrayList<String>();

        //go through filecontents and save all keywords into list (keywords = 0th elem of each line)
        for (String line : filecontents) {
            String[] lineValues = line.split(",");
            keywords.add(lineValues[0]); // add the keyword (0th elem) to keywords list
        }

        // loop through keywords list
        for (int i = 0; i < keywords.size(); i++) {
         
            String keyword = keywords.get(i);

            // list to hold uris
            ArrayList<String> uris = new ArrayList<String>();

            //call URL/KEYWORD (will be XML)
            URL tikki = new URL("http://tikki.neuinfo.org:9000/scigraph/vocabulary/term/" + keyword);

            //read XML result from URL
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(tikki.openStream());

            doc.getDocumentElement().normalize();

            //search for "uri=" in "concept" nodes
            NodeList listOfConcepts = doc.getElementsByTagName("concept");

            for (int j = 0; j < listOfConcepts.getLength(); j++) {

                Node firstConceptNode = listOfConcepts.item(i);
                if (firstConceptNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element firstElement = (Element) firstConceptNode;
                    String uri = firstElement.getAttribute("uri");

                    uris.add(uri);

                }                
                //convert List to Array
                String[] urisArray = new String[uris.size()];
                urisArray = uris.toArray(urisArray);

                // create a new text file for each keyword named after keyword
                //in the file write: filenames (get list of filenames from keyword in CSV file using same index i of keywords in list keywords = index of line in filecontents) and URIs

                try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\amarstudyhpw7\\Documents\\" + keyword + ".csv", true))) {
                    // write uris
                    writer.writeNext(urisArray);                    
                    // write filenames
                    String[] temp = new String[1];
                    temp[0] = filecontents.get(i);
                    writer.writeNext(temp);
                } // ends try

            }

        } // end getOutput()
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            try {
                try {
                    // TODO code application logic here
                    getoutput();
                } catch (ParserConfigurationException ex) {
                    Logger.getLogger(GetURIs.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SAXException ex) {
                Logger.getLogger(GetURIs.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(GetURIs.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}