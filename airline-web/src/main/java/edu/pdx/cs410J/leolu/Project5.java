package edu.pdx.cs410J.leolu;

import java.io.*;

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
    Airline anAirline;
    Flight aFlight;
    boolean printFlight = false, readMe = false, search = false;
    int hostNameState = 0, portState = 0;
    // 0: -option not invoked, 1: invoked but not set, -1: set
    int idx = 0, argCount = 0;
    String hostName = null;
    int port = -1;

    static final String readMeFile="src/main/resources/edu.pdx.cs410J.leolu/README.txt";
    static final String usageFile="src/main/resources/edu.pdx.cs410J.leolu/USAGE.txt";


    /*Project 5 variables*/
    AirlineRestClient client = null;
    static final int ARG_COUNT_REQUIREMENT = 10;
    static final int SEARCH_ARG_COUNT_REQUIREMENT = 3;
    public static final String MISSING_ARGS = "Missing command line arguments";

    public static void main(String[] args) {
        invokedByMain(args);
    }

    public static void invokedByMain(String[] args){
        if(args.length==0){
            System.out.println("No command line arguments. Try -README for more details or review usage below.");
            printFile(usageFile);
            return;
        }
        Project5 exec = new Project5();
        if(!argumentReader(args,exec)){
            printFile(usageFile);
            return;
        }
        if(exec.readMe){
            printFile(readMeFile);
            return;
        }

        exec.client = new AirlineRestClient(exec.hostName,exec.port);
        if(exec.search){
            //USE SEARCH arg parser
            searchForAirlineAndFlights(args,exec);
        }else{
            //USE normal Airline and Flight parser
            if(!createAirlineAndFlight(args,exec))return;
            if(exec.printFlight) {
                printFlight(exec);
            }
        }
        
    }

    /**
     * This method is called when the -search function is invoked
     * Within the String[] args, there are two types of valid args
     * 1: airlineName
     * 2: airlineName, SRC, DEST
     * @param args accepts String[] args
     * @param exec Project5 object
     * */
    protected static void searchForAirlineAndFlights(String[] args, Project5 exec) {
        int type = args.length-exec.idx;
        Airline airline = null;
        if(type<1){
            System.err.println("No arguments were entered for -search option.\n" +
                    "Please review usage and try again.");
        }else if(type==2||type>3){
            System.err.println("Please enter only the airline name to get all flights from that airline,\n" +
                    "OR enter the airline name, departure airport code, and arrival airport code " +
                    "to get flights that match the specified itinerary.\n" +
                    "Please review usage and try again.");
        }else if(type==1){
            airline = exec.client.getAirline(args[exec.idx],null,null);
        }else if(type==3){
            airline = exec.client.getAirline(args[exec.idx],args[exec.idx+1],args[exec.idx+2]);
        }
        if(airline!=null){
            exec.anAirline=airline;
            printFlight(exec);
        }
    }

    /**
     * Creates Airline with airlineName
     * Creates an empty Flight
     * Uses a try-catch to add flight args to Flight object
     * If any flight arg is incorrect, error is printed
     * If there is any missing arg, error of missing detail is printed
     * Calls AirlineRestClient to create the flight and airline in servlet
     * @return true if airline and flight creation is successful
     * @return false if any process fails
     * @param args User String args entered
     * @param exec Project5 object
     * */
    protected static boolean createAirlineAndFlight(String[] args, Project5 exec) {
        try{
            String airlineName = args[exec.idx];
            exec.anAirline = new Airline(airlineName);
            String flightNumber = args[exec.idx+1];
            String dAirport = args[exec.idx+2];
            /*Departure Date & Time*/
            String dDate = args[exec.idx+3];
            String dTime = args[exec.idx+4];
            dTime += " " +args[exec.idx+5];
            String aAirport = args[exec.idx+6];
            /*Arrival Date & Time*/
            String aDate = args[exec.idx+7];
            String aTime = args[exec.idx+8];
            aTime += " "+args[exec.idx+9];

            Flight fl = new Flight(flightNumber,dAirport,dDate,dTime,aAirport,aDate,aTime);
            if(!exec.anAirline.getError().equals("")||!fl.getError().equals("")){
                System.err.println("*------------*Please review input errors above and try again*------------*");
                return false;
            }

            exec.anAirline.addFlight(fl);
            exec.aFlight = fl;
            exec.client.addFlightToAirline(new String[]{airlineName,flightNumber
                    ,dAirport,dDate + " " +dTime
                    , aAirport,aDate + " " +aTime });
        }catch(ArrayIndexOutOfBoundsException e){
            missingArgsPrintln(exec.argCount);
            return false;
        }
        return true;
    }

    /**
     * CHANGE!
     * Reads command line String[] args
     * Calls optionChecker(arg,curr) method when '-' character is present
     * Sets fileName if -textFile is called before
     * Prints error if too many arguments are entered
     * Counts number of args
     * */
    protected static boolean argumentReader(String[] args, Project5 curr){
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
                    System.err.println("Port number must be an integer! Provided port is not an integer: "+ arg +
                            "\nPlease review usage and try again.");
                    return false;
                }
                curr.portState = -1; //hostName is set;
                curr.idx+=1;
            }else if(curr.search && curr.argCount >= SEARCH_ARG_COUNT_REQUIREMENT){
                System.err.println(arg + " is an extraneous argument when using -search function. \n" +
                        "Please review usage and try again.");
                return false;
            }else if(curr.argCount >= ARG_COUNT_REQUIREMENT){
                System.err.println(arg + " is an extraneous argument. \n" +
                        "Please review usage and try again.");
                return false;
            }
            else curr.argCount++;
        }
        if(curr.readMe)return true;
        if(curr.hostNameState != -1){
            System.err.println("Hostname is missing!\n" +
                    "Please review usage and try again.");
            return false;
        }
        if(curr.portState != -1){
            System.err.println("Port number is missing!\n" +
                    "Please review usage and try again.");
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

    public static boolean optionChecker(String opt, Project5 curr){
        if(opt.equalsIgnoreCase("-README")){
            curr.readMe=true;
            printFile(readMeFile);
            return true;
        }
        else if(opt.equalsIgnoreCase("-search")){
            if(curr.printFlight){
                System.err.println("The -search option cannot be invoked with -print option. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.search=true;
            curr.idx+=1;
            return true;
        }
        else if(opt.equalsIgnoreCase("-host")){
            if(curr.hostNameState != 0){
                System.err.println("The -host option can only be called once. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.hostNameState = 1;//await assignment;
            curr.idx+=1;
            return true;
        }
        else if(opt.equalsIgnoreCase("-port")){
            if(curr.portState != 0){
                System.err.println("The -port option can only be called once. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.portState = 1;
            curr.idx+=1;
            return true;
        }
        else if(opt.equalsIgnoreCase("-print")){
            if(curr.search){
                System.err.println("The -print option cannot be invoked with -search option. \n" +
                        "Please review usage and try again.");
                return false;
            }
            curr.printFlight = true;
            curr.idx+=1;
            return true;
        }
        System.err.println(opt + " is not a valid option. \n" +
                "Please review usage for available options and try again");

        return false;
    }

    /**
     * Uses the number of args entered to print out which airline and flight detail is missing
     * @param count number of args entered
     * */
    public static void missingArgsPrintln(int count){
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

    private static void printFlight(Project5 curr){
        System.out.println("*---------------------------------------------------------*");
        System.out.println("Airline       : "+curr.anAirline.getName());
        for(Flight fl: curr.anAirline.getFlights()){
            System.out.println("*---------------------------------------------------------*");
            System.out.println("Flight Number : " +fl.getNumber());
            System.out.println("Departing From: " + fl.getSource());
            System.out.println("Departure Date: " + fl.getDepDate());
            System.out.println("Departure Time: " + fl.getDepTime());
            System.out.println("Bound For     : " + fl.getDestination());
            System.out.println("Arrival Date  : " + fl.getArrDate());
            System.out.println("Arrival Time  : " + fl.getArrTime());
            System.out.println("Duration HH|MM: " + fl.getFlightDuration()/60 + "hrs " + fl.getFlightDuration()%60 + "mins");
            System.out.println("Duration(mins): " + fl.getFlightDuration());
        }
        System.out.println("*---------------------------END---------------------------*");
    }


    /**
     * Prints README.txt file to console when -README is present in command-line arguments
     * @param fileName takes any valid file path
     * */
    private static void printFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

}