/**
 * The main {@code Project2} class for the CS410J Airline Project
 * @author edited by Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import com.google.common.annotations.VisibleForTesting;
import java.io.InputStream;
import java.util.Scanner;

public class Project2 {

    boolean printFlight = false, readMe = false, tooMany=false;
    Airline anAirline;
    String fileName = "";
    int argCount=0,idx=0, file = 0;
    static final int min=8; // total number of airline and flight argument strings
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
    public static void main(String[] args) {
        System.out.println(welcome);
        printFile(usageFile);
        if(args.length==0){
            System.out.println("No command line arguments. Try -README for more details");
            return;
        }
        Project2 ex = new Project2();
        if(!newArgumentReader(args,ex))return;//If error found in args, exit
        if(ex.readMe) return; //If "-README" is called, exit
        newCreateAirlineAndFlight(args,ex); //try to create airline and flight
        System.out.println(end);
    }

    /**
     * Reads command line String[] args
     * Calls optionChecker(arg,curr) method when '-' character is present
     * Sets fileName if -textFile is called before
     * Prints error if too many arguments are entered
     * Counts number of args
     * */
    private static boolean newArgumentReader(String[] args, Project2 curr){
        for(String arg: args){
            if(arg.contains("-")){
                if(!optionChecker(arg,curr)) return false;
            }
            else if(curr.file == 1){// set fileName
                /* NO REQUIREMENT FOR THE SUFFIX/EXTENSION OF FILE NAME*/
                curr.fileName=arg;
                curr.file = -1; //fileName is set
                curr.idx+=1;
            }
            else if(curr.argCount>=curr.min){
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
     * Creates Airline with airlineName
     * Creates an empty Flight
     * Uses a try-catch to add flight args to Flight object
     * If any flight arg is incorrect, error is printed
     * If there is any missing arg, error of missing detail is printed
     * Prints airline and flight information when -print is called and all information is correct
     * */

    private static void newCreateAirlineAndFlight(String[] args, Project2 curr){
        Flight fl = new Flight();//empty flight constructor
        try{
            String airlineName = args[curr.idx];
            curr.anAirline = new Airline(airlineName);
            String flightNumber = args[curr.idx+1];
            fl.setFlightNumber(flightNumber);
            String dAirport = args[curr.idx+2];
            fl.setAirportCode(dAirport,"Departure");
            String dDate = args[curr.idx+3];
            fl.setDate(dDate,"Departure");
            String dTime = args[curr.idx+4];
            fl.setTime(dTime,"Departure");
            String aAirport = args[curr.idx+5];
            fl.setAirportCode(aAirport,"Arrival");
            String aDate = args[curr.idx+6];
            fl.setDate(aDate,"Arrival");
            String aTime = args[curr.idx+7];
            fl.setTime(aTime,"Arrival");

            if(!curr.anAirline.getError().equals("")||!fl.getError().equals("")){
                System.err.println("*------------*Please review input errors above and try again*------------*");
                return;
            }
            curr.anAirline.addFlight(fl);
            if(curr.printFlight){ //Prints Airline and Flight information when -print is called
                System.out.println("*--------------------------*Airline and Flights*-------------------------*");
                System.out.println(airlineName + " " +flightNumber);
                System.out.println("Departing From: " + dAirport);
                System.out.println("Departure Date: " + dDate);
                System.out.println("Departure Time: " + dTime);
                System.out.println("Bound For     : " + aAirport);
                System.out.println("Arrival Date  : " + aDate);
                System.out.println("Arrival Time  : " + aTime);
            }

        }catch(ArrayIndexOutOfBoundsException e){
            System.out.println("The following arguments are missing: ");
            if(curr.argCount<1) System.out.println("Airline Name");
            if(curr.argCount<2) System.out.println("Flight Number");
            if(curr.argCount<3) System.out.println("Departure Airport Code");
            if(curr.argCount<4) System.out.println("Departure Date");
            if(curr.argCount<5) System.out.println("Departure Time");
            if(curr.argCount<6) System.out.println("Arrival Airport Code");
            if(curr.argCount<7) System.out.println("Arrival Date");
            if(curr.argCount<8) System.out.println("Arrival Time");
            System.out.println("Please review usage and try again.");
        }
    }


    /**
     * Used to check if option invoked with '-' character is valid
     * When invalid, false is returned to propagate return up into argumentReader
     * */
    private static boolean optionChecker(String opt, Project2 curr){
        if(opt.equalsIgnoreCase("-README")){
            curr.readMe=true;
            printFile(readMeFile);
            return true;
        }
        else if(curr.file==1){
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
            if(curr.file==-1){//text file already set
                System.err.println("The -textFile option can only be called once. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.file = 1;
            curr.idx+=1;
            return true;
        }
        System.err.println(opt + " is not a valid option. \n" +
                "Please review usage for available options and try again");

        return false;
    }

    /**
     * Prints README.txt file to console when -README is present in command-line arguments
     * */
    private static void printFile(String fileName){
        InputStream file = Project2.class.getResourceAsStream(fileName);
        Scanner scanner = new Scanner(file);
        String line;
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            System.out.println(line);
        }
        scanner.close();
    }

    /**
     * 3 Possibilities:
     * Create New File
     * Open Existing File that is blank
     * Open Existing File with Airline and Contents
     *
     * */
    private static void txtFile(String path){

    }
}