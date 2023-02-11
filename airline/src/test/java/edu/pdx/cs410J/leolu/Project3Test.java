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
import edu.pdx.cs410J.ParserException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
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
      assertThat(line, containsString("Airline"));
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
  void toggleMin12hrChangeDefaultValueFrom2To0(){
    Project3 proj = new Project3();
    proj.toggleMin12hr();
    assertThat(proj.min12hr,equalTo(0));
    proj.toggleMin12hr();
    assertThat(proj.min12hr,equalTo(2));
  }

  @Test
  void missingArgsPrintlnShallPrint24Hr(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project3 proj = new Project3();
    proj.missingArgsPrintln(0,proj);
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
    proj.missingArgsPrintln(10,proj);
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
    proj.missingArgsPrintln(0,proj);
    String output = baos.toString();
    assertThat(output,containsString("Arrival Time"));
    System.setOut(System.out);
  }

  @Test
  void invokePrettyOptionUsingOptionChecker(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project3 proj = new Project3();

    proj.optionChecker("-pretty",proj);
    assertEquals(proj.pStatus,1);
    proj.optionChecker("-",proj);
    assertEquals(proj.pStatus,-2);
    proj.optionChecker("-pretty",proj);
    String output = baos.toString();
    assertThat(output,containsString("can only be called once."));
    System.setErr(System.err);
  }

  @Test
  void prettyOptionNotFollowedByDashOrFilename(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project3 proj = new Project3();

    proj.optionChecker("-pretty",proj);
    assertEquals(proj.pStatus,1);
    proj.optionChecker("-pretty",proj);
    String output = baos.toString();
    assertThat(output,containsString("should be followed by a file name"));
    System.setErr(System.err);
  }

  @Test
  void prettyPrintMethodInvokedWithWrongStatus(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project3 proj = new Project3();
    proj.prettyPrint(3,proj);
    String output = baos.toString();
    assertThat(output,containsString("Incorrect Status code!"));
    System.setErr(System.err);
  }
/*
  @Test
  void prettyPrintMethodInvokedWithBadFilePathThrowsIOException(){
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setErr(ps);
    Project3 proj = new Project3();
    proj.pFilePath="petty.txt";
    proj.anAirline = new Airline("Test");
    proj.prettyPrint(-2,proj);
    String output = baos.toString();
    assertThat(output,containsString("Bad file path:"));
    System.setErr(System.err);
  }*/

  @Test
  void prettyPrintMethodPrintToStdOut() throws IOException {
    String[] args = new String[]{"-pretty", "-","EVA Air", "25", "TPE",
            "05/19/2023", "12:40","am", "SEA", "05/20/2023", "12:10","pm"};
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    System.setOut(ps);
    Project3 proj = new Project3();
    proj.subMainForTesting(args);

    String output = baos.toString();
    assertThat(output,containsString("Flight Number : 25\n" +
            "Departing From: TPE\n" +
            "Departure Date: 05/19/2023\n" +
            "Departure Time: 12:40 AM\n" +
            "Bound For     : SEA\n" +
            "Arrival Date  : 05/20/2023\n" +
            "Arrival Time  : 12:10 PM\n" +
            "Duration      : 35hrs 30mins"));
    System.setOut(System.out);
  }

  @Test
  void prettyPrintMethodPrintToFile() throws IOException {
    String path=getClass().getResource("prettywriting.txt").getPath();
    String[] args = new String[]{"-pretty",path,"-print","EVA Air", "25", "TPE",
            "05/19/2023", "12:40","am", "SEA", "05/20/2023", "12:10","pm"};

    Project3 proj = new Project3();
    proj.subMainForTesting(args);
    //File file = proj.getpFile();
    /*
    BufferedReader reader = new BufferedReader(new FileReader(proj.getpFile()));
    String contents="";
    String line;
    while ((line = reader.readLine()) != null) {
      contents += line;
      contents += "\n";
    }
    reader.close();
    assertThat(contents,containsString("Flight Number : 25\n" +
            "Departing From: TPE\n" +
            "Departure Date: 05/19/2023\n" +
            "Departure Time: 12:40 AM\n" +
            "Bound For     : SEA\n" +
            "Arrival Date  : 05/20/2023\n" +
            "Arrival Time  : 12:10 PM\n" +
            "Duration      : 35hrs 30mins"));*/
  }
  @Test
  void prettyPrintPrintToFile(@TempDir File tempDir) throws IOException{
    File file = new File(tempDir, "test.txt");
    FileWriter writer = new FileWriter(file);
    PrettyPrinter printer = new PrettyPrinter(writer);
    Flight fl = new Flight("25", "TPE",
            "05/19/2023", "12:40 am", "SEA", "05/20/2023", "12:10 pm");
    Airline air = new Airline("EVA Air");
    air.addFlight(fl);
    printer.dump(air);
    BufferedReader reader = new BufferedReader(new FileReader(file));
    String contents="";
    for(String line : reader.lines().toArray(String[]::new)){
      contents += line;
      contents += "\n";
    }
    reader.close();
    assertThat(contents, containsString(air.getName()));
    assertThat(contents, containsString("Flight Number : "));
    assertThat(contents, containsString("Duration      : "));

  }

  @Test
  void prettyPrintAnAirlineToStdOut() throws IOException {
    //String path=getClass().getResource("prettywriting.txt").getPath();
    String airpath=getClass().getResource("evaair.txt").getPath();
    String[] args = new String[]{"-textFile",airpath,"-pretty","-","-print","EVA Air", "52", "TPE",
            "05/19/2023", "12:20","am", "IAH", "05/19/2023", "2:00","pm"};

    Project3 proj = new Project3();
    proj.subMainForTesting(args);
  }

  @Test
  void prettyPrintAnAirlineToFile() throws IOException {
    String path=getClass().getResource("prettywriting.txt").getPath();
    String airpath=getClass().getResource("evaair.txt").getPath();
    String[] args = new String[]{"-textFile",airpath,"-pretty",path,"-print","EVA Air", "52", "TPE",
            "05/19/2023", "12:20","am", "IAH", "05/19/2023", "2:00","pm"};

    Project3 proj = new Project3();
    proj.subMainForTesting(args);
  }

}
