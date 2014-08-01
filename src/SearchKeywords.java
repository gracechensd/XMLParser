/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package searchkeywords;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahaqqi
 */
public class SearchKeywords {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int i = 1;
        try {
            // TODO code application logic here
            makeFileOfKeywords();
        } catch (IOException ex) {
            int ii = 0;
        }
    }

    public static void makeFileOfKeywords() throws IOException {

        String[] emptyArray = new String[0];
        try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\amarstudyhpw7\\Documents\\Keywords.csv"), '\t')) {
            // feed in your array (or convert your data to an array)
            writer.writeNext(emptyArray);
        }
        BufferedReader reader;
        Path resultsfile;
        resultsfile = Paths.get("C:\\Users\\amarstudyhpw7\\Documents\\results_small.csv");
        reader = Files.newBufferedReader(resultsfile);

        String currLine;

        ArrayList<String> filecontents = new ArrayList<String>();

        while ((currLine = reader.readLine()) != null) {
            filecontents.add(currLine);
        }

            // all lines of results.csv in filecontents ArrayList<String>
        ArrayList<String> inputKeywords = new ArrayList<String>();

        for (String line : filecontents) {
            String[] lineValues = line.split(",");

            for (int i = 1; i < lineValues.length; i++) {
            // lineValues[] is an array of values from the line

                // get ith keyWordFound
                String keyWordFound = lineValues[i];

                // make array for keyWordFound
                ArrayList<String> filesForKeyword = new ArrayList<String>();
                if (inputKeywords.contains(keyWordFound)) {
                    continue;
                } else {
                    inputKeywords.add(keyWordFound);
                    filesForKeyword.add(keyWordFound);//note the keyword as the first element
                }
                // search all file lines for keyWordFound
                for (String line2 : filecontents) {
                    if (line2.contains(keyWordFound)) {
                        String[] line2Values = line2.split(",");
                        filesForKeyword.add(line2Values[0]); // read the file name for the keyword for each  line
                    }
                }

                //convert List to Array
                String[] filesForKeywordArray = new String[filesForKeyword.size()];
                filesForKeywordArray = filesForKeyword.toArray(filesForKeywordArray);

                // feed in your array (or convert your data to an array)
                try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\amarstudyhpw7\\Documents\\Keywords.csv", true))) {
                    // feed in your array (or convert your data to an array)
                    writer.writeNext(filesForKeywordArray);
                } // ends try
            } //end for loop

        }

        /*
         for (int i = 1; i < nextLine.length; i++) {
         // nextLine[] is an array of values from the line
            
         // get ith keyWordFound
         String keyWordFound = nextLine[i];
         //CSVReader reader2 = new CSVReader(new FileReader("C:\\Users\\lzaki\\Documents\\NetBeansProjects\\SearchKeywords\\src\\searchkeyWordFounds\\results.csv"));
         Charset charset = Charset.forName("US-ASCII");
            
         reader.close();
            
         BufferedReader reader2;
         Path resultsfile;
         resultsfile = Paths.get("C:\\Users\\lzaki\\Documents\\NetBeansProjects\\SearchKeywords\\src\\searchkeyWordFounds\\results.csv");
         reader2 = Files.newBufferedReader(resultsfile, charset);
            
         // make array for keyWordFound
         ArrayList<String> inputKeywords = new ArrayList<String>();
         inputKeywords.add(keyWordFound);
            
         // search all lines for keyWordFound
         //String [] lineNext;
         //while ((lineNext = reader2.readLine()) != null) {
         String lineNext = null;
         while ((lineNext = reader2.readLine()) != null) {
         // if line contains keyWordFound, then add that file name to files arraylist
         if(Arrays.asList(lineNext).contains(keyWordFound)) {
         inputKeywords.add((lineNext.split(" "))[0]);
         }
         }
         String[] lineArray = new String[inputKeywords.size()];
         lineArray = inputKeywords.toArray(lineArray);
         // feed in your array (or convert your data to an array)
         try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Users\\lzaki\\Documents\\NetBeansProjects\\SearchKeywords\\src\\searchkeyWordFounds\\Keywords.csv", true), '\t')) {
         // feed in your array (or convert your data to an array)
         writer.writeNext(lineArray);
         } // ends try
         } //end for loop
            
            
            
         }   catch (IOException ex) {
         Logger.getLogger(GetKeywordList.class.getName()).log(Level.SEVERE, null, ex);
         }
         */
    }

}