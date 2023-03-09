package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.ParserException;

import java.io.*;
import java.util.Map;
import java.util.Scanner;

/**
 * The main class that parses the command line and communicates with the
 * Airline server using REST.
 */
public class Project5 {

    /*
      private boolean isRealAirportCode(String input){
    return AirportNames.getNamesMap().containsKey(input);
  }

    * */
    /*Project 4 Variables*/
    boolean printFlight = false, readMe = false, tooManyArguments = false, search = false;
    int hostNameState = 0, portState = 0;
    // 0: -option not invoked, 1: invoked but not set, -1: set
    int idx = 0, argCount = 0;
    String airlineName = null;
    String hostName = null;
    int port = -1;

    Map<String, String>  airlineAndFlightStringInformation;
    String flightNumberAsString = null;
    static final String readMeFile="README.txt";
    static final String usageFile="USAGE.txt";


    /*Project 5 variables*/
    AirlineRestClient client = null;
    static final int ARG_COUNT_REQUIREMENT = 10;
    static final int SEARCH_ARG_COUNT_REQUIREMENT = 3;
    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void main(String[] args) {
        invokedByMain(args);
/*
        String hostName = null;
        String portString = null;
        String airlineName = null;
        String flightNumberAsString = null;


        for (String arg : args) {
            if (hostName == null) {
                hostName = arg;

            } else if ( portString == null) {
                portString = arg;

            } else if (airlineName == null) {
                airlineName = arg;

            } else if (flightNumberAsString == null) {
                flightNumberAsString = arg;

            } else {
                usage("Extraneous command line argument: " + arg);
            }
        }

        if (hostName == null) {
            usage( MISSING_ARGS );
            return;

        } else if ( portString == null) {
            usage( "Missing port" );
            return;
        }

        int port;
        try {
            port = Integer.parseInt( portString );

        } catch (NumberFormatException ex) {
            usage("Port \"" + portString + "\" must be an integer");
            return;
        }
        //when HostName and port are clearly defined, initialize AirlineRestClient
        AirlineRestClient client = new AirlineRestClient(hostName, port);

        try {
            if (airlineName == null) {
                error("Airline name required");
                // Print all word/definition pairs

                Map<String, String> dictionary = client.getAllAirlineEntries();
                StringWriter sw = new StringWriter();
                PrettyPrinter pretty = new PrettyPrinter(sw);
                pretty.dump(dictionary);
                message = sw.toString();

            } else if (flightNumberAsString == null) {
                // Pretty Print the entire airline
                Airline airline = client.getAirline(airlineName);
                System.out.println(airline.toString());

            } else {
                // Post the word/definition pair
                client.addFlightToAirline(airlineName, flightNumberAsString);
                //message = Messages.definedWordAs(airlineName, flightNumberAsString);
            }

        } catch (IOException | ParserException ex ) {
            error("While contacting server: " + ex.getMessage());
            return;
        }

        System.out.println(message);

 */
    }

    public static void invokedByMain(String[] args){
        if(args.length==0){
            System.out.println("No command line arguments. Try -README for more details or review usage below.");
            printFile(usageFile);
            return;
        }
        Project5 exec = new Project5();
        if(!argumentReader(args,exec))return;
        if(exec.readMe) return;

        exec.client = new AirlineRestClient(exec.hostName,exec.port);
        if(exec.search){
            //USE SEARCH arg parser
        }else{
            //USE normal Airline and Flight parser
        }
        if(exec.printFlight) {
            // TO DO: get flight from Servlet and print
            //printFlight(exec);
        }
        /*
        try{
        }catch(IOException | ParserException ex){
            error("While contacting server: " + ex.getMessage());
            return;
        }*/
    }
    /**
     * CHANGE!
     * Reads command line String[] args
     * Calls optionChecker(arg,curr) method when '-' character is present
     * Sets fileName if -textFile is called before
     * Prints error if too many arguments are entered
     * Counts number of args
     * */
    private static boolean argumentReader(String[] args, Project5 curr){
        for(String arg: args){
            if(arg.startsWith("-")){
                if(!optionChecker(arg,curr)) return false;
            }
            else if(curr.hostNameState == 1){
                curr.hostName = arg;
                curr.hostNameState = -1; //hostName is set;
                curr.idx+=1;
            }
            else if(curr.portState == 1){
                try {
                    curr.port = Integer.parseInt( arg );
                } catch (NumberFormatException ex) {
                    usage("Port \"" + arg + "\" must be an integer");
                    return false;
                }
                curr.portState = -1; //hostName is set;
                curr.idx+=1;
            }else if(curr.search && curr.argCount >= SEARCH_ARG_COUNT_REQUIREMENT){
                System.out.println(arg + " is an extraneous argument. \n" +
                        "Please review usage and try again.");
                curr.tooManyArguments=true; // use this to print usage
                return false;
            }else if(curr.argCount >= ARG_COUNT_REQUIREMENT){
                System.out.println(arg + " is an extraneous argument. \n" +
                        "Please review usage and try again.");
                curr.tooManyArguments=true; // use this to print usage
                return false;
            }
            else curr.argCount++;
        }
        if(curr.hostNameState != -1){
            System.err.println("Hostname is missing!\n" +
                    "Please review usage and try again.");
            return false;
        }
        if(curr.portState != -1){
            System.err.println("Port is missing!\n" +
                    "Please review usage and try again.");
            return false;
        }
        return true;
    }

