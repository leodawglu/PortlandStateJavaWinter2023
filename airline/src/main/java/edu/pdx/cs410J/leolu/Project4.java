/**
 * The main {@code Project3} class for the CS410J Airline Project
 * @author edited by Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.Scanner;

public class Project4 {

    boolean printFlight = false, readMe = false, tooMany=false;
    Airline anAirline;
    Flight aFlight;
    String txtFilePath = "", prettyFilePath = "", xmlFilePath="";
    File pFile = null;
    int argCount=0,idx=0, txtStatus = 0, prettyStatus = 0, xmlStatus = 0;
    //prettyStatus: 0 - not called, 1 - called but no file/std out, -1 - file specified, -2 - to std out
    //xmlStatus: 0 - not called, 1 - called but no filepath, -1 - file specified
    static final int min=8; // total number of airline and flight argument strings
    int min12hr=2; //arg index adjustment for 12hr format inputs: 0 for 24hr, 2 for 12hr format
    static final String readMeFile="README.txt";
    static final String usageFile="USAGE.txt";

    static final String welcome = "*------------------------------------------------------------------------*\n" +
            "*------------*Welcome to the Airline Flight Management System*-----------*\n" +
            "*------------------------------------------------------------------------*";
    static final String pretty =
            "*---------------------------------------------------------*\n" +
            "^~~~~~~~~~~~~~~~~~~~~~~Pretty Printing~~~~~~~~~~~~~~~~~~~~^\n" +
            "*---------------------------------------------------------*";
    static final String end = "*----------------------------*End of Program*----------------------------*";


    /**
     * This main method creates a new Project2 object
     * which is used for reading the user argument.
     * It checks if -README or -print are invoked
     * Ensures that airline and flight arguments are valid
     * before creating the airline and flight objects
     * @param args Command line argument
     * -README Overrides all inputs and prints README.txt on console
     * -input prints airline and flight information on console if valid
     * Input order:
     * airline -> Create Airline(name)
     * flightNumber (int)
     * src (3-letter alphabetical code of departure airport)
     * depart datetime
     * dest (3-letter alphabetical code of arrival airport)
     * arrive (3-letter alphabetical code of departure airport)
     * */
    public static void main(String[] args) throws IOException {
        subMainForTesting(args);
    }

    public static void subMainForTesting(String[] args)throws IOException{
        System.out.println(welcome);
        if(args.length==0){
            System.out.println("No command line arguments. Try -README for more details or review usage below.");
            printFile(usageFile);
            return;
        }
        Project4 ex = new Project4();
        /*
        try to read arguments from user
        if any error is found, exit program
        */
        if(!newArgumentReader(args,ex))return;
        if(ex.readMe) return; //If "-README" is called, exit

        /*
        try to create airline and flight
        if anything fails, exit program
        */
        if(!newCreateAirlineAndFlight(args,ex)) return;
        if(ex.printFlight) printFlight(ex);
        /*
        try to read txt file
        if anything fails, exit program
        */
        if(ex.txtStatus ==-1){
            if(!txtFile(ex.txtFilePath, ex.anAirline))return;
        }
        if(ex.xmlStatus ==-1){
            if(!createXmlFile(ex.xmlFilePath, ex.anAirline))return;
        }
        if(ex.prettyStatus ==-2 || ex.prettyStatus ==-1){
            if(!prettyPrint(ex.prettyStatus, ex))return;
        }
        System.out.println(end);
    }

    /**
     * Pretty prints the airline
     * Statuses: 0 - not invoked | 1 - invoked | -1 - file path provided | -2 - Print to Standard Out
     * @param curr Project3 instance
     * @param status pStatus
     * */
    public static boolean prettyPrint(int status, Project4 curr){
        Writer out = null;
        try{
            if(status == -1){
                File file = new File(curr.prettyFilePath);
                curr.pFile = file;
                if(file.length() == 0){
                    out = new FileWriter(file);
                }else{
                    out = new FileWriter(file,true);
                }
            }else if(status== -2){
                System.out.println(pretty);
                out = new PrintWriter(System.out);
            }else{
                System.err.println("Incorrect Status code!");
                return false;
            }
            PrettyPrinter dumper = new PrettyPrinter(out);
            dumper.dump(curr.anAirline);
        }catch(IOException e){
            System.err.println("Bad file path: "+curr.prettyFilePath);
            return false;
        }
        return true;
    }

    /**
     * Reads command line String[] args
     * Calls optionChecker(arg,curr) method when '-' character is present
     * Sets fileName if -textFile is called before
     * Prints error if too many arguments are entered
     * Counts number of args
     * */
    private static boolean newArgumentReader(String[] args, Project4 curr){
        for(String arg: args){
            if(arg.startsWith("-")){
                if(!optionChecker(arg,curr)) return false;
            }
            else if(curr.txtStatus == 1){// set fileName
                /* NO REQUIREMENT FOR THE SUFFIX/EXTENSION OF FILE NAME*/
                curr.txtFilePath = arg;
                curr.txtStatus = -1; //fileName is set
                curr.idx+=1;
            }
            else if(curr.xmlStatus == 1){// set fileName
                /* NO REQUIREMENT FOR THE SUFFIX/EXTENSION OF FILE NAME*/
                curr.xmlFilePath = arg;
                curr.xmlStatus = -1; //fileName is set
                curr.idx+=1;
            }
            else if(curr.prettyStatus == 1){
                /* NO REQUIREMENT FOR THE SUFFIX/EXTENSION OF FILE NAME*/
                curr.prettyFilePath = arg;
                curr.prettyStatus = -1;
                curr.idx+=1;
            }
            else if(curr.argCount>=(curr.min+curr.min12hr)){
                System.out.println(arg + " is an extraneous argument. \n" +
                        "Please review usage and try again.");
                curr.tooMany=true; // use this to print usage
                return false;
            }
            else curr.argCount++;
        }
        return true;
    }

    /**
     * @param path String xml file path provided in args
     * @param airline Airline created from user input args
     * If xml file has contents, it will be parsed first by XmlParser
     * and the file airline name will be checked against airline from input args
     * before both are merged together.
     * Airline is dumped into specified file path by XmlDumper
     * */
    public static boolean createXmlFile(String path, Airline airline){
        File file = new File(path);
        if(file.length()!=0){ //Not empty;
            XmlParser parser = new XmlParser(path);
            Airline xmlAirline = null;
            try{
                xmlAirline = parser.parse();
                compareAirlineNamesBetweenFileAndArgs(airline.getName(),xmlAirline.getName());
            }catch(ParserException e){
                System.err.println(e.getMessage());
                return false;
            }catch(IllegalArgumentException e){
                System.err.println(e.getMessage());
                System.err.println("Please make sure that airline name matches in both" +
                        " the file and the input string.");
                return false;
            }
            for(Flight fl: xmlAirline.getFlights()) airline.addFlight(fl);
        }
        XmlDumper dumper = new XmlDumper(path);
        dumper.dump(airline);
        return true;
    }

    /**
     * Ensures that both airlines have matching names
     * @throws IllegalArgumentException if input arg airline name does not match
     * */
    private static void compareAirlineNamesBetweenFileAndArgs(String name1, String name2){
        if(!name1.equalsIgnoreCase(name2)){
            throw new IllegalArgumentException("Input airline name \"" + name1 +
                    "\" does not match \"" + name2 +"\" airline in file.");
        }
    }
    /**
     * Uses Try-Catch to process file path provided by user
     * 3 Cases:
     *   Create New File
     *   Open Existing File that is blank
     *   Open Existing File with Airline and Contents
     * Case 1 and 2
     * If txt file is empty or does not exist, a new file is created,
     * then TextDumper is called to add Project2 Airline info.
     * Case 3
     * Read existing file using TextParser
     * Check if airline info in file is formatted correctly
     * Check if airline names of file and Project2 user arg matches
     *
     * @param path File path provided by user
     * @param airline Takes Project2 airline
     * @return true if all processes are successful
     * @return false should any process fail
     * */
    public static boolean txtFile(String path, Airline airline) throws IOException {
        File file = new File(path);
        try{
            if(file.length() == 0){ // If file is empty or does not exist
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                TextDumper dumper = new TextDumper(fw);
                dumper.dump(airline);
            }else{ // file has contents
                InputStream resource = new FileInputStream(path);
                TextParser parser = new TextParser(new InputStreamReader(resource));
                Airline fAirline = parser.parse();
                if(fAirline==null){
                    return false;
                }//error occurred during parsing, exit program
                // When airline names do not match, throw error and exit.
                compareAirlineNamesBetweenFileAndArgs(fAirline.getName(),airline.getName());
                //When file and String arg airline names match, add new flight to airline
                for(Flight fl: fAirline.getFlights())airline.addFlight(fl);
                FileWriter fw = new FileWriter(file);
                TextDumper dumper = new TextDumper(fw);
                dumper.dump(airline);
            }
        }catch(IOException e){
            System.out.println("An error occurred while create the file.");
            return false;
        }catch (ParserException e) {
            System.out.println("Runtime Error");
            return false;
            //throw new RuntimeException(e);
        }catch(IllegalArgumentException e){
            System.err.println(e.getMessage());
            System.err.println("Please make sure that airline name matches in both" +
                    " the file and the input string.");
            return false;
        }
        return true;
    }

    /**
     * Creates Airline with airlineName
     * Creates an empty Flight
     * Uses a try-catch to add flight args to Flight object
     * If any flight arg is incorrect, error is printed
     * If there is any missing arg, error of missing detail is printed
     * Prints airline and flight information when -print is called and all information is correct
     * @return true if airline and flight creation is successful
     * @return false if any process fails
     * @param args User String args entered
     * @param curr Project 2 Instance
     * */
    public static boolean newCreateAirlineAndFlight(String[] args, Project4 curr){
        try{
            String airlineName = args[curr.idx];
            curr.anAirline = new Airline(airlineName);

            String flightNumber = args[curr.idx+1];
            String dAirport = args[curr.idx+2];
            /*Departure Date & Time*/
            String dDate = args[curr.idx+3];
            String dTime = args[curr.idx+4];
            if(curr.min12hr==2)dTime += " "+args[curr.idx+5];
            String aAirport = args[curr.idx+5+curr.min12hr/2];
            /*Arrival Date & Time*/
            String aDate = args[curr.idx+6+curr.min12hr/2];
            String aTime = args[curr.idx+7+curr.min12hr/2];
            if(curr.min12hr==2)aTime += " "+args[curr.idx+9];

            Flight fl = new Flight(flightNumber,dAirport,dDate,dTime,aAirport,aDate,aTime);
            if(!curr.anAirline.getError().equals("")||!fl.getError().equals("")){
                System.err.println("*------------*Please review input errors above and try again*------------*");
                return false;
            }
            curr.anAirline.addFlight(fl);
            curr.aFlight = fl;
        }catch(ArrayIndexOutOfBoundsException e){
            missingArgsPrintln(curr.argCount,curr);
            return false;
        }
        return true;
    }

    /**
     * Uses the number of args entered to print out which airline and flight detail is missing
     * @param curr Project3 instance
     * @param count number of args entered
     * */
    public static void missingArgsPrintln(int count, Project4 curr){
        System.out.println("The following arguments are missing: ");
        if(count<1) System.out.println("Airline Name");
        if(count<2) System.out.println("Flight Number");
        if(count<3) System.out.println("Departure Airport Code");
        if(count<4) System.out.println("Departure Date");
        if(count<5) System.out.println("Departure Time");
        if(curr.min12hr!=0 && count<6) System.out.println("Departure Time AM/PM");
        if(count<6+curr.min12hr/2) System.out.println("Arrival Airport Code");
        if(count<7+curr.min12hr/2) System.out.println("Arrival Date");
        if(count<8+curr.min12hr/2) System.out.println("Arrival Time");
        if(curr.min12hr!=0 &&count<10) System.out.println("Arrival Time AM/PM");
        System.out.println("Please review usage and try again.");
    }

    /**
     * Used to check if option invoked with '-' character is valid
     * When invalid, false is returned to propagate return up into argumentReader
     * @param opt the candidate -option String invoked by user to be validated
     * @param curr Project 2 instance
     * */

    public static boolean optionChecker(String opt, Project4 curr){
        if(opt.equalsIgnoreCase("-README")){
            curr.readMe=true;
            printFile(readMeFile);
            return true;
        }
        else if(curr.txtStatus == 1){
            System.err.println("The -textFile option should be followed by a file name. \n" +
                    "Instead another option was invoked: " + opt +
                    "\nPlease review usage and try again.");
            return false;
        }
        else if(curr.xmlStatus == 1){
            System.err.println("The -xmlFile option should be followed by a xml file name. \n" +
                    "Instead another option was invoked: " + opt +
                    "\nPlease review usage and try again.");
            return false;
        }
        else if(curr.prettyStatus == 1 && opt.equals("-")){
            curr.prettyStatus = -2;
            curr.idx+=1;
            return true;
        }
        else if(curr.prettyStatus == 1){
            System.err.println("The -pretty option should be followed by a file name or '-' dash" +
                    " to indicate printing to standard out. \n" +
                    "Instead another option was invoked: " + opt +
                    "\nPlease review usage and try again.");
            return false;
        }
        else if(opt.contains("-print")){
            curr.printFlight = true;
            curr.idx+=1; //TO DO: REPLACE STRING ARG READ LOGIC
            return true;
        }
        else if(opt.equalsIgnoreCase("-textFile")){
            if(curr.txtStatus ==-1){//text file already set
                System.err.println("The -textFile option can only be called once. \n" +
                        "Please review usage and try again.");
                return false;
            }
            if(curr.xmlStatus != 0){
                System.err.println("The -xmlFile option was called before -textFile option. \n" +
                        "The -textFile and -xmlFile options cannot be used together.\n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.txtStatus = 1;
            curr.idx+=1;
            return true;
        }
        else if(opt.equalsIgnoreCase("-pretty")){
            if(curr.prettyStatus ==-1 || curr.prettyStatus ==-2){//text file already set
                System.err.println("The -pretty option can only be called once. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.prettyStatus = 1;
            curr.idx+=1;
            return true;
        }
        else if(opt.equalsIgnoreCase("-xmlFile")){
            if(curr.xmlStatus ==-1){//text file already set
                System.err.println("The -xmlFile option can only be called once. \n" +
                        "Please review usage and try again.");
                return false;
            }
            if(curr.txtStatus !=0){
                System.err.println("The -textFile option was called before -xmlFile option. \n" +
                        "The -textFile and -xmlFile options cannot be used together.\n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.xmlStatus = 1;
            curr.idx+=1;
            return true;
        }
        System.err.println(opt + " is not a valid option. \n" +
                "Please review usage for available options and try again");

        return false;
    }
    /**
     * Prints README.txt file to console when -README is present in command-line arguments
     * @param fileName takes any valid file path
     * */
    private static void printFile(String fileName){
        InputStream file = Project4.class.getResourceAsStream(fileName);
        Scanner scanner = new Scanner(file);
        String line;
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            System.out.println(line);
        }
        scanner.close();
    }

    /**
     * Prints flight information entered by user should -print option be called
     * @param curr Project2 Instance
     * */
    private static void printFlight(Project4 curr){
        Flight fl = curr.aFlight;
        System.out.println("*----------------------------*Flight Entered*----------------------------*");
        System.out.println(curr.anAirline.getName() + " " +fl.getNumber());
        System.out.println("Departing From: " + fl.getSource());
        System.out.println("Departure Date: " + fl.getDepDate());
        System.out.println("Departure Time: " + fl.getDepTime());
        System.out.println("Bound For     : " + fl.getDestination());
        System.out.println("Arrival Date  : " + fl.getArrDate());
        System.out.println("Arrival Time  : " + fl.getArrTime());
        System.out.println("*------------------------------------------------------------------------*");
    }

    /**
     * Changes time format from 12hr to 24hr
     * */
    public void toggleMin12hr(){
        if(min12hr==2)min12hr=0;
        else min12hr=2;
    }

    /**
     * @return pFile
     * */
    public File getpFile(){
        return pFile;
    }

}