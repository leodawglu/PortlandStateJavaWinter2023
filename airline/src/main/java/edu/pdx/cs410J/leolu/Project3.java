/**
 * The main {@code Project3} class for the CS410J Airline Project
 * @author edited by Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.Scanner;

public class Project3 {

    boolean printFlight = false, readMe = false, tooMany=false;
    Airline anAirline;
    Flight aFlight;
    String filePath = "";
    int argCount=0,idx=0, fStatus = 0;
    static final int min=8; // total number of airline and flight argument strings
    int min12hr=2; //arg index adjustment for 12hr format inputs
    static final String readMeFile="README.txt";
    static final String usageFile="USAGE.txt";

    static final String welcome = "*------------------------------------------------------------------------*\n" +
            "*------------*Welcome to the Airline Flight Management System*-----------*\n" +
            "*------------------------------------------------------------------------*";
    static final String end = "*----------------------------*End of Program*----------------------------*";
    @VisibleForTesting
    static boolean isValidDateAndTime(String dateAndTime) {
        return true;
    }

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
        System.out.println(welcome);
        printFile(usageFile);
        if(args.length==0){
            System.out.println("No command line arguments. Try -README for more details");
            return;
        }
        Project3 ex = new Project3();
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
        /*
        try to read txt file
        if anything fails, exit program
        */
        if(ex.fStatus==-1){
            if(!txtFile(ex.filePath, ex.anAirline))return;
        }
        if(ex.printFlight) printFlight(ex);
        System.out.println(end);
    }

    /**
     * Reads command line String[] args
     * Calls optionChecker(arg,curr) method when '-' character is present
     * Sets fileName if -textFile is called before
     * Prints error if too many arguments are entered
     * Counts number of args
     * */
    private static boolean newArgumentReader(String[] args, Project3 curr){
        for(String arg: args){
            if(arg.startsWith("-")){
                if(!optionChecker(arg,curr)) return false;
            }
            else if(curr.fStatus == 1){// set fileName
                /* NO REQUIREMENT FOR THE SUFFIX/EXTENSION OF FILE NAME*/
                curr.filePath =arg;
                curr.fStatus = -1; //fileName is set
                curr.idx+=1;
            }
            //STOPPING POINT
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
                if(fAirline==null)return false; //error occurred during parsing, exit program
                // When airline names do not match, throw error and exit.
                if(!fAirline.getName().equalsIgnoreCase(airline.getName())){
                    throw new IllegalArgumentException("Input airline name \"" + airline.getName() +
                            "\" does not match \"" + fAirline.getName() +"\" airline in file.");
                }
                //When file and String arg airline names match, add new flight to airline
                for(Flight fl: airline.getFlights())fAirline.addFlight(fl);
                FileWriter fw = new FileWriter(file);
                TextDumper dumper = new TextDumper(fw);
                dumper.dump(fAirline);
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

    private static boolean newCreateAirlineAndFlight(String[] args, Project3 curr) throws IOException {
        String airlineName = args[curr.idx];
        //if(curr.file==-1)txtFile(curr.filePath); //If file path is set, explore it
        curr.anAirline = new Airline(airlineName);
        Flight fl = new Flight();//empty flight constructor
        try{
            if(curr.min12hr==2)fl.set12hrTrue();//set flight to 12hr format;
            String flightNumber = args[curr.idx+1];
            String dAirport = args[curr.idx+2];
            /*Departure Date & Time*/
            String dDate = args[curr.idx+3];
            String dTime = args[curr.idx+4];
            if(curr.min12hr==2) dTime += " "+args[curr.idx+5];

            String aAirport = args[curr.idx+5+ curr.min12hr/2];
            /*Arrival Date & Time*/
            String aDate = args[curr.idx+6+ curr.min12hr/2];
            String aTime = args[curr.idx+7+ curr.min12hr/2];
            if(curr.min12hr==2) aTime += " "+args[curr.idx+9];

            fl.setFlightNumber(flightNumber);
            fl.setAirportCode(dAirport,"Departure");
            fl.setAirportCode(aAirport,"Arrival");

            if(curr.min12hr==2){
                fl.setDateTime12HrFormat(dDate, dTime, "Departure");
                fl.setDateTime12HrFormat(aDate, aTime, "Arrival");
            }else{
                fl.setDate(dDate,"Departure");
                fl.setTime(dTime,"Departure");
                fl.setDate(aDate,"Arrival");
                fl.setTime(aTime,"Arrival");
                fl.formatDatetime("Departure");
                fl.formatDatetime("Arrival");
            }
            fl.setFlightDuration();


            if(!curr.anAirline.getError().equals("")||!fl.getError().equals("")){
                System.err.println("*------------*Please review input errors above and try again*------------*");
                return false;
            }
            curr.anAirline.addFlight(fl);
            curr.aFlight = fl;

        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("The following arguments are missing: ");
            if(curr.argCount<1) System.out.println("Airline Name");
            if(curr.argCount<2) System.out.println("Flight Number");
            if(curr.argCount<3) System.out.println("Departure Airport Code");
            if(curr.argCount<4) System.out.println("Departure Date");
            if(curr.argCount<5) System.out.println("Departure Time");
            if(curr.min12hr==2){
                if(curr.argCount<6) System.out.println("Departure Time AM/PM");
                if(curr.argCount<7) System.out.println("Arrival Airport Code");
                if(curr.argCount<8) System.out.println("Arrival Date");
                if(curr.argCount<9) System.out.println("Arrival Time");
                if(curr.argCount<10) System.out.println("Arrival Time AM/PM");

            }else{
                if(curr.argCount<6) System.out.println("Arrival Airport Code");
                if(curr.argCount<7) System.out.println("Arrival Date");
                if(curr.argCount<8) System.out.println("Arrival Time");
            }
            System.out.println("Please review usage and try again.");
            return false;
        }
        return true;
    }


    /**
     * Used to check if option invoked with '-' character is valid
     * When invalid, false is returned to propagate return up into argumentReader
     * @param opt the candidate -option String invoked by user to be validated
     * @param curr Project 2 instance
     * */
    private static boolean optionChecker(String opt, Project3 curr){
        if(opt.equalsIgnoreCase("-README")){
            curr.readMe=true;
            printFile(readMeFile);
            return true;
        }
        else if(curr.fStatus ==1){
            System.err.println("The -textFile option should be followed by a file name. \n" +
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
            if(curr.fStatus ==-1){//text file already set
                System.err.println("The -textFile option can only be called once. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.fStatus = 1;
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
        InputStream file = Project3.class.getResourceAsStream(fileName);
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
    private static void printFlight(Project3 curr){
        Flight fl = curr.aFlight;
        System.out.println("*--------------------------*Airline and Flights*-------------------------*");
        System.out.println(curr.anAirline.getName() + " " +fl.getNumber());
        System.out.println("Departing From: " + fl.getSource());
        System.out.println("Departure Date: " + fl.getDepDate());
        System.out.println("Departure Time: " + fl.getDepTime());
        System.out.println("Bound For     : " + fl.getDestination());
        System.out.println("Arrival Date  : " + fl.getArrDate());
        System.out.println("Arrival Time  : " + fl.getArrTime());
    }

}