    public static boolean searchFunction(String[] args, Project5 curr){

        return true;
    }

    /**
     * Used to check if option invoked with '-' character is valid
     * When invalid, false is returned to propagate return up into argumentReader
     * @param opt the candidate -option String invoked by user to be validated
     * @param curr Project 2 instance
     * */

    public static boolean optionChecker(String opt, Project5 curr){
        if(opt.equalsIgnoreCase("-README")){
            curr.readMe=true;
            printFile(readMeFile);
            return true;
        }
        /*
        * TO DO!!! -SEARCH OPTION LOGIC
        * CHANGES THE ENTIRE BEHAVIOR
        * !*!*! -SEARCH IS NOT VALID WITH -PRINT !*!*!
        * Two behaviors:
        * -search "Some Airline"
        * > Pretty prints all flights in the airline
        * -search "Some Airline" PDX LAS
        * > Pretty prints all flights ORIGINATING from PDX and TERMINATING at LAS
        * */
        else if(opt.equalsIgnoreCase("-host")){
            if(curr.hostNameState != 0){
                System.err.println("The -host option can only be called once. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.hostNameState = 1;//await assignment;
            return true;
        }
        else if(opt.equalsIgnoreCase("-port")){
            if(curr.portState != 0){
                System.err.println("The -port option can only be called once. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.portState = 1;
            return true;
        }
        else if(opt.equalsIgnoreCase("-print")){
            if(curr.search){
                System.err.println("The -search option cannot be invoked with -print option. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.printFlight = true;
            curr.idx+=1; //TO DO: REPLACE STRING ARG READ LOGIC
            return true;
        }
        System.err.println(opt + " is not a valid option. \n" +
                "Please review usage for available options and try again");

        return false;
    }

    /**
     * Uses the number of args entered to print out which airline and flight detail is missing
     * @param curr Project5 instance
     * @param count number of args entered
     * */
    public static void missingArgsPrintln(int count, Project5 curr){
        System.out.println("The following arguments are missing: ");
        if(count<1) System.out.println("Airline Name");
        if(count<2) System.out.println("Flight Number");
        if(count<3) System.out.println("Departure Airport Code");
        if(count<4) System.out.println("Departure Date");
        if(count<5) System.out.println("Departure Time");
        if(count<6) System.out.println("Departure Time AM/PM");
        if(count<7) System.out.println("Arrival Airport Code");
        if(count<8) System.out.println("Arrival Date");
        if(count<9) System.out.println("Arrival Time");
        if(count<10) System.out.println("Arrival Time AM/PM");
        System.out.println("Please review usage and try again.");
    }

    /**
     * Prints flight information entered by user should -print option be called
     * @param curr Project2 Instance
     * */
    /*
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
    }*/


    /**
     * Prints README.txt file to console when -README is present in command-line arguments
     * @param fileName takes any valid file path
     * */
    private static void printFile(String fileName){
        InputStream file = Project5.class.getResourceAsStream(fileName);
        Scanner scanner = new Scanner(file);
        String line;
        while(scanner.hasNextLine()){
            line = scanner.nextLine();
            System.out.println(line);
        }
        scanner.close();
    }
    private static void error( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
    }

    /**
     * Prints usage information for this program and exits
     * @param message An error message to print
     */
    private static void usage( String message )
    {
        PrintStream err = System.err;
        err.println("** " + message);
        err.println();
        err.println("usage: java Project5 host port [word] [definition]");
        err.println("  host         Host of web server");
        err.println("  port         Port of web server");
        err.println("  word         Word in dictionary");
        err.println("  definition   Definition of word");
        err.println();
        err.println("This simple program posts words and their definitions");
        err.println("to the server.");
        err.println("If no definition is specified, then the word's definition");
        err.println("is printed.");
        err.println("If no word is specified, all dictionary entries are printed");
        err.println();
    }
}