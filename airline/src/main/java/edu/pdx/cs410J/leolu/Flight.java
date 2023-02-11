/**
 * The {code Flight} class
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */

package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.AbstractFlight;
import edu.pdx.cs410J.AirportNames;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Flight extends AbstractFlight implements Comparable<Flight> {
  private final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:mm a");
  private final SimpleDateFormat formatter24 = new SimpleDateFormat("MM/dd/yyyy H:mm");
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
  private final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
  private final SimpleDateFormat timeFormat24 = new SimpleDateFormat("H:mm");
  private int flightNumber;
  private int flightDuration;
  private boolean twelveHrFormat = true;
  private String dep; //Departure Airport 3-letter Code
  private String arr; //Arrival Airport 3-letter Code
  private String depDate;
  private String depTime;
  private String arrDate;
  private String arrTime;
  private String error = "";

  private Date departureDate;
  private Date arrivalDate;


  /**
   * Default Constructor
   * Creates a new <code>Flight</code>
   *
   * */
  public Flight(){}

  /**
   * Constructs a new instance of <code>Flight</code>
   * with a given flight number, departure airport code, arrival airport code,
   * departure datetime, and arrival datetime
   * Flight Constructor that accepts flight details as arguments
   * Creates a new <code>Flight</code>
   * @param fN Flight Number
   * @param dep Departure Airport 3-letter code
   * @param dDate Flight departure Date
   * @param dTime Flight departure Time
   * @param arr Arrival Airport 3-letter code
   * @param aDate Flight arrival Date
   * @param aTime Flight arrival Time
   *
   * */
  public Flight(String fN, String dep, String dDate, String dTime,  String arr, String aDate, String aTime){
    setDateTime(dDate,dTime,"Departure");
    setDateTime(aDate,aTime,"Arrival");
    setFlightNumber(fN);
    setAirportCode(dep,"Departure");
    setAirportCode(arr, "Arrival");
    if(error.isEmpty())setFlightDuration();
  }
  /**
   * Constructs a new instance of <code>Flight</code>
   * with a given flight number, departure airport code, arrival airport code,
   * departure datetime, and arrival datetime
   * Flight Constructor that accepts flight details as arguments
   * Creates a new <code>Flight</code>
   * @param fN Flight Number
   * @param dep Departure Airport 3-letter code
   * @param dDate Flight departure Date
   * @param dTime Flight departure Time
   * @param arr Arrival Airport 3-letter code
   * @param aDate Flight arrival Date
   * @param aTime Flight arrival Time
   * @param format if True, change time format to 24 hr (12 hr is default)
   * */
  public Flight(String fN, String dep, String dDate, String dTime,  String arr, String aDate, String aTime, boolean format){
    if(format)toggle12HrFormat();
    setDateTime(dDate,dTime,"Departure");
    setDateTime(aDate,aTime,"Arrival");
    setFlightNumber(fN);
    setAirportCode(dep,"Departure");
    setAirportCode(arr, "Arrival");
    if(error.isEmpty())setFlightDuration();
  }

  /**
   * @return integer Flight Number
   */
  @Override
  public int getNumber() {
    return flightNumber;
  }

  /**
   * Ensures String can be parsed into an integer
   * Ensures that the integer is greater than zero
   * @param input - input flight number String from Constructor
   * @throws NullPointerException if input String is null
   * @throws NumberFormatException if String cannot be parsed to int
   * @throws IllegalArgumentException if int is less than or equal to 0
   * */
  public void setFlightNumber(String input){
    try{
      if(input==null||input.length()==0)
        throw new NullPointerException("Flight Number cannot be null.");

      int temp = Integer.parseInt(input);//Throws NumberFormatException if not digits

      if(temp<=0)
        throw new IllegalArgumentException("Flight Number must be greater than zero: ");
      this.flightNumber = temp;
    }catch(NullPointerException e){
      error = e.getMessage();
      System.out.println(error);
    }catch(NumberFormatException e){
      error = "Flight Number must be a positive integer: " +input;
      System.out.println(error);
    }catch(IllegalArgumentException e){
      error = e.getMessage() + input;
      System.out.println(error);
    }
  }

  /**
   * Ensures input is only of length 3
   * Ensures input is alphabetical
   * Ensures input toUpperCase()
   * @param input - airport code String from Constructor
   * @param type - Departure or Arrival
   * @throws  NullPointerException if input is null or of length 0
   * @throws IllegalArgumentException if input is not a 3-letter alphabetical code
   * */
  public void setAirportCode(String input, String type){
    try{
      if(input==null||input.length()==0)
        throw new NullPointerException(" airport code cannot be null.");
      input = input.toUpperCase();//case insensitive
      if(input.length()!=3||!isAirportCodeAlphabetic(input))
        throw new IllegalArgumentException(" airport code must be a 3-letter alphabetical code: ");
      if(!isRealAirportCode(input)){
        throw new IllegalArgumentException(" airport code is not a real airport code: ");
      }
      if(type.equals("Departure")) this.dep=input.toUpperCase();
      if(type.equals("Arrival")) this.arr=input.toUpperCase();
    }catch(NullPointerException e){
      error = type + e.getMessage();
      System.out.println(error);
    }catch(IllegalArgumentException e){
      error = type + e.getMessage() + input;
      System.out.println(error);
    }
  }
  /**
   * @param input - airport code String from Constructor
   * @return boolean true when airport code is alphabetic
   * */
  private boolean isAirportCodeAlphabetic(String input)
  {
    return input.matches("^[a-zA-Z]*$");
  }

  /**
   * @param input - airport code String to be checked if real
   * @return boolean true when airport code is from a real airport
   * Utilizes {@code AirportNames.getNamesMap()}
   * */
  private boolean isRealAirportCode(String input){
    return AirportNames.getNamesMap().containsKey(input);
  }

  /**
   * Ensures date is not null and is of correct format
   * mm/dd/yyyy - leading zeros are ignored
   * */
  private boolean isValidDate(String input){
    return input.matches("^(?:(1[0-2]|0?[1-9])/(3[01]|[12][0-9]|0?[1-9])|↵\n" +
            "(3[01]|[12][0-9]|0?[1-9])/(1[0-2]|0?[1-9]))/(?:[0-9]{2})?[0-9]{2}$");
  }

  /**
   * Ensures time is correct 24 hr format
   * H:mm - leading zeros can be ignored
   * */
  private boolean isValidTime(String input){
    return input.matches("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
  }

  /**
   * Ensures time is correct 12 hr format
   * h:mm - leading zeros can be ignored
   * */
  private boolean isValid12HrMeridiem(String input){
    return input.matches("((1[0-2]|0?[1-9]):([0-5][0-9]) ?([AaPp][Mm]))");
  }

  public void toggle12HrFormat(){
    if(this.twelveHrFormat)twelveHrFormat=false;
    else twelveHrFormat= true;
  }

  /**
   * Sets Departure and Arrival Date objects
   * @param type Departure or Arrival types
   * @param date String args from user input
   * @param time String args from user input
   * */
  public void setDateTime(String date, String time, String type) {
    try {
      if (date == null || date.length() == 0)
        throw new NullPointerException(" date cannot be null.");
      if (time == null || time.length() == 0)
        throw new NullPointerException(" time cannot be null.");

      if (!isValidDate(date))
        throw new IllegalArgumentException(" date format incorrect (mm/dd/yyyy): " + date);
      time = time.toUpperCase();
      if (twelveHrFormat) {
        if (!time.endsWith("AM") && !time.endsWith("PM"))
          throw new IllegalArgumentException(" time incorrect or missing meridiem AM or PM only!");
        if (!isValid12HrMeridiem(time))
          throw new IllegalArgumentException(" time format incorrect (12hr - hh:mm AM/PM): " + time);
      } else {
        if (!isValidTime(time))
          throw new IllegalArgumentException(" time format incorrect (hh:mm): " + time);
      }
      if (date == null || date.length() == 0 || time == null || time.length() == 0) return;

      String dateTime = date + " " + time;
      Date d;
      if (twelveHrFormat) d = formatter.parse(dateTime);
      else d = formatter24.parse(dateTime);

      if (type.equals("Departure")) {
        this.departureDate = d;
        this.depDate = dateFormat.format(d);
      }
      if (type.equals("Arrival")) {
        this.arrivalDate = d;
        this.arrDate = dateFormat.format(d);
      }
    } catch (NullPointerException e) {
      error = type + e.getMessage();
      System.out.println(error);
    } catch (ParseException e) {
      error = "Failed to parse " + type + " date " + date + " and time " + time;
      System.out.println(error);
    } catch (IllegalArgumentException e) {
      error = type + e.getMessage();
      System.out.println(error);
    }
  }

  /**
   * @return String Departure Date and Time
   * */
  @Override
  public String getDepartureString() {
    DateFormat formatter2 = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    return formatter2.format(getDeparture());
  }

  /**
   * @return Date object for Departure datetime
   * */
  @Override
  public Date getDeparture(){
    return departureDate;
  }
  /**
   * @return String Departure Airport 3-letter code
   * */
  @Override
  public String getSource() {
    return dep;
  }

  /**
   * @return String Departure Date
   * */
  public String getDepDate(){return depDate;}


  /**
   * @return String Departure Time in 12-hr am/pm format
   * */
  public String getDepTime(){
    if(this.depTime==null)this.depTime = timeFormat.format(departureDate);
    return depTime;
  }

  /**
   * @return String Departure Time in 24-hr am/pm format
   * */
  public String getDepTime24(){
    return timeFormat24.format(departureDate);
  }

  /**
   * @return String Arrival Time in 12-hr format
   * */
  public String getArrTime(){
    if(this.arrTime==null)this.arrTime = timeFormat.format(arrivalDate);
    return arrTime;
  }
  /**
   * @return String Arrival Time in 24-hr format
   * */
  public String getArrTime24(){
    return timeFormat24.format(arrivalDate);
  }

  /**
   * @return String Arrival Airport 3-letter code
   * */
  @Override
  public String getDestination() {
    return arr;
  }

  /**
   * @return String Arrival Date and Time
   * */
  @Override
  public String getArrivalString() {
    DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    return formatter.format(getArrival());
  }

  /**
   * @return String Arrival Date
   * */
  public String getArrDate(){return arrDate;}

  /**
   * @return Date object Arrival datetime
   * */
  @Override
  public Date getArrival(){
    return arrivalDate;
  }

  public String getError(){
    return error;
  }

  /**
   * Compare alphabetically by departure airport code
   * then
   * Compare by departure time
   * */
  @Override
  public int compareTo(Flight o) {
    int codeCompare = this.dep.compareTo(o.dep);
    if(codeCompare!=0) return codeCompare;
    int timeCompare = this.getDeparture().compareTo(o.getDeparture());
    if(timeCompare!=0) return timeCompare;
    return 0;
  }

  /**
   * @return  boolean True if departure time comes before arrival time
   * */
  public boolean departureBeforeArrival(){
    return this.getDeparture().getTime()<this.getArrival().getTime();
  }

  /**
   * Sets flight duration in minutes
   * */
  public void setFlightDuration(){
    try{
      if(!departureBeforeArrival()) throw new IllegalArgumentException();
      flightDuration = (int)((getArrival().getTime()-getDeparture().getTime())/(1000*60));
    }catch(IllegalArgumentException e){
      error = "Arrival date & time " + formatter.format(this.getArrival()) +
              " cannot be earlier than Departure date & time " + formatter.format(this.getDeparture());
    }catch(NullPointerException e){

    }
  }

  /**
   * @return int flight duration in minutes
   * */
  public int getFlightDuration(){
    return flightDuration;
  }
}
