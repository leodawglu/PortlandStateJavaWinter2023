/**
 * The {code Converter} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;

import java.io.*;

/**
 * <code>Converter</code> class for Project 4.
 * Accepts a txt file path and destination xml file path
 * Txt file must be correctly formatted before converting to xml format
 * Uses <code>TextParser</code> to parse txt file
 * Uses <code>XmlDumper</code> to produce xml file
 */
public class Converter {

    static String err="";

    /**
     * main method which only accepts command line args: txtfilepath and xmlfilepath
     * Converts Valid Airline txt file to Valid Airline xml file
     * */
    public static void main(String[] args) throws ParserException {
        if(args==null || args.length < 2){
            err = "usage: java edu.pdx.cs410J.leolu.Converter txtFile xmlFile.";
            System.err.println(err);
            System.err.println("Txt file and xml file arguments are needed.\n" +
                    "Please try again.");
            return;
        }
        if(args.length>2){
            err = "usage: java edu.pdx.cs410J.leolu.Converter txtFile xmlFile.";
            System.err.println(err);
            System.err.println("Too many arguments. Txt file and xml file arguments are all that's needed.\n" +
                    "Please try again.");
            return;
        }
        String textFilePath = args[0];
        String xmlFilePath = args[1];
        Airline airline = getAirlineFromParsedTextFile(textFilePath);
        if(airline == null) return;
        XmlDumper dumper = new XmlDumper(xmlFilePath);
        dumper.dump(airline);
    }

    /**
     * @param path Accepts only a String txtFile path to be parsed by TextParser
     * @return  Airline object when TextParser successfully parses a valid airline txt file
     * */
    private static Airline getAirlineFromParsedTextFile(String path) throws ParserException {
        Airline fAirline = null;
        try{
            InputStream resource = new FileInputStream(path);
            TextParser parser = new TextParser(new InputStreamReader(resource));
            fAirline = parser.parse();
        } catch (FileNotFoundException e) {
            err="Provided text file path does not have an existing txt file. " +
                    "Please use a valid file path.";
            System.err.println(err);
        }
        return fAirline;
    }

}
