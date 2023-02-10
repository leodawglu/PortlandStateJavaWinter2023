/**
 * The {@code Project2Test} class contains unit tests for the {@link edu.pdx.cs410J.leolu.Project3} class
 *
 *
 * @author Leo Lu
 * PSU CS510 Advanced Java Winter 2023
 *
 * */
package edu.pdx.cs410J.leolu;

import edu.pdx.cs410J.InvokeMainTestCase;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A unit test for code in the <code>Project2</code> class.  This is different
 * from <code>Project2IT</code> which is an integration test (and can capture data
 * written to {@link System#out} and the like.
 *
 * How to write tests for Project2
 * Think of Project2 as Student class
 * Write tests of various arg inputs/scenarios
 * So class can be tested independently
 *
 */
class Project3Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    try (
      InputStream readme = Project3.class.getResourceAsStream("README.txt")
    ) {
      assertThat(readme, not(nullValue()));
      BufferedReader reader = new BufferedReader(new InputStreamReader(readme));
      String line = reader.readLine();
      assertThat(line, containsString("Project2"));
    }
  }

  @Test
  void sampleTestPrintInput() throws IOException {
    Project3.main(new String[] {"-print", "EVA Airways", "26", "TPE", "05/19/2023", "23:40", "SEA", "05/19/2023", "18:40"});
  }

  @Test
  void sampleREADMETestInput() throws IOException {
    Project3.main(new String[] {"-README","-print", "Java Airlines", "12345", "PDX", "05/19/2023", "11:53", "SEA", "05/21/2023", "17:33"});
  }

  @Test
  void sample12hrTestPrintInput() throws IOException {
    Project3.main(new String[] {"-print", "EVA Airways", "26", "TPE", "05/19/2023", "3:40","pm", "SEA", "05/19/2023", "8:40","AM"});
  }

  //https://stackoverflow.com/questions/12781273/what-are-the-date-formats-available-in-simpledateformat-class
  @Test
  void dateFormatter(){
    String input = "1/8/2023 1:07 aM";
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");
    SimpleDateFormat formatter3 = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    DateFormat formatter2 = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
    try {
      Date date = formatter.parse(input);
      System.out.println(date);
      System.out.println(formatter2.format(date)); //  getArrivalString and getDepartureString

      System.out.println(formatter3.format(date));
      System.out.println(date.getTime());
    } catch (ParseException e) {
      System.out.println("Failed to parse date and time: " + e.getMessage());
    }
  }

  @Test
  void testPrintFlightToStdOut() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project3 proj = new Project3();
    proj.main(new String[]{"-print", "EVA Air", "12345", "SEA", "05/19/2023", "11:03","am", "LAX" ,"05/19/2023", "11:53","pm"});
    String output = baos.toString();
    assertThat(output,containsString("EVA Air 12345\n" +
            "Departing From: SEA\n" +
            "Departure Date: 05/19/2023\n" +
            "Departure Time: 11:03 AM\n" +
            "Bound For     : LAX\n" +
            "Arrival Date  : 05/19/2023\n" +
            "Arrival Time  : 11:53 PM"));
    System.setOut(System.out);
  }

  @Test
  void txtFile() throws IOException{
    String filePath = getClass().getResource("valid-airline.txt").getPath();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project3 proj = new Project3();
    proj.main(new String[]{"EVA Air", "12345", "SEA", "05/19/2023", "11:03","am", "LAX" ,"05/19/2023", "11:53","pm"});
    proj.txtFile(filePath,proj.anAirline);
    String output = baos.toString();
    assertThat(output,containsString("EVA Air 12345\n" +
            "Departing From: SEA\n" +
            "Departure Date: 05/19/2023\n" +
            "Departure Time: 11:03 AM\n" +
            "Bound For     : LAX\n" +
            "Arrival Date  : 05/19/2023\n" +
            "Arrival Time  : 11:53 PM"));
    System.setErr(System.err);
  }

  @Test
  void toggleMin12hrChangeDefaultValueFrom2To0(){
    Project3 proj = new Project3();
    proj.toggleMin12hr();
    assertThat(proj.min12hr,equalTo(0));
  }

  @Test
  void missingArgsPrintlnShallPrint24Hr(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project3 proj = new Project3();
    proj.missingArgsPrintln(0);
    String output = baos.toString();
    assertThat(output,containsString("Arrival Time AM/PM"));
    System.setOut(System.out);
  }

  @Test
  void noMissingArgsPrintlnShallPrintNothing(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project3 proj = new Project3();
    proj.missingArgsPrintln(10);
    String output = baos.toString();
    assertThat(output,equalTo("The following arguments are missing: \nPlease review usage and try again.\n"));
    System.setOut(System.out);
  }
  @Test
  void missingArgsPrintlnShallPrintsOnly24hrMsg(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project3 proj = new Project3();
    proj.toggleMin12hr();
    proj.missingArgsPrintln(0);
    String output = baos.toString();
    assertThat(output,containsString("Arrival Time"));
    System.setOut(System.out);
  }
}
